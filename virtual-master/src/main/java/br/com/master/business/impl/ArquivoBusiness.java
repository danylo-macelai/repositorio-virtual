package br.com.master.business.impl;

import static br.com.common.utils.Utils.VIRTUAL_EXTENSION;
import static br.com.common.utils.Utils.fileEscrever;
import static br.com.common.utils.Utils.fileParticionar;
import static br.com.common.utils.Utils.gerarIdentificador;
import static br.com.common.utils.Utils.httpDelete;
import static br.com.common.utils.Utils.httpGet;

import br.com.common.access.property.ValidarToken;
import br.com.common.business.DBusiness;
import br.com.common.utils.Constants;

import br.com.master.business.IArquivo;
import br.com.master.business.IBloco;
import br.com.master.business.IConfiguracao;
import br.com.master.configuration.MasterBalance;
import br.com.master.configuration.MasterException;
import br.com.master.domain.ArquivoTO;
import br.com.master.domain.BlocoTO;
import br.com.master.domain.ConfiguracaoTO;
import br.com.master.persistence.ArquivoDAO;
import br.com.master.wrappers.SearchTab;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import okhttp3.Response;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 18 de nov de 2018
 * @version $
 */
@Service
public class ArquivoBusiness extends DBusiness<ArquivoTO> implements IArquivo {

    @Autowired
    ArquivoDAO persistence;

    @Autowired
    IConfiguracao configuracaoBusiness;

    @Autowired
    IBloco blocoBusiness;

    @Autowired
    MasterBalance masterBalance;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<ArquivoTO> carregarPor(final String nome, final SearchTab searchTab) throws MasterException {
        if (searchTab == null) {
            return persistence.findAllByNomeIgnoreCaseContaining(nome);
        }
        return persistence.findAllBySearchTabAndNomeIgnoreCaseContaining(searchTab, nome);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ArquivoTO> carregarPor(final String nome, final SearchTab searchTab, final Integer page)
            throws MasterException {
        final int p = page != null && page >= 0 ? page : Constants.PAGINATION_FIRST_PAGE;

        final PageRequest pr = PageRequest.of(p, Constants.PAGINATION_ITEMS_PER_PAGE);

        if (searchTab == null) {
            return persistence.findAllByNomeIgnoreCaseContaining(nome, pr);
        }
        return persistence.findAllBySearchTabAndNomeIgnoreCaseContaining(searchTab, nome, pr);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void excluir(final ValidarToken token, final long id) throws MasterException {
        final ArquivoTO arquivo = ache(id);
        if (arquivo == null) {
            throw new MasterException("slave.obj.nao.localizado").status(Status.NOT_FOUND);
        }
        if (!arquivo.getToken().getAccessId().equals(token.getAccessId())) {
            throw new MasterException("master.excluir.arquivo");
        }
        this.excluir(arquivo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void excluir(final ArquivoTO arquivo) throws MasterException {
        try {
            final List<BlocoTO> blocos = blocoBusiness.carregarTodosPor(arquivo);
            if (blocos.isEmpty()) {
                throw new MasterException("slave.obj.nao.localizado").status(Status.NOT_FOUND);
            }

            final Iterator<BlocoTO> blocoIterator = blocos.iterator();
            while (blocoIterator.hasNext()) {
                final BlocoTO bloco = blocoIterator.next();
                if (bloco.getInstanceId() != null) {
                    final Response response = httpDelete(masterBalance.volumeUrlSlave(bloco.getInstanceId()),
                            "exclusao",
                            bloco.getUuid());
                    if (Status.OK.getStatusCode() != response.code()) {
                        throw new MasterException(response.body().string()).status(Status.BAD_REQUEST);
                    }
                }
            }

            super.excluir(arquivo);
        } catch (final IOException e) {
            throw new MasterException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArquivoTO gravar(final ValidarToken token, final MultipartFile multipartFile) throws MasterException {
        final ConfiguracaoTO configuracao = configuracaoBusiness.buscar();

        final ArquivoTO arquivo = new ArquivoTO();
        arquivo.setNome(multipartFile.getOriginalFilename());
        arquivo.setTamanho((int) multipartFile.getSize());
        arquivo.setMimeType(multipartFile.getContentType());
        arquivo.setSearchTab(SearchTab.ofNullable(multipartFile.getContentType()));
        arquivo.setToken(token);

        final int tamanhoBloco = configuracao.getTamanhoBloco() * 1024;
        final int miniBloco = arquivo.getTamanho() % tamanhoBloco;
        final int qtdeBloco = arquivo.getTamanho() / tamanhoBloco;
        int qtdePecas = 0;
        try (FileChannel channel = ((FileInputStream) multipartFile.getInputStream()).getChannel()) {
            while (qtdePecas < qtdeBloco) {
                final BlocoTO bloco = createBlocoOffline(channel, ((qtdePecas++) * tamanhoBloco), tamanhoBloco,
                        qtdePecas);
                bloco.setArquivo(arquivo);
                arquivo.getBlocos().add(bloco);
            }
            if (miniBloco > 0) {
                final BlocoTO bloco = createBlocoOffline(channel, (qtdePecas * tamanhoBloco), miniBloco, ++qtdePecas);
                bloco.setArquivo(arquivo);
                arquivo.getBlocos().add(bloco);
            }
            arquivo.setPecas(qtdePecas);

            programmaticTransaction(() -> {
                super.incluir(arquivo);
            });

            return arquivo;
        } catch (final Exception e) {
            throw new MasterException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStreamResource ler(final ArquivoTO arquivo) throws MasterException {
        try {
            final List<BlocoTO> blocos = programmaticTransaction(() -> {
                return blocoBusiness.carregarTodosPor(arquivo);
            });

            if (blocos.isEmpty()) {
                throw new MasterException("slave.obj.nao.localizado").status(Status.NOT_FOUND);
            }

            Path path = Paths.get(masterBalance.getPathTmpDirectory());
            path = path.resolve(arquivo.getNome());
            final Iterator<BlocoTO> blocoIterator = blocos.iterator();
            try (OutputStream os = Files.newOutputStream(path)) {
                final byte[] buffer = new byte[4096];
                while (blocoIterator.hasNext()) {
                    final BlocoTO bloco = blocoIterator.next();
                    if (bloco.getInstanceId() != null) {
                        try (Response response = httpGet(masterBalance.volumeUrlSlave(bloco.getInstanceId()), "leitura",
                                bloco.getUuid())) {
                            if (Status.OK.getStatusCode() != response.code()) {
                                throw new MasterException(response.body().string()).status(Status.BAD_REQUEST);
                            }
                            try (InputStream in = response.body().byteStream()) {
                                int read = -1;
                                while ((read = in.read(buffer)) != -1) {
                                    os.write(buffer, 0, read);
                                }
                            }
                        }
                    } else {
                        try (InputStream in = new FileInputStream(bloco.getDiretorioOffLine())) {
                            int read = -1;
                            while ((read = in.read(buffer)) != -1) {
                                os.write(buffer, 0, read);
                            }
                        }
                    }
                }
                os.flush();
            }
            return new InputStreamResource(new FileInputStream(path.toFile()));
        } catch (final Exception e) {
            throw new MasterException(e.getMessage(), e);
        }
    }

    private BlocoTO createBlocoOffline(final FileChannel channel, final long position, final long byteSize,
            final int numero) {
        final String uuid = gerarIdentificador();
        Path path = Paths.get(masterBalance.getPathTmpDirectory());
        path = path.resolve(uuid + VIRTUAL_EXTENSION);
        final int tamanho = fileEscrever(path, fileParticionar(channel, position, byteSize));

        final BlocoTO bloco = new BlocoTO();
        bloco.setNumero(numero);
        bloco.setUuid(uuid);
        bloco.setTamanho(tamanho);
        bloco.setReplica(false);
        bloco.setDiretorioOffLine(path.toFile().getAbsolutePath());

        return bloco;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ArquivoTO> carregarPor(final ValidarToken access, final Integer page) {
        final int p = page != null && page >= 0 ? page : Constants.PAGINATION_FIRST_PAGE;
        return persistence.findAllByToken(access, PageRequest.of(p, Constants.PAGINATION_ITEMS_PER_PAGE));
    }
}

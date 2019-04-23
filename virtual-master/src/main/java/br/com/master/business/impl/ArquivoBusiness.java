package br.com.master.business.impl;

import static br.com.common.utils.Utils.VIRTUAL_EXTENSION;
import static br.com.common.utils.Utils.fileEscrever;
import static br.com.common.utils.Utils.fileParticionar;
import static br.com.common.utils.Utils.gerarIdentificador;
import static br.com.common.utils.Utils.httpDelete;
import static br.com.common.utils.Utils.httpGet;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.com.common.business.DBusiness;
import br.com.master.business.IArquivo;
import br.com.master.business.IBloco;
import br.com.master.business.IConfiguracao;
import br.com.master.configuration.MasterBalance;
import br.com.master.configuration.MasterException;
import br.com.master.domain.ArquivoTO;
import br.com.master.domain.BlocoTO;
import br.com.master.domain.ConfiguracaoTO;
import br.com.master.persistence.ArquivoDAO;
import okhttp3.Response;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 18 de nov de 2018
 */
@Service
public class ArquivoBusiness extends DBusiness<ArquivoTO> implements IArquivo {

    @Autowired
    ArquivoDAO    persistence;

    @Autowired
    IConfiguracao configuracaoBusiness;

    @Autowired
    IBloco        blocoBusiness;

    @Autowired
    MasterBalance masterBalance;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<ArquivoTO> carregarPor(String nome) throws MasterException {
        return persistence.findAllByNome(nome);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void excluir(ArquivoTO arquivo) throws MasterException {
        try {
            List<BlocoTO> blocos = blocoBusiness.carregarTodosPor(arquivo);
            if (blocos.isEmpty()) {
                throw new MasterException("slave.obj.nao.localizado").status(Status.NOT_FOUND);
            }

            Iterator<BlocoTO> blocoIterator = blocos.iterator();
            while (blocoIterator.hasNext()) {
                BlocoTO bloco = blocoIterator.next();
                Response response = httpDelete(masterBalance.volumeUrlSlave(bloco.getInstanceId()), "exclusao", bloco.getUuid());
                if (Status.OK.getStatusCode() != response.code()) {
                    throw new MasterException(response.body().string()).status(Status.BAD_REQUEST);
                }
            }

            super.excluir(arquivo);
        } catch (IOException e) {
            throw new MasterException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArquivoTO upload(MultipartFile multipartFile) throws MasterException {
        ConfiguracaoTO configuracao = configuracaoBusiness.buscar();

        ArquivoTO arquivo = new ArquivoTO();
        arquivo.setNome(multipartFile.getOriginalFilename());
        arquivo.setTamanho((int) multipartFile.getSize());
        arquivo.setMimeType(multipartFile.getContentType());

        int tamanhoBloco = configuracao.getTamanhoBloco() * 1024;
        int miniBloco = arquivo.getTamanho() % tamanhoBloco;
        int qtdeBloco = arquivo.getTamanho() / tamanhoBloco;
        int qtdePecas = 0;
        try (FileChannel channel = ((FileInputStream) multipartFile.getInputStream()).getChannel()) {
            while (qtdePecas < qtdeBloco) {
                BlocoTO bloco = createBlocoOffline(channel, ((qtdePecas++) * tamanhoBloco), tamanhoBloco, qtdePecas);
                bloco.setArquivo(arquivo);
                arquivo.getBlocos().add(bloco);
            }
            if (miniBloco > 0) {
                BlocoTO bloco = createBlocoOffline(channel, (qtdePecas * tamanhoBloco), miniBloco, ++qtdePecas);
                bloco.setArquivo(arquivo);
                arquivo.getBlocos().add(bloco);
            }
            arquivo.setPecas(qtdePecas);

            programmaticTransaction(() -> {
                super.incluir(arquivo);
            });

            return arquivo;
        } catch (Exception e) {
            throw new MasterException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStreamResource download(ArquivoTO arquivo) throws MasterException {
        try {
            List<BlocoTO> blocos = programmaticTransaction(() ->  {
                return blocoBusiness.carregarTodosPor(arquivo);
            });

            if (blocos.isEmpty()) {
                throw new MasterException("slave.obj.nao.localizado").status(Status.NOT_FOUND);
            }

            Path path = Paths.get(masterBalance.getPathTmpDirectory());
            path = path.resolve(arquivo.getNome());
            Iterator<BlocoTO> blocoIterator = blocos.iterator();
            try (OutputStream os = Files.newOutputStream(path)) {
                byte[] buffer = new byte[4096];
                while (blocoIterator.hasNext()) {
                    BlocoTO bloco = blocoIterator.next();
                    if (bloco.getInstanceId() != null) {
                        try(Response response = httpGet(masterBalance.volumeUrlSlave(bloco.getInstanceId()), "leitura", bloco.getUuid())) {
                            if (Status.OK.getStatusCode() != response.code()) {
                                throw new MasterException(response.body().string()).status(Status.BAD_REQUEST);
                            }
                            try(InputStream in = response.body().byteStream()) {
                                int read = -1;
                                while ((read = in.read(buffer)) != -1) {
                                    os.write(buffer, 0, read);
                                }
                            }
                        }
                    } else {
                        try(InputStream in = new FileInputStream(bloco.getDiretorioOffLine())) {
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
        } catch (Exception e) {
            throw new MasterException(e.getMessage(), e);
        }
    }

    private BlocoTO createBlocoOffline(FileChannel channel, long position, long byteSize, int numero) {
        String uuid = gerarIdentificador();
        Path path = Paths.get(masterBalance.getPathTmpDirectory());
        path = path.resolve(uuid + VIRTUAL_EXTENSION);
        int tamanho = fileEscrever(path, fileParticionar(channel, position, byteSize));

        BlocoTO bloco = new BlocoTO();
        bloco.setNumero(numero);
        bloco.setUuid(uuid);
        bloco.setTamanho(tamanho);
        bloco.setReplica(false);
        bloco.setDiretorioOffLine(path.toFile().getAbsolutePath());

        return bloco;
    }
}

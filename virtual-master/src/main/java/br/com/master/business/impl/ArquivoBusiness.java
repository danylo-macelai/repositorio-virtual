package br.com.master.business.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.shared.Application;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;

import br.com.common.business.DBusiness;
import br.com.common.utils.Utils;
import br.com.common.wrappers.File;
import br.com.master.business.IArquivo;
import br.com.master.business.IBloco;
import br.com.master.business.IConfiguracao;
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
    ArquivoDAO                persistence;

    @Autowired
    IConfiguracao             configuracaoBusiness;

    @Autowired
    IBloco                    blocoBusiness;

    @Autowired
    PeerAwareInstanceRegistry registry;

    ObjectMapper              mapper = new ObjectMapper();

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
    public ArquivoTO upload(MultipartFile multipartFile) throws MasterException {
        String host = getHomePageUrl();
        ConfiguracaoTO configuracao = configuracaoBusiness.buscar();

        ArquivoTO arquivo = new ArquivoTO();
        arquivo.setNome(multipartFile.getOriginalFilename());
        arquivo.setTamanho((int) multipartFile.getSize());
        arquivo.setMimeType(multipartFile.getContentType());

        int tamanhoBloco = 1024 * configuracao.getTamanhoBloco();
        int miniBloco = arquivo.getTamanho() % tamanhoBloco;
        int qtdeBloco = arquivo.getTamanho() / tamanhoBloco;
        int pecas = 0;
        try (FileChannel channel = ((FileInputStream) multipartFile.getInputStream()).getChannel()) {
            while (pecas < qtdeBloco) {
                File file = blocoUuid(host, channel, ((pecas++) * tamanhoBloco), tamanhoBloco);
                arquivo.getBlocos().add(new BlocoTO(pecas, file, arquivo));
            }
            if (miniBloco > 0) {
                File file = blocoUuid(host, channel, (pecas * tamanhoBloco), miniBloco);
                arquivo.getBlocos().add(new BlocoTO(++pecas, file, arquivo));
            }
            arquivo.setPecas(pecas);

            Integer qtdeReplicacao = configuracao.getQtdeReplicacao();
            if (qtdeReplicacao > 0) {
                Iterator<BlocoTO> blocos = arquivo.getBlocos().iterator();
                List<BlocoTO> replicacoes = new ArrayList<>();
                while (blocos.hasNext()) {
                    BlocoTO bloco = blocos.next();
                    for (int i = 0; i < qtdeReplicacao; i++) {
                        Response response = Utils.httpPost(bloco.getFile().getHost(), "/replicacao", "/" + bloco.getFile().getUuid());
                        File file = mapper.readValue(response.body().string(), File.class);
                        replicacoes.add(new BlocoTO(bloco.getNumero(), file, arquivo));
                    }
                }
                arquivo.getBlocos().addAll(replicacoes);
            }

            super.incluir(arquivo);

            return arquivo;
        } catch (Exception e) {
            throw new MasterException("", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public InputStreamResource download(ArquivoTO arquivo) throws MasterException {
        try {
            List<BlocoTO> blocos = blocoBusiness.carregarTodosPor(arquivo);
            if (blocos.isEmpty()) {
                throw new MasterException("no.result.exception").status(Status.NOT_FOUND);
            }

            Vector<InputStream> vector = new Vector<>();
            Iterator<BlocoTO> blocoIterator = blocos.iterator();
            Set<Integer> pecas = new HashSet<>();
            while (blocoIterator.hasNext()) {
                BlocoTO bloco = blocoIterator.next();
                if (pecas.add(bloco.getNumero())) {
                    try {
                        Response response = Utils.httpGet(bloco.getFile().getHost(), "/download/", bloco.getFile().getUuid());
                        vector.add(response.body().byteStream());
                    } catch (Exception e) {
                        pecas.remove(bloco.getNumero());
                    }
                }
            }
            if (!arquivo.getPecas().equals(pecas.size())) {
                Iterator<InputStream> vectorIterator = vector.iterator();
                while (vectorIterator.hasNext()) {
                    InputStream inputStream = vectorIterator.next();
                    inputStream.close();
                }
                throw new MasterException("");
            }

            return new InputStreamResource(new SequenceInputStream(vector.elements()));
        } catch (Exception e) {
            throw new MasterException(e.getMessage(), e);
        }
    }

    private File blocoUuid(String host, FileChannel channel, long position, long byteSize) throws IOException {
        InputStream stream = Utils.fileParticionar(channel, position, byteSize);
        Response response = Utils.httpPost(stream, host, "/upload");
        return mapper.readValue(response.body().string(), File.class);
    }

    private String getHomePageUrl() {
        List<Application> applications = registry.getApplications().getRegisteredApplications();
        String url = null;
        for (Application application : applications) {
            List<InstanceInfo> applicationsInstances = application.getInstances();
            for (InstanceInfo applicationsInstance : applicationsInstances) {
                url = applicationsInstance.getHomePageUrl();
            }
        }
        return url;
    }

}

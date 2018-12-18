package br.com.master.business.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    ArquivoDAO    persistence;

    @Autowired
    IConfiguracao configuracaoBusiness;

    @Autowired
    PeerAwareInstanceRegistry registry;

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
    public void upload(MultipartFile multipartFile)  {
        String host = getHomePageUrl();
        ConfiguracaoTO configuracao = configuracaoBusiness.buscar();

        ArquivoTO arquivo = new ArquivoTO();
        arquivo.setNome(multipartFile.getOriginalFilename());
        arquivo.setTamanho(multipartFile.getSize());
        arquivo.setMimeType(multipartFile.getContentType());

        long tamanhoBloco = 1024L * configuracao.getTamanhoBloco();
        long miniBloco = arquivo.getTamanho() % tamanhoBloco;
        long qtdeBloco = arquivo.getTamanho() / tamanhoBloco;
        int position = 0;
        try (FileChannel channel = ((FileInputStream) multipartFile.getInputStream()).getChannel()) {
            while (position < qtdeBloco) {
                String uuid = blocoUuid(host, channel, ((position++) * tamanhoBloco), tamanhoBloco);
                File file = new File(uuid, host);
                arquivo.getBlocos().add(new BlocoTO(position, file, arquivo));
            }
            if (miniBloco > 0) {
                String uuid = blocoUuid(host, channel, (position * tamanhoBloco), miniBloco);
                File file = new File(uuid, host);
                arquivo.getBlocos().add(new BlocoTO(position, file, arquivo));
            }

            Integer qtdeReplicacao = configuracao.getQtdeReplicacao();
            if (qtdeReplicacao > 0) {
                ObjectMapper mapper = new ObjectMapper();
                Iterator<BlocoTO> blocos = arquivo.getBlocos().iterator();
                List<BlocoTO> replicacoes = new ArrayList<>();
                while(blocos.hasNext()) {
                    BlocoTO bloco = blocos.next();
                    for (int i = 0; i < qtdeReplicacao; i++) {
                        Response response = Utils.httpPost(bloco.getFile().getHost(), "/replicacao", "/" + bloco.getFile().getUuid());
                        File file = mapper.readValue(response.body().string(), File.class);
                        replicacoes.add(new BlocoTO(bloco.getNumero(), file, arquivo));
                    }
                }
                arquivo.getBlocos().addAll(replicacoes);
            }
        } catch (Exception e) {
            throw new MasterException("", e);
        }

        super.incluir(arquivo);
    }

    private String blocoUuid(String host, FileChannel channel, long position, long byteSize) throws IOException {
        InputStream stream = Utils.fileParticionar(channel, position, byteSize);
        Response response = Utils.httpPost(stream, host, "/upload");
        return response.body().string();
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

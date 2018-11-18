package br.com.master.business.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.com.common.business.DBusiness;
import br.com.common.utils.Utils;
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

    Set<String> SLAVE = new HashSet<>();

    @Autowired
    ArquivoDAO    persistence;

    @Autowired
    IConfiguracao configuracaoBusiness;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public ArquivoTO carregarPor(String nome) throws MasterException {
        return persistence.findByNome(nome);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void upload(MultipartFile file)  {
        String host = getUrlSlave(null);
        ConfiguracaoTO configuracao = configuracaoBusiness.buscar();

        ArquivoTO arquivo = new ArquivoTO();
        arquivo.setNome(file.getOriginalFilename());
        arquivo.setTamanho(file.getSize());
        arquivo.setMimeType(file.getContentType());

        long tamanhoBloco = 1024L * configuracao.getTamanhoBloco();
        long miniBloco = arquivo.getTamanho() % tamanhoBloco;
        long qtdeBloco = arquivo.getTamanho() / tamanhoBloco;
        int position = 0;
        try (FileChannel channel = ((FileInputStream) file.getInputStream()).getChannel()) {
            while (position < qtdeBloco) {
                String uuid = blocoUuid(host, channel, (position * tamanhoBloco), tamanhoBloco);
                arquivo.getBlocos().add(new BlocoTO(arquivo, host, position, uuid));
            }
            if (miniBloco > 0) {
                String uuid = blocoUuid(host, channel, (position * tamanhoBloco), tamanhoBloco);
                arquivo.getBlocos().add(new BlocoTO(arquivo, host, position, uuid));
            }
        } catch (Exception e) {

        }

        try {
            Integer qtdeReplicacao = configuracao.getQtdeReplicacao();
            if (qtdeReplicacao > 0) {
                Iterator<BlocoTO> blocos = arquivo.getBlocos().iterator();
                List<BlocoTO> replicacoes = new ArrayList<>();
                while(blocos.hasNext()) {
                    BlocoTO bloco = (BlocoTO) blocos.next().clone();
                    String slave = getUrlSlave(bloco.getHost());
                    String[] params = {"slave="+slave};
                    Response response = Utils.httpPost(bloco.getHost(), "/replicacao", "/" + bloco.getUuid(), params);

                    bloco.setHost(slave);
                    bloco.setUuid(response.body().string());
                    replicacoes.add(bloco);
                }
                arquivo.getBlocos().addAll(replicacoes);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        super.incluir(arquivo);
    }

    private String blocoUuid(String host, FileChannel channel, long position, long byteSize) throws IOException {
        InputStream stream = Utils.fileParticionar(channel, position, byteSize);
        Response response = Utils.httpPost(stream, host, "/upload");
        return response.body().string();
    }

    private String getUrlSlave(String url) {
        Random r = new Random();
        String slave = SLAVE.stream().skip(r.nextInt(SLAVE.size()-1)).findFirst().get();
        if(url.equals(slave) && SLAVE.size() > 1) {
           return getUrlSlave(url);
        }
        return slave;
    }


}

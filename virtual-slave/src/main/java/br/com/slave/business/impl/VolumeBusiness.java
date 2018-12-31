package br.com.slave.business.impl;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.ws.rs.core.StreamingOutput;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wso2.msf4j.Request;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.common.business.Business;
import br.com.common.utils.Utils;
import br.com.common.wrappers.File;
import br.com.slave.business.IVolume;
import br.com.slave.configuration.SlaveEurekaClient;
import br.com.slave.configuration.SlaveException;
import br.com.slave.domain.VolumeTO;
import br.com.slave.persistence.VolumeDAO;
import br.com.slave.resource.VolumeResource;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 23 de out de 2018
 */
@Service
public class VolumeBusiness extends Business<VolumeTO> implements IVolume {

    static final int    BUFFER_SIZE     = 4096;
    static final String BLOCO_EXTENSION = ".rvf";
    OkHttpClient        client          = new OkHttpClient();
    VolumeTO            INSTANCE;

    @Autowired
    VolumeDAO           persistence;

    @Autowired
    SlaveEurekaClient eurekaClient;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public VolumeTO buscar() throws SlaveException {
        if (INSTANCE == null) {
            INSTANCE = persistence.buscar();
        }
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void incluir(VolumeTO volume) throws SlaveException {
        long count = persistence.count();
        if (count > 0) {
            throw new SlaveException("volume.inclusao.unica");
        }
        super.incluir(volume);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void alterar(VolumeTO volume) throws DataAccessException {
        super.alterar(volume);
        INSTANCE = null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public File upload(Request request) throws SlaveException {
        VolumeTO volume = buscar();
        String uuid = Utils.gerarIdentificador();
        Path path = Paths.get(volume.getLocalizacao(), uuid + BLOCO_EXTENSION);
        int tamanho = Utils.fileEscrever(path, request.getMessageContentStream(), volume.getTamanho(), volume.getCapacidade());
        volume.incrementar(tamanho);
        alterar(volume);

        String host = String.format("%s://%s%s", request.getProperties().get("PROTOCOL"), request.getProperties().get("listener.interface.id"), VolumeResource.RESOURCE_ROOT_URL);

        return new File(uuid, tamanho, host);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StreamingOutput download(String uuid) throws SlaveException {
        Path path = Paths.get(buscar().getLocalizacao(), uuid + BLOCO_EXTENSION);
        if (!path.toFile().exists()) {
            throw new SlaveException("volume.bloco.nao.existe").args(uuid);
        }
        return Utils.fileLer(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File replicacao(String uuid) throws SlaveException {
        try {
            Path path = Paths.get(buscar().getLocalizacao(), uuid + BLOCO_EXTENSION);
            String host = eurekaClient.getHomePageUrl();
            Response response = Utils.httpPost(new FileInputStream(path.toFile()), host, "/gravacao");
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body().string(), File.class);
        } catch (Exception e) {
            throw new SlaveException(e.getMessage(), e);
        }
    }

}

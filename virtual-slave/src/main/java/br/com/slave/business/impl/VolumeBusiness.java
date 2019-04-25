package br.com.slave.business.impl;

import static br.com.common.utils.Utils.VIRTUAL_EXTENSION;
import static br.com.common.utils.Utils.fileEscrever;
import static br.com.common.utils.Utils.fileLer;
import static br.com.common.utils.Utils.fileRemover;
import static br.com.common.utils.Utils.httpPost;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wso2.msf4j.Request;

import br.com.common.business.Business;
import br.com.common.wrappers.CommonException;
import br.com.slave.business.IVolume;
import br.com.slave.configuration.SlaveEurekaClient;
import br.com.slave.configuration.SlaveException;
import br.com.slave.domain.VolumeTO;
import br.com.slave.persistence.VolumeDAO;
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
            throw new SlaveException("slave.inclusao.unica");
        }
        super.incluir(volume);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void alterar(VolumeTO volume) throws SlaveException {
        INSTANCE = null;
        if (volume.getCapacidade() < buscar().getTamanho()) {
            throw new SlaveException("slave.capc.menor.tam").args(volume.getCapacidade().toString(), buscar().getTamanho().toString()).status(Status.BAD_REQUEST);
        }
        if (!buscar().getTamanho().equals(volume.getTamanho())) {
            throw new SlaveException("slave.proibido.update.tam").status(Status.FORBIDDEN);
        }
        if (!buscar().getContem().equals(volume.getContem())) {
            throw new SlaveException("slave.proibido.update.qtde").status(Status.FORBIDDEN);
        }
        if (Files.notExists(Paths.get(volume.getLocalizacao()))) {
            throw new SlaveException("slave.paths.invalido").args(volume.getLocalizacao()).status(Status.BAD_REQUEST);
        }
        _alterar(volume);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void gravar(Request request, String uuid) throws SlaveException {
        VolumeTO volume = buscar();
        Path path = Paths.get(volume.getLocalizacao());
        if (!path.toFile().exists()) {
            throw new CommonException("slave.paths.invalido").args(volume.getLocalizacao()).status(Status.BAD_REQUEST);
        }
        path = path.resolve(uuid + VIRTUAL_EXTENSION);
        int tamanho = fileEscrever(path, request.getMessageContentStream(), volume.getTamanho(), volume.getCapacidade());
        volume.incrementar(tamanho);
        _alterar(volume);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StreamingOutput ler(String uuid) throws SlaveException {
        Path path = Paths.get(buscar().getLocalizacao(), uuid + VIRTUAL_EXTENSION);
        return fileLer(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void replicar(String uuid, String instanceId) throws SlaveException {
        try {
            Path path = Paths.get(buscar().getLocalizacao(), uuid + VIRTUAL_EXTENSION);
            if (!path.toFile().exists()) {
                throw new CommonException("common.file.nao.existe").args(uuid).status(Status.BAD_REQUEST);
            }
            String host = eurekaClient.getReplicacaoUrl(instanceId);
            if (host == null) {
                throw new CommonException("slave.nao.registrado.discovery").status(Status.BAD_REQUEST);
            }
            try (Response response = httpPost(new FileInputStream(path.toFile()), host, "gravacao", uuid)) {
                if (Status.NO_CONTENT.getStatusCode() != response.code()) {
                    throw new SlaveException(response.body().string()).status(Status.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            throw new SlaveException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void excluir(String uuid) throws SlaveException {
        try {
            VolumeTO volume = buscar();
            Path path = Paths.get(volume.getLocalizacao(), uuid + VIRTUAL_EXTENSION);
            long read = Files.size(path);
            if (fileRemover(path)) {
                volume.decrementar((int) read);
                _alterar(volume);
            }
        } catch (Exception e) {
            throw new SlaveException(e.getMessage(), e);
        }
    }

    /**
     * O método alterarSemValidar executa uma instrução update com base nas informações do registro.
     *
     * @param volume
     */
    private void _alterar(VolumeTO volume) {
        if (!buscar().getDisponibilidade()) {
            throw new SlaveException("slave.volume.inativado").status(Status.BAD_REQUEST);
        }
        super.alterar(volume);
        INSTANCE = volume;
    }
}

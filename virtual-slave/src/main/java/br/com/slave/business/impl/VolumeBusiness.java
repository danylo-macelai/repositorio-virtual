package br.com.slave.business.impl;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.common.business.Business;
import br.com.common.utils.Utils;
import br.com.slave.business.IVolume;
import br.com.slave.configuration.SlaveException;
import br.com.slave.domain.VolumeTO;
import br.com.slave.persistence.VolumeDAO;

/**
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 23 de out de 2018
 */
@Service
public class VolumeBusiness extends Business<VolumeTO> implements IVolume {

    static final int BUFFER_SIZE     = 4096;
    String           BLOCO_EXTENSION = ".bl";
    VolumeTO         INSTANCE;

    @Autowired
    VolumeDAO        persistence;

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
    @Transactional(readOnly = true)
    public long count() throws SlaveException {
        return persistence.count();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public String upload(InputStream stream) throws SlaveException {
        VolumeTO volume = buscar();
        String uuid = Utils.gerarIdentificador();
        Path path = Paths.get(volume.getLocalizacao(), uuid + BLOCO_EXTENSION);
        try (InputStream in = stream) {
            if (path.toFile().exists()) {
                throw new SlaveException("volume.bloco.existe").args(uuid).status(Status.CONFLICT);
            }
            try (OutputStream os = Files.newOutputStream(path)) {
                int size = 0;
                byte[] buffer = new byte[BUFFER_SIZE];
                int read = -1;
                while ((read = in.read(buffer)) != -1) {
                    if ((size += read) > volume.getCapacidade()) {
                        throw new SlaveException("volume.capacidade.excedida");
                    }
                    os.write(buffer, 0, read);
                }
                os.flush();

                volume.setContem(volume.getContem() + 1);
                volume.setTamanho(volume.getTamanho() + size);
                alterar(volume);

                return uuid;
            }
        } catch (SlaveException e) {
            Utils.deleteFileQuietly(path);
            throw e;
        } catch (Exception e) {
            Utils.deleteFileQuietly(path);
            throw new SlaveException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void download(String uuid, OutputStream stream) throws SlaveException {
        Path path = Paths.get(buscar().getLocalizacao(), uuid + BLOCO_EXTENSION);
        if (!path.toFile().exists()) {
            throw new SlaveException("volume.bloco.nao.existe").args(uuid);
        }
        try (InputStream in = new FileInputStream(path.toFile())) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int read = -1;
            while ((read = in.read(buffer)) != -1) {
                stream.write(buffer, 0, read);
            }
            stream.flush();
        } catch (Exception e) {
            throw new SlaveException(e.getMessage(), e);
        }
    }

}

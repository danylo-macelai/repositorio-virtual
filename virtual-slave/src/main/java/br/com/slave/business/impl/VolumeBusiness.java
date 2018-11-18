package br.com.slave.business.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 23 de out de 2018
 */
@Service
public class VolumeBusiness extends Business<VolumeTO> implements IVolume {

    static final int    BUFFER_SIZE     = 4096;
    static final String BLOCO_EXTENSION = ".bl";
    OkHttpClient        client          = new OkHttpClient();
    VolumeTO            INSTANCE;

    @Autowired
    VolumeDAO           persistence;

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
    public StreamingOutput download(String uuid) throws SlaveException {
        StreamingOutput stream = os -> {
            Path path = Paths.get(buscar().getLocalizacao(), uuid + BLOCO_EXTENSION);
            if (!path.toFile().exists()) {
                throw new SlaveException("volume.bloco.nao.existe").args(uuid);
            }
            try (InputStream in = new FileInputStream(path.toFile())) {
                byte[] buffer = new byte[BUFFER_SIZE];
                int read = -1;
                while ((read = in.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
                os.flush();
            } catch (Exception e) {
                throw new SlaveException(e.getMessage(), e);
            }
        };
        return stream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String replicacao(String uuid, String host) throws SlaveException {
        try {
            Path path = Paths.get(buscar().getLocalizacao(), uuid + BLOCO_EXTENSION);
            File file = path.toFile();
            InputStream inputStream = new FileInputStream(file);
            RequestBody requestBody = new RequestBody() {
                @Override
                public MediaType contentType() {
                    return MediaType.parse("application/x-www-form-urlencoded");
                }

                @Override
                public void writeTo(BufferedSink sink) throws IOException {
                    try (Source src = Okio.source(inputStream)) {
                        sink.writeAll(src);
                    }
                }

            };
            Request request = new Request.Builder().url(host + "/upload").post(requestBody).build();
            okhttp3.Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            throw new SlaveException(e.getMessage(), e);
        }
    }

}

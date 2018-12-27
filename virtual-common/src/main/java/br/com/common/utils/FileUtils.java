package br.com.common.utils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import br.com.common.configuration.CommonException;

/**
 * <b>Project:</b> virtual-common <br>
 *
 * @author macelai
 * @date: 20 de nov de 2018
 */
abstract class FileUtils {
    
    static final int BUFFER_SIZE = 4096;
    
    private FileUtils() {
        
    }

    public final static int escrever(Path path, InputStream stream, long tamanho, long capacidade) throws CommonException {
        try (InputStream in = stream) {
            if (path.toFile().exists()) {
                throw new CommonException("volume.bloco.existe").status(Status.CONFLICT);
            }
            int size = 0;
            try (OutputStream os = Files.newOutputStream(path)) {
                byte[] buffer = new byte[BUFFER_SIZE];
                int read = -1;
                while ((read = in.read(buffer)) != -1) {
                    if ((tamanho += read) > capacidade) {
                        throw new CommonException("volume.capacidade.excedida");
                    }
                    os.write(buffer, 0, read);
                    size += read;
                }
                os.flush();
            }
            return size;
        } catch (CommonException e) {
            Utils.deleteFileQuietly(path);
            throw e;
        } catch (Exception e) {
            Utils.deleteFileQuietly(path);
            throw new CommonException(e.getMessage(), e);
        }
    }

    public final static StreamingOutput ler(Path path) throws CommonException {
        try {
            if (!path.toFile().exists()) {
                throw new CommonException("volume.bloco.nao.existe");
            }
            return ler(new FileInputStream(path.toFile()));
        } catch (Exception e) {
            throw new CommonException(e.getMessage(), e);
        }
    }

    public final static StreamingOutput ler(InputStream is) throws CommonException {
        StreamingOutput stream = os -> {
            try (InputStream in = is) {
                byte[] buffer = new byte[BUFFER_SIZE];
                int read = -1;
                while ((read = in.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
                os.flush();
            } catch (Exception e) {
                throw new CommonException(e.getMessage(), e);
            }
        };
        return stream;
    }
    
    public final static InputStream particionar(FileChannel channel, long position, long byteSize) throws CommonException {
        try {
            channel.position(position);
            ByteBuffer buffer = ByteBuffer.allocate((int) byteSize);
            channel.read(buffer);
            buffer.flip();
            if (!buffer.hasRemaining()) {
                throw new CommonException("volume.bloco.nao.existe");
            }
            return new ByteArrayInputStream(buffer.array(), 0, (int) byteSize);
        } catch (Exception e) {
            throw new CommonException(e.getMessage(), e);
        }
    }
    
}
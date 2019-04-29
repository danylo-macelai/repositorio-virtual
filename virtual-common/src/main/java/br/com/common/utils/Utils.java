package br.com.common.utils;

import br.com.common.wrappers.CommonException;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import javax.ws.rs.core.StreamingOutput;

import okhttp3.Response;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-common <br>
 *
 * @author macelai
 * @date: 22 de out de 2018
 * @version $
 */
public abstract class Utils {

    public static final int    BUFFER_SIZE       = 4096;
    public static final String VIRTUAL_EXTENSION = ".rvf";
    public static final int    DELAY_3_SEGUNDO   = 3;

    /**
     * O método {@link Utils#actualType(Object)} retorna o tipo parametrizado da classe genérica.
     *
     * @param object
     * @return Class<T>
     */
    @SuppressWarnings("unchecked")
    public static <T extends Object> Class<T> actualType(final Object object) {
        final ParameterizedType thisType = (ParameterizedType) object.getClass().getGenericSuperclass();
        return (Class<T>) thisType.getActualTypeArguments()[0];
    }

    /**
     * Cria um Array concatenado com os elementos do one seguido por todos os elementos two.
     *
     * @param one
     * @param two
     * @return T[]
     */
    public static String[] concat(final String[] one, final String[] two) {
        return Stream.concat(Arrays.stream(one), Arrays.stream(two)).toArray(String[]::new);
    }

    /**
     * Remove o arquivo se ele existir
     *
     * @param path
     * @return boolean
     */
    public static boolean deleteFileQuietly(final Path path) {
        try {
            if (path == null) {
                return false;
            }
            return Files.deleteIfExists(path);
        } catch (final Exception ignored) {
            return false;
        }
    }

    /**
     * Gera um identificador único
     *
     * @return String
     */
    public static String gerarIdentificador() {
        try {
            final MessageDigest salt = MessageDigest.getInstance("SHA-256");
            salt.update(UUID.randomUUID().toString().getBytes("UTF-8"));
            final byte[] hash = salt.digest();
            final StringBuffer uuid = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    uuid.append('0');
                }
                uuid.append(hex);
            }
            return uuid.toString();
        } catch (final Exception e) {
            throw new CommonException("", e);
        }
    }

    /**
     * Escreve os bytes do binário no disco
     *
     * @param path - Arquivo em disco
     * @param stream - bytes do binário
     * @param tamanho - tamanho atual do storage
     * @param capacidade - capacidade máxima do storage
     * @throws CommonException
     */
    public static int fileEscrever(Path path, InputStream stream) throws CommonException {
        return fileEscrever(path, stream, 0, Long.MAX_VALUE);
    }

    public static int fileEscrever(Path path, InputStream stream, long tamanho, long capacidade)
            throws CommonException {
        return FileUtils.escrever(path, stream, tamanho, capacidade);
    }

    /**
     * Retorna um streaming para leitura dos bytes do arquivo
     *
     * @param path - Arquivo em disco
     * @return StreamingOutput
     * @throws CommonException
     */
    public static StreamingOutput fileLer(Path path) throws CommonException {
        return FileUtils.ler(path);
    }

    /**
     * Retorna um streaming para leitura dos bytes do arquivo
     *
     * @param stream
     * @return StreamingOutput
     * @throws CommonException
     */
    public final static StreamingOutput fileLer(InputStream stream) throws CommonException {
        return FileUtils.ler(stream);
    }

    /**
     * Retorna um stream de um determinado tamanho a partir da posição informada
     *
     * @param channel - Arquivo em disco
     * @param position - Posição inicial para particionar
     * @param byteSize - Tamanho do stream que será retornado
     * @return InputStream
     * @throws CommonException
     */
    public static InputStream fileParticionar(FileChannel channel, long position, long byteSize)
            throws CommonException {
        return FileUtils.particionar(channel, position, byteSize);
    }

    /**
     * Remove o arquivo se não for possível excluir nenhuma exceção será lançada.
     *
     * @param path para excluir
     * @return {@code true} se o arquivo foi excluído, caso contrário {@code false}
     */
    public static boolean fileRemover(final Path path) {
        return FileUtils.remover(path);
    }

    /**
     * Remove o arquivo se não for possível excluir nenhuma exceção será lançada.
     *
     * @param file para excluir
     * @return {@code true} se o arquivo foi excluído, caso contrário {@code false}
     */
    public static boolean fileRemover(final File file) {
        return fileRemover(Paths.get(file.toURI()));
    }

    /**
     * Faz a execução parar temporariamente o processamento por alguns segundos informados.
     *
     * @param segundos da duração de espera
     */
    public static void delay(int segundos) {
        try {
            Thread.sleep(segundos * 1000);
        } catch (final Exception e) {
        }
    }

    public static Response httpPost(String host, String service, String path, Map<String, String> formParams)
            throws CommonException {
        return HttpUtils.getInstance().post(host, service, path, formParams, new HashMap<>());
    }

    public static Response httpPost(InputStream stream, String host, String service, String path)
            throws CommonException {
        return HttpUtils.getInstance().post(stream, host, service, path);
    }

    public static Response httpGet(String host) throws CommonException {
        return httpGet(host, "", "");
    }

    public static Response httpGet(String host, String service, String path) throws CommonException {
        return HttpUtils.getInstance().get(host, service, path, new HashMap<>());
    }

    public static Response httpDelete(String host, String service, String path) throws CommonException {
        return HttpUtils.getInstance().delete(host, service, path);
    }

}

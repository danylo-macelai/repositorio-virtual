package br.com.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;

import br.com.common.configuration.CommonException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * <b>Project:</b> virtual-common <br>
 *
 * @author macelai
 * @date: 20 de nov de 2018
 */
abstract class HttpUtils {
    
    static OkHttpClient client          = new OkHttpClient();
    static MediaType    form_urlencoded = MediaType.parse("application/x-www-form-urlencoded");
    
    private HttpUtils() {
    }
    
    public static Response post(String host, String service, String path, String... params) throws CommonException {
        try {
            RequestBody body = RequestBody.create(form_urlencoded, StringUtils.join(params, "&"));
            Request request = new Request.Builder().url(host + service + Objects.toString(path, "")).post(body)
                    .addHeader("content-type", form_urlencoded.type()).build();
            
            Response response = client.newCall(request).execute();
            return response;
        } catch (Exception e) {
            throw new CommonException(e.getMessage(), e);
        }
    }
    
    public static Response post(InputStream stream, String host, String service) throws CommonException {
        try (InputStream inputStream = stream) {
            RequestBody body = new RequestBody() {
                @Override
                public MediaType contentType() {
                    return form_urlencoded;
                }
                
                @Override
                public void writeTo(BufferedSink sink) throws IOException {
                    try (Source src = Okio.source(inputStream)) {
                        sink.writeAll(src);
                    }
                }
            };
            Request request = new Request.Builder().url(host + service).post(body).build();
            Response response = client.newCall(request).execute();
            return response;
        } catch (Exception e) {
            throw new CommonException(e.getMessage(), e);
        }
    }
    
}

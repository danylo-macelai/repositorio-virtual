package br.com.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import br.com.common.wrappers.CommonException;
import okhttp3.ConnectionPool;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
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
final class HttpUtils {

    final MediaType    form_urlencoded;
    final OkHttpClient client;

    private HttpUtils() {
        client = new OkHttpClient.Builder(). //
                connectTimeout(Duration.ofSeconds(120)). //
                readTimeout(Duration.ofSeconds(120)). //
                writeTimeout(Duration.ofSeconds(120)). //
                connectionPool(new ConnectionPool(200, 10, TimeUnit.SECONDS)). //
                build();

        form_urlencoded = MediaType.parse("application/x-www-form-urlencoded");
    }

    public static HttpUtils getInstance() {
        return new HttpUtils();
    }

    public Response post(String host, String service, String path, Map<String, String> formParams, Map<String, String> queryParams)
            throws CommonException {
        try {
            HttpUrl.Builder url = HttpUrl.parse(host).newBuilder();
            url.addPathSegment(service);
            url.addPathSegment(path);

            if (queryParams != null && !queryParams.isEmpty()) {
                queryParams.forEach((k, v) -> {
                    url.addQueryParameter(k, v);
                });
            }

            Request.Builder request = new Request.Builder().url(url.build());

            FormBody.Builder builder = new FormBody.Builder();
            if (formParams != null && !formParams.isEmpty()) {
                formParams.forEach((k, v) -> {
                    builder.add(k, v);
                });

                request.post(builder.build());
            }
            Response response = client.newCall(request.build()).execute();
            if (response.isSuccessful()) {
                return response;
            } else {
                throw new CommonException("common.resposta.http.status").args(String.valueOf(response.code()), url.build().toString());
            }
        } catch (Exception e) {
            throw new CommonException(e.getMessage(), e);
        }
    }

    public Response post(InputStream stream, String host, String service, String path) throws CommonException {
        try (InputStream inputStream = stream) {
            HttpUrl.Builder url = HttpUrl.parse(host).newBuilder();
            url.addPathSegment(service);
            url.addPathSegment(path);

            Request.Builder request = new Request.Builder().url(url.build());

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
            request.post(body);

            Response response = client.newCall(request.build()).execute();
            if (response.isSuccessful()) {
                return response;
            } else {
                throw new CommonException("common.resposta.http.status").args(String.valueOf(response.code()), url.build().toString());
            }
        } catch (Exception e) {
            throw new CommonException(e.getMessage(), e);
        }
    }

    public Response get(String host, String service, String path, Map<String, String> params) throws CommonException {
        try {
            HttpUrl.Builder url = HttpUrl.parse(host).newBuilder();
            url.addPathSegment(service);
            url.addPathSegment(path);

            if (params != null && !params.isEmpty()) {
                params.forEach((k, v) -> {
                    url.addQueryParameter(k, v);
                });
            }
            Request.Builder request = new Request.Builder().url(url.build());
            Response response = client.newCall(request.build()).execute();
            if (response.isSuccessful()) {
                return response;
            } else {
                throw new CommonException("common.resposta.http.status").args(String.valueOf(response.code()), url.build().toString());
            }
        } catch (IOException e) {
            throw new CommonException(e.getMessage(), e);
        }
    }

    public Response delete(String host, String service, String path) throws CommonException {

        try {
            HttpUrl.Builder url = HttpUrl.parse(host).newBuilder();
            url.addPathSegment(service);
            url.addPathSegment(path);

            Request request = new Request.Builder().url(url.build()).delete().addHeader("content-type", form_urlencoded.toString()).build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response;
            } else {
                throw new CommonException("common.resposta.http.status").args(String.valueOf(response.code()), url.build().toString());
            }
        } catch (IOException e) {
            throw new CommonException(e.getMessage(), e);
        }
    }

}

package br.com.mobile.Model;

import java.util.List;

import br.com.mobile.Domain.ArquivoTO;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Description: FIXME: Document this type Project: virtual-mobile
 *
 * @author breni
 * @version $$
 * @date: 22/07/2019
 */
public interface IArquivoModel {

    @Multipart
    @POST("arquivos")
    Call<ResponseBody> arquivoUpload(@Part MultipartBody.Part arquivo, @Header("Authorization") String token);

    @GET("arquivos")
    Call<List<ArquivoTO>> getArquivos(@Query("nome") String nome);

    @Streaming
    @GET
    Call<ResponseBody> arquivoDownloadStream(@Url String url);

}

package br.com.mobile.Model;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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
}

package br.com.mobile.Resource;

import java.io.File;

import android.content.Context;
import android.net.Uri;

import br.com.mobile.Model.IArquivoModel;
import br.com.mobile.Utils.FileUtils;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Description: FIXME: Document this type Project: virtual-mobile
 *
 * @author breni
 * @version $$
 * @date: 21/07/2019
 */

public class ArquivoResource {
    public static final String  BASE_URL = "http://10.0.2.2:8761/";
    private final IArquivoModel arquivoModel;
    private final Retrofit      retrofit;
    private SessionResource     session;

    public ArquivoResource() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        arquivoModel = retrofit.create(IArquivoModel.class);
    }

    public Call<ResponseBody> Upload(Uri arquivoUri, final Context context) {
        session = new SessionResource(context);
        File arquivoOriginal = new File(FileUtils.getPath(arquivoUri, context));
        RequestBody arquivoPart = RequestBody.create(arquivoOriginal, MultipartBody.FORM);
        MultipartBody.Part arquivo = MultipartBody.Part.createFormData("file", arquivoOriginal.getName(), arquivoPart);
        Call<ResponseBody> callUpload = arquivoModel.arquivoUpload(
                arquivo,
                session.getPreferencesToken());

        return callUpload;
    }
}

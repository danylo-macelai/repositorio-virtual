package br.com.mobile.Resource;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import br.com.mobile.Domain.ArquivoTO;
import br.com.mobile.Model.IArquivoModel;
import br.com.mobile.Utils.FileUtils;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
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
    public static final String    BASE_URL = "http://10.0.2.2:8761/";
    private final IArquivoModel   arquivoModel;
    private final Retrofit        retrofit;
    private final SessionResource session;

    public ArquivoResource(Context context) {
        session = new SessionResource(context);
        retrofit = new Retrofit.Builder()
                .baseUrl(session.getPreferencesApiURL())
                .client(setTimeout())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        arquivoModel = retrofit.create(IArquivoModel.class);

    }

    private OkHttpClient setTimeout() {
        return new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    public Call<ResponseBody> Upload(Uri arquivoUri, Context context) {
        Log.i("UPLOAD", session.getPreferencesToken());
        File arquivoOriginal = new File(FileUtils.getPath(arquivoUri, context));
        RequestBody arquivoPart = RequestBody.create(arquivoOriginal, MultipartBody.FORM);
        MultipartBody.Part arquivo = MultipartBody.Part.createFormData("file", arquivoOriginal.getName(), arquivoPart);
        Call<ResponseBody> callUpload = arquivoModel.arquivoUpload(
                arquivo,
                session.getPreferencesToken());

        return callUpload;
    }

    public Call<List<ArquivoTO>> Arquivos(String nome) {
        Call<List<ArquivoTO>> call = arquivoModel.getArquivos(nome);
        return call;
    }

    public Call<ResponseBody> Download(int idArquivo) {
        Log.i("DOWNLOAD", "" + session.getPreferencesApiURL() + "/arquivos/" + idArquivo);
        Call<ResponseBody> callDownload = arquivoModel
                .arquivoDownloadStream(session.getPreferencesApiURL() + "/arquivos/" + idArquivo);
        return callDownload;
    }
}

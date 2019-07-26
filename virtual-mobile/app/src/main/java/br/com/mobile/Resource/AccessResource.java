package br.com.mobile.Resource;

import org.jetbrains.annotations.NotNull;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import android.content.Context;
import android.util.Log;

import br.com.mobile.CriarTokenMutation;
import br.com.mobile.ValidarTokenQuery;
import okhttp3.OkHttpClient;

/**
 * Description: FIXME: Document this type > *Project: virtual-mobile
 *
 * @author breni > _@date: 18/10/2019 > _@version $$ >
 */
public class AccessResource {
    private static final String   BASE_URL = "http://10.0.2.2:3000/access";
    private static ApolloClient   myApolloClient;
    private final SessionResource session;

    public AccessResource(Context context) {
        this.session = new SessionResource(context);
    }

    public static ApolloClient getMyApolloClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        myApolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .build();
        return myApolloClient;
    }

    public void validarToken() {
        Log.i("APOLLO - VALIDAR", session.getPreferencesToken());
        this.getMyApolloClient().query(
                ValidarTokenQuery.builder().token(session.getPreferencesToken()).build())
                .enqueue(new ApolloCall.Callback<ValidarTokenQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<ValidarTokenQuery.Data> response) {
                        try {
                            if (response.data().validarToken().ativo()) {
                                Log.i("TOKEN-VALIDACAO", "True");
                            }

                        } catch (Exception e) {
                            criarToken();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.i("APOLLO - VALIDAR", e.getMessage());
                    }
                });
    }

    public void criarToken() {
        AccessResource.getMyApolloClient().mutate(
                CriarTokenMutation.builder()
                        .email(session.getPreferencesUserName())
                        .senha(session.getPreferencesPassword()).build())
                .enqueue(new ApolloCall.Callback<CriarTokenMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull final Response<CriarTokenMutation.Data> response) {
                        try {
                            if (!response.data().criarToken().token().equals("")) {
                                session.setPreferencesToken(response.data().criarToken().token());
                            }
                        } catch (Exception e) {
                            Log.i("TOKEN - TASK", response.errors().get(0).message());
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.i("APOLLO ERRO", e.getMessage());
                        criarToken();
                    }
                });
    }

}

package br.com.mobile.Resource;

import org.jetbrains.annotations.NotNull;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloMutationCall;
import com.apollographql.apollo.ApolloQueryCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import android.content.Context;
import android.util.Log;

import br.com.mobile.ConsultarUsuarioQuery;
import br.com.mobile.CriarTokenMutation;
import br.com.mobile.ValidarTokenQuery;
import okhttp3.OkHttpClient;

/**
 * Description: FIXME: Document this type > *Project: virtual-mobile
 *
 * @author breni > _@date: 18/10/2019 > _@version $$ >
 */
public class AccessResource {
    private static ApolloClient   myApolloClient;
    private final SessionResource session;

    public AccessResource(Context context) {
        this.session = new SessionResource(context);
        setAcessURL();
    }

    public ApolloClient getMyApolloClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        myApolloClient = ApolloClient.builder()
                .serverUrl(session.getPreferencesAccessURL())
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
                            atualizarToken();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.i("APOLLO - VALIDAR", e.getMessage());
                    }
                });
    }

    public void atualizarToken() {
        this.getMyApolloClient().mutate(
                CriarTokenMutation.builder()
                        .email(session.getPreferencesUserName())
                        .senha(session.getPreferencesPassword()).build())
                .enqueue(new ApolloCall.Callback<CriarTokenMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<CriarTokenMutation.Data> response) {
                        try {
                            session.setPreferencesToken(response.data().criarToken().token());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("ERROR-TOKEN", response.errors().get(0).message());
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.e("ERROR-TOKEN", e.getMessage());
                    }
                });
    }

    public ApolloQueryCall<ConsultarUsuarioQuery.Data> consultaUsuario() {
        return getMyApolloClient().query(
                ConsultarUsuarioQuery.builder()
                        .token(session.getPreferencesToken()).build());
    }

    public ApolloMutationCall<CriarTokenMutation.Data> criarToken(String email, String senha) {
        return getMyApolloClient().mutate(
                CriarTokenMutation.builder()
                        .email(email)
                        .senha(senha).build());
    }

    private void setAcessURL() {
        session.setPreferencesPortAccess("3000");
        session.setPreferencesAccessURL(
                "http://" + session.getPreferencesIP() + ":" + session.getPreferencesPortAccess() + "/access");
    }

}

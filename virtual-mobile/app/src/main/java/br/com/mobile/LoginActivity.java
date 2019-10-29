package br.com.mobile;

import org.jetbrains.annotations.NotNull;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.com.mobile.Resource.AccessResource;
import br.com.mobile.Resource.SessionResource;
import br.com.mobile.Task.TaskUpdateToken;

/**
 * Description: FIXME: Document this type > *Project: virtual-mobile
 *
 * @author breni > _@date: 18/10/2019 > _@version $$ >
 */
public class LoginActivity extends AppCompatActivity {
    SessionResource session;
    EditText        edtUsername;
    EditText        edtPassword;
    TaskUpdateToken taskUpdateToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new SessionResource(this);
        edtUsername = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtSenha);
        taskUpdateToken = new TaskUpdateToken(this.getApplicationContext());
    }

    public void login(View v) {
        criarToken();
    }

    private boolean isEmpty() {
        boolean result = false;
        if (TextUtils.isEmpty(edtUsername.getText().toString())) {
            edtUsername.setError("O Campo E-MAIL é obrigatorio!");
        } else if (TextUtils.isEmpty(edtPassword.getText().toString())) {
            session.setPreferenceUserName(edtUsername.getText().toString());
            edtPassword.setError("O Campo SENHA é obrigatorio!");

        } else {
            session.setPreferencePassword(edtPassword.getText().toString());
            result = true;
        }
        return result;
    }

    public void criarToken() {
        AccessResource.getMyApolloClient().mutate(
                CriarTokenMutation.builder()
                        .email(session.getPreferencesUserName())
                        .senha(session.getPreferencesPassword()).build())
                .enqueue(new ApolloCall.Callback<CriarTokenMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull final Response<CriarTokenMutation.Data> response) {
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                session.setPreferencesToken(response.data().criarToken().token());
                                if (isEmpty()) {
                                    if (!session.getPreferencesToken().equals("")) {
                                        Log.i("APOLLO - token", session.getPreferencesToken());
                                        Toast.makeText(LoginActivity.this, "LOGIN EFETUADO COM SUCESSO!",
                                                Toast.LENGTH_LONG).show();
                                        taskUpdateToken.run();
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "CAMPOS REQUERIDOS NÃO PREENCHIDOS!",
                                            Toast.LENGTH_LONG)
                                            .show();
                                }

                            }
                        });
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.i("APOLLO ERRO", e.getMessage());
                    }
                });
    }

}

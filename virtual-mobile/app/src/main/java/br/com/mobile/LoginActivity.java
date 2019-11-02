package br.com.mobile;

import org.jetbrains.annotations.NotNull;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
    SessionResource        session;
    EditText               edtUsername;
    EditText               edtPassword;
    TaskUpdateToken        taskUpdateToken;
    Intent                 intent;
    private AccessResource access;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new SessionResource(this);
        access = new AccessResource(this);
        edtUsername = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtSenha);

        intent = new Intent(this, UploadActivity.class);
        verificaSessao(intent);
    }

    public void login(View v) {
        if (isEmpty()) {
            login();
        }
    }

    private void login() {
        access.criarToken(session.getPreferencesUserName(), session.getPreferencesPassword())
                .enqueue(new ApolloCall.Callback<CriarTokenMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull final Response<CriarTokenMutation.Data> response) {
                        try {
                            session.setPreferencesToken(response.data().criarToken().token());
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "LOGIN EFETUADO COM SUCESSO",
                                            Toast.LENGTH_LONG).show();
                                    startActivity(intent);
                                }
                            });
                        } catch (Exception e) {
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), response.errors().get(0).message(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean isEmpty() {
        boolean result = false;
        if (TextUtils.isEmpty(edtUsername.getText().toString())) {
            edtUsername.setError("O Campo E-MAIL é obrigatorio!");
        } else if (TextUtils.isEmpty(edtPassword.getText().toString())) {
            session.setPreferenceUserName(edtUsername.getText().toString());
            edtPassword.setError("O Campo SENHA é obrigatorio!");

        } else {
            session.setPreferenceUserName(edtUsername.getText().toString());
            session.setPreferencePassword(edtPassword.getText().toString());
            result = true;
        }
        return result;
    }

    private void verificaSessao(Intent intent) {
        if (session.getPreferencesApiURL().equals("")) {
            intent = new Intent(LoginActivity.this, ApiConnection.class);
            startActivity(intent);
        } else {
            taskUpdateToken = new TaskUpdateToken(this.getApplicationContext());
        }
    }

}

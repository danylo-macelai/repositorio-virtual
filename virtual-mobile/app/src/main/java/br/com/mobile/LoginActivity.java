package br.com.mobile;

import org.jetbrains.annotations.NotNull;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
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
    private SessionResource session;
    private EditText        edtUsername;
    private EditText        edtPassword;
    private TaskUpdateToken taskUpdateToken;
    private Intent          intent;
    private AccessResource  access;
    private Bundle          extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Login");
        extras = getIntent().getExtras();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            startActivity(new Intent(LoginActivity.this, PesquisaArquivosActivity.class));
            finishAffinity();
            break;
        default:
            break;
        }
        return true;
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

                                    if (extras.getInt("action") == 1) {
                                        intent = new Intent(LoginActivity.this, UserActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        startActivity(intent);
                                        finish();
                                    }

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
            finish();
        } else if (extras.getInt("action") == R.id.user_menu && !session.getPreferencesToken().equals("")) {
            intent = new Intent(LoginActivity.this, UserActivity.class);
            startActivity(intent);
            finish();
        } else if (extras.getInt("action") == 0 && !session.getPreferencesToken().equals("")) {
            startActivity(intent);
            finish();
        }

    }

}

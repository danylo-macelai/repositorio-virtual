package br.com.mobile;

import org.jetbrains.annotations.NotNull;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import br.com.mobile.Resource.AccessResource;
import br.com.mobile.Resource.SessionResource;

public class UserActivity extends AppCompatActivity {
    private TextView             txtUserName, txtEmail, txtPerfilType, txtStatus;
    private AccessResource       access;
    private BottomNavigationView bottomNavigationView;
    private SessionResource      session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtPerfilType = (TextView) findViewById(R.id.txtPerfilType);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        access = new AccessResource(this);
        session = new SessionResource(this);
        consultarUsuario();
        setSelectionMenu();
        setActionBottomNavigationView();

    }

    private void consultarUsuario() {
        access.consultaUsuario().enqueue(new ApolloCall.Callback<ConsultarUsuarioQuery.Data>() {
            @Override
            public void onResponse(@NotNull final Response<ConsultarUsuarioQuery.Data> response) {
                try {
                    UserActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtUserName.setText(response.data().validarToken().nome);
                            txtEmail.setText(response.data().validarToken().email());
                            txtPerfilType.setText(response.data().validarToken().perfilType().toString());
                            if (response.data().validarToken().ativo()) {
                                txtStatus.setText("Ativo");
                            } else {
                                txtStatus.setText("Inativo");
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });
    }

    private void setActionBottomNavigationView() {
        bottomNavigationView
                .setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    Intent intent;

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                        case (R.id.search_menu):
                            intent = new Intent(UserActivity.this, PesquisaArquivosActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            finish();
                            break;

                        case (R.id.apiconnection_menu):
                            intent = new Intent(UserActivity.this, ApiConnection.class);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            finish();
                            break;
                        case (R.id.upload_menu):
                            intent = new Intent(UserActivity.this, LoginActivity.class);
                            intent.putExtra("action", 0);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            finish();
                            break;
                        }
                        return true;
                    }
                });
    }

    private void setSelectionMenu() {
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
    }

    public void desconectarUsuario(View v) {
        session.setRemoveUserSession();
        Toast.makeText(getApplicationContext(), "USUÁRIO DESCONECTADO!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(UserActivity.this, LoginActivity.class);
        intent.putExtra("action", 1);
        startActivity(intent);
    }
}

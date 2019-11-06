package br.com.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import br.com.mobile.Resource.SessionResource;

public class PesquisaArquivosActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private EditText             edtPesquisa;
    private SessionResource      session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa_arquivos);
        session = new SessionResource(this);
        verificaSessao();
        edtPesquisa = (EditText) findViewById(R.id.edtPesquisa);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    Intent intent;

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                        case (R.id.search_menu):
                            intent = new Intent(PesquisaArquivosActivity.this, PesquisaArquivosActivity.class);
                            startActivity(verificaSessao(intent));
                            overridePendingTransition(0, 0);
                            finish();
                            break;

                        case (R.id.upload_menu):
                            intent = new Intent(PesquisaArquivosActivity.this, LoginActivity.class);
                            intent.putExtra("action", 0);
                            startActivity(verificaSessao(intent));
                            overridePendingTransition(0, 0);
                            finish();
                            break;

                        case (R.id.apiconnection_menu):
                            intent = new Intent(PesquisaArquivosActivity.this, ApiConnection.class);
                            startActivity(verificaSessao(intent));
                            overridePendingTransition(0, 0);
                            finish();
                            break;
                        case (R.id.user_menu):
                            intent = new Intent(PesquisaArquivosActivity.this, LoginActivity.class);
                            intent.putExtra("action", R.id.user_menu);
                            startActivity(verificaSessao(intent));
                            overridePendingTransition(0, 0);
                            finish();
                            break;
                        }
                        return true;
                    }
                });

    }

    public void getNomeConsulta(View v) {
        String consulta = edtPesquisa.getText().toString();

        Intent intent = new Intent(PesquisaArquivosActivity.this, ListArquivosActivity.class);
        intent.putExtra("consulta", consulta);
        startActivity(intent);
    }

    private Intent verificaSessao(Intent intent) {
        if (session.getPreferencesApiURL().equals("")) {
            intent = new Intent(PesquisaArquivosActivity.this, ApiConnection.class);
        }
        return intent;
    }

    private void verificaSessao() {
        if (session.getPreferencesApiURL().equals("")) {
            Intent intent = new Intent(PesquisaArquivosActivity.this, ApiConnection.class);
            startActivity(intent);
        }
    }
}

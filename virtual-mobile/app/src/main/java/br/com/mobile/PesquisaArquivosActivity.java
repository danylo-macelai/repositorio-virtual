package br.com.mobile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import br.com.mobile.Domain.ArquivoTO;
import br.com.mobile.Resource.ArquivoResource;
import br.com.mobile.Resource.SessionResource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PesquisaArquivosActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private List<ArquivoTO>      arquivos;
    private EditText             edtPesquisa;
    private ArquivoResource      arquivoResource;
    private SessionResource      session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa_arquivos);
        session = new SessionResource(this);
        verificaSessao();
        arquivos = new ArrayList<>();
        arquivoResource = new ArquivoResource();
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
                            intent = new Intent(PesquisaArquivosActivity.this, UploadActivity.class);
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
                        }
                        return true;
                    }
                });

    }

    public void getArquivos(View v) {
        String nome = edtPesquisa.getText().toString();
        // String nome = edtPesquisa.getText().toString();
        Call<List<ArquivoTO>> callArquivos = arquivoResource.Arquivos(nome);

        callArquivos.enqueue(new Callback<List<ArquivoTO>>() {
            @Override
            public void onResponse(Call<List<ArquivoTO>> call, Response<List<ArquivoTO>> response) {

                Iterator<ArquivoTO> iterator = response.body().iterator();
                while (iterator.hasNext()) {
                    ArquivoTO arquivoTo = iterator.next();
                    Log.i("ARQUIVOS", "\n ID: " + arquivoTo.getId() + " NOME: " + arquivoTo.getNome() + " MimeType: "
                            + arquivoTo.getMimeType());
                }

            }

            @Override
            public void onFailure(Call<List<ArquivoTO>> call, Throwable t) {

            }
        });
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

package br.com.mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ApiConnection extends AppCompatActivity {
    private static final String FILE_NAME = "apiURL";
    EditText                    edtUrl;
    EditText                    edtPort;
    TextView                    txtStatus;
    ImageView                   imgStatus;
    SharedPreferences           prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_connection);
        edtUrl = (EditText) findViewById(R.id.edtUrl);
        edtPort = (EditText) findViewById(R.id.edtPort);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        imgStatus = (ImageView) findViewById(R.id.imgStatus);
        prefs = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        verificaStatus();
    }

    public void salvar(View v) {
        String apiUrl = edtUrl.getText().toString();
        String apiPort = edtPort.getText().toString();
        String apiConnection = "";

        if (apiPort.equals("")) {
            apiConnection = "http://" + apiUrl;
        } else {
            apiConnection = "http://" + apiUrl + ":" + apiPort;
        }
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(FILE_NAME, apiConnection);
        prefsEditor.apply();
        verificaStatus();
    }

    private String carregar() {
        String urlConnection = prefs.getString(FILE_NAME, "");
        return urlConnection;
    }

    private void verificaStatus() {
        String urlConnection = carregar();
        if (!urlConnection.equals("")) {
            txtStatus.setText("A Conexão com a API foi configurada com sucesso. URL: " + urlConnection);
            imgStatus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.success_api_connection));
        } else {
            txtStatus.setText("Conexão não configurada!");
            imgStatus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.error_api_connection));
        }
        txtStatus.setVisibility(View.VISIBLE);
        imgStatus.setVisibility(View.VISIBLE);
    }

}

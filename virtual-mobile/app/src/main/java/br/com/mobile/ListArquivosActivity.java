package br.com.mobile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import br.com.mobile.Adapter.ArquivoListAdapter;
import br.com.mobile.Domain.ArquivoTO;
import br.com.mobile.Resource.ArquivoResource;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListArquivosActivity extends AppCompatActivity implements ArquivoListAdapter.OnItemClickListener {
    private ArquivoTO          arquivo;
    private List<ArquivoTO>    arquivos;
    private List<String>       fileDoneList;
    private RecyclerView       rcView;
    private ArquivoListAdapter adapter;
    private ArquivoResource    arquivoResource;
    private PopupMenu          popupMenu;
    private ProgressDialog     progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_arquivos);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Lista de Arquivos");

        rcView = (RecyclerView) findViewById(R.id.rcvFilesList);
        Intent intent = getIntent();
        String consulta = (String) intent.getSerializableExtra("consulta");
        arquivoResource = new ArquivoResource(this);
        arquivos = new ArrayList<>();
        fileDoneList = new ArrayList<>();

        getArquivos(consulta);

    }

    private void getArquivos(String consulta) {
        Call<List<ArquivoTO>> callArquivos = arquivoResource.Arquivos(consulta);

        callArquivos.enqueue(new Callback<List<ArquivoTO>>() {
            @Override
            public void onResponse(Call<List<ArquivoTO>> call, Response<List<ArquivoTO>> response) {

                Iterator<ArquivoTO> iterator = response.body().iterator();
                while (iterator.hasNext()) {
                    ArquivoTO arquivoTo = iterator.next();
                    arquivo = arquivoTo;
                    arquivos.add(arquivoTo);
                    fileDoneList.add("notdownloading");
                }
                ListArquivosActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new ArquivoListAdapter(arquivos, fileDoneList, ListArquivosActivity.this);
                        rcView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        rcView.setHasFixedSize(true);
                        rcView.setAdapter(adapter);

                    }
                });

            }

            @Override
            public void onFailure(Call<List<ArquivoTO>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onItemClick(final int position) {
        Log.d("CLICK", "TESTE DE ITEM CLICK: " + position);

    }

    @Override
    public void onButtonClick(final int position, View v, Context context) {
        popupMenu = new PopupMenu(context, v);
        popupMenu.inflate(R.menu.arquivo_options);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                case R.id.download_menu:
                    downloadFile(arquivos.get(position), position);
                    return true;

                default:
                    return false;

                }
            }
        });
        popupMenu.show();
    }

    private void downloadFile(final ArquivoTO arquivo, final int posititon) {
        Call<ResponseBody> call = arquivoResource.Download(arquivo.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                new AsyncTask<String, Integer, String>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog = new ProgressDialog(ListArquivosActivity.this);
                        progressDialog.setTitle("DOWNLOAD DO ARQUIVO");
                        progressDialog.setMessage("Baixando, Por favor espere!");
                        progressDialog.setIndeterminate(false);
                        progressDialog.setMax(100);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setCancelable(true);
                        progressDialog.show();
                    }

                    @Override
                    protected String doInBackground(String... strings) {
                        try {
                            // todo change the file location/name according to your needs
                            File fileDownload = new File(
                                    getExternalFilesDir(null) + File.separator + arquivo.getNome());

                            InputStream inputStream = null;
                            OutputStream outputStream = null;

                            try {
                                byte[] fileReader = new byte[4096];
                                long fileSize = response.body().contentLength();
                                long fileSizeDownloaded = 0;

                                inputStream = new BufferedInputStream(response.body().byteStream());
                                outputStream = new FileOutputStream(fileDownload);

                                while (true) {
                                    int read = inputStream.read(fileReader);

                                    if (read == -1) {
                                        break;
                                    }

                                    outputStream.write(fileReader, 0, read);

                                    fileSizeDownloaded += read;
                                    publishProgress((int) (fileSizeDownloaded * 100 / fileSize));

                                    Log.d("DOWNLOAD", "file download: " + fileSizeDownloaded + " of " + fileSize);
                                }

                                outputStream.flush();

                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                if (inputStream != null) {
                                    inputStream.close();
                                }

                                if (outputStream != null) {
                                    outputStream.close();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(Integer... values) {
                        super.onProgressUpdate(values);
                        progressDialog.setProgress(values[0]);
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        progressDialog.dismiss();
                        fileDoneList.set(posititon, "downloading");
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(),
                                "Download Concluido! O arquivo se encontra em: "
                                        + getExternalFilesDir(null).getAbsolutePath() + File.separator
                                        + arquivo.getNome(),
                                Toast.LENGTH_LONG).show();
                    }
                }.execute();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("DOWNLOAD", call.toString());
            }
        });
    }

}

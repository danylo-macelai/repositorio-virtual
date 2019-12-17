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
import br.com.mobile.Domain.ContentTO;
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
    private int                page = 0;
    private String             consulta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_arquivos);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Lista de Arquivos");

        Intent intent = getIntent();
        consulta = (String) intent.getSerializableExtra("consulta");
        arquivoResource = new ArquivoResource(this);
        arquivos = new ArrayList<>();
        fileDoneList = new ArrayList<>();

        rcView = (RecyclerView) findViewById(R.id.rcvFilesList);
        adapter = new ArquivoListAdapter(arquivos, fileDoneList, ListArquivosActivity.this);
        rcView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rcView.setHasFixedSize(true);
        rcView.setAdapter(adapter);

        getArquivos(consulta, page);

    }

    private void getArquivos(String consulta, final int pagination) {
        int listPagination = 0;
        Call<ContentTO> callArquivos = arquivoResource.Arquivos(consulta, pagination);

        callArquivos.enqueue(new Callback<ContentTO>() {
            @Override
            public void onResponse(Call<ContentTO> call, Response<ContentTO> response) {
                try {

                    ContentTO content = response.body();
                    List<ArquivoTO> list = content.getListArchives();
                    Iterator<ArquivoTO> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        ArquivoTO arquivoTo = iterator.next();
                        arquivos.add(arquivoTo);
                        fileDoneList.add("notdownloading");
                    }
                    if (page != 0) {
                        Toast.makeText(ListArquivosActivity.this, "MAIS ITENS FORAM ADICIONADOS A LISTA!!",
                                Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();

                } catch (Exception ex) {
                    Log.e("ERROR LIST FILES", response.errorBody().toString());
                    Toast.makeText(ListArquivosActivity.this, "ERRO: NÃO TEM ARQUIVOS PARA CONSULTAR!!",
                            Toast.LENGTH_LONG).show();
                    if (page != 0) {
                        page--;
                    }

                    ex.printStackTrace();
                }

                // Log.e("ERROR LIST FILES", response.errorBody().toString());
            }

            @Override
            public void onFailure(Call<ContentTO> call, Throwable t) {
                Log.e("ERROR LIST FILES: ", t.getMessage());
                t.printStackTrace();
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

    public void carregarItens(View v) {
        page++;
        Log.i("PAGINATION", String.valueOf(page));
        getArquivos(consulta, page);
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

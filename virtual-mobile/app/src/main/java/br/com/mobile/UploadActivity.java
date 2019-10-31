package br.com.mobile;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import br.com.mobile.Adapter.UploadListAdapter;
import br.com.mobile.Resource.ArquivoResource;
import br.com.mobile.Resource.SessionResource;
import br.com.mobile.Task.TaskUpdateToken;
import br.com.mobile.Utils.FileUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Description: FIXME: Document this type > *Project: virtual-mobile
 *
 * @author breni > _@date: 22/10/2019 > _@version $$ >
 */
public class UploadActivity extends AppCompatActivity {

    private static final int     RESULT_LOAD_FILES = 1;
    private Button               btnSelectFiles;
    private RecyclerView         listUploadFiles;
    private List<String>         fileNameList;
    private List<String>         fileDoneList;
    private UploadListAdapter    uploadListAdapter;
    private ArquivoResource      arquivoResource;
    private SessionResource      session;
    private BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        session = new SessionResource(this);
        btnSelectFiles = (Button) findViewById(R.id.btnSelectFiles);
        listUploadFiles = (RecyclerView) findViewById(R.id.rcvFilesUpload);

        arquivoResource = new ArquivoResource();
        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();
        new TaskUpdateToken(this).run();

        uploadListAdapter = new UploadListAdapter(fileNameList, fileDoneList);

        // RecyclerView

        listUploadFiles.setLayoutManager(new LinearLayoutManager(this));
        listUploadFiles.setHasFixedSize(true);
        listUploadFiles.setAdapter(uploadListAdapter);

        btnSelectFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadListAdapter.clearList();
                uploadListAdapter.notifyDataSetChanged();
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select files"), RESULT_LOAD_FILES);
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    Intent intent;

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                        case (R.id.search_menu):
                            intent = new Intent(UploadActivity.this, PesquisaArquivosActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            finish();
                            break;

                        case (R.id.upload_menu):
                            intent = new Intent(UploadActivity.this, UploadActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            finish();
                            break;

                        case (R.id.apiconnection_menu):
                            intent = new Intent(UploadActivity.this, ApiConnection.class);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            finish();
                            break;
                        }
                        return true;
                    }
                });

        if (session.getPreferencesToken().equals("")) {
            Intent intent = new Intent(UploadActivity.this, LoginActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_FILES && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                int totalItemsSelected = data.getClipData().getItemCount();

                for (int i = 0; i < totalItemsSelected; i++) {

                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    String fileName = FileUtils.getFileName(fileUri, UploadActivity.this);

                    fileNameList.add(fileName);
                    fileDoneList.add("uploading");
                    uploadListAdapter.notifyDataSetChanged();
                    final int finalI = i;

                    Call<ResponseBody> uploadList = arquivoResource.Upload(fileUri, getApplicationContext());

                    uploadList.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            fileDoneList.remove(finalI);
                            fileDoneList.add(finalI, "done");
                            uploadListAdapter.notifyDataSetChanged();
                            try {
                                Toast.makeText(getApplicationContext(), response.body().string(), Toast.LENGTH_LONG)
                                        .show();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });

                }
                // SELECT MULTIPLE FILES
            } else if (data.getData() != null) {
                // SINGLE SELECT
                Uri fileUri = data.getData();
                final String fileName = FileUtils.getFileName(fileUri, this);

                fileNameList.add(fileName);
                fileDoneList.add("uploading");
                uploadListAdapter.notifyDataSetChanged();

                Call<ResponseBody> uploadList = arquivoResource.Upload(fileUri, getApplicationContext());

                uploadList.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        fileDoneList.remove(0);
                        fileDoneList.add(0, "done");
                        uploadListAdapter.notifyDataSetChanged();
                        try {
                            Toast.makeText(getApplicationContext(), response.body().string(), Toast.LENGTH_LONG)
                                    .show();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        }
    }
}

package br.com.mobile.Adapter;

import java.util.List;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.mobile.R;

/**
 * Description: FIXME: Document this type > *Project: virtual-mobile
 *
 * @author breni > _@date: 23/10/2019 > _@version $$ >
 */
public class UploadListAdapter extends RecyclerView.Adapter<UploadListAdapter.ViewHolder> {

    public List<String> fileNameList;
    public List<String> fileDoneList;

    public UploadListAdapter(List<String> fileNameList, List<String> fileDoneList) {
        this.fileNameList = fileNameList;
        this.fileDoneList = fileDoneList;
    }

    public void clearList() {
        this.fileDoneList.clear();
        this.fileNameList.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_item_upload, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        String fileName = fileNameList.get(i);
        viewHolder.fileNameView.setText(fileName);

        String fileDone = fileDoneList.get(i);

        if (fileDone.equals("uploading")) {
            viewHolder.fileDoneView.setImageResource(R.drawable.progress);
        } else {
            viewHolder.fileDoneView.setImageResource(R.drawable.checked);
        }
    }

    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;

        public TextView  fileNameView;
        public ImageView fileDoneView;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            fileNameView = (TextView) view.findViewById(R.id.txtFileName);
            fileDoneView = (ImageView) view.findViewById(R.id.imgFileUploading);
        }
    }
}

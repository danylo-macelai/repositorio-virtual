package br.com.mobile.Adapter;

import java.util.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import br.com.mobile.R;
import br.com.mobile.Domain.ArquivoTO;
import br.com.mobile.Utils.FileUtils;

/**
 * Description: FIXME: Document this type > *Project: virtual-mobile
 *
 * @author breni > _@date: 23/10/2019 > _@version $$ >
 */
public class ArquivoListAdapter extends RecyclerView.Adapter<ArquivoListAdapter.ViewHolder> {

    public List<ArquivoTO>            files;
    private final List<String>        fileDoneList;
    private final OnItemClickListener onItemClickListener;
    private static final long         MEGABYTE = 1024L * 1024L;

    public ArquivoListAdapter(List<ArquivoTO> files, List<String> fileDoneList,
            OnItemClickListener onItemClickListener) {
        this.files = files;
        this.fileDoneList = fileDoneList;
        this.onItemClickListener = onItemClickListener;
    }

    public void clearList() {
        this.files.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_lista, viewGroup, false);
        return new ViewHolder(v, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        String fileName = files.get(i).getNome();
        viewHolder.fileNameView.setText(fileName);
        String extension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        viewHolder.fileImageView.setImageResource(FileUtils.getFileImageExtension(extension));

        viewHolder.fileSizeView.setText(bytesToMeg(files.get(i).getTamanho()) + " mb");

        if (fileDoneList.get(i).equals("downloading")) {
            viewHolder.fileImgSucess.setImageResource(R.drawable.download_concluido);
        }

    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View                       view;
        public TextView            fileNameView;
        public TextView            fileSizeView;
        public ImageView           fileImageView;
        public ImageView           fileImgView;
        public PopupMenu           popupMenu;
        public OnItemClickListener onItemClickListener;
        public ImageView           fileImgSucess;

        public ViewHolder(View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);
            view = itemView;

            fileNameView = (TextView) view.findViewById(R.id.txtFileNameList);
            fileSizeView = (TextView) view.findViewById(R.id.txtFileSizeList);
            fileImageView = (ImageView) view.findViewById(R.id.imgFileList);
            fileImgView = (ImageView) view.findViewById(R.id.imgMoreOptionsList);
            fileImgSucess = (ImageView) view.findViewById(R.id.imgDownloadSuccess);
            this.onItemClickListener = onItemClickListener;
            popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.arquivo_options);

            fileImgView.setOnClickListener(this);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.imgMoreOptionsList) {
                onItemClickListener.onButtonClick(getAdapterPosition(), v, view.getContext());
            } else {
                onItemClickListener.onItemClick(getAdapterPosition());
            }

        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onButtonClick(int position, View v, Context context);
    }

    private static long bytesToMeg(long bytes) {
        return bytes / MEGABYTE;
    }
}

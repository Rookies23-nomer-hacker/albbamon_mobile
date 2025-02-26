package com.example.albbamon.Resume;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.albbamon.R;

import java.util.List;

public class PortfolioFileAdapter extends RecyclerView.Adapter<PortfolioFileAdapter.FileViewHolder> {

    private List<String> fileList;
    private OnFileListChangeListener onFileListChangeListener;

    public interface OnFileListChangeListener {
        void onFileListChanged();
    }

    public PortfolioFileAdapter(List<String> fileList, OnFileListChangeListener listener) {
        this.fileList = fileList;
        this.onFileListChangeListener = listener;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_portfolio_file, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        String fileName = fileList.get(position);
        holder.fileNameTextView.setText(fileName);

        holder.btnDelete.setOnClickListener(v -> {
            fileList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, fileList.size());
            onFileListChangeListener.onFileListChanged(); // ✅ 파일 삭제 후 UI 업데이트
        });
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {
        TextView fileNameTextView;
        ImageView btnDelete;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameTextView = itemView.findViewById(R.id.fileNameTextView);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

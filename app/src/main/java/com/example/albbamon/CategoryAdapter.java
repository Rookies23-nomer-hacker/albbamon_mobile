package com.example.albbamon;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<String> categories;
    private int selectedPosition = 0; // 선택된 위치 저장
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public CategoryAdapter(List<String> categories, OnItemClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged(); // UI 갱신
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int currentPosition = holder.getAdapterPosition(); // 올바른 위치 가져오기

        holder.tvCategory.setText(categories.get(currentPosition));

        // 선택된 경우에만 배경 표시
        if (selectedPosition == currentPosition) {
            holder.itemView.setBackgroundColor(Color.WHITE); // 선택된 항목 배경 흰색
            holder.tvCategory.setTextColor(Color.BLACK); // 글씨 검정
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT); // 기본 배경 투명
            holder.tvCategory.setTextColor(Color.GRAY); // 기본 글씨 색상
        }

        holder.itemView.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition(); // 클릭된 항목 업데이트
            notifyDataSetChanged(); // UI 갱신
            listener.onItemClick(selectedPosition);
        });
    }


    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.category_text);
        }
    }
}


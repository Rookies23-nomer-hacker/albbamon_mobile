package com.example.albbamon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MyInquiriesAdapter extends RecyclerView.Adapter<MyInquiriesAdapter.ViewHolder> {

    private List<InquiryItem> inquiryList;

    public MyInquiriesAdapter(List<InquiryItem> inquiryList) {
        this.inquiryList = inquiryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inquiry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InquiryItem inquiry = inquiryList.get(position);
        holder.tvCategory.setText(inquiry.getCategory());
        holder.tvTitle.setText(inquiry.getTitle());
        holder.tvDate.setText(inquiry.getDate());
        holder.tvStatus.setText(inquiry.getStatus());
    }

    @Override
    public int getItemCount() {
        return inquiryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvTitle, tvDate, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}


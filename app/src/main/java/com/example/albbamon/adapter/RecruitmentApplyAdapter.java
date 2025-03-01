package com.example.albbamon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.albbamon.R;
import com.example.albbamon.model.RecruitmentApply;

import java.util.List;

public class RecruitmentApplyAdapter extends RecyclerView.Adapter<RecruitmentApplyAdapter.ViewHolder> {
    private List<RecruitmentApply> applyList;
    private Context context;

    public RecruitmentApplyAdapter(List<RecruitmentApply> applyList, Context context) {
        this.applyList = applyList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textUserName, textStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            textUserName = itemView.findViewById(R.id.textUserName);
            textStatus = itemView.findViewById(R.id.textStatus);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_apply, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecruitmentApply apply = applyList.get(position);
        holder.textUserName.setText(apply.getUserName());
        holder.textStatus.setText(apply.getApplyStatus());
    }

    @Override
    public int getItemCount() {
        return applyList.size();
    }
}

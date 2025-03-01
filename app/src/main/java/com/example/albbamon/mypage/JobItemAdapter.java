package com.example.albbamon.mypage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.albbamon.R;
import com.example.albbamon.model.JobItemModel;
import com.example.albbamon.mypage.ManagementApplyer;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class JobItemAdapter extends RecyclerView.Adapter<JobItemAdapter.ViewHolder> {

    private List<JobItemModel> jobItemList;
    private Context context;

    public JobItemAdapter(List<JobItemModel> jobItemList, Context context) {
        this.jobItemList = jobItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_item_button, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JobItemModel jobItem = jobItemList.get(position);
        holder.tvDate.setText(jobItem.getDate());
        holder.tvApplyType.setText(jobItem.getApplyType());
        holder.tvCompany.setText(jobItem.getCompany());
        holder.tvJobTitle.setText(jobItem.getJobTitle());
        holder.tvDeadline.setText("마감 " + jobItem.getDeadline());

        // 버튼 클릭 시 RecruitmentApplyListActivity 이동
        holder.btnReview.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecruitmentApplyListActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return jobItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvApplyType, tvCompany, tvJobTitle, tvDeadline;
        ImageView ivCall, ivMore;
        MaterialButton btnReview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvApplyType = itemView.findViewById(R.id.tv_apply_type);
            tvCompany = itemView.findViewById(R.id.tv_company);
            tvJobTitle = itemView.findViewById(R.id.tv_job_title);
            tvDeadline = itemView.findViewById(R.id.tv_deadline);
            ivCall = itemView.findViewById(R.id.iv_call);
            ivMore = itemView.findViewById(R.id.iv_more);
            btnReview = itemView.findViewById(R.id.btn_review);
        }
    }
}

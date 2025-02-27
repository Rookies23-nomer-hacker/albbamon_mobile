package com.example.albbamon;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    private List<JobItem> jobList;

    public JobAdapter(List<JobItem> jobList) {
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        JobItem job = jobList.get(position);

        // NullPointerException 방지
        if (holder.tvTitle != null) {
            holder.tvTitle.setText(job.getTitle());
        }
        if (holder.tvCompanyName != null) {
            holder.tvCompanyName.setText(job.getCompany());
        }
        if (holder.tvLocation != null) {
            holder.tvLocation.setText(job.getLocation());
        }
        if (holder.tvSalary != null) {
            holder.tvSalary.setText(job.getSalary());
        }
        if (holder.tvTime != null) {
            holder.tvTime.setText(job.getTime());
        }

        // 🔹 공고 아이템 클릭 시 상세 페이지로 이동
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), JobDetailActivity.class);
            intent.putExtra("title", job.getTitle());
            intent.putExtra("company", job.getCompany());
            intent.putExtra("location", job.getLocation());
            intent.putExtra("salary", job.getSalary());
            intent.putExtra("time", job.getTime());
            v.getContext().startActivity(intent);
        });

        // 지원하기 버튼 클릭 이벤트 추가
        holder.btnApply.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), job.getTitle() + " 지원 완료!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return jobList != null ? jobList.size() : 0;
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCompanyName, tvLocation, tvSalary, tvTime;
        Button btnApply; // 지원하기 버튼 추가

        public JobViewHolder(View itemView) {
            super(itemView);

            // 올바른 ID 연결 확인
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCompanyName = itemView.findViewById(R.id.tvCompanyName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvSalary = itemView.findViewById(R.id.tvSalary);
            tvTime = itemView.findViewById(R.id.tvTime);
            btnApply = itemView.findViewById(R.id.btnApply); // 지원하기 버튼 추가
        }
    }
}

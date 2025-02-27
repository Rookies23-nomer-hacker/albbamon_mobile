package com.example.albbamon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    private List<JobModel> jobList;
    private OnItemClickListener listener;

    // 인터페이스 정의 (클릭 이벤트 전달)
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // 클릭 리스너 설정
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public JobAdapter(List<JobModel> jobList) {
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        JobModel job = jobList.get(position);
        holder.jobTitle.setText(job.getTitle());
        holder.salary.setText(job.getSalary());
        holder.jobImage.setImageResource(job.getImageResId());
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, salary;
        ImageView jobImage;

        public JobViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.job_title);
            salary = itemView.findViewById(R.id.job_salary);
            jobImage = itemView.findViewById(R.id.job_image);

            // 아이템 클릭 이벤트 설정
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}

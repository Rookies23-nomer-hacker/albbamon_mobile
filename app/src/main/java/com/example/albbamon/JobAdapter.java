package com.example.albbamon;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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

        // 🔥 커뮤니티 게시글이면 subtitle 표시, 아니면 salary 표시
        if (job.getSubtitle() != null) {
            holder.subtitle.setText(job.getSubtitle()); // 커뮤니티 데이터 (작성자 정보)
            holder.salary.setVisibility(View.GONE); // 🔥 연봉 정보 숨기기
        } else if (job.getSalary() != null) {
            holder.salary.setText(job.getSalary()); // 일반 알바 데이터
            holder.salary.setVisibility(View.VISIBLE); // 🔥 연봉 정보 표시
        } else {
            holder.subtitle.setText(""); // 데이터가 없을 경우 빈 값
            holder.salary.setVisibility(View.GONE); // 기본적으로 숨김
        }

        // 🔥 file 값이 NULL이면 기본 이미지(b_logo) 표시
        if (job.getImageUrl() == null || job.getImageUrl().isEmpty()) {
            Log.d("JobAdapter", "❌ 이미지 없음, 기본 이미지 사용");
            holder.jobImage.setImageResource(R.drawable.b_logo);
        } else {
            Log.d("JobAdapter", "🔥 이미지 로드 시도: " + job.getImageUrl());

            Glide.with(holder.itemView.getContext())
                    .load(job.getImageUrl())
                    .placeholder(R.drawable.b_logo) // ✅ 로딩 중 기본 이미지
                    .error(R.drawable.b_logo) // ✅ 에러 시 기본 이미지
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // ✅ 캐싱 활성화
                    .into(holder.jobImage);
        }

        holder.itemView.setOnClickListener(v -> {
            Log.d("JobAdapter", "🔥 클릭된 아이템: " + job.getTitle() + ", ID: " + job.getId());

            if (listener != null) {
                Log.d("JobAdapter", "✅ onItemClick 실행");
                listener.onItemClick(position);
            } else {
                Log.e("JobAdapter", "❌ onItemClickListener가 null 상태!");
            }
        });
    }




    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, subtitle, salary; // 🔥 subtitle 추가
        ImageView jobImage;

        public JobViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.job_title);
            subtitle = itemView.findViewById(R.id.job_subtitle);
            salary = itemView.findViewById(R.id.job_salary);// 🔥 subtitle ID 추가
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

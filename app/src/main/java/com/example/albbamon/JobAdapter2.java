package com.example.albbamon;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.albbamon.model.RecruitmentModel;
import java.util.List;

public class JobAdapter2 extends RecyclerView.Adapter<JobAdapter2.JobViewHolder> {

    private Context context;
    private List<RecruitmentModel> jobList;
    private OnItemClickListener listener;

    // 🔥 클릭 리스너 인터페이스
    public interface OnItemClickListener {
        void onApplyClick(Long jobId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public JobAdapter2(Context context, List<RecruitmentModel> jobList) {
        this.context = context;
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_job2, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        RecruitmentModel job = jobList.get(position);

        holder.tvTitle.setText(job.getTitle());

        // ✅ 회사명 설정 (널 체크)
        String companyText = (job.getCompany() != null && !job.getCompany().isEmpty()) ? job.getCompany() : "회사명 없음";
        holder.tvCompany.setText(companyText);

        // ✅ 급여 정보 설정 (널 체크)
        String wageText = (job.getWage() != null) ? "급여: " + job.getWage() + "원" : "급여 정보 없음";
        holder.tvSalary.setText(wageText);

        // ✅ 지원하기 버튼 클릭 리스너 설정
        holder.btnApply.setOnClickListener(v -> {
            if (listener != null) {
                listener.onApplyClick(job.getId());
            }
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecruitmentViewActivity.class);
            intent.putExtra("job_id", job.getId());  // ✅ job_id 전달
            Log.d("NAVIGATION", "Moving to RecruitmentViewActivity with job_id: " + job.getId());
            context.startActivity(intent);
        });



    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCompany, tvSalary;
        Button btnApply;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCompany = itemView.findViewById(R.id.tvCompanyName); // ✅ 회사명 추가
            tvSalary = itemView.findViewById(R.id.tvSalary);
            btnApply = itemView.findViewById(R.id.btnApply);
        }
    }
}

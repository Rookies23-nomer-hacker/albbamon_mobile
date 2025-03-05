package com.example.albbamon.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.albbamon.R;
import com.example.albbamon.model.MyRecruitment;
import com.example.albbamon.mypage.RecruitmentApplyListActivity;

import java.util.List;

public class MyRecruitmentAdapter extends RecyclerView.Adapter<MyRecruitmentAdapter.ViewHolder> {
    private List<MyRecruitment> recruitmentList;
    private Context context;

    public MyRecruitmentAdapter(List<MyRecruitment> recruitmentList, Context context) {
        this.recruitmentList = recruitmentList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvStatus, tvDeadline, tvCompany, tvJobTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvDeadline = itemView.findViewById(R.id.tv_deadline);
            tvCompany = itemView.findViewById(R.id.tv_company);
            tvJobTitle = itemView.findViewById(R.id.tv_job_title);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_item_button, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyRecruitment recruitment = recruitmentList.get(position);

        holder.tvDate.setText(recruitment.getFormattedCreateDate());
        holder.tvDeadline.setText(recruitment.getFormattedDueDate());

        holder.tvStatus.setText(recruitment.getExpiredStatus());
        holder.tvCompany.setText(recruitment.getCompany());
        holder.tvJobTitle.setText(recruitment.getTitle());

        // btnReview 클릭 시 recruitmentId를 Intent로 넘기기
        holder.itemView.findViewById(R.id.btn_review).setOnClickListener(v -> {
            Log.d("MyRecruitmentAdapter", "📌 recruitmentId 전달: " + recruitment.getRecruitmentId());

            Intent intent = new Intent(context, RecruitmentApplyListActivity.class);
            intent.putExtra("recruitmentId", recruitment.getRecruitmentId()); // recruitmentId 추가
            intent.putExtra("recruitmentTitle", recruitment.getTitle()); // recruitmentId 추가

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recruitmentList.size();
    }
}

package com.example.albbamon.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.albbamon.R;
import com.example.albbamon.model.RecruitmentApply;
import com.example.albbamon.mypage.RecruitementResultActivity;

import java.util.List;

public class RecruitmentApplyAdapter extends RecyclerView.Adapter<RecruitmentApplyAdapter.ViewHolder> {
    private Context context;
    private long recruitmentId;
    private List<RecruitmentApply> applyList;

    public RecruitmentApplyAdapter(Context context, long recruitmentId, List<RecruitmentApply> applyList) {
        this.context = context;
        this.recruitmentId = recruitmentId;
        this.applyList = applyList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textUserName, textStatus, textSchool, textPortfolio;

        public ViewHolder(View itemView) {
            super(itemView);
            textUserName = itemView.findViewById(R.id.textUserName);
            textSchool = itemView.findViewById(R.id.textSchool);
            textPortfolio = itemView.findViewById(R.id.textPortfolio);
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
        holder.textSchool.setText(apply.getSchool());
        holder.textStatus.setText(apply.getApplyStatus());

        // 포트폴리오 URL 클릭 시 브라우저에서 열기
        holder.textPortfolio.setText("포트폴리오 보기");
        holder.textPortfolio.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(apply.getPortfolioUrl()));
            context.startActivity(intent);
        });

        // 클릭하면 상세 이력서 페이지로 이동
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), RecruitementResultActivity.class);
            intent.putExtra("recruitmentId", recruitmentId);  // 채용 공고 ID 전달
            intent.putExtra("applyId", apply.getApplyId());  // 지원서 ID 전달
            intent.putExtra("resumeId", apply.getresumeId());  // 지원서 ID 전달
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return applyList.size();
    }
}

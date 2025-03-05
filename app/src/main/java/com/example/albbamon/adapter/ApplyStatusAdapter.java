package com.example.albbamon.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.albbamon.R;
import com.example.albbamon.model.ApplyStatusModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ApplyStatusAdapter extends RecyclerView.Adapter<ApplyStatusAdapter.ViewHolder> {
    private List<ApplyStatusModel> applyVoList;

    public ApplyStatusAdapter(List<ApplyStatusModel> applyVoList) {
        this.applyVoList = applyVoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ApplyStatusModel applyStatusModel = applyVoList.get(position);

        Log.d("ApplyStatusAdapter", "Position: " + position);
        Log.d("ApplyStatusAdapter", "Recruitment Title: " + applyStatusModel.getRecruitmentTitle());
        Log.d("ApplyStatusAdapter", "Company: " + applyStatusModel.getCompany());
        Log.d("ApplyStatusAdapter", "Wage: " + applyStatusModel.getRecruitmentWage());
        Log.d("ApplyStatusAdapter", "Status: " + applyStatusModel.getStatus());
        Log.d("ApplyStatusAdapter", "Create Date: " + applyStatusModel.getCreateDate());

        holder.recruitmentTitle.setText(applyStatusModel.getRecruitmentTitle());
        holder.company.setText(applyStatusModel.getCompany());
        holder.wage.setText(String.valueOf(applyStatusModel.getRecruitmentWage()));
        holder.status.setText(applyStatusModel.getStatus());
        // holder.createDate.setText(applyStatusModel.getCreateDate());

        // 날짜 변환 (ISO 8601 → yyyy-MM-dd)
        String formattedDate = formatDate(applyStatusModel.getCreateDate());
        holder.createDate.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return applyVoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView recruitmentTitle, company, wage, status, createDate;

        public ViewHolder(View itemView) {
            super(itemView);
            recruitmentTitle = itemView.findViewById(R.id.recruitmentTitle);
            company = itemView.findViewById(R.id.company);
            wage = itemView.findViewById(R.id.wage);
            status = itemView.findViewById(R.id.status);
            createDate = itemView.findViewById(R.id.createDate);
        }
    }

    private String formatDate(String isoDate) {
        if (isoDate == null || isoDate.isEmpty()) return ""; // 날짜가 없을 경우 빈 값 반환

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = inputFormat.parse(isoDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return isoDate; // 변환 실패 시 원본 반환
        }
    }
}
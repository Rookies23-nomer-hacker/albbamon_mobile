package com.example.albbamon.mypage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.albbamon.R;
import com.example.albbamon.model.ResumeModel;
import java.util.List;

public class ResumeAdapter extends RecyclerView.Adapter<ResumeAdapter.ViewHolder> {

    private List<ResumeModel> resumeList;

    public ResumeAdapter(List<ResumeModel> resumeList) {
        this.resumeList = resumeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.resume_card_for_listview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResumeModel resume = resumeList.get(position);
        holder.createDate.setText(resume.getCreateDate());
        holder.tvCareer.setText(resume.getCareer());
        holder.tvAddress.setText(resume.getAddress());
        holder.tvWorkType.setText(resume.getWorkType());
    }

    @Override
    public int getItemCount() {
        return resumeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView createDate, tvCareer, tvAddress, tvWorkType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            createDate = itemView.findViewById(R.id.createDate);
            tvCareer = itemView.findViewById(R.id.tv_career);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvWorkType = itemView.findViewById(R.id.tv_work_type);
        }
    }
}

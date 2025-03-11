package com.example.albbamon.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.albbamon.R;
import com.example.albbamon.api.ResumeAPI;
import com.example.albbamon.model.RecruitmentApply;
import com.example.albbamon.mypage.RecruitementResultActivity;
import com.example.albbamon.network.RetrofitClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            if (!apply.getPortfolioUrl().isEmpty()) {
                downloadResumeFile(apply.getPortfolioUrl());
            } else {
                Toast.makeText(context, "포트폴리오 URL이 없습니다.", Toast.LENGTH_SHORT).show();
            }
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

    private void downloadResumeFile(String fileName) {
        ResumeAPI resumeAPI = RetrofitClient.getRetrofitInstanceWithSession(context).create(ResumeAPI.class);
        Call<ResponseBody> call = resumeAPI.downloadResumeFile(fileName);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // 파일 쓰기 성공 시
                    boolean writtenToDisk = writeResponseBodyToDisk(response.body(), fileName);

                    if (writtenToDisk) {
                        Toast.makeText(context, "다운로드 성공", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "파일 저장 실패", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "서버 에러 발생", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "다운로드 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String fileName) {
        try {
            File file = new File(context.getExternalFilesDir(null) + File.separator + fileName);
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                }

                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

}

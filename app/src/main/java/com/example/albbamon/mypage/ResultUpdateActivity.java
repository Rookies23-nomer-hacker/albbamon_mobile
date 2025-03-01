//package com.example.albbamon.mypage;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.example.albbamon.R;
//import com.example.albbamon.api.RecruitmentAPI;
//import com.example.albbamon.dto.request.UpdateApplyStatusRequestDto;
//import com.example.albbamon.network.RetrofitClient;
//
//
//private void ResultUpdateActivity(Long recruitmentId, Long applyId, String newStatus) {
//    RecruitmentAPI recruitmentAPI = RetrofitClient.getRetrofitInstanceWithSession(this).create(RecruitmentAPI.class);
//    UpdateApplyStatusRequestDto requestDto = new UpdateApplyStatusRequestDto(newStatus);
//
//    recruitmentAPI.updateApplyStatus(recruitmentId, applyId, requestDto).enqueue(new Callback<SuccessResponse<String>>() {
//        @Override
//        public void onResponse(Call<SuccessResponse<String>> call, Response<SuccessResponse<String>> response) {
//            if (response.isSuccessful() && response.body() != null) {
//                Log.d("API_RESPONSE", "✅ 지원 상태 변경 성공: " + response.body().getData());
//                Toast.makeText(getApplicationContext(), "지원 상태가 변경되었습니다.", Toast.LENGTH_SHORT).show();
//            } else {
//                Log.e("API_RESPONSE", "🚨 지원 상태 변경 실패: " + response.code());
//                Toast.makeText(getApplicationContext(), "상태 변경 실패: " + response.message(), Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        @Override
//        public void onFailure(Call<SuccessResponse<String>> call, Throwable t) {
//            Log.e("API_ERROR", "❌ 네트워크 오류 발생: " + t.getMessage());
//            Toast.makeText(getApplicationContext(), "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
//        }
//    });
//}

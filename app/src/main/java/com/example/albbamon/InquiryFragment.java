package com.example.albbamon;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InquiryFragment extends Fragment {

    private Spinner spinnerMainCategory, spinnerSubCategory;
    private EditText etInquiryContent, etInquiryEmail, etInquiryPhone;
    private CheckBox cbAgreePrivacy, cbNotifyReply;
    private Button btnSubmitInquiry, btnBack, btnGoToWithdrawal;
    private Button btnGoToFindId;  // 아이디 찾기 버튼
    private Button btnGoToFindPw;  // **비밀번호 찾기 버튼 (새로 추가)**

    private TextView tvAttachedFile;  // 파일 이름을 표시할 TextView

    private Uri attachedFileUri; // 첨부 파일 URI
    private Map<String, List<String>> subCategoriesMap; // 대분류별 상세 분류 매핑
    private ActivityResultLauncher<String> filePickerLauncher; // 파일 선택 API 런처

    public InquiryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // filePickerLauncher 등록은 onCreateView()에서 수행
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inquiry, container, false);

        // 레이아웃에서 뷰 찾아오기
        spinnerMainCategory = view.findViewById(R.id.spinner_main_category);
        spinnerSubCategory = view.findViewById(R.id.spinner_sub_category);
        etInquiryContent = view.findViewById(R.id.et_inquiry_content);
        etInquiryEmail = view.findViewById(R.id.et_inquiry_email);
        etInquiryPhone = view.findViewById(R.id.et_inquiry_phone);
        cbAgreePrivacy = view.findViewById(R.id.cb_agree_privacy);
        cbNotifyReply = view.findViewById(R.id.cb_notify_reply);
        btnSubmitInquiry = view.findViewById(R.id.btn_submit_inquiry);
        tvAttachedFile = view.findViewById(R.id.tv_attached_file);
        btnBack = view.findViewById(R.id.btn_back);
        btnGoToWithdrawal = view.findViewById(R.id.btn_go_to_withdrawal);

        // 아이디 찾기 버튼
        btnGoToFindId = view.findViewById(R.id.btn_go_to_find_id);
        btnGoToFindId.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "아이디 찾기 버튼 클릭됨", Toast.LENGTH_SHORT).show();
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), FindIdPersonalActivity.class);
                startActivity(intent);
            }
        });

        // **비밀번호 찾기 버튼 연결 (XML에 btn_go_to_find_pw가 있어야 함)**
        btnGoToFindPw = view.findViewById(R.id.btn_go_to_find_pw);
        btnGoToFindPw.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "비밀번호 찾기 버튼 클릭됨", Toast.LENGTH_SHORT).show();
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), FindPwPersonalActivity.class);
                startActivity(intent);
            }
        });

        // 회원 탈퇴 버튼
        btnGoToWithdrawal.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MemberWithdrawalActivity.class);
            startActivity(intent);
        });

        // 뒤로가기 버튼
        btnBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // 파일 선택 API 런처 등록
        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                attachedFileUri = uri;
                String fileName = getFileName(uri);
                tvAttachedFile.setText("첨부된 파일: " + fileName);
                uploadFile(uri);
            }
        });

        // 첨부파일 아이콘 클릭 이벤트
        FrameLayout attachmentBox = view.findViewById(R.id.attachment_box);
        attachmentBox.setOnClickListener(v -> filePickerLauncher.launch("*/*"));

        initSubCategoriesMap();
        setupMainCategorySpinner();
        setupSubmitButton();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("InquiryFragment", "onResume() 호출됨");
        etInquiryContent.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(etInquiryContent, InputMethodManager.SHOW_IMPLICIT);
            Log.d("InquiryFragment", "키보드 showSoftInput() 호출됨");
        }
    }

    private String getFileName(Uri uri) {
        String lastSegment = uri.getLastPathSegment();
        return lastSegment != null ? lastSegment : "알 수 없음";
    }

    private void initSubCategoriesMap() {
        subCategoriesMap = new HashMap<>();
        subCategoriesMap.put("회원정보", Arrays.asList("회원가입/탈퇴", "회원정보수정", "아이디/비밀번호찾기"));
        subCategoriesMap.put("이력서관리", Arrays.asList("이력서 등록", "이력서 수정", "이력서 삭제"));
        subCategoriesMap.put("구직활동관리", Arrays.asList("지원 현황", "지원 취소", "면접 일정 관리"));
        subCategoriesMap.put("공고등록관리", Arrays.asList("공고 등록", "공고 수정", "공고 삭제"));
        subCategoriesMap.put("유료서비스", Arrays.asList("광고등록상품", "이력서열람서비스", "알바제공", "이력서검증상품",
                "환불", "결제오류", "세금신고", "쿠폰/충전금/이미니"));
        subCategoriesMap.put("오류/의견", Arrays.asList("모바일 서비스오류", "PC웹 서비스오류", "불편사항 개선요청"));
        subCategoriesMap.put("신고", Arrays.asList("거짓채용공고 신고", "부적합제의 신고", "근로분쟁"));
        subCategoriesMap.put("기타", Arrays.asList("면접·출근 증빙", "알바토크", "이벤트/경품", "공지·소식/발신", "기타"));
        subCategoriesMap.put("직군 서비스", Arrays.asList("의견 제안", "장애 신고", "서비스 이용 문의", "이벤트 문의", "기타"));
        subCategoriesMap.put("알바몬 제트", Arrays.asList("문의/제안"));
    }

    private void setupMainCategorySpinner() {
        List<String> mainCategories = new ArrayList<>(subCategoriesMap.keySet());
        mainCategories.add(0, "대분류 선택");

        ArrayAdapter<String> mainAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, mainCategories);
        mainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMainCategory.setAdapter(mainAdapter);

        spinnerMainCategory.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ArrayAdapter<String> defaultSubAdapter = new ArrayAdapter<>(requireContext(),
                            android.R.layout.simple_spinner_item, new String[]{"상세 선택"});
                    defaultSubAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerSubCategory.setAdapter(defaultSubAdapter);
                    return;
                }
                String selectedMain = mainCategories.get(position);
                List<String> subList = subCategoriesMap.get(selectedMain);
                if (subList != null) {
                    ArrayAdapter<String> subAdapter = new ArrayAdapter<>(requireContext(),
                            android.R.layout.simple_spinner_item, subList);
                    subAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerSubCategory.setAdapter(subAdapter);
                }
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });
    }

    private void setupSubmitButton() {
        btnSubmitInquiry.setOnClickListener(v -> {
            if (!cbAgreePrivacy.isChecked()) {
                Toast.makeText(requireContext(), "개인정보 동의를 해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(requireContext(), "문의가 전송되었습니다.", Toast.LENGTH_SHORT).show();
        });
    }

    private void uploadFile(Uri fileUri) {
        Log.d("InquiryFragment", "파일 업로드 시작: " + fileUri.toString());
        new Handler(Looper.getMainLooper()).postDelayed(() -> onUploadSuccess(), 2000);
    }

    private void onUploadSuccess() {
        Log.d("InquiryFragment", "파일 업로드 성공");
        Toast.makeText(requireContext(), "파일이 성공적으로 업로드되었습니다.", Toast.LENGTH_SHORT).show();
    }
}

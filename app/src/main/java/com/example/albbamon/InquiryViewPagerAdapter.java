package com.example.albbamon;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class InquiryViewPagerAdapter extends FragmentStateAdapter {

    public InquiryViewPagerAdapter(@NonNull InquiryActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // position 값에 따라 다른 Fragment 반환
        switch (position) {
            case 0:
                return new InquiryFragment();      // 문의/제안/신고
            case 1:
                return new MyInquiriesFragment(); // 나의 문의내역
            default:
                return new InquiryFragment();
        }
    }

    @Override
    public int getItemCount() {
        // 탭이 2개이므로 2 반환
        return 2;
    }
}


package com.example.albbamon.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

/**
 * TabLayout + ViewPager2 용 공용 어댑터
 */
public class ViewPagerAdapter extends FragmentStateAdapter {

    private final List<Fragment> fragmentList;
    private final List<String> titleList;

    public ViewPagerAdapter(@NonNull FragmentActivity fa,
                            @NonNull List<Fragment> fragments,
                            @NonNull List<String> titles) {
        super(fa);
        this.fragmentList = fragments;
        this.titleList = titles;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

    public String getTabTitle(int position) {
        return titleList.get(position);
    }
}

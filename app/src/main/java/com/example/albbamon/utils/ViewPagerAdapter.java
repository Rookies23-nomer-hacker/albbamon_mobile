package com.example.albbamon.utils;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private final List<Fragment> fragmentList;
    private final List<String> tabTitles;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> fragments, List<String> titles) {
        super(fragmentActivity);
        this.fragmentList = fragments;
        this.tabTitles = titles;
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
        return tabTitles.get(position);
    }
}

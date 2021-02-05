package com.hjhj.daedan;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Tab1PagerAdapter extends FragmentPagerAdapter {
    Fragment[] pages = new Fragment[2];
    String[] titles = new String[]{"지도로 보기","게시판으로 보기"};
    public Tab1PagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);

        pages[0] = new Tab1_MapFragment();
        pages[1] = new Tab1_ListFragment();

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return pages[position];
    }

    @Override
    public int getCount() {
        return pages.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}

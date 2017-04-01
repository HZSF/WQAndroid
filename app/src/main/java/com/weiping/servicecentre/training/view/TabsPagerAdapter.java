package com.weiping.servicecentre.training.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.weiping.servicecentre.training.activity.ActivityReviewFragment;
import com.weiping.servicecentre.training.activity.CourseListFragment;
import com.weiping.servicecentre.training.activity.NoticeFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    private String tabTitles[];
    private Context mContext;

    public TabsPagerAdapter(FragmentManager fm, Context context){
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int i) {

        switch (i){
            case 0:
                return new NoticeFragment();
            case 1:
                CourseListFragment fragment1 = new CourseListFragment();
                fragment1.setMContext(mContext);
                return fragment1;
            case 2:
                return new ActivityReviewFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    public void setTabTitles(String[] tabs){
        tabTitles = tabs;
    }
}

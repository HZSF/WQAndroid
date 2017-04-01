package com.weiping.servicecentre.training.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiping.common.BitmapWorkerTask;
import com.weiping.servicecentre.training.view.SlidingTabLayout;
import com.weiping.servicecentre.training.view.TabsPagerAdapter;

import platform.tyk.weping.com.weipingplatform.R;

public class ManageTrainingActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_manage_training));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(ManageTrainingActivity.this);
            }
        });
        setContentView(R.layout.activity_manage_training);

        // Initilization
        ViewPager viewPager;
        TabsPagerAdapter mAdapter;
        viewPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), this);

        // Adding Tabs
        String tab_notice = getResources().getString(R.string.lbl_tab_train_notice);
        String tab_course = getResources().getString(R.string.lbl_tab_train_courses);
        String tab_review = getResources().getString(R.string.lbl_tab_train_review);
        String[] tabTitles = {tab_notice, tab_course, tab_review};

        mAdapter.setTabTitles(tabTitles);
        viewPager.setAdapter(mAdapter);

        // Give the SlidingTabLayout the ViewPager
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        // Center the tabs in the layout
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);
    }

    public void loadBitmap(int resId, ImageView imageView){
        BitmapWorkerTask task = new BitmapWorkerTask(imageView, ManageTrainingActivity.this);
        task.execute(resId);
    }

    public void onClickActionBarBack(View view){
        onBackPressed();
    }

}

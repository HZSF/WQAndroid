package com.weiping.InteProperty;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiping.InteProperty.patent.activity.PatentAchieveTransformListActivity;
import com.weiping.InteProperty.patent.activity.PatentAnnualFeeMonitorListActivity;
import com.weiping.InteProperty.patent.activity.PatentIntegratedSearchActivity;
import com.weiping.InteProperty.patent.activity.PatentProtectionActivity;
import com.weiping.InteProperty.patent.activity.PatentStatusSearchActivity;
import com.weiping.InteProperty.trademark.activity.TradeMarkIntegratedSearchActivity;
import com.weiping.InteProperty.trademark.activity.TradeMarkNamingActivity;
import com.weiping.InteProperty.trademark.activity.TradeMarkSimilarSearchActivity;
import com.weiping.InteProperty.trademark.activity.TrademarkTradeActivity;
import com.weiping.InteProperty.view.SlidingTabLayout;
import com.weiping.InteProperty.trademark.activity.TradeMarkStatusSearchActivity;
import com.weiping.common.BitmapWorkerTask;

import platform.tyk.weping.com.weipingplatform.R;


public class IntePropSearchActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_inte_prop_search));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_inte_prop_search);

        // Initilization
        ViewPager viewPager;
        TabsPagerAdapter mAdapter;
        viewPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        // Adding Tabs
        String tab_trademark = getResources().getString(R.string.lbl_tab_trademark_service);
        String tab_patent = getResources().getString(R.string.lbl_tab_patent_service);
        String[] tabTitles = {tab_trademark, tab_patent};

        mAdapter.setTabTitles(tabTitles);
        viewPager.setAdapter(mAdapter);

        viewPager.setCurrentItem(getIntent().getExtras().getInt("page"));

        // Give the SlidingTabLayout the ViewPager
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        // Center the tabs in the layout
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);
    }

    public void onClickSimilarSearchTrademarkPage(View view){
        Intent intent = new Intent(getBaseContext(), TradeMarkSimilarSearchActivity.class);
        startActivity(intent);
    }

    public void onClickIntegratedSearchTrademarkPage(View view){
        Intent intent = new Intent(getBaseContext(), TradeMarkIntegratedSearchActivity.class);
        startActivity(intent);
    }

    public void onClickSearchTrademarkStatusPage(View view){
        Intent tradeMarkStatusSearchIntent = new Intent(getBaseContext(), TradeMarkStatusSearchActivity.class);
        startActivity(tradeMarkStatusSearchIntent);
    }

    public void onClickTrademarkNamingPage(View view){
        Intent intent = new Intent(getBaseContext(), TradeMarkNamingActivity.class);
        startActivity(intent);
    }

    public void onClickIntegratedSearchPatentPage(View view){
        Intent patentIntegratedSearchIntent = new Intent(getBaseContext(), PatentIntegratedSearchActivity.class);
        startActivity(patentIntegratedSearchIntent);
    }

    public void onclickPatentAnnualFeeMonitor(View view){
        Intent patentAnnualFeeMonitorIntent = new Intent(getBaseContext(), PatentAnnualFeeMonitorListActivity.class);
        startActivity(patentAnnualFeeMonitorIntent);
    }

    public void onClickPatentAchieveTransformPage(View view){
        Intent intent = new Intent(getBaseContext(), PatentAchieveTransformListActivity.class);
        startActivity(intent);
    }

    public void onClickProtectionPatentPage(View view){
        Intent intent = new Intent(getBaseContext(), PatentProtectionActivity.class);
        startActivity(intent);
    }

    public void onClickTrademarkTradingPage(View view){
        Intent intent = new Intent(getBaseContext(), TrademarkTradeActivity.class);
        startActivity(intent);
    }

    public void onClickPatentPatentStatusSearchPage(View view){
        Intent intent = new Intent(getBaseContext(), PatentStatusSearchActivity.class);
        startActivity(intent);
    }

    public void loadBitmap(int resId, ImageView imageView, int x, int y){
        BitmapWorkerTask task = new BitmapWorkerTask(imageView, IntePropSearchActivity.this);
        task.setX(x);
        task.setY(y);
        task.execute(resId);
    }

    public void onClickActionBarBack(View view){
        onBackPressed();
    }


}

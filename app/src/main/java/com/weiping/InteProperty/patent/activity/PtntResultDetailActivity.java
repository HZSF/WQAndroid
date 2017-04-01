package com.weiping.InteProperty.patent.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import platform.tyk.weping.com.weipingplatform.R;

public class PtntResultDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_ptnt_result_detail));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_ptnt_result_detail);
        String title = getIntent().getStringExtra("title");
        String detail = getIntent().getStringExtra("detail");
        ((TextView)findViewById(R.id.txt_ptnt_regNum)).setText(title);
        ((TextView)findViewById(R.id.txt_ptnt_detail)).setText(detail);
    }

}

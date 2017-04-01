package com.weiping.servicecentre.finance.onlend.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import platform.tyk.weping.com.weipingplatform.R;

public class OnlendActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_onlend));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(OnlendActivity.this);
            }
        });
        setContentView(R.layout.activity_onlend);
        TextView notice = (TextView)findViewById(R.id.cs_finance_onlend_notice);
        notice.setText(Html.fromHtml(getString(R.string.onlend_notice)));
    }

    public void onClickListenerApplyOnlend(View view){
        Intent intent = new Intent(getBaseContext(), OnlendApplicationFormActivity.class);
        startActivity(intent);
    }

    public void onClickActionBarBack(View view){
        onBackPressed();
    }

}

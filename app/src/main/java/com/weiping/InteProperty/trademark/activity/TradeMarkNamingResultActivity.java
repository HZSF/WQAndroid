package com.weiping.InteProperty.trademark.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import platform.tyk.weping.com.weipingplatform.R;

public class TradeMarkNamingResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_trade_mark_naming_result));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_trade_mark_naming_result);
        String result = getIntent().getStringExtra("search_result");
        WebView webView = (WebView)findViewById(R.id.webView_trademark_naming_result);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL("", result, "text/html", "UTF-8", "");
        //webView.loadData(result, "text/html", "UTF-8");
    }

    public void onClickActionBarBack(View view){
        onBackPressed();
    }

}

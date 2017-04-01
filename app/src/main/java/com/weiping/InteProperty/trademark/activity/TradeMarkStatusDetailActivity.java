package com.weiping.InteProperty.trademark.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.InteProperty.trademark.asyncTasks.AddMonitorTask;
import com.weiping.InteProperty.trademark.asyncTasks.TrademarkApplyFlowDetailTask;
import com.weiping.InteProperty.trademark.core.Constants;

import platform.tyk.weping.com.weipingplatform.R;

public class TradeMarkStatusDetailActivity extends Activity {
    private String regNum;
    private String categoryNum;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //actionBar.setDisplayShowCustomEnabled(true);
        //actionBar.setDisplayShowHomeEnabled(false);
        //actionBar.setDisplayShowTitleEnabled(false);
        //actionBar.setCustomView(R.layout.actionbar_common);
        //((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_trade_mark_status_detail));
        //actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
          //  @Override
            //public void onClick(View v) {
         //       onBackPressed();
        //    }
        //});
        setContentView(R.layout.activity_trade_mark_status_detail);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_dashboard);
        ((TextView)actionBar.getCustomView().findViewById(R.id.txt_login)).setText("");
        ((TextView)actionBar.getCustomView().findViewById(R.id.txt_bar_title)).setText(getString(R.string.title_activity_trade_mark_status_detail));


        regNum = getIntent().getStringExtra(Constants.JSON_NAME_REG_NUM);
        categoryNum = getIntent().getStringExtra(Constants.JSON_NAME_CATEGORY_NUM);
        name = getIntent().getStringExtra(Constants.JSON_NAME_NAME);
        ((TextView)findViewById(R.id.txt_trademark_status_detail_register_number)).setText(getResources().getString(R.string.lbl_applNum) + regNum);
        ((TextView)findViewById(R.id.txt_trademark_status_detail_category_number)).setText(getResources().getString(R.string.lbl_categoryNum) + categoryNum);

        if(!networkConnect()){
            Toast.makeText(TradeMarkStatusDetailActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
        }else {
            TrademarkApplyFlowDetailTask task = new TrademarkApplyFlowDetailTask(this, regNum, categoryNum);
            task.doTask();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.trademark_detail_status, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.item1:
                AddMonitorTask task1 = new AddMonitorTask(this, regNum, categoryNum, name);
                task1.execute(Constants.URL_TRADEMARK_ADD_MONITOR);
                return true;
        }
        return true;
    }

    public boolean networkConnect() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    public void onClickActionBarBack(View view){
        onBackPressed();
    }

}

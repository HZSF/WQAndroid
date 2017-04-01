package com.weiping.InteProperty.trademark.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.InteProperty.trademark.asyncTasks.AddMonitorTask;
import com.weiping.InteProperty.trademark.asyncTasks.DownloadTrademarkImageTask;
import com.weiping.InteProperty.trademark.asyncTasks.TrademarkApplyFlowDetailTask;
import com.weiping.InteProperty.trademark.core.Constants;
import com.weiping.platform.asyncTasks.TrdmkMonCancelTask;

import platform.tyk.weping.com.weipingplatform.R;

public class TrademarkMonitorDetailActivity extends Activity {

    private boolean fromMonitor = false;

    private String regNum;
    private String categoryNum;
    private String name;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trademark_monitor_detail);

        regNum = getIntent().getStringExtra(Constants.JSON_NAME_REG_NUM);
        categoryNum = getIntent().getStringExtra(Constants.JSON_NAME_CATEGORY_NUM);
        name = getIntent().getStringExtra(Constants.JSON_NAME_NAME);
        from = getIntent().getStringExtra("from");
        if ("monitor".equalsIgnoreCase(from)){
            fromMonitor = true;
        }

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_trademark_monitor_detail));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView addMonitorUp = (TextView) actionBar.getCustomView().findViewById(R.id.textView_settings_up);
        if(fromMonitor){
            addMonitorUp.setText("取消");
        } else {
            addMonitorUp.setText("添加");
        }
        TextView addMonitorDown = (TextView) actionBar.getCustomView().findViewById(R.id.textView_settings_down);
        addMonitorDown.setText("监测");
        actionBar.getCustomView().findViewById(R.id.ll_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fromMonitor){
                    TrdmkMonCancelTask task = new TrdmkMonCancelTask(TrademarkMonitorDetailActivity.this, regNum);
                    task.execute(Constants.URL_TRADEMARK_CANCEL_MONITOR);
                } else {
                    AddMonitorTask task = new AddMonitorTask(v.getContext(), regNum, categoryNum, name);
                    task.execute(Constants.URL_TRADEMARK_ADD_MONITOR);
                }
            }
        });

        ((TextView)findViewById(R.id.txt_trademark_status_detail_register_number)).setText(getResources().getString(R.string.lbl_applNum) + regNum);
        ((TextView)findViewById(R.id.txt_trademark_status_detail_category_number)).setText(getResources().getString(R.string.lbl_categoryNum) + categoryNum);

        if(!networkConnect()){
            Toast.makeText(TrademarkMonitorDetailActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
        }else {
            DownloadTrademarkImageTask task1 = new DownloadTrademarkImageTask(this, regNum, categoryNum);
            task1.doTask();
            TrademarkApplyFlowDetailTask task2 = new TrademarkApplyFlowDetailTask(this, regNum, categoryNum);
            task2.doTask();
        }
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

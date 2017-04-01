package com.weiping.InteProperty.trademark.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.InteProperty.trademark.asyncTasks.AddMonitorTask;
import com.weiping.InteProperty.trademark.asyncTasks.AddSellTask;
import com.weiping.InteProperty.trademark.asyncTasks.DownloadTrademarkImageTask;
import com.weiping.InteProperty.trademark.asyncTasks.TrademarkApplyFlowDetailTask;
import com.weiping.InteProperty.trademark.core.Constants;
import com.weiping.common.StringUtility;

import platform.tyk.weping.com.weipingplatform.R;

public class TradeMarkSimilarDetailActivity extends Activity {

    private boolean fromMonitor = false;

    private String regNum;
    private String categoryNum;
    private String name;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_mark_similar_detail);

        regNum = getIntent().getStringExtra(Constants.JSON_NAME_REG_NUM);
        categoryNum = getIntent().getStringExtra(Constants.JSON_NAME_CATEGORY_NUM);
        name = getIntent().getStringExtra(Constants.JSON_NAME_NAME);
        from = getIntent().getStringExtra("from");
        if ("monitor".equalsIgnoreCase(from)){
            fromMonitor = true;
        }

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_dashboard);
        ((TextView)actionBar.getCustomView().findViewById(R.id.txt_login)).setText("");
        ((TextView)actionBar.getCustomView().findViewById(R.id.txt_bar_title)).setText(getString(R.string.title_activity_trade_mark_similar_detail));
        /*actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
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
                    TrdmkMonCancelTask task = new TrdmkMonCancelTask(TradeMarkSimilarDetailActivity.this, regNum);
                    task.execute(Constants.URL_TRADEMARK_CANCEL_MONITOR);
                } else {
                    AddMonitorTask task = new AddMonitorTask(v.getContext(), regNum, categoryNum, name);
                    task.execute(Constants.URL_TRADEMARK_ADD_MONITOR);
                }
            }
        });
*/
        ((TextView)findViewById(R.id.txt_trademark_status_detail_register_number)).setText(getResources().getString(R.string.lbl_applNum) + regNum);
        ((TextView)findViewById(R.id.txt_trademark_status_detail_category_number)).setText(getResources().getString(R.string.lbl_categoryNum) + categoryNum);

        if(!networkConnect()){
            Toast.makeText(TradeMarkSimilarDetailActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
        }else {
            TrademarkApplyFlowDetailTask task2 = new TrademarkApplyFlowDetailTask(this, regNum, categoryNum);
            task2.doTask();
            DownloadTrademarkImageTask task1 = new DownloadTrademarkImageTask(this, regNum, categoryNum);
            task1.doTask();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.trademark_detail, menu);
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
            case R.id.item2:
                buildPriceDialog(regNum, categoryNum, name);
                return true;
        }
        return true;
    }

    private void buildPriceDialog(final String regNum, final String categoryNum, final String name){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("输入价格");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton(getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String price = input.getText().toString();
                if (!StringUtility.isEmptyString(price)) {
                    AddSellTask task2 = new AddSellTask(TradeMarkSimilarDetailActivity.this, regNum, categoryNum, name, price);
                    task2.execute(Constants.URL_TRADEMARK_ADD_SELL);
                }
            }
        });
        builder.setNegativeButton(getString(R.string.lbl_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}

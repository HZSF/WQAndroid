package com.weiping.InteProperty.trademark.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.InteProperty.trademark.asyncTasks.TrademarkSearchByRegNumTask;

import java.io.IOException;

import platform.tyk.weping.com.weipingplatform.R;

public class TradeMarkStatusSearchActivity extends Activity {

    public TrademarkSearchByRegNumTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_trade_mark_status_search));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_trade_mark_status_search);
    }

    public boolean networkConnect() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void performSearchTMStatus(View view) throws IOException{
        EditText keyText = (EditText)findViewById(R.id.searchText);
        String searchKey = keyText.getText().toString();
        //TextView resultTextView = (TextView)findViewById(R.id.resultTextView);
        if(searchKey.isEmpty() || searchKey.equalsIgnoreCase("")){
            return;
        }
        if(!networkConnect()){
            Toast.makeText(TradeMarkStatusSearchActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }
        task = new TrademarkSearchByRegNumTask(this, searchKey);
        task.doTask();
    }

    public void onClickActionBarBack(View view){
        if (task != null){
            task.cancel(true);
        }
        onBackPressed();
    }

}

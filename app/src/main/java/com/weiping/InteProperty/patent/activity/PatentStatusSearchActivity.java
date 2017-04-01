package com.weiping.InteProperty.patent.activity;

import android.app.ActionBar;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.InteProperty.patent.asyncTasks.PtntStatusSearchTask;
import com.weiping.InteProperty.patent.base.Constants;

import platform.tyk.weping.com.weipingplatform.R;

public class PatentStatusSearchActivity extends Activity {

    public PtntStatusSearchTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_patent_status_search));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_patent_status_search);
    }

    public void performSearchPatentStatus(View view){
        EditText keyText = (EditText)findViewById(R.id.searchText);
        String searchKey = keyText.getText().toString();
        if(searchKey.isEmpty() || searchKey.equalsIgnoreCase("")){
            return;
        }
        if(!networkConnect()){
            Toast.makeText(PatentStatusSearchActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }

        task = new PtntStatusSearchTask(this, searchKey);
        task.execute(Constants.URL_PATENT_STATUS_SEARCH);
    }
    public boolean networkConnect() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void onClickActionBarBack(View view){
        if (task != null){
            task.cancel(true);
        }
        onBackPressed();
    }

}

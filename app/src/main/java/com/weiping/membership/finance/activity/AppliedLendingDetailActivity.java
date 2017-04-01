package com.weiping.membership.finance.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.credential.activity.LoginActivity;
import com.weiping.membership.common.Constants;
import com.weiping.membership.finance.asyncTasks.AppliedLendingFetchTask;
import com.weiping.membership.finance.asyncTasks.CancelAppliedLendingTask;

import platform.tyk.weping.com.weipingplatform.R;

public class AppliedLendingDetailActivity extends Activity {

    private String search_lend_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_applied_lending_detail));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        search_lend_id = intent.getExtras().getString("search_lend_id");
        setContentView(R.layout.activity_applied_lending_detail);
        setLendFormData();
    }

    public void setLendFormData(){
        if(!networkConnect()){
            Toast.makeText(AppliedLendingDetailActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }

        AppliedLendingFetchTask task = new AppliedLendingFetchTask(this, search_lend_id);
        task.execute(Constants.URL_MEMBER_FINANCE_APPOINTED_LEND);
    }

    public void onClickListenerCancelOnlendFormSubmit(View view){
        if(!networkConnect()){
            Toast.makeText(AppliedLendingDetailActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(Constants.IS_USER_LOGIN, false);
        if(isLoggedIn) {
            CancelAppliedLendingTask cancelAppliedLendingTask = new CancelAppliedLendingTask(this, search_lend_id);
            cancelAppliedLendingTask.execute(Constants.URL_MEMBER_FINANCE_CANCEL_APPOINTED_LEND);
        }else{
            AlertDialog.Builder notLoginDialogBuilder = new AlertDialog.Builder(this);
            notLoginDialogBuilder
                    .setMessage("请登录！")
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog notLoginDialog = notLoginDialogBuilder.create();
            notLoginDialog.show();
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

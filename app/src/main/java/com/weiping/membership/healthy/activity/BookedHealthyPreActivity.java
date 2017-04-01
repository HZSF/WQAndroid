package com.weiping.membership.healthy.activity;

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
import com.weiping.membership.healthy.asyncTasks.BookedHealthCheckFetchTask;
import com.weiping.membership.healthy.asyncTasks.CancelHealthCheckBookTask;

import platform.tyk.weping.com.weipingplatform.R;

public class BookedHealthyPreActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_booked_healthy_pre));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_booked_healthy_pre);
        if(!networkConnect()){
            Toast.makeText(BookedHealthyPreActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
        }else {
            BookedHealthCheckFetchTask task = new BookedHealthCheckFetchTask(this);
            task.execute(Constants.URL_CS_HEALTHY_APPOINTED);
        }
    }

    public void onClickListenerHealthCheckCancelAppoint(View view){
        if(!networkConnect()){
            Toast.makeText(BookedHealthyPreActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(Constants.IS_USER_LOGIN, false);
        if(isLoggedIn){
            CancelHealthCheckBookTask cancelHealthCheckBookTask = new CancelHealthCheckBookTask(this);
            cancelHealthCheckBookTask.execute(Constants.URL_CS_HEALTHY_CANCEL_APPOINT);
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

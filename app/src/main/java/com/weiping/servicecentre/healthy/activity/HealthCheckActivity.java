package com.weiping.servicecentre.healthy.activity;

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
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.common.BitmapWorkerTask;
import com.weiping.common.Constants;
import com.weiping.credential.activity.LoginActivity;
import com.weiping.servicecentre.healthy.asyncTasks.SubmitHealthCheckAppointTask;

import platform.tyk.weping.com.weipingplatform.R;

public class HealthCheckActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_health_check));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(HealthCheckActivity.this);
            }
        });
        setContentView(R.layout.activity_health_check);
        loadBitmap(R.drawable.cs_health, (ImageView) findViewById(R.id.cs_health_image));
    }

    public void onClickListenerHealthCheckAppoint(View view){
        if(!networkConnect()){
            Toast.makeText(HealthCheckActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(Constants.IS_USER_LOGIN, false);
        if(isLoggedIn){
            SubmitHealthCheckAppointTask submitCourseRegistrationTask = new SubmitHealthCheckAppointTask(this);
            submitCourseRegistrationTask.execute(Constants.URL_CS_HEALTHY_APPOINT);
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

    public void loadBitmap(int resId, ImageView imageView){
        BitmapWorkerTask task = new BitmapWorkerTask(imageView, HealthCheckActivity.this);
        task.execute(resId);
    }

}

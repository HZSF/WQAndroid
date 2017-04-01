package com.weiping.servicecentre.quality.textile.activity;

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
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.common.BitmapWorkerTask;
import com.weiping.common.Constants;
import com.weiping.credential.activity.LoginActivity;
import com.weiping.servicecentre.quality.textile.asyncTasks.SubmitTextileInspectionAppointTask;

import platform.tyk.weping.com.weipingplatform.R;

public class TextileInspectionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_textile_inspection));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_textile_inspection);
        loadBitmap(R.drawable.cs_quality_textile, (ImageView) findViewById(R.id.cs_quality_textile_image));
        TextView textile_text1 = (TextView)findViewById(R.id.txt_cs_quality_textile1);
        TextView textile_text2 = (TextView)findViewById(R.id.txt_cs_quality_textile2);
        textile_text1.setText(Html.fromHtml(getString(R.string.textile_1)));
        textile_text2.setText(Html.fromHtml(getString(R.string.textile_2)));
    }

    public void onClickListenerAppointTextileInspection(View view){
        if(!networkConnect()){
            Toast.makeText(TextileInspectionActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(Constants.IS_USER_LOGIN, false);
        if(isLoggedIn){
            SubmitTextileInspectionAppointTask submitTextileInspectionAppointTask = new SubmitTextileInspectionAppointTask(this);
            submitTextileInspectionAppointTask.execute(Constants.URL_CS_INSPECTION_TEXTILE_APPOINT);
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
        BitmapWorkerTask task = new BitmapWorkerTask(imageView, TextileInspectionActivity.this);
        task.execute(resId);
    }

}

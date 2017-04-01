package com.weiping.servicecentre.quality.textile.asyncTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.weiping.credential.activity.LoginActivity;
import com.weiping.servicecentre.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import platform.tyk.weping.com.weipingplatform.R;

public class SubmitTextileInspectionAppointTask extends AsyncTask<String, Void, String> {
    private Activity mActivity;
    protected ProgressDialog progressDialog;
    private AbstractHttpClient mClient;
    private HttpPost httpPost;

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            httpPost.abort();
            mActivity.onBackPressed();
        }
    };

    public SubmitTextileInspectionAppointTask(Activity a){
        super();
        mActivity = a;
        mClient = new DefaultHttpClient();
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        progressDialog = ProgressDialog.show(mActivity, null, mActivity.getResources().getString(R.string.dialog_loading));
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(progressDialogCancelListener);
    }

    @Override
    protected String doInBackground(String... params) {
        try{
            SharedPreferences sharedPreferences = mActivity.getSharedPreferences(Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
            String AUTH_SESSION_TOKEN = sharedPreferences.getString(Constants.HEAD_AUTH_TOKEN, "");

            httpPost = new HttpPost(params[0]);
            httpPost.setHeader(Constants.HEAD_AUTH_TOKEN, AUTH_SESSION_TOKEN);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse response = mClient.execute(httpPost);

            return EntityUtils.toString(response.getEntity());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result){
        progressDialog.dismiss();
        AlertDialog.Builder notLoginDialogBuilder = new AlertDialog.Builder(mActivity);
        notLoginDialogBuilder.setCancelable(false);

        if (Constants.EVENT_FAIL.equalsIgnoreCase(result)){
            notLoginDialogBuilder
                    .setMessage("预约失败！")
                    .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

        }else if(Constants.EVENT_SUCCESS.equalsIgnoreCase(result)){
            notLoginDialogBuilder
                    .setMessage("预约成功！")
                    .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

        }else if(Constants.EVENT_EXISTED.equalsIgnoreCase(result)){
            notLoginDialogBuilder
                    .setMessage("您已经预约！")
                    .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

        }else{
            notLoginDialogBuilder
                    .setMessage("请登录！")
                    .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
        }
        AlertDialog notLoginDialog = notLoginDialogBuilder.create();
        notLoginDialog.show();
    }
}

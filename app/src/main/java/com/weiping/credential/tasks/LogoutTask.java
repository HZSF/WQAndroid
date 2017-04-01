package com.weiping.credential.tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.weiping.credential.Common.Constants;
import com.weiping.platform.DashboardActivity;

import platform.tyk.weping.com.weipingplatform.R;

public class LogoutTask extends AsyncTask<String, Void, String> {
    private Activity mActivity;
    protected ProgressDialog progressDialog;
    private AbstractHttpClient mClient;
    private HttpPost httpPost;

    private int indicator;

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            httpPost.abort();
            mActivity.onBackPressed();
        }
    };

    public LogoutTask(Activity a){
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

            HttpResponse response = mClient.execute(httpPost);

            return EntityUtils.toString(response.getEntity());
        }catch (Exception e){
            e.printStackTrace();
            progressDialog.dismiss();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result){
        progressDialog.dismiss();
        AlertDialog.Builder notLoginDialogBuilder = new AlertDialog.Builder(mActivity);
        notLoginDialogBuilder.setCancelable(false);

        if(indicator == 1){
            if(Constants.EVENT_SUCCESS.equalsIgnoreCase(result)){
                SharedPreferences sharedPreferences = mActivity.getSharedPreferences(Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(Constants.IS_USER_LOGIN, false);
                editor.putString(Constants.SHARE_PREFERENCE_USERNAME, "");
                editor.putString(Constants.HEAD_AUTH_TOKEN, "");
                editor.commit();
                Intent intent = new Intent(mActivity.getBaseContext(), DashboardActivity.class);
                mActivity.startActivity(intent);
                mActivity.finish();
            }
            return;
        }
        if(Constants.EVENT_SUCCESS.equalsIgnoreCase(result)){
            notLoginDialogBuilder
                    .setMessage("登出成功！")
                    .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            SharedPreferences sharedPreferences = mActivity.getSharedPreferences(Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(Constants.IS_USER_LOGIN, false);
                            editor.putString(Constants.SHARE_PREFERENCE_USERNAME, "");
                            editor.putString(Constants.HEAD_AUTH_TOKEN, "");
                            editor.commit();
                            mActivity.onBackPressed();
                        }
                    });
            AlertDialog notLoginDialog = notLoginDialogBuilder.create();
            notLoginDialog.show();
        }else{
            mActivity.onBackPressed();
        }
    }

    public void setIndicator(int indicator) {
        this.indicator = indicator;
    }
}

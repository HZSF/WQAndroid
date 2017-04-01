package com.weiping.membership.asyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.weiping.membership.common.Constants;
import com.weiping.platform.MainMemberActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import platform.tyk.weping.com.weipingplatform.R;

public class TokenPingTask extends AsyncTask<String, Void, String> {
    private Activity mActivity;
    private AbstractHttpClient mClient;
    private String AUTH_SESSION_TOKEN;
    protected ProgressDialog progressDialog;
    private HttpPost httpPost;
    private boolean isWelcomePage = false;

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            httpPost.abort();
            mActivity.onBackPressed();
        }
    };

    public TokenPingTask(Activity a, String token){
        super();
        mActivity = a;
        AUTH_SESSION_TOKEN = token;
        mClient = new DefaultHttpClient();
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        if(!isWelcomePage) {
            progressDialog = ProgressDialog.show(mActivity, null, mActivity.getResources().getString(R.string.dialog_loading));
            progressDialog.setCancelable(true);
            progressDialog.setOnCancelListener(progressDialogCancelListener);
        }
    }

    @Override
    protected String doInBackground(String... params) {
        try{
            httpPost = new HttpPost(params[0]);
            httpPost.setHeader(Constants.HEAD_AUTH_TOKEN, AUTH_SESSION_TOKEN);
            HttpResponse response = mClient.execute(httpPost);

            return EntityUtils.toString(response.getEntity());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result){
        if(!isWelcomePage) {

        } else {
            if (result != null && result.equalsIgnoreCase("weiwei")) {
                Intent intent = new Intent(mActivity.getBaseContext(), MainMemberActivity.class);
                mActivity.startActivity(intent);
                mActivity.finish();
                System.exit(0);
            }
            else {
                SharedPreferences sharedPreferences = mActivity.getSharedPreferences(com.weiping.credential.Common.Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(com.weiping.credential.Common.Constants.IS_USER_LOGIN, false);
                editor.commit();
            }
        }
    }

    public void setIsWelcomePage(boolean isWelcomePage) {
        this.isWelcomePage = isWelcomePage;
    }
}

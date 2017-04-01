package com.weiping.credential.tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.widget.Toast;

import com.weiping.common.StringUtility;
import com.weiping.credential.activity.LoginActivity;
import com.weiping.servicecentre.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import platform.tyk.weping.com.weipingplatform.R;

public class UpdUserInfoTask extends AsyncTask<String, Void, String> {
    private Activity mActivity;
    private AbstractHttpClient mClient;
    protected ProgressDialog progressDialog;
    private HttpPost httpPost;
    private String field_name;
    private String field_value;

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            httpPost.abort();
            progressDialog.dismiss();
        }
    };

    public UpdUserInfoTask(Activity a, String name, String value){
        super();
        mActivity = a;
        field_name = name;
        field_value = value;
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
        try {
            SharedPreferences sharedPreferences = mActivity.getSharedPreferences(Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
            String AUTH_SESSION_TOKEN = sharedPreferences.getString(Constants.HEAD_AUTH_TOKEN, "");
            httpPost = new HttpPost(params[0]);
            httpPost.setHeader(Constants.HEAD_AUTH_TOKEN, AUTH_SESSION_TOKEN);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(field_name, URLEncoder.encode(URLEncoder.encode(field_value, "UTF-8"), "UTF-8"));
            StringEntity se = new StringEntity(jsonObject.toString());
            httpPost.setEntity(se);
            HttpResponse response = mClient.execute(httpPost);

            return EntityUtils.toString(response.getEntity());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (StringUtility.isEmptyString(result)){
            progressDialog.dismiss();
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_data_updating), Toast.LENGTH_SHORT).show();
            mActivity.onBackPressed();
            return;
        }
        progressDialog.dismiss();
        AlertDialog.Builder notLoginDialogBuilder = new AlertDialog.Builder(mActivity);
        notLoginDialogBuilder.setCancelable(false);

        if (Constants.EVENT_FAIL.equalsIgnoreCase(result)){
            notLoginDialogBuilder
                    .setMessage("提交失败！")
                    .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog notLoginDialog = notLoginDialogBuilder.create();
            notLoginDialog.show();

        }else if(Constants.EVENT_SUCCESS.equalsIgnoreCase(result)){
            NavUtils.navigateUpFromSameTask(mActivity);
        }else {
            try {
                JSONObject jsonData = new JSONObject(result.trim());
                if (jsonData != null) {
                    if (jsonData.has("error") && com.weiping.membership.common.Constants.UNAUTHORIZED.equalsIgnoreCase((String) jsonData.get("error"))) {
                        notLoginDialogBuilder
                                .setMessage("请登录！")
                                .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog notLoginDialog = notLoginDialogBuilder.create();
                        notLoginDialog.show();
                    } else {
                        notLoginDialogBuilder
                                .setMessage("请稍后再试！")
                                .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog notLoginDialog = notLoginDialogBuilder.create();
                        notLoginDialog.show();
                    }
                } else {
                    notLoginDialogBuilder
                            .setMessage("请稍后再试！")
                            .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog notLoginDialog = notLoginDialogBuilder.create();
                    notLoginDialog.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

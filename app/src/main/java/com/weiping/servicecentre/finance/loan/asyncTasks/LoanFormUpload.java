package com.weiping.servicecentre.finance.loan.asyncTasks;

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
import com.weiping.servicecentre.finance.loan.LoanForm;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import platform.tyk.weping.com.weipingplatform.R;

public class LoanFormUpload extends AsyncTask<String, Void, String> {

    private Activity mActivity;
    protected ProgressDialog progressDialog;
    protected LoanForm loanSubmitForm;
    private AbstractHttpClient mClient;
    private HttpPost httpPost;

    public LoanFormUpload(Activity activity, LoanForm submitForm){
        super();
        mActivity = activity;
        loanSubmitForm = submitForm;
        mClient = new DefaultHttpClient();
    }

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            httpPost.abort();
            mActivity.onBackPressed();
        }
    };

    public void uploadForm(){
        execute(Constants.URL_CS_FINANCE_LOAN_SUBMIT);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(mActivity, null, mActivity.getResources().getString(R.string.dialog_uploading));
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
            StringEntity se = new StringEntity(loanSubmitForm.toJson().toString());
            httpPost.setEntity(se);
            HttpResponse response = mClient.execute(httpPost);

            return EntityUtils.toString(response.getEntity());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
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
            notLoginDialogBuilder
                    .setMessage("提交成功！")
                    .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog notLoginDialog = notLoginDialogBuilder.create();
            notLoginDialog.show();

        }else if(Constants.EVENT_EXISTED.equalsIgnoreCase(result)){
            notLoginDialogBuilder
                    .setMessage("您已提交贷款申请！")
                    .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog notLoginDialog = notLoginDialogBuilder.create();
            notLoginDialog.show();
        } else {
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

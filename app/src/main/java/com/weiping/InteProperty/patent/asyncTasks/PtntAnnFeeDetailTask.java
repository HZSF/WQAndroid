package com.weiping.InteProperty.patent.asyncTasks;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.weiping.InteProperty.patent.base.Constants;
import com.weiping.common.StringUtility;
import com.weiping.credential.activity.LoginActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import platform.tyk.weping.com.weipingplatform.R;

public class PtntAnnFeeDetailTask extends AsyncTask<String, Void, String> {
    private Context context;
    protected ProgressDialog progressDialog;
    private AbstractHttpClient mClient;
    private HttpPost httpPost;
    private int responseCode;

    private String patentId;

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            progressDialog.dismiss();
            httpPost.abort();
        }
    };

    public PtntAnnFeeDetailTask(Context context, String pid) {
        super();
        this.context = context;
        patentId = pid;
        mClient = new DefaultHttpClient();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, null, context.getResources().getString(R.string.dialog_wait));
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(progressDialogCancelListener);
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(com.weiping.servicecentre.Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
            String AUTH_SESSION_TOKEN = sharedPreferences.getString(com.weiping.servicecentre.Constants.HEAD_AUTH_TOKEN, "");

            httpPost = new HttpPost(params[0]);
            httpPost.setHeader(Constants.HEAD_AUTH_TOKEN, AUTH_SESSION_TOKEN);

            StringEntity se = new StringEntity(patentId);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse response = mClient.execute(httpPost);
            responseCode = response.getStatusLine().getStatusCode();
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        switch (responseCode) {
            case Constants.STATUS_UNAUTHORIZED:
                progressDialog.dismiss();
                loginAlert();
                return;
        }

        if (StringUtility.isEmptyString(result)) {
            progressDialog.dismiss();
            Toast.makeText(context, context.getResources().getString(R.string.toast_data_updating), Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setCancelable(false);
        if (Constants.EVENT_FAIL.equalsIgnoreCase(result)){
            alertDialogBuilder
                    .setMessage("提交失败！")
                    .setPositiveButton(context.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }else if(Constants.EVENT_SUCCESS.equalsIgnoreCase(result)){
            alertDialogBuilder
                    .setMessage("已经成功申请代缴！")
                    .setPositiveButton(context.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }else{
            alertDialogBuilder
                    .setMessage("请稍后再试！")
                    .setPositiveButton(context.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        progressDialog.dismiss();
    }

    private void loginAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder
                .setMessage("请登录！")
                .setPositiveButton(context.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

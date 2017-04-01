package com.weiping.servicecentre.property.asyncTasks;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.weiping.servicecentre.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import platform.tyk.weping.com.weipingplatform.R;

public class PropertyAddFavoritesTask extends AsyncTask<String, Void, String> {
    private Context mActivity;
    protected ProgressDialog progressDialog;
    private AbstractHttpClient mClient;
    private HttpPost httpPost;

    private int responseCode;

    private int id;
    private int category;

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            if (httpPost != null)
                httpPost.abort();
        }
    };

    public PropertyAddFavoritesTask(Context activity, int id, int category){
        super();
        mActivity = activity;
        this.id = id;
        this.category = category;
        mClient = new DefaultHttpClient();
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
        try {
            SharedPreferences sharedPreferences = mActivity.getSharedPreferences(Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
            String AUTH_SESSION_TOKEN = sharedPreferences.getString(Constants.HEAD_AUTH_TOKEN, "");
            httpPost = new HttpPost(params[0]);
            httpPost.setHeader(Constants.HEAD_AUTH_TOKEN, AUTH_SESSION_TOKEN);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",id);
            jsonObject.put("category", category);
            StringEntity se = new StringEntity(jsonObject.toString());
            httpPost.setEntity(se);

            HttpResponse response = mClient.execute(httpPost);
            responseCode = response.getStatusLine().getStatusCode();
            return EntityUtils.toString(response.getEntity());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        switch (responseCode) {
            case Constants.STATUS_UNAUTHORIZED:
                loginAlert();
                progressDialog.dismiss();
                return;
        }
        progressDialog.dismiss();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setCancelable(false);

        if (Constants.EVENT_FAIL.equalsIgnoreCase(result)) {
            alertDialogBuilder
                    .setMessage("提交失败！")
                    .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog notLoginDialog = alertDialogBuilder.create();
            notLoginDialog.show();

        } else if (Constants.EVENT_SUCCESS.equalsIgnoreCase(result)) {
            alertDialogBuilder
                    .setMessage("提交成功！")
                    .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog notLoginDialog = alertDialogBuilder.create();
            notLoginDialog.show();

        } else if(Constants.EVENT_EXISTED.equalsIgnoreCase(result)){
            alertDialogBuilder
                    .setMessage("您已添加过，无需重复添加！")
                    .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }else {
            alertDialogBuilder
                    .setMessage("请稍后再试！")
                    .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog notLoginDialog = alertDialogBuilder.create();
            notLoginDialog.show();
        }
    }
    private void loginAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder
                .setMessage("请登录！")
                .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

package com.weiping.servicecentre.property.sell.asyncTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

public class PropertySellFormUploadTask extends AsyncTask<String, Void, String> {
    private Activity mActivity;
    protected ProgressDialog progressDialog;
    private AbstractHttpClient mClient;
    private HttpPost httpPost;

    private int responseCode;
    private boolean exception = false;

    private String area;
    private String category;
    private String property_area;
    private String levels;
    private String ask_price;
    private String description;

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            if (httpPost != null)
                httpPost.abort();
        }
    };

    public PropertySellFormUploadTask(Activity activity, String area, String category, String property_area, String levels, String ask_price, String description){
        super();
        mActivity = activity;
        this.area = area;
        this.category = category;
        this.property_area = property_area;
        this.levels = levels;
        this.ask_price = ask_price;
        this.description = description;
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
        try{
            SharedPreferences sharedPreferences = mActivity.getSharedPreferences(Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
            String AUTH_SESSION_TOKEN = sharedPreferences.getString(Constants.HEAD_AUTH_TOKEN, "");
            httpPost = new HttpPost(params[0]);
            httpPost.setHeader(Constants.HEAD_AUTH_TOKEN, AUTH_SESSION_TOKEN);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            JSONObject formJsonObject = new JSONObject();
            formJsonObject.put("region", URLEncoder.encode(URLEncoder.encode(area, "UTF-8"), "UTF-8"));
            formJsonObject.put("category", URLEncoder.encode(URLEncoder.encode(category, "UTF-8"), "UTF-8"));
            formJsonObject.put("property_area", property_area);
            formJsonObject.put("levels", levels);
            formJsonObject.put("ask_price", ask_price);
            formJsonObject.put("description", URLEncoder.encode(URLEncoder.encode(description, "UTF-8"), "UTF-8"));

            StringEntity se = new StringEntity(formJsonObject.toString());
            httpPost.setEntity(se);

            HttpResponse response = mClient.execute(httpPost);
            responseCode = response.getStatusLine().getStatusCode();
            return EntityUtils.toString(response.getEntity());
        }catch (Exception e){
            e.printStackTrace();
            exception = true;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (exception){
            progressDialog.dismiss();
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_data_updating), Toast.LENGTH_SHORT).show();
            return;
        }
        switch (responseCode) {
            case Constants.STATUS_UNAUTHORIZED:
                loginAlert();
                progressDialog.dismiss();
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

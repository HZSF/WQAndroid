package com.weiping.credential.tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.weiping.common.StringUtility;
import com.weiping.credential.Common.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import platform.tyk.weping.com.weipingplatform.R;

public class RegisterVerifyCodeSubmitTask extends AsyncTask<String, Void, String> {
    private Activity mActivity;
    private AbstractHttpClient mClient;
    private String verificationCode;
    protected ProgressDialog progressDialog;
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

    public RegisterVerifyCodeSubmitTask(Activity a, String verificationCode){
        super();
        mActivity = a;
        this.verificationCode = verificationCode;
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
            SharedPreferences sharedPreferences = mActivity.getSharedPreferences(com.weiping.servicecentre.Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
            String AUTH_SESSION_TOKEN = sharedPreferences.getString(com.weiping.servicecentre.Constants.HEAD_AUTH_TOKEN, "");

            httpPost = new HttpPost(params[0]);
            httpPost.setHeader(Constants.HEAD_REG_TOKEN, (AUTH_SESSION_TOKEN + verificationCode));

            HttpResponse response = mClient.execute(httpPost);

            return EntityUtils.toString(response.getEntity());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setCancelable(false);
        try {
            if(Constants.EVENT_SUCCESS.equalsIgnoreCase(result)) {
                alertDialogBuilder
                        .setMessage("注册成功！")
                        .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Intent intent = new Intent();
                                intent.putExtra("onBack", "Y");
                                mActivity.setResult(Activity.RESULT_OK, intent);
                                mActivity.onBackPressed();
                            }
                        });
            }else if(StringUtility.isEmptyString(result)) {
                alertDialogBuilder
                        .setMessage("注册失败！")
                        .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                mActivity.onBackPressed();
                            }
                        });
            }else{
                JSONObject jsonData = new JSONObject(result.trim());
                if (jsonData != null) {
                    if (jsonData.has("error") && Constants.UNAUTHORIZED.equalsIgnoreCase((String) jsonData.get("error"))) {
                        alertDialogBuilder
                                .setMessage("验证码错误！")
                                .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        mActivity.onBackPressed();
                                    }
                                });

                    } else {
                        alertDialogBuilder
                                .setMessage("请重试！")
                                .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        mActivity.onBackPressed();
                                    }
                                });
                    }
                } else {
                    alertDialogBuilder
                            .setMessage("请稍后再试！")
                            .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    mActivity.onBackPressed();
                                }
                            });
                }
            }
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            progressDialog.dismiss();
        }
    }
}

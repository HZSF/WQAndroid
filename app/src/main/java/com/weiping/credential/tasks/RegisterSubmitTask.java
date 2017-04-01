package com.weiping.credential.tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.common.StringUtility;
import com.weiping.credential.Common.Constants;
import com.weiping.credential.activity.RegisterActivity;
import com.weiping.credential.activity.RegisterVerifyCodeActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import platform.tyk.weping.com.weipingplatform.R;

public class RegisterSubmitTask extends AsyncTask<String, Void, String> {
    private Activity mActivity;
    private AbstractHttpClient mClient;
    private String REG_USER = "";
    private String REG_PASS = "";
    private String REG_PHONE = "";
    private String REG_SESSION = "";
    private String REG_COMPANY_NAME = "";
    protected ProgressDialog progressDialog;
    private HttpPost httpPost;
    private int responseCode;

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            httpPost.abort();
            mActivity.onBackPressed();
        }
    };

    public RegisterSubmitTask(Activity a, String username, String password, String phoneNumber, String companyName, String session){
        super();
        mActivity = a;
        REG_USER = username;
        REG_PASS = password;
        REG_PHONE = phoneNumber;
        REG_SESSION = session;
        REG_COMPANY_NAME = companyName;
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
            httpPost = new HttpPost(params[0]);
            httpPost.setHeader(Constants.HEAD_REG_USERNAME, URLEncoder.encode(URLEncoder.encode(REG_USER, "UTF-8"), "UTF-8"));
            httpPost.setHeader(Constants.HEAD_REG_PASSWORD, REG_PASS);
            httpPost.setHeader(Constants.HEAD_REG_PHONE, REG_PHONE);
            httpPost.setHeader(Constants.HEAD_REG_COMNAME, URLEncoder.encode(URLEncoder.encode(REG_COMPANY_NAME, "UTF-8"), "UTF-8"));
            httpPost.setHeader(Constants.HEAD_REG_CODE, REG_SESSION);

            HttpResponse response = mClient.execute(httpPost);
            responseCode = response.getStatusLine().getStatusCode();
            return EntityUtils.toString(response.getEntity());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result){
        Log.i("response",String.valueOf(responseCode));
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setCancelable(false);
        switch (responseCode) {
            case Constants.STATUS_UNAUTHORIZED:
                progressDialog.dismiss();
                alertDialogBuilder
                        .setMessage("验证码错误！")
                        .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                GetRegisterImgTask getImgTask = ((RegisterActivity) mActivity).getGetImgTask();
                                if (getImgTask != null)
                                    getImgTask.cancel(true);
                                getImgTask = new GetRegisterImgTask(mActivity);
                                getImgTask.execute(Constants.URL_REGISTER_CODE_GET);
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                clearPasswordText();
                return;
            case Constants.STATUS_CONFLICT:
                progressDialog.dismiss();
                alertDialogBuilder
                        .setMessage("用户名已存在！")
                        .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                GetRegisterImgTask getImgTask = ((RegisterActivity)mActivity).getGetImgTask();
                                if (getImgTask != null)
                                    getImgTask.cancel(true);
                                getImgTask = new GetRegisterImgTask(mActivity);
                                getImgTask.execute(Constants.URL_REGISTER_CODE_GET);
                            }
                        });
                AlertDialog alertDialog1 = alertDialogBuilder.create();
                alertDialog1.show();
                clearPasswordText();
                return;
            case Constants.STATUS_CONFLICT_PHONE:
                progressDialog.dismiss();
                alertDialogBuilder
                        .setMessage("该手机号已注册！")
                        .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                GetRegisterImgTask getImgTask = ((RegisterActivity)mActivity).getGetImgTask();
                                if (getImgTask != null)
                                    getImgTask.cancel(true);
                                getImgTask = new GetRegisterImgTask(mActivity);
                                getImgTask.execute(Constants.URL_REGISTER_CODE_GET);
                            }
                        });
                AlertDialog alertDialog2 = alertDialogBuilder.create();
                alertDialog2.show();
                clearPasswordText();
                return;
        }
        if (StringUtility.isEmptyString(result)){
            progressDialog.dismiss();
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_data_updating), Toast.LENGTH_SHORT).show();
            mActivity.onBackPressed();
            return;
        }
        SharedPreferences sharedPreferences = mActivity.getSharedPreferences(Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
        try {
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.has("token")){
                String token = jsonObject.getString("token");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.HEAD_AUTH_TOKEN, token);
                editor.commit();
                Intent intent = new Intent(mActivity.getBaseContext(), RegisterVerifyCodeActivity.class);
                intent.putExtra(Constants.HEAD_REG_USERNAME, REG_USER);
                intent.putExtra(Constants.HEAD_REG_PASSWORD, REG_PASS);
                intent.putExtra(Constants.HEAD_REG_PHONE, REG_PHONE);
                intent.putExtra(Constants.HEAD_REG_COMNAME, REG_COMPANY_NAME);
                mActivity.startActivityForResult(intent, 1);
            } else {
                alertDialogBuilder
                        .setMessage("请重试！")
                        .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                GetRegisterImgTask getImgTask = ((RegisterActivity)mActivity).getGetImgTask();
                                if (getImgTask != null)
                                    getImgTask.cancel(true);
                                getImgTask = new GetRegisterImgTask(mActivity);
                                getImgTask.execute(Constants.URL_REGISTER_CODE_GET);
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                clearPasswordText();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            progressDialog.dismiss();
        }
    }

    private void clearPasswordText(){
        ((TextView)mActivity.findViewById(R.id.et_signup_password)).setText("");
        ((TextView)mActivity.findViewById(R.id.et_signup_password_again)).setText("");
    }
}

package com.weiping.credential.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.weiping.credential.Common.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import platform.tyk.weping.com.weipingplatform.R;

public class AuthTask extends AsyncTask<String, Void, String>{

    private Activity mActivity;
    private AbstractHttpClient mClient;
    private String AUTH_USER = "";
    private String AUTH_PASS = "";
    protected ProgressDialog progressDialog;
    private HttpPost httpPost;

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            httpPost.abort();
        }
    };

    public AuthTask(Activity a, String username, String password){
        super();
        mActivity = a;
        AUTH_USER = username;
        AUTH_PASS = password;
        mClient = new DefaultHttpClient();
        //UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(AUTH_USER, AUTH_PASS);
        //mClient.getCredentialsProvider().setCredentials(AuthScope.ANY, credentials);
    }

    public void startTask(){
        execute(Constants.URL_AUTHENTICATE);
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
            httpPost.setHeader(Constants.HEAD_AUTH_USERNAME, URLEncoder.encode(URLEncoder.encode(AUTH_USER, "UTF-8"), "UTF-8"));
            httpPost.setHeader(Constants.HEAD_AUTH_PASSWORD, AUTH_PASS);

            HttpResponse response = mClient.execute(httpPost);

            return EntityUtils.toString(response.getEntity());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result){
        if(result == null){
            progressDialog.dismiss();
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_data_updating), Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences sharedPreferences = mActivity.getSharedPreferences(Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
        try {
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.has("token")){
                String token = jsonObject.getString("token");
                Toast.makeText(mActivity, "登陆成功!", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.SHARE_PREFERENCE_USERNAME, AUTH_USER);
                editor.putString(Constants.HEAD_AUTH_TOKEN, token);
                editor.putBoolean(Constants.IS_USER_LOGIN, true);
                editor.commit();
                Intent intent = new Intent();
                intent.putExtra("login", "success");
                mActivity.setResult(Activity.RESULT_OK, intent);
                mActivity.onBackPressed();
            }
            else{
                Toast.makeText(mActivity, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(Constants.IS_USER_LOGIN, false);
                editor.commit();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            progressDialog.dismiss();
        }
    }
}

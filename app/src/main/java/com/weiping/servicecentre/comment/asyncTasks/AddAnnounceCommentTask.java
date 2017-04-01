package com.weiping.servicecentre.comment.asyncTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

import com.weiping.common.StringUtility;
import com.weiping.platform.MainMemberActivity;
import com.weiping.servicecentre.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.net.URLEncoder;

import platform.tyk.weping.com.weipingplatform.R;

public class AddAnnounceCommentTask extends AsyncTask<String, Void, String> {
    private Activity mActivity;
    protected ProgressDialog progressDialog;
    private AbstractHttpClient mClient;
    private HttpPost httpPost;
    private int responseCode;

    private String comment;
    //private String parenCommentId;
    private String announceId;
    private String sessionId;

    private boolean refresh = true;
    public FragmentManager fragmentManager;

    public AddAnnounceCommentTask(Activity mActivity, String comment, String sessionId, String announceId) {
        super();
        this.mActivity = mActivity;
        this.comment = comment;
        this.announceId = announceId;
        this.sessionId = sessionId;
        mClient = new DefaultHttpClient();
    }

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            if (httpPost != null)
                httpPost.abort();
        }
    };
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
            if(StringUtility.isEmptyString(sessionId)){
                sessionId = "";
            }
            //jsonObject.put("parenCommentId", parenCommentId);
            jsonObject.put("announceId", announceId);
            jsonObject.put("commentContent", URLEncoder.encode(URLEncoder.encode(comment, "UTF-8"), "UTF-8"));
            jsonObject.put("sessionId", sessionId);
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
        EditText et_comment = (EditText)mActivity.findViewById(R.id.et_announce_comment);
        et_comment.setText("");

        if (StringUtility.isEmptyString(result)) {
            progressDialog.dismiss();
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_data_updating), Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.dismiss();
        if (isRefresh()) {
            mActivity.finish();
            mActivity.startActivity(mActivity.getIntent());
        } else {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.container, MainMemberActivity.PlaceholderFragment.newInstance(8));
            ft.commit();

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

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }
}

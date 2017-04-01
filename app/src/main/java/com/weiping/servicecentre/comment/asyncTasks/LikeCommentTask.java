package com.weiping.servicecentre.comment.asyncTasks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.common.Constants;
import com.weiping.common.StringUtility;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import platform.tyk.weping.com.weipingplatform.R;

public class LikeCommentTask extends AsyncTask<String, Void, String> {
    private Context context;
    private AbstractHttpClient mClient;
    private HttpPost httpPost;
    private int responseCode;

    protected String sessionId;
    protected boolean like;
    protected TextView tv_likeCount;

    public LikeCommentTask(Context context, String sessionId, boolean like, TextView tv) {
        super();
        this.context = context;
        this.sessionId = sessionId;
        this.like = like;
        tv_likeCount = tv;
        mClient = new DefaultHttpClient();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(com.weiping.servicecentre.Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
            String AUTH_SESSION_TOKEN = sharedPreferences.getString(com.weiping.servicecentre.Constants.HEAD_AUTH_TOKEN, "");

            httpPost = new HttpPost(params[0]);
            httpPost.setHeader(com.weiping.InteProperty.patent.base.Constants.HEAD_AUTH_TOKEN, AUTH_SESSION_TOKEN);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sessionId", Integer.valueOf(sessionId));
            jsonObject.put("likeUnlike", like ? Constants.YES : Constants.NO);
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
        Log.i("tag", result);
        Log.i("tagResCode", String.valueOf(responseCode));
        switch (responseCode) {
            case com.weiping.InteProperty.patent.base.Constants.STATUS_UNAUTHORIZED:
                updateTextView();
                loginAlert();
                return;
        }

        if (StringUtility.isEmptyString(result)) {
            Toast.makeText(context, context.getResources().getString(R.string.toast_data_updating), Toast.LENGTH_SHORT).show();
            updateTextView();
            return;
        }

    }

    private void updateTextView(){
        if (like) {
            if (Integer.valueOf(tv_likeCount.getText().toString()) > 0) {
                tv_likeCount.setText(String.valueOf(Integer.valueOf(tv_likeCount.getText().toString()) - 1));
            }
        } else {
            tv_likeCount.setText(String.valueOf(Integer.valueOf(tv_likeCount.getText().toString()) + 1));
        }
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
        SharedPreferences sharedPreferences = context.getSharedPreferences(com.weiping.membership.common.Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(com.weiping.credential.Common.Constants.IS_USER_LOGIN, false);
        editor.commit();
    }
}

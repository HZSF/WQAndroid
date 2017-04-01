package com.weiping.credential.tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.widget.Toast;

import com.weiping.common.StringUtility;
import com.weiping.credential.Common.Constants;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import platform.tyk.weping.com.weipingplatform.R;

public class UpdUserInfoImgTask extends AsyncTask<String, Void, String> {
    private Activity mActivity;
    private AbstractHttpClient mClient;
    protected ProgressDialog progressDialog;
    private HttpPost httpPost;
    private File imageFile;
    private int size;

    private String boundary = "*****";
    private String lineEnd = "\r\n";
    private String twoHyphens = "--";
    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            httpPost.abort();
            progressDialog.dismiss();
        }
    };

    public UpdUserInfoImgTask(Activity a, File file){
        super();
        mActivity = a;
        imageFile = file;
        //this.size = size;
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


            FileInputStream fileInputStream = new FileInputStream(imageFile);
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy            s
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            //conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
            conn.setRequestProperty(Constants.HEAD_AUTH_TOKEN, AUTH_SESSION_TOKEN);

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"file\""+ lineEnd);
            dos.writeBytes(lineEnd);
            int bytesAvailable = fileInputStream.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0){
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0,bufferSize);
            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            fileInputStream.close();
            dos.flush();

            InputStream is = conn.getInputStream();
            // retrieve the response from server
            int ch;
            StringBuffer b =new StringBuffer();
            while( ( ch = is.read() ) != -1 ){
                b.append( (char)ch );
            }
            String s=b.toString();
            Log.i("Response",s);
            dos.close();
            return s;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (StringUtility.isEmptyString(result)) {
            progressDialog.dismiss();
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_data_updating), Toast.LENGTH_SHORT).show();
            mActivity.onBackPressed();
            return;
        }
        progressDialog.dismiss();
        AlertDialog.Builder notLoginDialogBuilder = new AlertDialog.Builder(mActivity);
        notLoginDialogBuilder.setCancelable(false);

        if (com.weiping.servicecentre.Constants.EVENT_FAIL.equalsIgnoreCase(result)){
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

        }else if(com.weiping.servicecentre.Constants.EVENT_SUCCESS.equalsIgnoreCase(result)){
            mActivity.onBackPressed();
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

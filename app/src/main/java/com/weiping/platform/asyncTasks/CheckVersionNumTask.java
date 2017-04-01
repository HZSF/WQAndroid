package com.weiping.platform.asyncTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.weiping.platform.model.UpdateInfo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import platform.tyk.weping.com.weipingplatform.R;

public class CheckVersionNumTask extends AsyncTask<String, Void, String> {
    private Activity mActivity;
    protected static String TAG = "CheckVersionNumTask";
    private UpdateInfo info;

    public CheckVersionNumTask(Activity a){
        super();
        mActivity = a;
    }

    private String getVersionName() throws Exception{
        PackageManager packageManager = mActivity.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo(mActivity.getPackageName(), 0);
        return packageInfo.versionName;
    }

    @Override
    protected void onPreExecute(){
        info = new UpdateInfo();
    }

    @Override
    protected String doInBackground(String... params) {
        String url = params[0];
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        } catch (IOException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result){
        if(result == null){
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.has("version") && !"null".equalsIgnoreCase(jsonObject.getString("version"))){
                info.setVersion(jsonObject.getString("version"));
            }
            if(jsonObject.has("url") && !"null".equalsIgnoreCase(jsonObject.getString("url"))){
                info.setUrl(jsonObject.getString("url"));
            }
            if(jsonObject.has("username") && !"null".equalsIgnoreCase(jsonObject.getString("username"))){
                info.setUsername(jsonObject.getString("username"));
            }
            if(jsonObject.has("password") && !"null".equalsIgnoreCase(jsonObject.getString("password"))){
                info.setPassword(jsonObject.getString("password"));
            }
            if(jsonObject.has("description") && !"null".equalsIgnoreCase(jsonObject.getString("description"))){
                info.setDescription(jsonObject.getString("description"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        checkVersion();
    }

    private void checkVersion(){
        try {
            if (info.getVersion().equalsIgnoreCase(getVersionName())){
                Log.i(TAG, "same version");
            }else{
                Log.i(TAG, "different version, notice user");
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
                alertDialogBuilder.setTitle("版本升级");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder
                        .setMessage("检测到最新版本，立即更新！")
                        .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i(TAG, "download apk, update");
                                downLoadApk();
                            }
                        })
                        .setNegativeButton(mActivity.getResources().getString(R.string.lbl_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }catch (Exception e){
            Log.e(TAG, "checkVersion exception");
            e.printStackTrace();
        }
    }

    private void downLoadApk(){
        DownloadApkTask task = new DownloadApkTask(mActivity);
        task.execute("120.55.96.224");
    }
}

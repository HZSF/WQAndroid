package com.weiping.InteProperty.trademark.asyncTasks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.weiping.InteProperty.patent.base.Constants;
import com.weiping.common.StringUtility;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

import platform.tyk.weping.com.weipingplatform.R;

public class AddSellTask extends AsyncTask<String, Void, String> {
    private Context context;
    private AbstractHttpClient mClient;
    private HttpPost httpPost;
    private int responseCode;

    public String regNum;
    public String categoryNum;
    public String name;
    public String price;

    public AddSellTask(Context context, String regNum, String categoryNum, String name, String price) {
        super();
        this.context = context;
        this.regNum = regNum;
        this.categoryNum = categoryNum;
        this.name = name;
        this.price = price;
        mClient = new DefaultHttpClient();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(com.weiping.servicecentre.Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
            String AUTH_SESSION_TOKEN = sharedPreferences.getString(com.weiping.servicecentre.Constants.HEAD_AUTH_TOKEN, "");

            httpPost = new HttpPost(params[0]);
            httpPost.setHeader(Constants.HEAD_AUTH_TOKEN, AUTH_SESSION_TOKEN);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("regNum", regNum);
            jsonObject.put("categoryNum", categoryNum);
            jsonObject.put("name", URLEncoder.encode(URLEncoder.encode(name, "UTF-8"), "UTF-8"));
            jsonObject.put("price", price);
            StringEntity se = new StringEntity(jsonObject.toString());
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse response = mClient.execute(httpPost);
            responseCode = response.getStatusLine().getStatusCode();
            return EntityUtils.toString(response.getEntity());
        }catch (JSONException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        switch (responseCode) {
            case Constants.STATUS_UNAUTHORIZED:
                loginAlert();
                return;
        }

        if (StringUtility.isEmptyString(result)) {
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
                    .setMessage("提交成功！")
                    .setPositiveButton(context.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }else if(Constants.EVENT_EXISTED.equalsIgnoreCase(result)){
            alertDialogBuilder
                    .setMessage("您已添加过，无需重复添加！")
                    .setPositiveButton(context.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        else{
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

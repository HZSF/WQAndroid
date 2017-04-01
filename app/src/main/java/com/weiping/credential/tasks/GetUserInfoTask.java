package com.weiping.credential.tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class GetUserInfoTask extends AsyncTask<String, Void, String> {
    private Activity mActivity;
    private AbstractHttpClient mClient;
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

    public GetUserInfoTask(Activity a){
        super();
        mActivity = a;
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
            httpPost = new HttpPost(params[0]);
            httpPost.setHeader(Constants.HEAD_AUTH_TOKEN, AUTH_SESSION_TOKEN);
            HttpResponse response = mClient.execute(httpPost);
            return EntityUtils.toString(response.getEntity());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (StringUtility.isEmptyString(result)){
            progressDialog.dismiss();
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_data_updating), Toast.LENGTH_SHORT).show();
            mActivity.onBackPressed();
            return;
        }
        progressDialog.dismiss();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setCancelable(false);
        try{
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject != null) {
                if (jsonObject.has("error") && com.weiping.membership.common.Constants.UNAUTHORIZED.equalsIgnoreCase((String) jsonObject.get("error"))) {
                    alertDialogBuilder
                            .setMessage("请登录！")
                            .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    mActivity.onBackPressed();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else {
                    TextView et_contact_name = (TextView) mActivity.findViewById(R.id.member_contact_name);
                    TextView et_email = (TextView) mActivity.findViewById(R.id.member_email);
                    TextView et_company = (TextView) mActivity.findViewById(R.id.member_company_name);
                    TextView et_company_addr = (TextView) mActivity.findViewById(R.id.member_company_address);
                    TextView et_area = (TextView) mActivity.findViewById(R.id.member_area);
                    if (jsonObject.has(Constants.JSON_CONTACT_NAME) && !"null".equalsIgnoreCase(jsonObject.getString(Constants.JSON_CONTACT_NAME))) {
                        et_contact_name.setText(jsonObject.getString(Constants.JSON_CONTACT_NAME));
                    }
                    if (jsonObject.has(Constants.JSON_EMAIL) && !"null".equalsIgnoreCase(jsonObject.getString(Constants.JSON_EMAIL))) {
                        et_email.setText(jsonObject.getString(Constants.JSON_EMAIL));
                    }
                    if (jsonObject.has(Constants.JSON_COMPANY_NAME) && !"null".equalsIgnoreCase(jsonObject.getString(Constants.JSON_COMPANY_NAME))) {
                        et_company.setText(jsonObject.getString(Constants.JSON_COMPANY_NAME));
                    }
                    String area = "";
                    if (jsonObject.has(Constants.JSON_PROVINCE) && !"null".equalsIgnoreCase(jsonObject.getString(Constants.JSON_PROVINCE))) {
                        area = jsonObject.getString(Constants.JSON_PROVINCE);
                    }
                    if (jsonObject.has(Constants.JSON_CITY) && !"null".equalsIgnoreCase(jsonObject.getString(Constants.JSON_CITY))) {
                        area += jsonObject.getString(Constants.JSON_CITY);
                    }
                    et_area.setText(area);
                    if (jsonObject.has(Constants.JSON_COMPANY_ADDR) && !"null".equalsIgnoreCase(jsonObject.getString(Constants.JSON_COMPANY_ADDR))) {
                        et_company_addr.setText(jsonObject.getString(Constants.JSON_COMPANY_ADDR));
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

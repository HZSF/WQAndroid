package com.weiping.membership.finance.asyncTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.RadioButton;

import com.weiping.membership.common.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import platform.tyk.weping.com.weipingplatform.R;

public class AppliedLendingFetchTask extends AsyncTask<String, Void, String> {
    private Activity mActivity;
    protected ProgressDialog progressDialog;
    private AbstractHttpClient mClient;
    private HttpPost httpPost;
    private String lendId;

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            httpPost.abort();
            mActivity.onBackPressed();
        }
    };

    public AppliedLendingFetchTask(Activity activity, String lendId){
        super();
        mActivity = activity;
        this.lendId = lendId;
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
            SharedPreferences sharedPreferences = mActivity.getSharedPreferences(com.weiping.servicecentre.Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
            String AUTH_SESSION_TOKEN = sharedPreferences.getString(com.weiping.servicecentre.Constants.HEAD_AUTH_TOKEN, "");

            httpPost = new HttpPost(params[0]);
            httpPost.setHeader(Constants.HEAD_AUTH_TOKEN, AUTH_SESSION_TOKEN);

            StringEntity se = new StringEntity(lendId);
            httpPost.setEntity(se);

            HttpResponse response = mClient.execute(httpPost);
            return EntityUtils.toString(response.getEntity());
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setCancelable(false);

        try {
            JSONObject jsonData = new JSONObject(result.trim());
            if (jsonData != null) {
                if (jsonData.has("error") && Constants.UNAUTHORIZED.equalsIgnoreCase((String) jsonData.get("error"))) {
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
                }else{
                    ((EditText)mActivity.findViewById(R.id.et_cs_finance_lend_amount_readonly)).setText(jsonData.getString("loadSum"));
                    ((EditText)mActivity.findViewById(R.id.et_cs_finance_onlend_deadline_readonly)).setText(jsonData.getString("deadLine"));
                    String bankAbbr = jsonData.getString("bankAbbr");
                    if(com.weiping.servicecentre.Constants.BANK_CMB.equalsIgnoreCase(bankAbbr)){
                        ((RadioButton)mActivity.findViewById(R.id.radioButton_lend_bank_cmb_readonly)).setChecked(true);
                    }else if(com.weiping.servicecentre.Constants.BANK_CCB.equalsIgnoreCase(bankAbbr)){
                        ((RadioButton)mActivity.findViewById(R.id.radioButton_lend_bank_ccb_readonly)).setChecked(true);
                    }else if(com.weiping.servicecentre.Constants.BANK_JXCCB.equalsIgnoreCase(bankAbbr)){
                        ((RadioButton)mActivity.findViewById(R.id.radioButton_lend_bank_jxccb_readonly)).setChecked(true);
                    }else if(com.weiping.servicecentre.Constants.BANK_CZCB.equalsIgnoreCase(bankAbbr)){
                        ((RadioButton)mActivity.findViewById(R.id.radioButton_lend_bank_czcb_readonly)).setChecked(true);
                    }else if(com.weiping.servicecentre.Constants.BANK_HXB.equalsIgnoreCase(bankAbbr)){
                        ((RadioButton)mActivity.findViewById(R.id.radioButton_lend_bank_hxb_readonly)).setChecked(true);
                    }else if(com.weiping.servicecentre.Constants.BANK_CZB.equalsIgnoreCase(bankAbbr)){
                        ((RadioButton)mActivity.findViewById(R.id.radioButton_lend_bank_czb_readonly)).setChecked(true);
                    }else if(com.weiping.servicecentre.Constants.BANK_ZJWX.equalsIgnoreCase(bankAbbr)){
                        ((RadioButton)mActivity.findViewById(R.id.radioButton_lend_bank_zjwx_readonly)).setChecked(true);
                    }
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }finally {
            progressDialog.dismiss();
        }
    }
}

package com.weiping.InteProperty.patent.asyncTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.weiping.InteProperty.patent.adapter.PtntAnnFeeMonitRecyAdapter;
import com.weiping.InteProperty.patent.base.Constants;
import com.weiping.InteProperty.patent.model.PatentAnnualFeeMonitorListItem;
import com.weiping.common.StringUtility;
import com.weiping.credential.activity.LoginActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class PatentAnnualFeeMonitorFetchTask extends AsyncTask<String, Void, String> {
    private Activity mActivity;
    private PtntAnnFeeMonitRecyAdapter adapter;
    protected ProgressDialog progressDialog;
    private AbstractHttpClient mClient;
    private HttpPost httpPost;
    private int responseCode;

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            progressDialog.dismiss();
            httpPost.abort();
            mActivity.onBackPressed();
        }
    };

    public PatentAnnualFeeMonitorFetchTask(Activity activity, PtntAnnFeeMonitRecyAdapter adapter) {
        super();
        mActivity = activity;
        this.adapter = adapter;
        mClient = new DefaultHttpClient();
    }

    @Override
    protected void onPreExecute() {
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

            HttpResponse response = mClient.execute(httpPost);
            responseCode = response.getStatusLine().getStatusCode();
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        switch (responseCode) {
            case Constants.STATUS_UNAUTHORIZED:
                progressDialog.dismiss();
                loginAlert();
                return;
        }

        if(StringUtility.isEmptyString(result)){
            progressDialog.dismiss();
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_data_updating), Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setCancelable(false);

        try {
            JSONObject jsonData = new JSONObject(result.trim());
            if (jsonData != null) {
                if (jsonData.has("error") && Constants.UNAUTHORIZED.equalsIgnoreCase((String) jsonData.get("error"))) {
                    loginAlert();
                } else {
                    JSONArray jsonItems = jsonData.getJSONArray(Constants.JSON_NAME_RESPONSE_OBJECT);
                    ArrayList<PatentAnnualFeeMonitorListItem> items = new ArrayList<>();
                    if (jsonItems != null && jsonItems.length() > 0) {
                        for (int i = 0; i < jsonItems.length(); i++) {
                            JSONObject row = jsonItems.getJSONObject(i);
                            String patent_id = "";
                            if (!StringUtility.isEmptyString(row.getString(Constants.PATENT_ID))) {
                                patent_id = row.getString(Constants.PATENT_ID);
                            }
                            String patent_title = "";
                            if (!StringUtility.isEmptyString(row.getString(Constants.PATENT_TITLE))) {
                                patent_title = row.getString(Constants.PATENT_TITLE);
                            }
                            String applicant = "";
                            if (!StringUtility.isEmptyString(row.getString(Constants.APPLICANT))){
                                applicant = row.getString(Constants.APPLICANT);
                            }
                            String patent_apply_date = "申请日: ";
                            if (!StringUtility.isEmptyString(row.getString(Constants.PATENT_APPLY_DATE))) {
                                patent_apply_date += row.getString(Constants.PATENT_APPLY_DATE);
                            }
                            String patent_expire_date = "到期日: ";
                            String expire_date = row.getString(Constants.PATENT_EXPIRE_DATE);
                            if (!StringUtility.isEmptyString(expire_date) && expire_date.length()>5) {
                                patent_expire_date += expire_date.substring(5);
                            }
                            String patent_annual_fee = "年费金额：";
                            if (!StringUtility.isEmptyString(row.getString(Constants.PATENT_ANNUAL_FEE))) {
                                if(!"null".equalsIgnoreCase(row.getString(Constants.PATENT_ANNUAL_FEE)))
                                    patent_annual_fee += row.getString(Constants.PATENT_ANNUAL_FEE);
                            }
                            items.add(new PatentAnnualFeeMonitorListItem(patent_id, patent_title, applicant, patent_apply_date, patent_expire_date, row.getString(Constants.PATENT_EXPIRE_STATUS),  row.getString(Constants.PATENT_PAYMENT_STATUS), patent_annual_fee));
                        }
                        //PatentAnnualFeeMonitorListAdapter adapter = new PatentAnnualFeeMonitorListAdapter(mActivity, items);
                        //ListView v = (ListView) mActivity.findViewById(R.id.list_view_patent_annual_fee);
                        adapter.setItemArrayList(items);
                        adapter.notifyDataSetChanged();
                    } else {
                        alertDialogBuilder
                                .setMessage("您尚未提交年费监测！")
                                .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }
            } else {
                alertDialogBuilder
                        .setMessage("请稍后再试！")
                        .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            alertDialogBuilder
                    .setMessage("请稍后再试！")
                    .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } finally {
            progressDialog.dismiss();
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
                        mActivity.onBackPressed();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

package com.weiping.membership.finance.asyncTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.EditText;

import com.weiping.membership.common.Constants;
import com.weiping.servicecentre.finance.loan.LoanForm;

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

public class AppliedCreditLoanFetchTask extends AsyncTask<String, Void, String> {
    private Activity mActivity;
    protected ProgressDialog progressDialog;
    private AbstractHttpClient mClient;
    private HttpPost httpPost;
    private String loanId;

    protected int[] formFieldsID = new int[]{
            R.id.et_cs_finance_loan_reg_capital_readonly,
            R.id.et_cs_finance_loan_total_asset_readonly,
            R.id.et_cs_finance_loan_total_debt_readonly,
            R.id.et_cs_finance_loan_bank_loan_readonly,
            R.id.et_cs_finance_loan_other_loan_readonly,
            R.id.et_cs_finance_loan_owner_equity_readonly,
            R.id.et_cs_finance_loan_asset_liability_ratio_readonly,
            R.id.et_cs_finance_loan_main_product_readonly,
            R.id.et_cs_finance_loan_productivity_readonly,
            R.id.et_cs_finance_loan_product_capability_readonly,
            R.id.et_cs_finance_loan_industrial_output_readonly,
            R.id.et_cs_finance_loan_sales_income_readonly,
            R.id.et_cs_finance_loan_total_profit_readonly,
            R.id.et_cs_finance_loan_total_tax_readonly,
            R.id.et_cs_finance_loan_added_value_tax_readonly,
            R.id.et_cs_finance_loan_purpose_readonly,
            R.id.et_cs_finance_loan_limit_readonly,
            R.id.et_cs_finance_loan_period_readonly
    };

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            httpPost.abort();
            mActivity.onBackPressed();
        }
    };

    public AppliedCreditLoanFetchTask(Activity activity, String loanId){
        super();
        mActivity = activity;
        this.loanId = loanId;
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

            StringEntity se = new StringEntity(loanId);
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
                    for(int i=0; i<LoanForm.JSON_CS_FINANCE_LOAN_FORM_FIELDS.length; i++){
                        if (jsonData.has(LoanForm.JSON_CS_FINANCE_LOAN_FORM_FIELDS[i])){
                            ((EditText)mActivity.findViewById(formFieldsID[i])).setText(jsonData.getString(LoanForm.JSON_CS_FINANCE_LOAN_FORM_FIELDS[i]));
                        }
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

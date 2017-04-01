package com.weiping.servicecentre.finance.loan.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.weiping.servicecentre.finance.loan.LoanForm;
import com.weiping.servicecentre.finance.loan.LoanValidation;
import com.weiping.servicecentre.finance.loan.asyncTasks.LoanFormUpload;

import platform.tyk.weping.com.weipingplatform.R;

public class LoanApplicationFormActivity extends Activity {

    protected ScrollView scrollView;
    protected String[] formFieldsName;
    protected int[] formFieldsID = new int[]{
            R.id.et_cs_finance_loan_reg_capital,
            R.id.et_cs_finance_loan_total_asset,
            R.id.et_cs_finance_loan_total_debt,
            R.id.et_cs_finance_loan_bank_loan,
            R.id.et_cs_finance_loan_other_loan,
            R.id.et_cs_finance_loan_owner_equity,
            R.id.et_cs_finance_loan_asset_liability_ratio,
            R.id.et_cs_finance_loan_main_product,
            R.id.et_cs_finance_loan_productivity,
            R.id.et_cs_finance_loan_product_capability,
            R.id.et_cs_finance_loan_industrial_output,
            R.id.et_cs_finance_loan_sales_income,
            R.id.et_cs_finance_loan_total_profit,
            R.id.et_cs_finance_loan_total_tax,
            R.id.et_cs_finance_loan_added_value_tax,
            R.id.et_cs_finance_loan_purpose,
            R.id.et_cs_finance_loan_limit,
            R.id.et_cs_finance_loan_period
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_loan_application_form));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(LoanApplicationFormActivity.this);
            }
        });
        setContentView(R.layout.activity_loan_application_form);
        scrollView = (ScrollView)findViewById(R.id.sv_loan_application_form);

        for(int i=0; i<formFieldsID.length; i++) {
            EditText editText = (EditText) findViewById(formFieldsID[i]);
            final int index = i;

            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        final int offset_textView = findViewById(R.id.txt_cs_finance_loan_company_capital_status).getHeight();
                        final int offset_editText_space = findViewById(R.id.view_space_et_cs_finance_loan_form).getHeight();
                        final int offset_field_space = findViewById(R.id.view_space_field_cs_finance_loan_form).getHeight();

                        int offset = 0;
                        int field_height = v.getHeight() + offset_editText_space*2;
                        if(index<7){
                            offset = field_height*index + offset_field_space*index + offset_textView;
                        }
                        else if(index<15){
                            offset = field_height*index + offset_field_space*index + offset_textView*2;
                        }
                        else{
                            offset = field_height*index + offset_field_space*index + offset_textView*3;
                        }
                        final int y = offset;

                        scrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.smoothScrollTo(0, y);
                            }
                        });

                    }
                }
            });
        }

        formFieldsName = new String[]{
                getResources().getString(R.string.txtView_loan_reg_capital),
                getResources().getString(R.string.txtView_loan_total_asset),
                getResources().getString(R.string.txtView_loan_total_debt),
                getResources().getString(R.string.txtView_loan_bank_loan),
                getResources().getString(R.string.txtView_loan_other_loan),
                getResources().getString(R.string.txtView_loan_owner_equity),
                getResources().getString(R.string.txtView_loan_asset_liability_ratio),
                getResources().getString(R.string.txtView_loan_main_product),
                getResources().getString(R.string.txtView_loan_productivity),
                getResources().getString(R.string.txtView_loan_product_capability),
                getResources().getString(R.string.txtView_loan_industrial_output),
                getResources().getString(R.string.txtView_loan_sales_income),
                getResources().getString(R.string.txtView_loan_total_profit),
                getResources().getString(R.string.txtView_loan_total_tax),
                getResources().getString(R.string.txtView_loan_added_value_tax),
                getResources().getString(R.string.txtView_loan_purpose),
                getResources().getString(R.string.txtView_loan_limit),
                getResources().getString(R.string.txtView_loan_period),
        };
    }

    public void onClickListenerSubmitLoanApplication(View view){
        LoanForm submitForm = new LoanForm();
        String[] fieldValueList = new String[formFieldsID.length];
        for(int i=0; i<formFieldsID.length; i++) {
            fieldValueList[i] = ((EditText) findViewById(formFieldsID[i])).getText().toString();
        }
        submitForm.setFields(fieldValueList);
        LoanValidation validator = new LoanValidation();
        int errorIndex = validator.validateCreditFormMandatoryFields(submitForm);
        if(errorIndex >= 0){
            String errorFieldName = formFieldsName[errorIndex];
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder
                    .setMessage(errorFieldName + "不能为空！")
                    .setPositiveButton(getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;
        }

        LoanFormUpload uploader = new LoanFormUpload(this, submitForm);
        uploader.uploadForm();
    }

    public void onClickActionBarBack(View view){
        onBackPressed();
    }

}

package com.weiping.servicecentre.finance.loan.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import platform.tyk.weping.com.weipingplatform.R;

public class CreditLoanActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_credit_loan));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(CreditLoanActivity.this);
            }
        });
        setContentView(R.layout.activity_credit_loan);
        TextView notice = (TextView)findViewById(R.id.cs_finance_loan_notice);
        notice.setText(Html.fromHtml(getString(R.string.loan_notices)));
    }

    public void onClickListenerApplyLoan(View view){
        Intent intent = new Intent(getBaseContext(), LoanApplicationFormActivity.class);
        startActivity(intent);
    }

    public void onClickActionBarBack(View view){
        onBackPressed();
    }

}

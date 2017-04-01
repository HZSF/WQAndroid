package com.weiping.servicecentre.finance.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.weiping.common.BitmapWorkerTask;
import com.weiping.servicecentre.finance.loan.activity.CreditLoanActivity;
import com.weiping.servicecentre.finance.onlend.activity.OnlendActivity;

import platform.tyk.weping.com.weipingplatform.R;

public class FinancialServiceActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_service);

       // loadBitmap(R.drawable.cs_financial_center_top, (ImageView) findViewById(R.id.cs_finance_center_image));
    }

    public void onClickListenerCreditLoan(View view){
        Intent intent = new Intent(getBaseContext(), CreditLoanActivity.class);
        startActivity(intent);
    }

    public void onClickListenerEnlending(View view){
        Intent intent = new Intent(getBaseContext(), OnlendActivity.class);
        startActivity(intent);
    }

    public void onClickListenerOtherFinance(View view){

    }

    public void loadBitmap(int resId, ImageView imageView){
        BitmapWorkerTask task = new BitmapWorkerTask(imageView, FinancialServiceActivity.this);
        task.execute(resId);
    }

}

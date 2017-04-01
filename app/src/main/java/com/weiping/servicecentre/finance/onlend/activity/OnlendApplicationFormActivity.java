package com.weiping.servicecentre.finance.onlend.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.weiping.common.DatePickerFragment;
import com.weiping.common.EditDate;
import com.weiping.common.StringUtility;
import com.weiping.servicecentre.Constants;
import com.weiping.servicecentre.finance.onlend.asyncTasks.LendFormUploadTask;

import java.util.Calendar;

import platform.tyk.weping.com.weipingplatform.R;

public class OnlendApplicationFormActivity extends Activity implements EditDate {

    private EditText deadline;
    private DatePickerFragment newFragment;
    private int year;
    private int month;
    private int day;
    private String selectedBank = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_onlend));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(OnlendApplicationFormActivity.this);
            }
        });
        setContentView(R.layout.activity_onlend_application_form);
        newFragment = new DatePickerFragment();
        deadline = (EditText)findViewById(R.id.et_cs_finance_onlend_deadline);
        setCurrentDateOnView();

        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup_lend_bank);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton_lend_bank_cmb:
                        selectedBank = Constants.BANK_CMB;
                        break;
                    case R.id.radioButton_lend_bank_ccb:
                        selectedBank = Constants.BANK_CCB;
                        break;
                    case R.id.radioButton_lend_bank_jxccb:
                        selectedBank = Constants.BANK_JXCCB;
                        break;
                    case R.id.radioButton_lend_bank_czcb:
                        selectedBank = Constants.BANK_CZCB;
                        break;
                    case R.id.radioButton_lend_bank_hxb:
                        selectedBank = Constants.BANK_HXB;
                        break;
                    case R.id.radioButton_lend_bank_czb:
                        selectedBank = Constants.BANK_CZB;
                        break;
                    case R.id.radioButton_lend_bank_zjwx:
                        selectedBank = Constants.BANK_ZJWX;
                        break;
                }
            }
        });
    }

    public void setCurrentDateOnView(){
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
    }

    public void onClickListenerOnlendChangeDeadline(View view){
        FragmentManager fm = getFragmentManager();
        newFragment.setDate(year, month, day);
        newFragment.show(fm, "datePicker");
    }

    public void onClickListenerOnlendFormSubmit(View view){
        String amount = ((EditText)findViewById(R.id.et_cs_finance_lend_amount)).getText().toString();
        String deadline = ((EditText)findViewById(R.id.et_cs_finance_onlend_deadline)).getText().toString();
        if(StringUtility.isEmptyString(amount) || StringUtility.isEmptyString(deadline) || StringUtility.isEmptyString(selectedBank)){
            return;
        }

        LendFormUploadTask task = new LendFormUploadTask(this, amount, deadline, selectedBank);
        task.execute(Constants.URL_CS_FINANCE_LEND_SUBMIT);
    }

    public void onClickActionBarBack(View view){
        onBackPressed();
    }

    @Override
    public void onFinishDateDialog(int y, int m, int d) {
        year = y;
        month = m;
        day = d;
        String monthStr = "";
        if(m+1 >= 10) {
            monthStr = String.valueOf(m + 1);
        }else{
            monthStr = "0" + String.valueOf(m + 1);
        }
        String dayStr = "";
        if(d >= 10){
            dayStr = String.valueOf(d);
        }else{
            dayStr = "0" + String.valueOf(d);
        }
        deadline.setText(year+"-"+monthStr+"-"+dayStr);
    }
}

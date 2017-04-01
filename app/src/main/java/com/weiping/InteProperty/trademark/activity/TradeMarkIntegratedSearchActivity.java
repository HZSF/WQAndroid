package com.weiping.InteProperty.trademark.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.InteProperty.trademark.asyncTasks.TrademarkIntegratedSearchTask;
import com.weiping.InteProperty.trademark.core.Constants;
import com.weiping.common.StringUtility;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import platform.tyk.weping.com.weipingplatform.R;

public class TradeMarkIntegratedSearchActivity extends Activity {

    private String categoryNum = "0";
    private String resultSort = "0";
    private String queryMethodTrademarkName = "0";
    private String queryMethodApplicantNameC = "0";
    private String queryMethodApplicantNameE = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_trade_mark_integrated_search));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_trade_mark_integrated_search);
        setSpinnerView();
    }

    public void setSpinnerView(){
        Spinner spinner_categoryNum = (Spinner)findViewById(R.id.sp_trademark_integrated_query_category_num);
        ArrayAdapter<CharSequence> arrayAdapter_categoryNum = ArrayAdapter.createFromResource(this, R.array.category_numbers, android.R.layout.simple_spinner_item);
        arrayAdapter_categoryNum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_categoryNum.setAdapter(arrayAdapter_categoryNum);
        spinner_categoryNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryNum = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner resultSortSpinner = (Spinner)findViewById(R.id.result_sort_spinner);
        ArrayAdapter<CharSequence> resultSortAdapter = ArrayAdapter.createFromResource(this, R.array.result_sort_style_array, android.R.layout.simple_spinner_item);
        resultSortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        resultSortSpinner.setAdapter(resultSortAdapter);
        resultSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                resultSort = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner queryMethodSpinner_trademarkName = (Spinner)findViewById(R.id.sp_trademark_integrated_query_methods_trademark_name);
        ArrayAdapter<CharSequence> queryMethodAdapter_trademarkName = ArrayAdapter.createFromResource(this, R.array.spinner_trademark_query_method_array, android.R.layout.simple_spinner_item);
        queryMethodAdapter_trademarkName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        queryMethodSpinner_trademarkName.setAdapter(queryMethodAdapter_trademarkName);
        queryMethodSpinner_trademarkName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                queryMethodTrademarkName = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner queryMethodSpinner_applicantNameC = (Spinner)findViewById(R.id.sp_trademark_integrated_query_methods_applicant_nameC);
        ArrayAdapter<CharSequence> queryMethodAdapter_applicantNameC = ArrayAdapter.createFromResource(this, R.array.spinner_trademark_query_method_array, android.R.layout.simple_spinner_item);
        queryMethodAdapter_applicantNameC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        queryMethodSpinner_applicantNameC.setAdapter(queryMethodAdapter_applicantNameC);
        queryMethodSpinner_applicantNameC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                queryMethodApplicantNameC = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner queryMethodSpinner_applicantNameE = (Spinner)findViewById(R.id.sp_trademark_integrated_query_methods_applicant_nameE);
        ArrayAdapter<CharSequence> queryMethodAdapter_applicantNameE = ArrayAdapter.createFromResource(this, R.array.spinner_trademark_query_method_array, android.R.layout.simple_spinner_item);
        queryMethodAdapter_applicantNameE.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        queryMethodSpinner_applicantNameE.setAdapter(queryMethodAdapter_applicantNameE);
        queryMethodSpinner_applicantNameE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                queryMethodApplicantNameE = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public boolean networkConnect() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void performSearch(View view) throws IOException {

        Map submitData = new HashMap();
        submitData.put(Constants.SUBMIT_DATA_CATEGORYNUM, categoryNum);
        submitData.put(Constants.SUBMIT_DATA_RESULT_SORT_NUM, resultSort);

        EditText trademarkApplNumText = (EditText)findViewById(R.id.et_trademark_integrated_appl_num);
        String trademarkApplNum = trademarkApplNumText.getText().toString();
        submitData.put(Constants.SUBMIT_DATA_APPLICATION_NUM, trademarkApplNum);

        EditText trademarkNameText = (EditText)findViewById(R.id.et_trademarkNameText);
        String trademarkName = trademarkNameText.getText().toString();
        submitData.put(Constants.SUBMIT_DATA_SEARCHCONTENT, trademarkName);

        EditText applicantNameCText = (EditText)findViewById(R.id.et_applicantNameCText);
        String applicantNameC = applicantNameCText.getText().toString();
        submitData.put(Constants.SUBMIT_DATA_APPLICANT_NAME_C, applicantNameC);

        EditText applicantNameEText = (EditText)findViewById(R.id.et_applicantNameEText);
        String applicantNameE = applicantNameEText.getText().toString();
        submitData.put(Constants.SUBMIT_DATA_APPLICANT_NAME_E, applicantNameE);

        if(!networkConnect()){
            Toast.makeText(TradeMarkIntegratedSearchActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }

        boolean applNumEmpty = StringUtility.isEmptyString(trademarkApplNum);
        boolean nameEmpty = StringUtility.isEmptyString(trademarkName);
        boolean nameCEmpty = StringUtility.isEmptyString(applicantNameC);
        boolean nameEEmpty = StringUtility.isEmptyString(applicantNameE);
        if(applNumEmpty && nameEmpty && nameCEmpty && nameEEmpty){
            return;
        }else if(!applNumEmpty && nameEmpty && nameCEmpty && nameEEmpty){
            TrademarkIntegratedSearchTask task = new TrademarkIntegratedSearchTask(this, true, false, false, false, submitData);
            task.execute(Constants.URL_TRADEMARK_INTEGRATED);
        }else if(applNumEmpty && !nameEmpty && nameCEmpty && nameEEmpty) {
            submitData.put(Constants.SUBMIT_DATA_QUERY_METHOD, queryMethodTrademarkName);
            TrademarkIntegratedSearchTask task = new TrademarkIntegratedSearchTask(this, false, true, false, false, submitData);
            task.execute(Constants.URL_TRADEMARK_INTEGRATED);
        }else if(applNumEmpty && nameEmpty && !nameCEmpty && nameEEmpty){
            submitData.put(Constants.SUBMIT_DATA_QUERY_METHOD, queryMethodApplicantNameC);
            TrademarkIntegratedSearchTask task = new TrademarkIntegratedSearchTask(this, false, false, true, false, submitData);
            task.execute(Constants.URL_TRADEMARK_INTEGRATED);
        }else if(applNumEmpty && nameEmpty && nameCEmpty && !nameEEmpty){
            submitData.put(Constants.SUBMIT_DATA_QUERY_METHOD, queryMethodApplicantNameE);
            TrademarkIntegratedSearchTask task = new TrademarkIntegratedSearchTask(this, false, false, false, true, submitData);
            task.execute(Constants.URL_TRADEMARK_INTEGRATED);
        }else{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder
                    .setMessage(getResources().getString(R.string.dialog_error_message_trademark_query_content))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            AlertDialog notLoginDialog = alertDialogBuilder.create();
            notLoginDialog.show();
            return;
        }
    }

    public void onClickActionBarBack(View view){
        onBackPressed();
    }

}

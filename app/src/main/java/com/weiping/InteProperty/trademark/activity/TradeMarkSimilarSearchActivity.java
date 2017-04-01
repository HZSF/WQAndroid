package com.weiping.InteProperty.trademark.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
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

import com.weiping.InteProperty.trademark.core.Constants;
import com.weiping.InteProperty.trademark.core.TradeMarkSearcherJSON;
import com.weiping.InteProperty.trademark.asyncTasks.TradeMarkSimilarSearcher;
import com.weiping.InteProperty.trademark.core.TradeMarkValidation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import platform.tyk.weping.com.weipingplatform.R;

public class TradeMarkSimilarSearchActivity extends Activity {

    private String categoryNum = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_trade_mark_similar_search));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_trade_mark_similar_search);
        setSpinnerView();
    }

    public void setSpinnerView(){
        Spinner spinner_categoryNum = (Spinner)findViewById(R.id.sp_trademark_similar_query_category_num);
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


        Spinner searchMethodSpinner = (Spinner)findViewById(R.id.combo_search_method);
        ArrayAdapter<CharSequence> searchMethodAdapter = ArrayAdapter.createFromResource(this, R.array.combo_search_method, android.R.layout.simple_spinner_item);
        searchMethodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchMethodSpinner.setAdapter(searchMethodAdapter);

    }

    public boolean networkConnect() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    public void performAutoSearch(View view) throws IOException {

        Map submitData = new HashMap();
        submitData.put(Constants.SUBMIT_DATA_CATEGORYNUM, categoryNum);

        String[] searchMethodList;
        String[] searchMethodListValue;
        searchMethodList = getResources().getStringArray(R.array.combo_search_method);
        searchMethodListValue = getResources().getStringArray(R.array.combo_search_method_value);
        Spinner searchMethodSpinner=(Spinner) findViewById(R.id.combo_search_method);
        String searchMethodSelected = searchMethodSpinner.getSelectedItem().toString();
        String searchMethodSelectedValue = "-1";
        for(int i=0; i<searchMethodList.length; i++){
            if(searchMethodSelected.equalsIgnoreCase(searchMethodList[i])){
                searchMethodSelectedValue = searchMethodListValue[i];
                break;
            }
        }
        submitData.put(Constants.SUBMIT_DATA_SEARCHMETHOD, searchMethodSelectedValue);

        EditText searchContentText = (EditText)findViewById(R.id.et_searchContent);
        String searchContent = searchContentText.getText().toString();
        submitData.put(Constants.SUBMIT_DATA_SEARCHCONTENT, searchContent);

        boolean isError = TradeMarkValidation.validateSimilarSearchInput(submitData);
        if(isError){
            return;
        }

        if(!networkConnect()){
            Toast.makeText(TradeMarkSimilarSearchActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }

        TradeMarkSimilarSearcher searcher = new TradeMarkSimilarSearcher(TradeMarkSimilarSearchActivity.this);
        TradeMarkSearcherJSON searcherJSON = new TradeMarkSearcherJSON();
        searcherJSON.setCategoryNumber(categoryNum);
        searcherJSON.setSearchMethod(searchMethodSelectedValue);
        searcherJSON.setSearchContent(searchContent);
        searcherJSON.setSearchType(Constants.JSON_NAME_SIMILAR_SEARCH);
        searcher.getTrademarkResult(searcherJSON);
    }

    public void onClickActionBarBack(View view){
        onBackPressed();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

}

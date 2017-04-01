package com.weiping.InteProperty.trademark.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.InteProperty.trademark.adapters.TrademarkIntegratedPrecisionResultListAdapter;
import com.weiping.InteProperty.trademark.asyncTasks.TrademarkIntegratedPrecisionSearchTask;
import com.weiping.InteProperty.trademark.base.TrademarkIntegratedResultItem;
import com.weiping.InteProperty.trademark.core.Constants;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class TradeMarkIntegratedPrecisionResultListActivity extends Activity {

    private ArrayList<TrademarkIntegratedResultItem> itemArrayList;
    private TrademarkIntegratedPrecisionResultListAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_trade_mark_integrated_precision_result_list));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_trade_mark_integrated_precision_result_list);

        itemArrayList = (ArrayList<TrademarkIntegratedResultItem>)getIntent().getSerializableExtra("result_list");

        if(itemArrayList != null && itemArrayList.size() > 0){
            initPage();
        }else{
            downloadLists();
        }

    }

    private void initPage(){
        adapter = new TrademarkIntegratedPrecisionResultListAdapter(this, itemArrayList);
        listView = (ListView)findViewById(R.id.listView_trademark_integrated_precision_result);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<TrademarkIntegratedResultItem> trademarkList = ((TrademarkIntegratedPrecisionResultListAdapter) parent.getAdapter()).getItemArrayList();
                String categoryNum = trademarkList.get(position).getCategoryNum();
                String regNum = trademarkList.get(position).getRegNum();
                Intent intent = new Intent(getBaseContext(), TradeMarkSimilarDetailActivity.class);
                intent.putExtra(Constants.JSON_NAME_CATEGORY_NUM, categoryNum);
                intent.putExtra(Constants.JSON_NAME_REG_NUM, regNum);
                intent.putExtra(Constants.JSON_NAME_NAME, trademarkList.get(position).getName());
                startActivity(intent);
            }
        });
    }

    private void downloadLists(){
        if(!networkConnect()){
            Toast.makeText(TradeMarkIntegratedPrecisionResultListActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
        }else {
            String name = getIntent().getStringExtra(Constants.SUBMIT_DATA_SEARCHCONTENT);
            String categoryNum = getIntent().getStringExtra(Constants.SUBMIT_DATA_CATEGORYNUM);
            String sortResult = getIntent().getStringExtra(Constants.SUBMIT_DATA_RESULT_SORT_NUM);
            String searchBy = getIntent().getStringExtra(Constants.SUBMIT_DATA_SEARCHMETHOD);
            String link = getIntent().getStringExtra(Constants.SUBMIT_DATA_LINK);
            TrademarkIntegratedPrecisionSearchTask task = new TrademarkIntegratedPrecisionSearchTask(this, categoryNum, name, sortResult, searchBy);
            task.setLink(link);
            task.execute(Constants.URL_TRADEMARK_INTEGRATED_PRECISION);
        }
    }

    public boolean networkConnect() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void onClickActionBarBack(View view){
        onBackPressed();
    }

}

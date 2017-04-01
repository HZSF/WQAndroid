package com.weiping.InteProperty.trademark.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.InteProperty.trademark.asyncTasks.TradeMarkSimilarMoreTask;
import com.weiping.InteProperty.trademark.base.TrademarkSimilarResultItem;
import com.weiping.InteProperty.trademark.adapters.TrademarkSimilarResultListAdapter;
import com.weiping.InteProperty.trademark.core.Constants;
import com.weiping.InteProperty.trademark.core.TradeMarkSearcherJSON;

import java.util.ArrayList;
import java.util.HashMap;

import platform.tyk.weping.com.weipingplatform.R;

public class TradeMarkSimilarResultListActivity extends Activity {

    private ListView listView;
    private TrademarkSimilarResultListAdapter adapter;
    private ArrayList<TrademarkSimilarResultItem> itemArrayList;
    private HashMap<String, String> pageInfoMap;
    private TradeMarkSearcherJSON basicInfo;
    private View moreView;
    private int lastItem;
    public int count=50;

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
        setContentView(R.layout.activity_trade_mark_similar_result_list);
        itemArrayList = (ArrayList<TrademarkSimilarResultItem>)getIntent().getSerializableExtra("result_list");
        pageInfoMap = (HashMap)getIntent().getSerializableExtra("page_info_map");
        basicInfo = (TradeMarkSearcherJSON)getIntent().getSerializableExtra("basic_info_json");

        adapter = new TrademarkSimilarResultListAdapter(this, itemArrayList);
        listView = (ListView)findViewById(R.id.listView_trademark_similar_result);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<TrademarkSimilarResultItem> trademarkList = adapter.getItemArrayList();
                String categoryNum = trademarkList.get(position).getCategoryNum();
                String regNum = trademarkList.get(position).getRegNum();
                Intent intent = new Intent(getBaseContext(), TradeMarkSimilarDetailActivity.class);
                intent.putExtra(Constants.JSON_NAME_CATEGORY_NUM, categoryNum);
                intent.putExtra(Constants.JSON_NAME_REG_NUM, regNum);
                intent.putExtra(Constants.JSON_NAME_NAME, trademarkList.get(position).getName());
                startActivity(intent);
            }
        });

        if(Constants.YES.equalsIgnoreCase(pageInfoMap.get(Constants.JSON_NAME_HAS_NEXT_PAGE))){
            moreView = getLayoutInflater().inflate(R.layout.listview_footer_loading, null);
            moreView.setVisibility(View.GONE);
            listView.addFooterView(moreView);

            listView.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (lastItem == count && scrollState == SCROLL_STATE_IDLE) {
                        if (!adapter.isDownloading()) {
                            adapter.setDownloading(true);
                            moreView.setVisibility(view.VISIBLE);
                            loadMoreData();
                        }
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    lastItem = firstVisibleItem + visibleItemCount - 1;
                    if (totalItemCount - 1 != count) {
                        count = totalItemCount - 1;
                    }
                }
            });
        }
    }

    public void loadMoreData(){
        if(!networkConnect()){
            Toast.makeText(TradeMarkSimilarResultListActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }
        TradeMarkSimilarMoreTask task = new TradeMarkSimilarMoreTask(this, listView, adapter, moreView, pageInfoMap, basicInfo);
        task.execute(Constants.URL_TRADEMARK);
    }

    public boolean networkConnect() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void onClickActionBarBack(View view){
        onBackPressed();
    }

    public HashMap<String, String> getPageInfoMap() {
        return pageInfoMap;
    }

    public void setPageInfoMap(HashMap<String, String> pageInfoMap) {
        this.pageInfoMap = pageInfoMap;
    }
}

package com.weiping.servicecentre.property.buy.activity;

import android.app.ActionBar;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.common.Constants;
import com.weiping.servicecentre.property.buy.adapter.PropertySellingListAdapter;
import com.weiping.servicecentre.property.buy.asyncTasks.PropertySellingFetchMoreTask;
import com.weiping.servicecentre.property.buy.asyncTasks.PropertySellingFetchTask;
import com.weiping.servicecentre.property.buy.model.PropertySellingListItem;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class PropertySellingListActivity extends Activity {
    private ArrayList<PropertySellingListItem> itemArrayList;
    private ListView listView;
    private PropertySellingListAdapter adapter;
    private View moreView;
    private int lastItem;
    public int count=20;

    public PropertySellingFetchTask taskFetch;
    public PropertySellingFetchMoreTask taskFetchMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_property_selling_list));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_property_selling_list);
        initListView();
        fetchData();
    }

    private void initListView(){
        itemArrayList = new ArrayList<>();
        adapter = new PropertySellingListAdapter(this, itemArrayList);
        listView = (ListView)findViewById(R.id.lv_property_selling);
        moreView = getLayoutInflater().inflate(R.layout.listview_footer_loading, null);
        moreView.setVisibility(View.GONE);
        listView.addFooterView(moreView);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (lastItem == count && scrollState == SCROLL_STATE_IDLE) {
                    if (!adapter.isDownloading()) {
                        adapter.setDownloading(true);
                        moreView.setVisibility(view.VISIBLE);
                        ArrayList<PropertySellingListItem> arrayList = adapter.getItemArrayList();
                        int id = arrayList.get(lastItem - 1).getId();
                        loadMoreData(id);
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

    public void loadMoreData(int startId){
        if(!networkConnect()){
            Toast.makeText(PropertySellingListActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }
        taskFetchMore = new PropertySellingFetchMoreTask(this, listView, adapter, moreView, startId);
        taskFetchMore.execute(Constants.URL_PROPERTY_FETCH_SELL);
    }

    public void fetchData(){

        if(!networkConnect()){
            Toast.makeText(PropertySellingListActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }

        taskFetch = new PropertySellingFetchTask(this, listView, adapter, moreView);
        taskFetch.execute(Constants.URL_PROPERTY_FETCH_SELL);
    }

    public boolean networkConnect() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void onClickActionBarBack(View view){
        if (taskFetch != null){
            taskFetch.cancel(true);
        }
        if (taskFetchMore != null){
            taskFetchMore.cancel(true);
        }
        onBackPressed();
    }

}

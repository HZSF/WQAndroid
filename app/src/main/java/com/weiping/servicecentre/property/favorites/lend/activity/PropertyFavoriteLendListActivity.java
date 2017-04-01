package com.weiping.servicecentre.property.favorites.lend.activity;

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
import com.weiping.servicecentre.property.favorites.lend.adapter.PropertyFavoriteLendListAdapter;
import com.weiping.servicecentre.property.favorites.lend.asyncTasks.PropertyFavoriteLendFetchMoreTask;
import com.weiping.servicecentre.property.favorites.lend.asyncTasks.PropertyFavoriteLendFetchTask;
import com.weiping.servicecentre.property.rent.model.PropertyLendingListItem;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class PropertyFavoriteLendListActivity extends Activity {
    private ArrayList<PropertyLendingListItem> itemArrayList;
    private ListView listView;
    private PropertyFavoriteLendListAdapter adapter;
    private View moreView;
    private int lastItem;
    public int count=20;
    public PropertyFavoriteLendFetchTask taskFetch;
    public PropertyFavoriteLendFetchMoreTask taskFetchMore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_property_favorite_lend_list));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_property_favorite_lend_list);
        initListView();
        fetchData();
    }

    private void initListView(){
        itemArrayList = new ArrayList<>();
        adapter = new PropertyFavoriteLendListAdapter(this, itemArrayList);
        listView = (ListView)findViewById(R.id.lv_property_lending);
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
                        ArrayList<PropertyLendingListItem> arrayList = adapter.getItemArrayList();
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
            Toast.makeText(PropertyFavoriteLendListActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }
        taskFetchMore = new PropertyFavoriteLendFetchMoreTask(this, listView, adapter, moreView, startId);
        taskFetchMore.execute(Constants.URL_PROPERTY_FETCH_FAVORITE_LEND);
    }

    public void fetchData(){

        if(!networkConnect()){
            Toast.makeText(PropertyFavoriteLendListActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }

        taskFetch = new PropertyFavoriteLendFetchTask(this, listView, adapter, moreView);
        taskFetch.execute(Constants.URL_PROPERTY_FETCH_FAVORITE_LEND);
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

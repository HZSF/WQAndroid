package com.weiping.InteProperty.patent.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.InteProperty.patent.adapter.PatentAchieveTransformListAdapter;
import com.weiping.InteProperty.patent.asyncTasks.PatentAchieveTransformFetchMoreTask;
import com.weiping.InteProperty.patent.asyncTasks.PatentAchieveTransformFetchTask;
import com.weiping.InteProperty.patent.base.Constants;
import com.weiping.InteProperty.patent.model.PatentAchieveTransformListItem;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class PatentAchieveTransformListActivity extends Activity {

    private ArrayList<PatentAchieveTransformListItem> itemArrayList;
    private ListView listView;
    private PatentAchieveTransformListAdapter adapter;
    private View moreView;
    private int lastItem;
    public int count=20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_patent_achieve_transform_list));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_patent_achieve_transform_list);
        initListView();
        fetchData();
    }

    public void onClickSearchPatentAchieveTransformList(View view){
        Intent intent = new Intent(getBaseContext(), PatentAchieveTransformListSearchPageActivity.class);
        intent.putExtra("patent_list", adapter.getItemArrayList());
        startActivity(intent);
    }

    private void initListView(){
        itemArrayList = new ArrayList<>();
        adapter = new PatentAchieveTransformListAdapter(this, itemArrayList);
        listView = (ListView)findViewById(R.id.list_view_patent_achieve_transform);
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
                        ArrayList<PatentAchieveTransformListItem> arrayList = adapter.getItemArrayList();
                        String id = arrayList.get(lastItem - 1).getId();
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

    public void loadMoreData(String startId){
        if(!networkConnect()){
            Toast.makeText(PatentAchieveTransformListActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }
        PatentAchieveTransformFetchMoreTask task = new PatentAchieveTransformFetchMoreTask(this, listView, adapter, moreView, startId);
        task.execute(Constants.URL_PATENT_ACHIEVE_TRANSFORM);
    }

    public void fetchData(){

        if(!networkConnect()){
            Toast.makeText(PatentAchieveTransformListActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }

        PatentAchieveTransformFetchTask task = new PatentAchieveTransformFetchTask(this, listView, adapter);
        task.execute(Constants.URL_PATENT_ACHIEVE_TRANSFORM);
    }

    public boolean networkConnect() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void onClickActionBarBack(View view){
        onBackPressed();
    }

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }
}

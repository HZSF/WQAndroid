package com.weiping.platform.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.weiping.InteProperty.patent.activity.PatentAchieveTransformListSearchPageActivity;
import com.weiping.InteProperty.patent.adapter.PatentAchieveTransformListAdapter;
import com.weiping.InteProperty.patent.asyncTasks.PatentAchieveTransformFetchMoreTask;
import com.weiping.InteProperty.patent.asyncTasks.PatentAchieveTransformFetchTask;
import com.weiping.InteProperty.patent.base.Constants;
import com.weiping.InteProperty.patent.model.PatentAchieveTransformListItem;
import com.weiping.platform.MainMemberActivity;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class PatentTradeFragment extends MainMemberActivity.PlaceholderFragment {
    public View rootView;

    private ArrayList<PatentAchieveTransformListItem> itemArrayList;
    private ListView listView;
    private PatentAchieveTransformListAdapter adapter;
    private View moreView;
    private int lastItem;
    public int count=20;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_patent_trade, container, false);
        initListView();
        fetchData();

        rootView.findViewById(R.id.rl_patent_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getBaseContext(), PatentAchieveTransformListSearchPageActivity.class);
                intent.putExtra("patent_list", adapter.getItemArrayList());
                startActivity(intent);
            }
        });
        return rootView;
    }
    private void initListView(){
        itemArrayList = new ArrayList<>();
        adapter = new PatentAchieveTransformListAdapter(getActivity(), itemArrayList);
        listView = (ListView)rootView.findViewById(R.id.list_view_patent_achieve_transform);
        moreView = getActivity().getLayoutInflater().inflate(R.layout.listview_footer_loading, null);
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
            Toast.makeText(getActivity(), getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }
        PatentAchieveTransformFetchMoreTask task = new PatentAchieveTransformFetchMoreTask(getActivity(), listView, adapter, moreView, startId);
        task.execute(Constants.URL_PATENT_ACHIEVE_TRANSFORM);
    }

    public void fetchData(){

        if(!networkConnect()){
            Toast.makeText(getActivity(), getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }

        PatentAchieveTransformFetchTask task = new PatentAchieveTransformFetchTask(getActivity(), listView, adapter);
        task.execute(Constants.URL_PATENT_ACHIEVE_TRANSFORM);
    }
    public boolean networkConnect() {
        ConnectivityManager connMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}

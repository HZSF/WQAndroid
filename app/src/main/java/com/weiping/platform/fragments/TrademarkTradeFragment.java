package com.weiping.platform.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.weiping.InteProperty.trademark.activity.TrademarkTradeDetailActivity;
import com.weiping.InteProperty.trademark.adapters.TrademarkTradeListAdapter;
import com.weiping.InteProperty.trademark.asyncTasks.TrademarkTradeFetchMoreTask;
import com.weiping.InteProperty.trademark.asyncTasks.TrademarkTradeFetchTask;
import com.weiping.InteProperty.trademark.base.TrademarkTradeItem;
import com.weiping.InteProperty.trademark.core.Constants;
import com.weiping.platform.MainMemberActivity;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class TrademarkTradeFragment extends MainMemberActivity.PlaceholderFragment  {
    public View rootView;

    private ArrayList<TrademarkTradeItem> itemArrayList;
    private ListView listView;
    private TrademarkTradeListAdapter adapter;
    private View moreView;
    private int lastItem;
    public int count=20;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_trademark_trade, container, false);
        initListView();
        fetchData();
        return rootView;
    }

    private void initListView(){
        itemArrayList = new ArrayList<>();
        adapter = new TrademarkTradeListAdapter(getActivity(), itemArrayList);
        listView = (ListView)rootView.findViewById(R.id.list_view_trademark_trade);
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
                        ArrayList<TrademarkTradeItem> arrayList = adapter.getItemArrayList();
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<TrademarkTradeItem> trademarkList = adapter.getItemArrayList();
                String categoryNum = trademarkList.get(position).getCategoryNum();
                String regNum = trademarkList.get(position).getRegNum();
                Intent intent = new Intent(getActivity(), TrademarkTradeDetailActivity.class);
                intent.putExtra(Constants.JSON_NAME_CATEGORY_NUM, categoryNum);
                intent.putExtra(Constants.JSON_NAME_REG_NUM, regNum);
                intent.putExtra(Constants.JSON_NAME_NAME, trademarkList.get(position).getName());
                startActivityForResult(intent, 1);
            }
        });
    }

    public void loadMoreData(String startId) {
        if (!networkConnect()) {
            Toast.makeText(getActivity(), getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }
        TrademarkTradeFetchMoreTask task = new TrademarkTradeFetchMoreTask(getActivity(), listView, adapter, moreView, startId);
        task.execute(Constants.URL_TRADEMARK_GET_TRADE);
    }

    public void fetchData() {
        if (!networkConnect()) {
            Toast.makeText(getActivity(), getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }
        TrademarkTradeFetchTask task = new TrademarkTradeFetchTask(getActivity(), listView, adapter);
        task.execute(Constants.URL_TRADEMARK_GET_TRADE);
    }

    public boolean networkConnect() {
        ConnectivityManager connMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == getActivity().RESULT_OK){
                boolean refresh = data.getBooleanExtra("update", false);
                if(refresh){
                    adapter.getItemArrayList().clear();
                    fetchData();
                }
            }
        }
    }
}

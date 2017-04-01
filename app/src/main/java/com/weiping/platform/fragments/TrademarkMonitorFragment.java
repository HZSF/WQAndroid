package com.weiping.platform.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.weiping.InteProperty.trademark.activity.TrademarkMonitorDetailActivity;
import com.weiping.InteProperty.trademark.adapters.TrademarkSimilarResultListAdapter;
import com.weiping.InteProperty.trademark.base.TrademarkSimilarResultItem;
import com.weiping.common.Constants;
import com.weiping.platform.MainMemberActivity;
import com.weiping.platform.asyncTasks.TrademarkMonitorListTask;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class TrademarkMonitorFragment extends MainMemberActivity.PlaceholderFragment {
    public View rootView;
    private ListView listView;
    private TrademarkSimilarResultListAdapter adapter;
    private ArrayList<TrademarkSimilarResultItem> itemArrayList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_trademark_monitor, container, false);
        listView =  (ListView) rootView.findViewById(R.id.list_view_trademark_monitor);
        itemArrayList = new ArrayList<>();
        adapter = new TrademarkSimilarResultListAdapter(getActivity(), itemArrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<TrademarkSimilarResultItem> trademarkList = adapter.getItemArrayList();
                String categoryNum = trademarkList.get(position).getCategoryNum();
                String regNum = trademarkList.get(position).getRegNum();
                Intent intent = new Intent(getActivity().getBaseContext(), TrademarkMonitorDetailActivity.class);
                intent.putExtra(com.weiping.InteProperty.trademark.core.Constants.JSON_NAME_CATEGORY_NUM, categoryNum);
                intent.putExtra(com.weiping.InteProperty.trademark.core.Constants.JSON_NAME_REG_NUM, regNum);
                intent.putExtra(com.weiping.InteProperty.trademark.core.Constants.JSON_NAME_NAME, trademarkList.get(position).getName());
                intent.putExtra("from", "monitor");
                startActivityForResult(intent, 1);
            }
        });
        fetchData();
        return rootView;
    }

    public void fetchData() {
        if (!networkConnect()) {
            Toast.makeText(getActivity(), getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }
        TrademarkMonitorListTask task = new TrademarkMonitorListTask(getActivity(), listView, adapter);
        task.execute(Constants.URL_TRADEMARK_GET_MONITOR);
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

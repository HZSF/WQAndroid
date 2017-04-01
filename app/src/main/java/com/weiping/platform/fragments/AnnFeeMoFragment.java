package com.weiping.platform.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.weiping.InteProperty.patent.adapter.PtntAnnFeeMonitRecyAdapter;
import com.weiping.InteProperty.patent.asyncTasks.PatentAnnualFeeMonitorFetchTask;
import com.weiping.InteProperty.patent.model.PatentAnnualFeeMonitorListItem;
import com.weiping.common.Constants;
import com.weiping.platform.MainMemberActivity;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class AnnFeeMoFragment extends MainMemberActivity.PlaceholderFragment {
    public View rootView;
    private ArrayList<PatentAnnualFeeMonitorListItem> itemArrayList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private PatentAnnualFeeMonitorFetchTask task;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ann_fee_mo, container, false);
        initRecyclerView();
        fetchData();
        return rootView;
    }
    private void initRecyclerView(){
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view_patent_annual_fee);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        itemArrayList = new ArrayList<>();
        mAdapter = new PtntAnnFeeMonitRecyAdapter(itemArrayList);
        recyclerView.setAdapter(mAdapter);
        registerForContextMenu(recyclerView);
    }
    public void fetchData(){

        if(!networkConnect()){
            Toast.makeText(getActivity(), getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }

        task = new PatentAnnualFeeMonitorFetchTask(getActivity(), (PtntAnnFeeMonitRecyAdapter)mAdapter);
        task.execute(Constants.URL_PATENT_ANNUAL_FEE_MONITOR);
    }

    public boolean networkConnect() {
        ConnectivityManager connMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}

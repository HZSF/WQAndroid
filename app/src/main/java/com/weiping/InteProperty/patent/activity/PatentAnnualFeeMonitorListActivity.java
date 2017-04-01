package com.weiping.InteProperty.patent.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.InteProperty.patent.adapter.PatentAnnualFeeMonitorListAdapter;
import com.weiping.InteProperty.patent.adapter.PtntAnnFeeMonitRecyAdapter;
import com.weiping.InteProperty.patent.asyncTasks.PatentAnnualFeeMonitorFetchTask;
import com.weiping.InteProperty.patent.asyncTasks.PtntAnnFeeMoCancelTask;
import com.weiping.InteProperty.patent.base.Constants;
import com.weiping.InteProperty.patent.model.PatentAnnualFeeMonitorListItem;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class PatentAnnualFeeMonitorListActivity extends Activity {

    private ArrayList<PatentAnnualFeeMonitorListItem> itemArrayList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private PatentAnnualFeeMonitorFetchTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_patent_annual_fee_monitor_list));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_patent_annual_fee_monitor_list);
        //initListView();
        initRecyclerView();
        fetchData();
    }

    private void initRecyclerView(){
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view_patent_annual_fee);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        itemArrayList = new ArrayList<>();
        mAdapter = new PtntAnnFeeMonitRecyAdapter(itemArrayList);
        recyclerView.setAdapter(mAdapter);
        registerForContextMenu(recyclerView);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        try {
            position = ((PtntAnnFeeMonitRecyAdapter)mAdapter).getPosition();
        }catch (Exception e) {
            Log.d("tag", e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case 0:
                ArrayList<PatentAnnualFeeMonitorListItem> itemlist = ((PtntAnnFeeMonitRecyAdapter) mAdapter).getItemArrayList();
                if(itemlist != null && itemlist.size() > position) {
                    String patentId = itemlist.get(position).getPatentId();
                    PtntAnnFeeMoCancelTask task = new PtntAnnFeeMoCancelTask(this, patentId);
                    task.execute(Constants.URL_PATENT_DEL_ANN_FEE_MON);
                }
                break;
            case 1:
                Log.i("tag", "cancel poa " + position);
                break;
        }
        return super.onContextItemSelected(item);
    }
/*
    private void initListView(){
        itemArrayList = new ArrayList<>();
        PatentAnnualFeeMonitorListAdapter adapter = new PatentAnnualFeeMonitorListAdapter(this, itemArrayList);
        final ListView listView = (ListView)findViewById(R.id.list_view_patent_annual_fee);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                if (v.getId() == R.id.list_view_patent_annual_fee) {
                    String[] menuItems = new String[]{"取消年费监测"};
                    for (int i = 0; i < menuItems.length; i++) {
                        menu.add(Menu.NONE, i, i, menuItems[i]);
                    }
                }
            }
        });
    }
*/

    public void fetchData(){

        if(!networkConnect()){
            Toast.makeText(PatentAnnualFeeMonitorListActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }

        task = new PatentAnnualFeeMonitorFetchTask(this, (PtntAnnFeeMonitRecyAdapter)mAdapter);
        task.execute(Constants.URL_PATENT_ANNUAL_FEE_MONITOR);
    }

    public boolean networkConnect() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void onClickActionBarBack(View view){
        if(task != null){
            task.cancel(true);
        }
        onBackPressed();
    }

}

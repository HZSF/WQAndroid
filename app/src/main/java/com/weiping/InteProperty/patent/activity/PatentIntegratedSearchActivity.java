package com.weiping.InteProperty.patent.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.InteProperty.patent.adapter.PatentISearchAdapter;
import com.weiping.InteProperty.patent.asyncTasks.PatentIntegratedSearchTask;
import com.weiping.InteProperty.patent.base.Constants;
import com.weiping.InteProperty.patent.model.PatentIntegratedItem;
import com.weiping.common.StringUtility;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class PatentIntegratedSearchActivity extends Activity {

    private PatentIntegratedSearchTask task_pis;

    private ArrayList<PatentIntegratedItem> itemArrayList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar progressBar;

    private String content;
    private boolean invent_checked;
    private boolean utility_checked;
    private boolean design_checked;
    private boolean grant_checked;
    private int page = 1;
    private int sort = 0;

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 3;
    public int visibleItemCount, totalItemCount, firstVisibleItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_patent_integrated_search));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_patent_integrated_search);
        progressBar = (ProgressBar)findViewById(R.id.pb_patent_search);
        progressBar.setVisibility(View.GONE);
        initRecyclerView();
    }

    private void initRecyclerView(){
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view_patent);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        itemArrayList = new ArrayList<>();
        mAdapter = new PatentISearchAdapter(itemArrayList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();

                //      if (loading) {
                //        if (totalItemCount > previousTotal) {
                //          loading = false;
                //         previousTotal = totalItemCount;
                //    }
                // }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold) && page > 0) {
                    loading = true;
                    loadMore();
                }
            }
        });
    }

    public void loadMore(){
        if (!networkConnect()) {
            Toast.makeText(PatentIntegratedSearchActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }
        task_pis = new PatentIntegratedSearchTask(this, content, page, sort);
        task_pis.setIsInvent(invent_checked);
        task_pis.setIsUtility(utility_checked);
        task_pis.setIsDesign(design_checked);
        task_pis.setIsGrant(grant_checked);
        task_pis.setAdapter((PatentISearchAdapter) mAdapter);
        task_pis.execute(Constants.URL_PATENT_INTEGRATED);
    }

    public void performSearch(View view){
        sort = 0;
        searchAction(sort);
    }

    public void onClickSort1(View view){
        sort = 0;
        searchAction(sort);
    }

    public void onClickSort2(View view){
        sort = 1;
        searchAction(sort);
    }

    public void onClickSort3(View view){
        sort = 2;
        searchAction(sort);
    }

    public void onClickSortMore(View view){
        openOptionsMenu();
    }

    private void searchAction(int sort){
        content = ((EditText)findViewById(R.id.et_patent_integrated)).getText().toString();
        if(StringUtility.isEmptyString(content)){
            return;
        }
        if(!networkConnect()){
            Toast.makeText(PatentIntegratedSearchActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }
        invent_checked = ((CheckBox)findViewById(R.id.chk_patent_invent)).isChecked();
        utility_checked = ((CheckBox)findViewById(R.id.chk_patent_utility)).isChecked();
        design_checked = ((CheckBox)findViewById(R.id.chk_patent_design)).isChecked();
        grant_checked = ((CheckBox)findViewById(R.id.chk_patent_invent_grant)).isChecked();
        page = 1;
        task_pis = new PatentIntegratedSearchTask(this, content, page, sort);
        task_pis.setIsInvent(invent_checked);
        task_pis.setIsUtility(utility_checked);
        task_pis.setIsDesign(design_checked);
        task_pis.setIsGrant(grant_checked);
        ((PatentISearchAdapter)mAdapter).clear();
        task_pis.setAdapter((PatentISearchAdapter) mAdapter);
        loading = true;
        task_pis.execute(Constants.URL_PATENT_INTEGRATED);
        progressBar.setVisibility(View.VISIBLE);
    }

    public boolean networkConnect() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.patent_search_sort, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.item1:
                sort = 3;
                searchAction(sort);
                return true;
            case R.id.item2:
                sort = 4;
                searchAction(sort);
                return true;
        }
        return true;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    @Override
    public void onBackPressed(){
        if (task_pis != null)
            task_pis.cancel(true);
        super.onBackPressed();
    }
}

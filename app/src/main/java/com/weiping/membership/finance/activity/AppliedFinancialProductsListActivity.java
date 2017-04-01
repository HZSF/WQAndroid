package com.weiping.membership.finance.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.membership.common.Constants;
import com.weiping.membership.finance.adapter.AppliedFinancialProductListAdapter;
import com.weiping.membership.finance.asyncTasks.AppliedFinancialProductsFetchTask;
import com.weiping.membership.finance.model.AppliedFinancialProductListItem;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class AppliedFinancialProductsListActivity extends Activity {

    private ArrayList<AppliedFinancialProductListItem> itemArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_applied_financial_products_list));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_applied_financial_products_list);
        initListView();
        fetchData();
    }

    private void initListView(){
        itemArrayList = new ArrayList<>();
        AppliedFinancialProductListAdapter adapter = new AppliedFinancialProductListAdapter(this, itemArrayList);
        final ListView listView = (ListView)findViewById(R.id.list_view_member_applied_financial_products);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<AppliedFinancialProductListItem> appliedFinancialProductListItems = ((AppliedFinancialProductListAdapter) listView.getAdapter()).getItemArrayList();
                String searchKey = appliedFinancialProductListItems.get(position).getId();
                String productType = appliedFinancialProductListItems.get(position).getProductType();
                if ("loan".equalsIgnoreCase(productType)) {
                    Intent intent = new Intent(getBaseContext(), AppliedCreditLoanDetailActivity.class);
                    intent.putExtra("search_loan_id", searchKey);
                    startActivityForResult(intent, 1);
                } else if ("lend".equalsIgnoreCase(productType)) {
                    Intent intent = new Intent(getBaseContext(), AppliedLendingDetailActivity.class);
                    intent.putExtra("search_lend_id", searchKey);
                    startActivityForResult(intent, 1);
                }
            }
        });
    }

    public void fetchData(){

        if(!networkConnect()){
            Toast.makeText(AppliedFinancialProductsListActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }

        AppliedFinancialProductsFetchTask task = new AppliedFinancialProductsFetchTask(this);
        task.execute(Constants.URL_MEMBER_FINANCE_APPOINTED_LOAN);
    }

    public boolean networkConnect() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                String refresh = data.getStringExtra("refreshList");
                if("Y".equalsIgnoreCase(refresh)){
                    fetchData();
                }
            }
        }
    }

    public void onClickActionBarBack(View view){
        onBackPressed();
    }

}

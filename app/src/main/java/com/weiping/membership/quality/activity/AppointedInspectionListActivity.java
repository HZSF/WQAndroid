package com.weiping.membership.quality.activity;

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
import com.weiping.membership.quality.adapter.AppointedInspectionListAdapter;
import com.weiping.membership.quality.asyncTasks.AppointedInspectionFetchTask;
import com.weiping.membership.quality.model.AppointedInspectionListItem;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class AppointedInspectionListActivity extends Activity {

    private ArrayList<AppointedInspectionListItem> itemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_appointed_inspection_list));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_appointed_inspection_list);
        initListView();
        fetchData();
    }

    private void initListView(){
        itemArrayList = new ArrayList<>();
        AppointedInspectionListAdapter adapter = new AppointedInspectionListAdapter(this, itemArrayList);
        final ListView listView = (ListView)findViewById(R.id.list_view_member_appointed_inspection);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<AppointedInspectionListItem> inspectionListItems = ((AppointedInspectionListAdapter) listView.getAdapter()).getItemArrayList();
                String inspectionType = inspectionListItems.get(position).getInspectionType();
                Intent intent;
                if (Constants.INSPECTION_TYPE_WOOD.equalsIgnoreCase(inspectionType)) {
                    intent = new Intent(getBaseContext(), AppointedInspectionWoodActivity.class);
                    startActivityForResult(intent, 1);
                } else if (Constants.INSPECTION_TYPE_KIDS.equalsIgnoreCase(inspectionType)) {
                    intent = new Intent(getBaseContext(), AppointedInspectionKidsActivity.class);
                    startActivityForResult(intent, 1);
                } else if (Constants.INSPECTION_TYPE_TEXTILE.equalsIgnoreCase(inspectionType)) {
                    intent = new Intent(getBaseContext(), AppointedInspectionTextileActivity.class);
                    startActivityForResult(intent, 1);
                }
            }
        });
    }

    public void fetchData(){
        if(!networkConnect()){
            Toast.makeText(AppointedInspectionListActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }
        AppointedInspectionFetchTask task = new AppointedInspectionFetchTask(this);
        task.execute(Constants.URL_MEMBER_QUALITY_APPOINTED_INSPECTION);
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

    public boolean networkConnect() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    public void onClickActionBarBack(View view){
        onBackPressed();
    }


}

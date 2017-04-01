package com.weiping.servicecentre.training.course.activity;

import android.app.ActionBar;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.platform.adapter.AppointedListAdapter;
import com.weiping.platform.asyncTasks.AppointedListTask;
import com.weiping.platform.model.AppointedListItem;
import com.weiping.servicecentre.Constants;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class AppointedCourseListActivity extends Activity {

    private ListView listView;
    private ArrayList<AppointedListItem> itemArrayList;
    private AppointedListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.lbl_appoint_course));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_appointed_course_list);
        listView = (ListView) findViewById(R.id.list_view_appointment);
        itemArrayList = new ArrayList<>();
        adapter = new AppointedListAdapter(this, itemArrayList);
        listView.setAdapter(adapter);
        fetchData();
    }
    public void fetchData() {
        if (!networkConnect()) {
            Toast.makeText(this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }
        AppointedListTask task = new AppointedListTask(this, listView, adapter);
        task.execute(Constants.URL_MEMBER_TRAIN_REGISTERED_COURSE);
    }
    public boolean networkConnect() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}

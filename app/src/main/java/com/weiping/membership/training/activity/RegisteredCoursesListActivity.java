package com.weiping.membership.training.activity;

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
import com.weiping.membership.training.adapter.RegisteredCourseListAdapter;
import com.weiping.membership.training.asyncTasks.RegisteredCoursesFetchTask;
import com.weiping.membership.training.model.RegisteredCourseListItem;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class RegisteredCoursesListActivity extends Activity {

    private ArrayList<RegisteredCourseListItem> itemArrayList;
    private RegisteredCoursesFetchTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_registered_courses_list));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_registered_courses_list);
        initListView();
        fetchData();
    }

    private void initListView(){
        itemArrayList = new ArrayList<>();
        RegisteredCourseListAdapter adapter = new RegisteredCourseListAdapter(this, itemArrayList);
        final ListView listView = (ListView)findViewById(R.id.list_view_member_registered_course);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<RegisteredCourseListItem> courseListItems = ((RegisteredCourseListAdapter) listView.getAdapter()).getItemArrayList();
                String searchKey = courseListItems.get(position).getId();
                Intent intent = new Intent(getBaseContext(), RegisteredCourseBodyActivity.class);
                intent.putExtra("search_course_id", searchKey);
                startActivityForResult(intent, 1);
            }
        });
    }

    public void fetchData(){

        if(!networkConnect()){
            Toast.makeText(RegisteredCoursesListActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }

        task = new RegisteredCoursesFetchTask(this);
        task.execute(Constants.URL_MEMBER_TRAIN_REGISTERED_COURSE);
    }

    public boolean networkConnect() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    public void onClickActionBarBack(View view){
        if (task != null)
            task.cancel(true);
        onBackPressed();
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

}

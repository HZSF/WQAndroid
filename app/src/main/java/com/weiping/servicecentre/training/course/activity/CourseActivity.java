package com.weiping.servicecentre.training.course.activity;

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

import com.weiping.servicecentre.Constants;
import com.weiping.servicecentre.training.course.CourseFetch;
import com.weiping.servicecentre.training.course.CourseListAdapter;
import com.weiping.servicecentre.training.course.CourseListItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class CourseActivity extends Activity {
    protected int categoryId;
    private ArrayList<CourseListItem> itemArrayList;
    private CourseFetch asyncFetch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            categoryId = extras.getInt("categoryID");
        }
        String[] courseList = getResources().getStringArray(R.array.cs_course_grid_name);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(courseList[categoryId]);
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_course);

        initListView();
        fetchData(Constants.COURSE_CATEGORY_ID_MAP[categoryId]);
    }

    public void initListView(){
        itemArrayList = new ArrayList<>();
        CourseListAdapter adapter = new CourseListAdapter(this, itemArrayList);
        ListView listView = (ListView)findViewById(R.id.course_list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<CourseListItem> courseListItems = ((CourseListAdapter)parent.getAdapter()).getItemArrayList();
                String searchKey = courseListItems.get(position).getId();
                String title = courseListItems.get(position).getTitle();
                Intent intent = new Intent(getBaseContext(), CourseBodyActivity.class);
                intent.putExtra("search_course_id", searchKey);
                intent.putExtra("course_title", title);
                intent.putExtra("category_id", Constants.COURSE_CATEGORY_ID_MAP[categoryId]);
                startActivity(intent);
            }
        });
    }

    public void fetchData(int courseCategoryId){
        if(!networkConnect()){
            Toast.makeText(CourseActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }

        asyncFetch = new CourseFetch(courseCategoryId);
        asyncFetch.setMActivity(this);
        asyncFetch.getResult();

    }

    public boolean networkConnect() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    public void onClickActionBarBack(View view){
        onBackPressed();
    }

    @Override
    public void onBackPressed(){
        if (asyncFetch != null)
            asyncFetch.cancel(true);

        super.onBackPressed();
    }

    	

}

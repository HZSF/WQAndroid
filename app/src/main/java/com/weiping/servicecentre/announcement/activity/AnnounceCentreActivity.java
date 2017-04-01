package com.weiping.servicecentre.announcement.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.servicecentre.Constants;
import com.weiping.servicecentre.announcement.AnnouncementFetch;
import com.weiping.servicecentre.announcement.NewsItem;
import com.weiping.servicecentre.announcement.NewsListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class AnnounceCentreActivity extends Activity {

    private ArrayList<NewsItem> itemArrayList;
    private AnnouncementFetch asyncFetch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_announce_centre));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(AnnounceCentreActivity.this);
            }
        });
        setContentView(R.layout.activity_announce_centre);

        itemArrayList = new ArrayList<NewsItem>();
        generateData();
        final NewsListAdapter adapter = new NewsListAdapter(this, itemArrayList);
        final ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ArrayList<NewsItem> newsList = ((NewsListAdapter)listView.getAdapter()).getItemArrayList();

                String searchKey = newsList.get(position).getUrl();
                String title = newsList.get(position).getTitle();
                Intent intent = new Intent(getBaseContext(), AnnouncementBodyActivity.class);
                intent.putExtra("search_url", searchKey);
                intent.putExtra("title", title);
                startActivity(intent);
            }
        });
    }

    private ArrayList<NewsItem> generateData(){
        if(!networkConnect()){
            Toast.makeText(AnnounceCentreActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return null;
        }

        ArrayList<NewsItem> items = new ArrayList<NewsItem>();
        asyncFetch = new AnnouncementFetch(AnnounceCentreActivity.this, items);
        asyncFetch.setMActivity(this);

        asyncFetch.getResult();

        return items;
    }

    public boolean networkConnect() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void onResume(){
        super.onResume();

        try{
            // Load in an object
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(new File(getCacheDir(),"")+"cacheAnnounceList.srl")));
            String jsonDataStr = (String) in.readObject();
            JSONObject jsonData = new JSONObject(jsonDataStr);
            in.close();

            JSONArray jsonItems = jsonData.getJSONArray(Constants.JSON_NAME_RESPONSE_OBJECT);
            for(int i=0; i<jsonItems.length(); i++){
                JSONObject row = jsonItems.getJSONObject(i);
                String title = row.getString(Constants.JSON_ANNOUNCE_TITLE);
                String subTitle = row.getString(Constants.JSON_ANNOUNCE_PUBLISH_TIME);
                String url = row.getString(Constants.JSON_ANNOUNCE_URL);
                itemArrayList.add(new NewsItem(title, subTitle, url));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

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

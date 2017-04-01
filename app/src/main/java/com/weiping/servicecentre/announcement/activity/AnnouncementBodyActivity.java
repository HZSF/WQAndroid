package com.weiping.servicecentre.announcement.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.servicecentre.announcement.AnnouncementBodyFetch;

import platform.tyk.weping.com.weipingplatform.R;

public class AnnouncementBodyActivity extends Activity {

    private AnnouncementBodyFetch asyncFetch;
    public String titleHTML = "";

    private String aid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_announcement_body));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        String search_url = intent.getExtras().getString("search_url");
        String title = "";
        if(intent.getExtras().getString("title") != null){
            title = intent.getExtras().getString("title");
        }
        titleHTML = toHTMLTitle(title);
        setContentView(R.layout.activity_announcement_body);

        getAnnouncementData(search_url);
    }

    public void getAnnouncementData(String searchKey){
        if(!networkConnect()){
            Toast.makeText(AnnouncementBodyActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            return;
        }

        asyncFetch = new AnnouncementBodyFetch(AnnouncementBodyActivity.this);
        asyncFetch.setMActivity(this);
        asyncFetch.setAnnounceTitle(titleHTML);
        asyncFetch.getResult(searchKey);

    }

    public String toHTMLTitle(String title){
        String hTitle = "<p><big><b>";
        hTitle += title;
        hTitle += "</b></big></p>";
        return hTitle;
    }

    public void onClickAnnounceCommentPage(View view){
        Intent intent = new Intent(getBaseContext(), AnnounceCommentPageActivity.class);
        intent.putExtra("aid", aid);
        startActivity(intent);
    }

    public boolean networkConnect() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void onResume() {
        super.onResume();
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

    public String getAid() {
        return aid;
    }
    public void setAid(String aid) {
        this.aid = aid;
    }
}

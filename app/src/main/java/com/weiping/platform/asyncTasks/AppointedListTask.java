package com.weiping.platform.asyncTasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.ListView;

import com.weiping.membership.common.Constants;
import com.weiping.platform.adapter.AppointedListAdapter;
import com.weiping.platform.model.AppointedListItem;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class AppointedListTask extends AsyncTask<String, Void, String> {
    private Context context;
    private AbstractHttpClient mClient;
    private HttpPost httpPost;
    private ListView listView;
    private AppointedListAdapter adapter;

    public AppointedListTask(Context context,ListView listView, AppointedListAdapter adapter) {
        super();
        this.context = context;
        this.listView = listView;
        this.adapter = adapter;
        mClient = new DefaultHttpClient();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(com.weiping.servicecentre.Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
            String AUTH_SESSION_TOKEN = sharedPreferences.getString(com.weiping.servicecentre.Constants.HEAD_AUTH_TOKEN, "");
            httpPost = new HttpPost(params[0]);
            httpPost.setHeader(Constants.HEAD_AUTH_TOKEN, AUTH_SESSION_TOKEN);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse response = mClient.execute(httpPost);
            return EntityUtils.toString(response.getEntity());
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(String result) {
        try {
            JSONObject jObject = new JSONObject(result.trim());
            if (jObject != null) {
                JSONArray jsonItems = jObject.getJSONArray(Constants.JSON_NAME_RESPONSE_OBJECT);
                ArrayList<AppointedListItem> items = new ArrayList<>();
                if (adapter != null) {
                    items = adapter.getItemArrayList();
                    if (items == null) {
                        items = new ArrayList<>();
                    }
                }
                for (int i = 0; i < jsonItems.length(); i++) {
                    JSONObject row = jsonItems.getJSONObject(i);
                    String title = row.getString(com.weiping.servicecentre.Constants.JSON_TITLE);
                    items.add(new AppointedListItem(R.mipmap.cs_btn_icon_management, title, null, "课程预约"));
                }
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //AppointedListHealthTask task = new AppointedListHealthTask(context, adapter);
        //task.execute(Constants.URL_CS_HEALTHY_APPOINTED);
    }
}
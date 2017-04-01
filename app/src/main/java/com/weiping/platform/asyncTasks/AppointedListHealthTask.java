package com.weiping.platform.asyncTasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.weiping.common.StringUtility;
import com.weiping.membership.common.Constants;
import com.weiping.membership.quality.model.AppointedInspectionListItem;
import com.weiping.platform.adapter.AppointedListAdapter;
import com.weiping.platform.model.AppointedListItem;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class AppointedListHealthTask extends AsyncTask<String, Void, String> {
    private Context context;
    private AppointedListAdapter adapter;
    private AbstractHttpClient mClient;
    private HttpPost httpPost;
    public AppointedListHealthTask(Context context, AppointedListAdapter adapter){
        this.context = context;
        this.adapter = adapter;
        mClient = new DefaultHttpClient();
    }
    @Override
    protected String doInBackground(String... params) {
        try{
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
            String AUTH_SESSION_TOKEN = sharedPreferences.getString(Constants.HEAD_AUTH_TOKEN, "");

            httpPost = new HttpPost(params[0]);
            httpPost.setHeader(Constants.HEAD_AUTH_TOKEN, AUTH_SESSION_TOKEN);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse response = mClient.execute(httpPost);

            return EntityUtils.toString(response.getEntity());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    protected void onPostExecute(String result){
        if(!StringUtility.isEmptyString(result)){
            if(Constants.YES.equalsIgnoreCase(result)){
                ArrayList<AppointedListItem> items = new ArrayList<>();
                if (adapter != null) {
                    items = adapter.getItemArrayList();
                    if (items == null) {
                        items = new ArrayList<>();
                    }
                }
                items.add(new AppointedListItem(R.mipmap.cs_btn_icon_health, "九八医院全体体检", null, "体检预约"));
                adapter.notifyDataSetChanged();
            }
        }
        AppointListInspectionTask task = new AppointListInspectionTask(context, adapter);
        task.execute();
    }
}

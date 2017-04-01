package com.weiping.platform.asyncTasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.ListView;

import com.weiping.InteProperty.trademark.adapters.TrademarkSimilarResultListAdapter;
import com.weiping.InteProperty.trademark.base.TrademarkSimilarResultItem;
import com.weiping.InteProperty.trademark.core.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class TrademarkMonitorListTask extends AsyncTask<String, Void, String> {
    private Context context;
    private AbstractHttpClient mClient;
    private HttpGet httpGet;
    private ListView listView;
    private TrademarkSimilarResultListAdapter adapter;

    public TrademarkMonitorListTask(Context context, ListView listView, TrademarkSimilarResultListAdapter adapter) {
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
            httpGet = new HttpGet(params[0]);
            httpGet.setHeader(Constants.HEAD_AUTH_TOKEN, AUTH_SESSION_TOKEN);
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");

            HttpResponse response = mClient.execute(httpGet);
            return EntityUtils.toString(response.getEntity());
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonItems = jsonObject.getJSONArray(Constants.JSON_NAME_RESPONSE_OBJECT);
            ArrayList<TrademarkSimilarResultItem> items = new ArrayList();
            if (adapter != null) {
                items = adapter.getItemArrayList();
                if (items == null) {
                    items = new ArrayList<>();
                }
            }
            for(int i=1; i<jsonItems.length(); i++){
                JSONObject row = jsonItems.getJSONObject(i);
                String regNum = row.getString(Constants.JSON_NAME_REG_NUM);
                if(regNum == "null"){
                    continue;
                }
                String categoryNum = row.getString(Constants.JSON_NAME_CATEGORY_NUM);
                String name = row.getString(Constants.JSON_NAME_NAME);
                if(regNum != null) {
                    items.add(new TrademarkSimilarResultItem(regNum, categoryNum, name, null));
                }
            }
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

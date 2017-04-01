package com.weiping.servicecentre.property.favorites.sell.asyncTasks;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.weiping.common.StringUtility;
import com.weiping.servicecentre.Constants;
import com.weiping.servicecentre.property.buy.model.PropertySellingListItem;
import com.weiping.servicecentre.property.favorites.sell.adapter.PropertyFavoriteSellListAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class PropertyFavoriteSellFetchMoreTask extends AsyncTask<String, Void, String> {
    private Activity mActivity;
    private ListView listView;
    private View moreView;
    private AbstractHttpClient mClient;
    private PropertyFavoriteSellListAdapter adapter;
    private int startId;
    private int numbers = 20;
    private int responseCode;

    public PropertyFavoriteSellFetchMoreTask(Activity mActivity, ListView listView, PropertyFavoriteSellListAdapter adapter, View moreView, int startId) {
        super();
        this.mActivity = mActivity;
        this.listView = listView;
        this.moreView = moreView;
        this.adapter = adapter;
        this.startId = startId;
        mClient = new DefaultHttpClient();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            SharedPreferences sharedPreferences = mActivity.getSharedPreferences(Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
            String AUTH_SESSION_TOKEN = sharedPreferences.getString(Constants.HEAD_AUTH_TOKEN, "");
            HttpPost httpPost = new HttpPost(params[0]);
            httpPost.setHeader(Constants.HEAD_AUTH_TOKEN, AUTH_SESSION_TOKEN);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("startFromId", startId);
            jsonObject.put("numbers", numbers);
            StringEntity se = new StringEntity(jsonObject.toString());
            httpPost.setEntity(se);

            HttpResponse response = mClient.execute(httpPost);
            responseCode = response.getStatusLine().getStatusCode();
            return EntityUtils.toString(response.getEntity());
        }catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        switch (responseCode) {
            case Constants.STATUS_UNAUTHORIZED:
                noMoreItems();
                return;
        }
        try {
            JSONObject jsonData = new JSONObject(result.trim());
            if (jsonData != null) {
                if (jsonData.has("error") && Constants.UNAUTHORIZED.equalsIgnoreCase((String) jsonData.get("error"))) {

                } else {
                    JSONArray jsonItems = jsonData.getJSONArray(Constants.JSON_NAME_RESPONSE_OBJECT);
                    ArrayList<PropertySellingListItem> items = new ArrayList<>();
                    if (adapter != null) {
                        items = adapter.getItemArrayList();
                        if(items == null){
                            items = new ArrayList<>();
                        }
                    }
                    if(jsonItems != null && jsonItems.length() > 0){
                        for(int i=0; i<jsonItems.length(); i++){
                            JSONObject row = jsonItems.getJSONObject(i);
                            int id = 0;
                            if(!StringUtility.isEmptyString(row.getString(Constants.ID))){
                                id = row.getInt(Constants.ID);
                            }
                            String address_area = "";
                            if(!StringUtility.isEmptyString(row.getString(Constants.ADDR_AREA))){
                                address_area = row.getString(Constants.ADDR_AREA);
                            }
                            String category_name = "";
                            if(!StringUtility.isEmptyString(row.getString(Constants.CATE_NAME))){
                                category_name = row.getString(Constants.CATE_NAME);
                            }
                            int area = 0;
                            if(!StringUtility.isEmptyString(row.getString(Constants.AREA))){
                                area = row.getInt(Constants.AREA);
                            }
                            int levels = 0;
                            if(!StringUtility.isEmptyString(row.getString(Constants.LEVELS))){
                                levels = row.getInt(Constants.LEVELS);
                            }
                            int ask_price = 0;
                            if(!StringUtility.isEmptyString(row.getString(Constants.ASK_PRICE))){
                                ask_price = row.getInt(Constants.ASK_PRICE);
                            }
                            String description = "";
                            if(!StringUtility.isEmptyString(row.getString(Constants.DESCRIPTION))){
                                description = row.getString(Constants.DESCRIPTION);
                            }
                            String submit_time = "";
                            if(!StringUtility.isEmptyString(row.getString(Constants.SUBMIT_DATE))){
                                submit_time = row.getString(Constants.SUBMIT_DATE);
                            }

                            items.add(new PropertySellingListItem(id, address_area, category_name, area, levels, ask_price, description, submit_time));
                        }
                        moreView.setVisibility(View.GONE);
                        if (jsonItems.length() < numbers){
                            noMoreItems();
                        }
                        adapter.notifyDataSetChanged();
                    }else {
                        noMoreItems();
                    }
                }
            } else {
                noMoreItems();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            adapter.setDownloading(false);
        }
    }

    private void noMoreItems(){
        listView.removeFooterView(moreView);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }
}

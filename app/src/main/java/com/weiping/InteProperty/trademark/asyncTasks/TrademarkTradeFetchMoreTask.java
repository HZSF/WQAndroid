package com.weiping.InteProperty.trademark.asyncTasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.weiping.InteProperty.trademark.adapters.TrademarkTradeListAdapter;
import com.weiping.InteProperty.trademark.base.TrademarkTradeItem;
import com.weiping.InteProperty.trademark.core.Constants;
import com.weiping.common.StringUtility;

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

public class TrademarkTradeFetchMoreTask extends AsyncTask<String, Void, String> {
    private Activity mActivity;
    private ListView listView;
    private View moreView;
    private AbstractHttpClient mClient;
    private TrademarkTradeListAdapter adapter;
    private String startId;
    private int numbers = 20;

    public TrademarkTradeFetchMoreTask(Activity mActivity, ListView listView, TrademarkTradeListAdapter adapter, View moreView, String startId) {
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
            HttpPost httpPost = new HttpPost(params[0]);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("startFromId", startId);
            jsonObject.put("numbers", numbers);
            StringEntity se = new StringEntity(jsonObject.toString());
            httpPost.setEntity(se);

            HttpResponse response = mClient.execute(httpPost);
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

        try {
            JSONObject jsonData = new JSONObject(result.trim());
            if (jsonData != null) {
                if (jsonData.has("error") && Constants.UNAUTHORIZED.equalsIgnoreCase((String) jsonData.get("error"))) {

                } else {
                    JSONArray jsonItems = jsonData.getJSONArray(Constants.JSON_NAME_RESPONSE_OBJECT);
                    ArrayList<TrademarkTradeItem> items = new ArrayList<>();
                    if (adapter != null) {
                        items = adapter.getItemArrayList();
                        if(items == null){
                            items = new ArrayList<>();
                        }
                    }
                    if(jsonItems != null && jsonItems.length() > 0){
                        for(int i=0; i<jsonItems.length(); i++){
                            JSONObject row = jsonItems.getJSONObject(i);
                            String id = "";
                            if(!StringUtility.isEmptyString(row.getString(Constants.ID))){
                                id = row.getString(Constants.ID);
                            }
                            String reg_num = "";
                            if(!StringUtility.isEmptyString(row.getString(Constants.JSON_NAME_REG_NUM))){
                                reg_num = row.getString(Constants.JSON_NAME_REG_NUM);
                            }
                            String category_num = "";
                            if(!StringUtility.isEmptyString(row.getString(Constants.JSON_NAME_CATEGORY_NUM))){
                                category_num = row.getString(Constants.JSON_NAME_CATEGORY_NUM);
                            }
                            String name = "";
                            if(!StringUtility.isEmptyString(row.getString(Constants.JSON_NAME_NAME))){
                                name = row.getString(Constants.JSON_NAME_NAME);
                            }
                            String apply_date = "申请日: ";
                            if(!StringUtility.isEmptyString(row.getString(Constants.TRADEMARK_APPLY_DATE))){
                                apply_date += row.getString(Constants.TRADEMARK_APPLY_DATE);
                            }
                            String price = "";
                            if(!StringUtility.isEmptyString(row.getString(Constants.PRICE))){
                                price = row.getString(Constants.PRICE);
                            }

                            items.add(new TrademarkTradeItem(id, reg_num, category_num, name, apply_date, price));
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

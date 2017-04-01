package com.weiping.InteProperty.trademark.asyncTasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.weiping.InteProperty.trademark.activity.TradeMarkSimilarResultListActivity;
import com.weiping.InteProperty.trademark.adapters.TrademarkSimilarResultListAdapter;
import com.weiping.InteProperty.trademark.base.TrademarkSimilarResultItem;
import com.weiping.InteProperty.trademark.core.Constants;
import com.weiping.InteProperty.trademark.core.TradeMarkSearcherJSON;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TradeMarkSimilarMoreTask extends AsyncTask<String, Void, JSONObject> {
    private Activity mActivity;
    private AbstractHttpClient mClient;
    private ListView listView;
    private TrademarkSimilarResultListAdapter adapter;
    private View moreView;
    private HashMap<String, String> pageInfoMap;
    private TradeMarkSearcherJSON basicInfo;

    public TradeMarkSimilarMoreTask(Activity a, ListView listView, TrademarkSimilarResultListAdapter adapter, View moreView, HashMap<String, String> map, TradeMarkSearcherJSON basicInfo) {
        mActivity = a;
        this.listView = listView;
        this.adapter = adapter;
        this.moreView = moreView;
        pageInfoMap = map;
        this.basicInfo = basicInfo;
        mClient = new DefaultHttpClient();
    }

    @Override
    protected void onPreExecute(){
        basicInfo.setSearchType(Constants.JSON_NAME_SIMILAR_MORE);
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            HttpPost httpPost = new HttpPost(params[0]);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            JSONObject jsonObject = basicInfo.toJson();
            jsonObject.put(Constants.JSON_NAME_PAGE_NUM, Integer.valueOf(pageInfoMap.get(Constants.JSON_NAME_PAGE_NUM))+1);
            jsonObject.put(Constants.JSON_NAME_PAGE_SIZE, pageInfoMap.get(Constants.JSON_NAME_PAGE_SIZE));
            jsonObject.put(Constants.JSON_NAME_SUM, pageInfoMap.get(Constants.JSON_NAME_SUM));
            jsonObject.put(Constants.JSON_NAME_COUNT_PAGE, pageInfoMap.get(Constants.JSON_NAME_COUNT_PAGE));
            jsonObject.put(Constants.JSON_NAME_OFFICIAL_COOKIES, pageInfoMap.get(Constants.JSON_NAME_OFFICIAL_COOKIES));
            StringEntity se = new StringEntity(jsonObject.toString());
            httpPost.setEntity(se);

            HttpResponse response = mClient.execute(httpPost);
            return new JSONObject(EntityUtils.toString(response.getEntity(), HTTP.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonData) {
        if (jsonData == null) {
            noMoreItems();
            return;
        }

        try {
            JSONArray jsonItems = jsonData.getJSONArray(Constants.JSON_NAME_RESPONSE_OBJECT);
            ArrayList<TrademarkSimilarResultItem> items = new ArrayList();
            if (adapter != null) {
                items = adapter.getItemArrayList();
                if (items == null) {
                    items = new ArrayList<>();
                }
            }
            if (jsonItems.length() > 0) {
                JSONObject pageInfo = jsonItems.getJSONObject(0);
                pageInfoMap.put(Constants.JSON_NAME_PAGE_NUM, pageInfo.getString(Constants.JSON_NAME_PAGE_NUM));
                pageInfoMap.put(Constants.JSON_NAME_PAGE_SIZE, pageInfo.getString(Constants.JSON_NAME_PAGE_SIZE));
                pageInfoMap.put(Constants.JSON_NAME_SUM, pageInfo.getString(Constants.JSON_NAME_SUM));
                pageInfoMap.put(Constants.JSON_NAME_COUNT_PAGE, pageInfo.getString(Constants.JSON_NAME_COUNT_PAGE));
                pageInfoMap.put(Constants.JSON_NAME_OFFICIAL_COOKIES, pageInfo.getString(Constants.JSON_NAME_OFFICIAL_COOKIES));
                pageInfoMap.put(Constants.JSON_NAME_HAS_NEXT_PAGE, pageInfo.getString(Constants.JSON_NAME_HAS_NEXT_PAGE));
                ((TradeMarkSimilarResultListActivity)mActivity).setPageInfoMap(pageInfoMap);
            } else {
                noMoreItems();
                return;
            }
            for(int i=1; i<jsonItems.length(); i++){
                JSONObject row = jsonItems.getJSONObject(i);
                String regNum = row.getString(Constants.JSON_NAME_REG_NUM);
                if(regNum == "null"){
                    continue;
                }
                String categoryNum = row.getString(Constants.JSON_NAME_CATEGORY_NUM);
                String name = row.getString(Constants.JSON_NAME_NAME);
                String detailLink = row.getString(Constants.JSON_NAME_DETAIL_LINK);
                if(regNum != null) {
                    items.add(new TrademarkSimilarResultItem(regNum, categoryNum, name, detailLink));
                }
            }
            moreView.setVisibility(View.GONE);
            if(!Constants.YES.equalsIgnoreCase(pageInfoMap.get(Constants.JSON_NAME_HAS_NEXT_PAGE))){
                noMoreItems();
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
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

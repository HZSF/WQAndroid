package com.weiping.InteProperty.trademark.asyncTasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

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

import platform.tyk.weping.com.weipingplatform.R;

public class TrademarkTradeFetchTask extends AsyncTask<String, Void, String> {
    private Activity mActivity;
    private ListView listView;
    private TrademarkTradeListAdapter adapter;
    private AbstractHttpClient mClient;
    private HttpPost httpPost;

    public TrademarkTradeFetchTask(Activity mActivity, ListView listView, TrademarkTradeListAdapter adapter) {
        super();
        this.mActivity = mActivity;
        this.listView = listView;
        this.adapter = adapter;
        mClient = new DefaultHttpClient();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            httpPost = new HttpPost(params[0]);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("startFromId", 0);
            jsonObject.put("numbers", 20);
            StringEntity se = new StringEntity(jsonObject.toString());
            httpPost.setEntity(se);

            HttpResponse response = mClient.execute(httpPost);
            return EntityUtils.toString(response.getEntity());
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (StringUtility.isEmptyString(result)) {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_data_updating), Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            JSONObject jsonData = new JSONObject(result.trim());
            if (jsonData != null) {
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
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

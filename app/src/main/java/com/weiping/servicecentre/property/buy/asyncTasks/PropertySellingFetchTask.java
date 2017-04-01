package com.weiping.servicecentre.property.buy.asyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.weiping.common.StringUtility;
import com.weiping.servicecentre.Constants;
import com.weiping.servicecentre.property.buy.adapter.PropertySellingListAdapter;
import com.weiping.servicecentre.property.buy.model.PropertySellingListItem;

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

public class PropertySellingFetchTask extends AsyncTask<String, Void, String> {
    private Activity mActivity;
    private View moreView;
    private ListView listView;
    protected ProgressDialog progressDialog;
    private AbstractHttpClient mClient;
    private PropertySellingListAdapter adapter;
    private HttpPost httpPost;
    private int numbers = 20;

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            if (httpPost != null)
                httpPost.abort();
        }
    };

    public PropertySellingFetchTask(Activity activity, ListView listView, PropertySellingListAdapter adapter, View moreView){
        super();
        mActivity = activity;
        this.listView = listView;
        this.moreView = moreView;
        this.adapter = adapter;
        mClient = new DefaultHttpClient();
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        progressDialog = ProgressDialog.show(mActivity, null, mActivity.getResources().getString(R.string.dialog_loading));
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(progressDialogCancelListener);
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            httpPost = new HttpPost(params[0]);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("startId", 0);
            jsonObject.put("numbers", numbers);
            StringEntity se = new StringEntity(jsonObject.toString());
            httpPost.setEntity(se);

            HttpResponse response = mClient.execute(httpPost);
            return EntityUtils.toString(response.getEntity());
        }catch (IOException e){
            progressDialog.dismiss();
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if(StringUtility.isEmptyString(result)){
            progressDialog.dismiss();
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_data_updating), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject jsonData = new JSONObject(result.trim());
            if (jsonData != null) {
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
                        String submit_date = "";
                        if(!StringUtility.isEmptyString(row.getString(Constants.SUBMIT_DATE))){
                            submit_date = row.getString(Constants.SUBMIT_DATE);
                        }

                        items.add(new PropertySellingListItem(id, address_area, category_name, area, levels, ask_price, description, submit_date));
                    }
                    listView.setAdapter(adapter);
                    moreView.setVisibility(View.GONE);
                    if (jsonItems.length() < numbers){
                        noMoreItems();
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            progressDialog.dismiss();
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

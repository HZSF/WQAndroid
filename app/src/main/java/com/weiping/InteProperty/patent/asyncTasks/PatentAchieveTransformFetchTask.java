package com.weiping.InteProperty.patent.asyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.weiping.InteProperty.patent.adapter.PatentAchieveTransformListAdapter;
import com.weiping.InteProperty.patent.base.Constants;
import com.weiping.InteProperty.patent.model.PatentAchieveTransformListItem;
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

public class PatentAchieveTransformFetchTask extends AsyncTask<String, Void, String> {
    private Activity mActivity;
    private ListView listView;
    protected ProgressDialog progressDialog;
    private AbstractHttpClient mClient;
    private PatentAchieveTransformListAdapter adapter;
    private HttpPost httpPost;

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            httpPost.abort();
        }
    };

    public PatentAchieveTransformFetchTask(Activity activity, ListView listView, PatentAchieveTransformListAdapter adapter){
        super();
        mActivity = activity;
        this.listView = listView;
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
            jsonObject.put("startFromId", 0);
            jsonObject.put("numbers", 20);
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
                ArrayList<PatentAchieveTransformListItem> items = new ArrayList<>();
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
                        String patent_id = "";
                        if(!StringUtility.isEmptyString(row.getString(Constants.PATENT_ID))){
                            patent_id = row.getString(Constants.PATENT_ID);
                        }
                        String patent_title = "";
                        if(!StringUtility.isEmptyString(row.getString(Constants.PATENT_TITLE))){
                            patent_title = row.getString(Constants.PATENT_TITLE);
                        }
                        String patent_apply_date = "申请日: ";
                        if(!StringUtility.isEmptyString(row.getString(Constants.PATENT_APPLY_DATE))){
                            patent_apply_date += row.getString(Constants.PATENT_APPLY_DATE);
                        }
                        String patent_price = "";
                        if(!StringUtility.isEmptyString(row.getString(Constants.PATENT_PRICE))){
                            patent_price = row.getString(Constants.PATENT_PRICE);
                        }

                        items.add(new PatentAchieveTransformListItem(id, patent_id, patent_title, patent_apply_date, patent_price));
                    }
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            progressDialog.dismiss();
        }
    }
}

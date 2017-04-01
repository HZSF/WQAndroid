package com.weiping.InteProperty.trademark.asyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.weiping.InteProperty.trademark.activity.TradeMarkSimilarResultListActivity;
import com.weiping.InteProperty.trademark.base.TrademarkSimilarResultItem;
import com.weiping.InteProperty.trademark.core.Constants;
import com.weiping.InteProperty.trademark.core.TradeMarkSearcherJSON;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import platform.tyk.weping.com.weipingplatform.R;

public class TradeMarkSimilarSearcher extends AsyncTask<String, Void, JSONObject> {

    private Activity mActivity;
    private TradeMarkSearcherJSON upLinkJson;
    protected ProgressDialog progressDialog;
    private HttpPost httpPost;

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            httpPost.abort();
        }
    };

    public TradeMarkSimilarSearcher(Activity c){
        super();
        mActivity = c;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        progressDialog = ProgressDialog.show(mActivity, null, mActivity.getResources().getString(R.string.dialog_loading));
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(progressDialogCancelListener);
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            httpPost = new HttpPost(params[0]);
            StringEntity se = new StringEntity(upLinkJson.toJson().toString());
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse response = httpclient.execute(httpPost);

            return new JSONObject(EntityUtils.toString(response.getEntity(), HTTP.UTF_8));
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public void getTrademarkResult(TradeMarkSearcherJSON searchKeys) throws IOException {

        upLinkJson = searchKeys;
        execute(Constants.URL_TRADEMARK);
    }

    @Override
    protected void onPostExecute(JSONObject jsonData) {
        if(jsonData == null){
            progressDialog.dismiss();
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_data_updating), Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<TrademarkSimilarResultItem> items = new ArrayList();
        try{
            JSONArray jsonItems = jsonData.getJSONArray(Constants.JSON_NAME_RESPONSE_OBJECT);
            HashMap<String, String> pageInfoMap = new HashMap<>();
            if(jsonItems.length() > 0) {
                JSONObject pageInfo = jsonItems.getJSONObject(0);
                pageInfoMap.put(Constants.JSON_NAME_PAGE_NUM, pageInfo.getString(Constants.JSON_NAME_PAGE_NUM));
                pageInfoMap.put(Constants.JSON_NAME_PAGE_SIZE, pageInfo.getString(Constants.JSON_NAME_PAGE_SIZE));
                pageInfoMap.put(Constants.JSON_NAME_SUM, pageInfo.getString(Constants.JSON_NAME_SUM));
                pageInfoMap.put(Constants.JSON_NAME_COUNT_PAGE, pageInfo.getString(Constants.JSON_NAME_COUNT_PAGE));
                pageInfoMap.put(Constants.JSON_NAME_OFFICIAL_COOKIES, pageInfo.getString(Constants.JSON_NAME_OFFICIAL_COOKIES));
                pageInfoMap.put(Constants.JSON_NAME_HAS_NEXT_PAGE, pageInfo.getString(Constants.JSON_NAME_HAS_NEXT_PAGE));

            }else{
                progressDialog.dismiss();
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
            if(items.size() > 0) {
                Intent intent_result_page = new Intent(mActivity.getBaseContext(), TradeMarkSimilarResultListActivity.class);
                intent_result_page.putExtra("result_list", items);
                intent_result_page.putExtra("page_info_map", pageInfoMap);
                intent_result_page.putExtra("basic_info_json", upLinkJson);
                mActivity.startActivity(intent_result_page);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }finally {
            progressDialog.dismiss();
        }
    }
}

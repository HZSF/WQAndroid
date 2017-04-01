package com.weiping.credential.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.weiping.credential.Common.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import platform.tyk.weping.com.weipingplatform.R;

public class GetProvinceTask extends AsyncTask<String, Void, String> {
    private Activity mActivity;
    private Spinner spinner;
    private AbstractHttpClient mClient;
    private HttpPost httpPost;

    public GetProvinceTask(Activity a, Spinner spinner){
        super();
        mActivity = a;
        this.spinner = spinner;
        mClient = new DefaultHttpClient();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            httpPost = new HttpPost(params[0]);
            HttpResponse response = mClient.execute(httpPost);
            return EntityUtils.toString(response.getEntity());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            JSONObject jsonData = new JSONObject(result.trim());
            JSONArray jsonItems = jsonData.getJSONArray(Constants.JSON_NAME_RESPONSE_OBJECT);
            List<String> array = new ArrayList<>();
            array.add("请选择省份");
            if(jsonItems != null && jsonItems.length() > 0){
                for(int i=0; i<jsonItems.length(); i++){
                    array.add(i+1, jsonItems.getString(i));
                }
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mActivity.getApplicationContext(), R.layout.spinner_item1, array);
            spinner.setAdapter(arrayAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

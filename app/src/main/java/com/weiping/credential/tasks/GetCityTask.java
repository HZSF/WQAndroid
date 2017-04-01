package com.weiping.credential.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.weiping.credential.Common.Constants;
import com.weiping.credential.activity.UserInfoUpdAreaActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import platform.tyk.weping.com.weipingplatform.R;

public class GetCityTask extends AsyncTask<String, Void, String> {
    private Activity mActivity;
    private Spinner spinner;
    private int province_id;
    private AbstractHttpClient mClient;
    private HttpPost httpPost;

    public GetCityTask(Activity a, Spinner spinner, int province_id){
        super();
        mActivity = a;
        this.spinner = spinner;
        this.province_id = province_id;
        mClient = new DefaultHttpClient();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            httpPost = new HttpPost(params[0]);
            StringEntity se = new StringEntity(String.valueOf(province_id));
            httpPost.setEntity(se);
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
            array.add("请选择城市");
            int[] city_index = null;
            if(jsonItems != null && jsonItems.length() > 0){
                city_index = new int[jsonItems.length()];
                for(int i=0; i<jsonItems.length(); i++){
                    JSONObject jsonObject = jsonItems.getJSONObject(i);
                    city_index[i] = jsonObject.getInt("id");
                    array.add(i+1, jsonObject.getString("cityName"));
                }
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mActivity.getApplicationContext(), R.layout.spinner_item1, array);
            ((UserInfoUpdAreaActivity)mActivity).setCity_index(city_index);
            spinner.setAdapter(arrayAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

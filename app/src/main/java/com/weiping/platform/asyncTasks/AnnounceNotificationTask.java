package com.weiping.platform.asyncTasks;

import android.os.AsyncTask;
import android.widget.TextView;

import com.weiping.servicecentre.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AnnounceNotificationTask extends AsyncTask<String, Void, JSONObject> {
    private TextView scrollText;
    private HttpPost httpPost;

    public AnnounceNotificationTask(TextView txt){
        scrollText = txt;
    }
    @Override
    protected JSONObject doInBackground(String... url) {
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            httpPost = new HttpPost(url[0]);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.INDEX_START, String.valueOf(1));
            jsonObject.put(Constants.INDEX_END, String.valueOf(10));
            StringEntity se = new StringEntity(jsonObject.toString());
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            JSONObject jObject = new JSONObject(result.trim());
            return jObject;
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonData) {
        if(jsonData == null){
            return;
        }
        try{
            JSONArray jsonItems = jsonData.getJSONArray(Constants.JSON_NAME_RESPONSE_OBJECT);
            String titles = "";
            for(int i=0; i<jsonItems.length(); i++){
                JSONObject row = jsonItems.getJSONObject(i);
                titles += row.getString(Constants.JSON_ANNOUNCE_TITLE);
                titles += ";    ";
            }
            scrollText.setText(titles);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

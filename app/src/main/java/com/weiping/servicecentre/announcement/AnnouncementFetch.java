package com.weiping.servicecentre.announcement;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.ListView;

import com.weiping.servicecentre.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class AnnouncementFetch extends AsyncTask<String, Void, JSONObject> {
    private Context mContext;
    private Activity mActivity;
    private HttpPost httpPost;
    protected ProgressDialog progressDialog;
    private ArrayList<NewsItem> items;

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            httpPost.abort();
            mActivity.onBackPressed();
        }
    };

    public AnnouncementFetch(Context context, ArrayList<NewsItem> itemList){
        super();
        mContext = context;
        items = itemList;
    }

    public void getResult(){
        execute(Constants.URL_ANNOUNCE);
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        progressDialog = ProgressDialog.show(mContext, null, mContext.getResources().getString(R.string.dialog_loading));
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(progressDialogCancelListener);
    }

    @Override
    protected JSONObject doInBackground(String... url) {
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            httpPost = new HttpPost(url[0]);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.INDEX_START, String.valueOf(1));
            jsonObject.put(Constants.INDEX_END, String.valueOf(20));
            StringEntity se = new StringEntity(jsonObject.toString());

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse response = httpclient.execute(httpPost);
            InputStream inputStream = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            String line = "";
            String result = "";
            while ((line = reader.readLine()) != null)
                result += line;
            inputStream.close();

            JSONObject jObject = new JSONObject(result.trim());
            return jObject;

            //return new JSONObject(EntityUtils.toString(response.getEntity(), HTTP.UTF_8));
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
            progressDialog.dismiss();
            return;
        }

        // Save the JSONObject
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(new File(mContext.getCacheDir(), "") + "cacheAnnounceList.srl"));
            out.writeObject(jsonData.toString());
            out.close();
        }catch (Exception e){
            e.printStackTrace();
            if(out != null){
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }finally {
            progressDialog.dismiss();
        }

        try{
            JSONArray jsonItems = jsonData.getJSONArray(Constants.JSON_NAME_RESPONSE_OBJECT);
            for(int i=0; i<jsonItems.length(); i++){
                JSONObject row = jsonItems.getJSONObject(i);
                String title = row.getString(Constants.JSON_ANNOUNCE_TITLE);
                String subTitle = row.getString(Constants.JSON_ANNOUNCE_PUBLISH_TIME);
                String url = row.getString(Constants.JSON_ANNOUNCE_URL);
                items.add(new NewsItem(title, subTitle, url));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }finally {
            //progressDialog.dismiss();
        }

        NewsListAdapter adapter = new NewsListAdapter(mContext, items);


        ListView v = (ListView)mActivity.findViewById(R.id.list_view);
        v.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
    public void setMActivity(Activity a){
        mActivity = a;
    }
}

package com.weiping.servicecentre.announcement;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

import com.weiping.servicecentre.Constants;
import com.weiping.servicecentre.announcement.activity.AnnouncementBodyActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import platform.tyk.weping.com.weipingplatform.R;

public class AnnouncementBodyFetch extends AsyncTask<String, Void, JSONObject> {

    private Context mContext;
    private Activity mActivity;
    private HttpPost httpPost;
    protected ProgressDialog progressDialog;
    private String keyword_url = "";
    public String announceTitle = "";

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            if(httpPost != null)
                httpPost.abort();
            mActivity.onBackPressed();
        }
    };

    public AnnouncementBodyFetch(Context c){
        super();
        mContext = c;
    }

    public void getResult(String keyword){
        keyword_url = keyword;
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

        try{
            DefaultHttpClient httpclient = new DefaultHttpClient();
            httpPost = new HttpPost(url[0]);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.ANNOUNCE_URL, keyword_url);
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
        if(jsonData == null) {
            progressDialog.dismiss();
            return;
        }

        String announceArticle = announceTitle;
        try{
            JSONArray jsonItems = jsonData.getJSONArray(Constants.JSON_NAME_RESPONSE_OBJECT);
            for(int i=0; i<jsonItems.length(); i++){
                JSONObject row = jsonItems.getJSONObject(i);
                String body = row.getString(Constants.JSON_ANNOUNCE_BODY);
                announceArticle += body;
                String aid = row.getString(Constants.JSON_ANNOUNCE_AID);
                ((AnnouncementBodyActivity)mActivity).setAid(aid);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }finally {
            progressDialog.dismiss();
        }

        TextView textView = (TextView)mActivity.findViewById(R.id.txt_announcement_context);
        textView.setText(Html.fromHtml(announceArticle));
    }

    public void setMActivity(Activity a){
        mActivity = a;
    }

    public void setAnnounceTitle(String str){
        announceTitle = str;
    }
}

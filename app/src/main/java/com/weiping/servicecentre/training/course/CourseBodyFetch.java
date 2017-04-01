package com.weiping.servicecentre.training.course;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

import com.weiping.servicecentre.Constants;

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

public class CourseBodyFetch extends AsyncTask<String, Void, JSONObject> {

    private Activity mActivity;
    private HttpPost httpPost;
    protected ProgressDialog progressDialog;
    private String keyword_course_id = "";

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            httpPost.abort();
            mActivity.onBackPressed();
        }
    };

    public void getResult(String keyword){
        keyword_course_id = keyword;
        execute(Constants.URL_CS_TRAIN_COURSES);
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        progressDialog = ProgressDialog.show(mActivity, null, mActivity.getResources().getString(R.string.dialog_loading));
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(progressDialogCancelListener);
    }

    @Override
    protected JSONObject doInBackground(String... urls) {
        try{
            DefaultHttpClient httpclient = new DefaultHttpClient();
            httpPost = new HttpPost(urls[0]);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.COURSE_ID, keyword_course_id);
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

        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonData) {
        if (jsonData == null) {
            progressDialog.dismiss();
            return;
        }

        String courseBody = "";
        try{
            JSONArray jsonItems = jsonData.getJSONArray(Constants.JSON_NAME_RESPONSE_OBJECT);
            for(int i=0; i<jsonItems.length(); i++){
                JSONObject row = jsonItems.getJSONObject(i);
                String body = row.getString(Constants.JSON_CS_TRAIN_COURSE_BODY);
                courseBody += body;
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }finally {
            progressDialog.dismiss();
        }

        TextView textView = (TextView)mActivity.findViewById(R.id.txt_course_context);
        textView.setText(Html.fromHtml(courseBody));
    }

    public void setMActivity(Activity a){
        mActivity = a;
    }
}

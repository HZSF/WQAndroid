package com.weiping.InteProperty.trademark.asyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.weiping.InteProperty.trademark.activity.TradeMarkNamingResultActivity;
import com.weiping.InteProperty.trademark.core.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

import platform.tyk.weping.com.weipingplatform.R;

public class TrademarkNamingTask extends AsyncTask<String, Void, String> {
    private Activity mActivity;
    protected ProgressDialog progressDialog;
    private HttpPost httpPost;

    private String SearchArea = "";
    private String areaNum = "";
    private String SearchWord = "";
    private String AllSerStr = "";
    private String AllSerStrValue = "";
    private String AllSerNum = "1";
    private String AllSerNumLast = "1";

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            httpPost.abort();
            mActivity.onBackPressed();
        }
    };

    public TrademarkNamingTask(Activity c){
        super();
        mActivity = c;
    }

    public void startTask(){
        execute(Constants.URLTRADEMARKNAMING);
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
        try{
            AllSerStr = SearchArea + "||||";
            AllSerStrValue = areaNum+"||||";
            AbstractHttpClient httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(params[0]);
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("area", SearchArea));
            nameValuePairs.add(new BasicNameValuePair("areaNum", areaNum));
            nameValuePairs.add(new BasicNameValuePair("w", SearchWord));
            nameValuePairs.add(new BasicNameValuePair("AllSerStr", AllSerStr));
            nameValuePairs.add(new BasicNameValuePair("AllSerStrValue", AllSerStrValue));
            nameValuePairs.add(new BasicNameValuePair("AllSerNum", AllSerNum));
            nameValuePairs.add(new BasicNameValuePair("AllSerNumLast", AllSerNumLast));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "GB2312"));
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            HttpResponse response = httpClient.execute(httpPost);
            return EntityUtils.toString(response.getEntity(), "GB2312");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    protected void onPostExecute(String result) {
        String resultListString = "";
        int count = 1;
        try {
            if(result != null){
                result = result.substring(result.indexOf("DataShow"));
                result = result.substring(result.indexOf("<form"), result.indexOf("</form>"));
                while (result.contains("<tr>")){
                    result = result.substring(result.indexOf("<tr>"));
                    if(result.contains("\"DS2\">")) {
                        result = result.substring(result.indexOf("\"DS2\">"));
                        String name = result.substring(6, result.indexOf("</td>"));
                        resultListString += "<p>";
                        resultListString += count;
                        count++;
                        resultListString += ". " + name;
                        resultListString += "</p>";
                    }else{
                        return;
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        Intent intent_result_page = new Intent(mActivity.getBaseContext(), TradeMarkNamingResultActivity.class);
        intent_result_page.putExtra("search_result", resultListString);
        mActivity.startActivity(intent_result_page);

        progressDialog.dismiss();
    }

    public void setSearchWord(String s){
        SearchWord = s;
    }
    public void setSearchArea(String s){
        SearchArea = s;
    }
    public void setAreaNum(String s){
        areaNum = s;
    }
}

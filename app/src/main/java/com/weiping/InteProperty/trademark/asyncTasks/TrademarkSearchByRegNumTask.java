package com.weiping.InteProperty.trademark.asyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.weiping.InteProperty.trademark.activity.TradeMarkStatusResultListActivity;
import com.weiping.InteProperty.trademark.base.TrademarkStatusResultItem;
import com.weiping.InteProperty.trademark.core.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class TrademarkSearchByRegNumTask extends AsyncTask<String, Void, String> {

    private Activity mActivity;
    protected ProgressDialog progressDialog;
    public String registerNumber;
    private HttpGet httpGet;

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            httpGet.abort();
        }
    };

    public TrademarkSearchByRegNumTask(Activity activity, String regNum){
        mActivity = activity;
        registerNumber = regNum;
    }

    public void doTask(){
        execute(Constants.URL_TRADEMARK_BY_REG_NUM + registerNumber);
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
        String url = params[0];
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            httpGet = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        }catch (Exception e){
            Log.e("Error", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result){
        try {
            result = result.substring(result.indexOf("list_01"), result.length());
            result = result.substring(result.indexOf("<td"), result.indexOf("</tr>"));
            result = result.substring(result.indexOf("<a"));
            result = result.substring(result.indexOf(">"));
            String regNum = result.substring(1, result.indexOf("</a>"));

            result = result.substring(result.indexOf("<a"));
            result = result.substring(result.indexOf(">"));
            String categoryNum = result.substring(1, result.indexOf("</a>"));

            result = result.substring(result.indexOf("<a"));
            result = result.substring(result.indexOf(">"));
            String name = result.substring(1, result.indexOf("</a>"));

            result = result.substring(result.indexOf("<a"));
            result = result.substring(result.indexOf(">"));
            String applicant = result.substring(1, result.indexOf("</a>"));

            ArrayList<TrademarkStatusResultItem> items = new ArrayList();
            items.add(new TrademarkStatusResultItem(regNum, categoryNum, name, applicant));
            Intent intent_result_page = new Intent(mActivity.getBaseContext(), TradeMarkStatusResultListActivity.class);
            intent_result_page.putExtra("result_list", items);
            mActivity.startActivity(intent_result_page);

        } catch (Exception e){
            Log.e("Error onPost", e.getMessage());
        }
        finally {
            progressDialog.dismiss();
        }
    }
}

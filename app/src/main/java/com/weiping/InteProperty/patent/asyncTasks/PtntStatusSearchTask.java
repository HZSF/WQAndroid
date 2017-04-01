package com.weiping.InteProperty.patent.asyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.weiping.InteProperty.patent.activity.PtntResultDetailActivity;
import com.weiping.InteProperty.patent.model.PatentStatusItem;
import com.weiping.common.StringUtility;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class PtntStatusSearchTask extends AsyncTask<String, Void, String> {
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

    public PtntStatusSearchTask(Activity activity, String regNum){
        mActivity = activity;
        registerNumber = regNum;
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
            url += "no=";
            url += registerNumber;
            url += "&type=0";
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
    protected void onPostExecute(String result) {
        if (StringUtility.isEmptyString(result)) {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_data_updating), Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<PatentStatusItem> items = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(result);
            String string = new String("");
            string += "\n\n";
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String applyNumber = jsonObject.getString("applyNumber");
                String patentStatusDate = jsonObject.getString("patentStatusDate");
                String patentStatus = jsonObject.getString("patentStatus");
                items.add(new PatentStatusItem(applyNumber, patentStatusDate, patentStatus));
                string += "专利状态日期: ";
                string += patentStatusDate;
                string += "\n";
                string += "专利状态: ";
                string += patentStatus;
                string += "\n\n";
            }
            Intent intent = new Intent(mActivity.getBaseContext(), PtntResultDetailActivity.class);
            intent.putExtra("title", "专利申请号: " + registerNumber);
            intent.putExtra("detail", string);
            mActivity.startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            progressDialog.dismiss();
        }

    }
}

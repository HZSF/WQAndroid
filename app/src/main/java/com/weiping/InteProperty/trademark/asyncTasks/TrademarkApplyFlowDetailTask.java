package com.weiping.InteProperty.trademark.asyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.weiping.InteProperty.trademark.core.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import platform.tyk.weping.com.weipingplatform.R;

public class TrademarkApplyFlowDetailTask extends AsyncTask<String, Void, String> {
    private Activity mActivity;
    protected ProgressDialog progressDialog;
    public String registerNumber;
    public String categoryNumber;
    private HttpGet httpGet;

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            if(httpGet != null)
                httpGet.abort();
        }
    };

    public TrademarkApplyFlowDetailTask(Activity activity, String regNum, String cateNum){
        mActivity = activity;
        registerNumber = regNum;
        categoryNumber = cateNum;
    }

    public void doTask(){
        execute(Constants.URL_TRADEMARK_APPLY_FLOW_DETAIL + "regNum=" + registerNumber + "&intcls=" + categoryNumber);
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
    protected void onPostExecute(String result) {
        String details = "";
        try {
            result = result.substring(result.indexOf("商标流程"), result.length());
            result = result.substring(result.indexOf("<tr>"), result.indexOf("</table>"));
            while (result.toLowerCase().contains("<tr>")){
                String rowString = "";
                for(int i=0; i<2; i++) {
                    result = result.substring(result.indexOf("<td"));
                    rowString = result.substring(0, result.indexOf("</td>"));
                    rowString = rowString.substring(rowString.indexOf(">") + 1);
                    rowString = rowString.replace("\r", "");
                    rowString = rowString.replace("\n", "");
                    rowString = rowString.replace("\t", "");
                    rowString.trim();
                    details += rowString;
                    if(i == 0) {
                        details += ": ";
                    }else{
                        details += "\n";
                    }
                    result = result.substring(result.indexOf("</td>"));
                }
            }


        } catch (Exception e) {
            Log.e("Error onPost", e.getMessage());
        } finally {
            progressDialog.dismiss();
        }
        ((TextView)mActivity.findViewById(R.id.txt_trademark_status_apply_details)).setText(details);
    }
}

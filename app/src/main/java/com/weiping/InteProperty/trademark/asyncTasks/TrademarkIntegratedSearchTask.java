package com.weiping.InteProperty.trademark.asyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.weiping.InteProperty.trademark.activity.TradeMarkIntegratedPrecisionResultListActivity;
import com.weiping.InteProperty.trademark.activity.TradeMarkIntegratedResultListActivity;
import com.weiping.InteProperty.trademark.base.TrademarkIntegratedResultItem;
import com.weiping.InteProperty.trademark.core.Constants;
import com.weiping.common.StringUtility;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

import platform.tyk.weping.com.weipingplatform.R;

public class TrademarkIntegratedSearchTask extends AsyncTask<String, Void, String> {
    private Activity mActivity;
    protected ProgressDialog progressDialog;
    private String categoryNum;
    private String resultSort;
    private String searchMethod;
    private boolean isByRegNum;
    private boolean isByTrademarkName;
    private boolean isByApplicantCName;
    private boolean isByApplicantEName;
    private String regNum;
    private String trademarkName;
    private String applicantNameC;
    private String applicantNameE;
    private HttpGet httpGet;
    private String searchBy;

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            httpGet.abort();
        }
    };

    public TrademarkIntegratedSearchTask(Activity c, boolean isByRegNum, boolean isByTrademarkName, boolean isByApplicantCName, boolean isByApplicantEName, Map submitData){
        super();
        mActivity = c;
        this.isByRegNum = isByRegNum;
        this.isByTrademarkName = isByTrademarkName;
        this.isByApplicantCName = isByApplicantCName;
        this.isByApplicantEName = isByApplicantEName;
        if (submitData != null) {
            categoryNum = (String)submitData.get(Constants.SUBMIT_DATA_CATEGORYNUM);
            resultSort = (String)submitData.get(Constants.SUBMIT_DATA_RESULT_SORT_NUM);
            regNum = (String)submitData.get(Constants.SUBMIT_DATA_APPLICATION_NUM);
            trademarkName = (String)submitData.get(Constants.SUBMIT_DATA_SEARCHCONTENT);
            applicantNameC = (String)submitData.get(Constants.SUBMIT_DATA_APPLICANT_NAME_C);
            applicantNameE = (String)submitData.get(Constants.SUBMIT_DATA_APPLICANT_NAME_E);
            searchMethod = (String)submitData.get(Constants.SUBMIT_DATA_QUERY_METHOD);
        }
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
        if(Integer.valueOf(categoryNum.trim()) == 0){
            categoryNum = "";
        }
        try {
            if(isByRegNum){
                url += "type=reg&intcls=" + categoryNum + "&regNum=" + regNum + "&paiType=" + resultSort;
            }else if(isByTrademarkName){
                searchBy = "tmName";
                url += "type=tm&intcls=" + categoryNum + "&tmName=" + URLEncoder.encode(URLEncoder.encode(trademarkName, "UTF-8"), "UTF-8") + "&tmFlag=" + searchMethod + "&paiType=" + resultSort;
            }else if(isByApplicantCName){
                searchBy = "appCnName";
                url += "type=cn&intcls=" + categoryNum + "&appCnName=" + URLEncoder.encode(URLEncoder.encode(applicantNameC, "UTF-8"), "UTF-8") + "&cnNameFlag=" + searchMethod + "&paiType=" + resultSort;
            }else if(isByApplicantEName){
                searchBy = "appEnName";
                url += "type=en&intcls=" + categoryNum + "&appEnName=" + URLEncoder.encode(URLEncoder.encode(applicantNameE, "UTF-8"), "UTF-8") + "&enNameFlag=" + searchMethod + "&paiType=" + resultSort;
            }

            DefaultHttpClient httpclient = new DefaultHttpClient();
            httpGet = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result){
        boolean hasResult = false;
        ArrayList<TrademarkIntegratedResultItem> items = new ArrayList();
        try {
            if(isByRegNum){
                while(result.contains("list_01")){
                    result = result.substring(result.indexOf("list_01"), result.length());
                    result = result.substring(result.indexOf("<td"));
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

                    items.add(new TrademarkIntegratedResultItem(regNum, categoryNum, name, applicant));

                    if(result.contains("</tr>")){
                        result = result.substring(result.indexOf("</tr>"));
                    }

                    if(result.contains("list_02")){
                        result = result.substring(result.indexOf("list_02"), result.length());
                        result = result.substring(result.indexOf("<td"));
                        result = result.substring(result.indexOf("<a"));
                        result = result.substring(result.indexOf(">"));
                        String regNum2 = result.substring(1, result.indexOf("</a>"));

                        result = result.substring(result.indexOf("<a"));
                        result = result.substring(result.indexOf(">"));
                        String categoryNum2 = result.substring(1, result.indexOf("</a>"));

                        result = result.substring(result.indexOf("<a"));
                        result = result.substring(result.indexOf(">"));
                        String name2 = result.substring(1, result.indexOf("</a>"));

                        result = result.substring(result.indexOf("<a"));
                        result = result.substring(result.indexOf(">"));
                        String applicant2 = result.substring(1, result.indexOf("</a>"));

                        items.add(new TrademarkIntegratedResultItem(regNum2, categoryNum2, name2, applicant2));
                    }
                }
                if(items != null && items.size() > 0){
                    Intent intent_precision = new Intent(mActivity.getBaseContext(), TradeMarkIntegratedPrecisionResultListActivity.class);
                    intent_precision.putExtra("result_list", items);
                    mActivity.startActivity(intent_precision);
                }else{
                    progressDialog.dismiss();
                    return;
                }
            }else {
                while (result.contains("list_01")) {
                    result = result.substring(result.indexOf("list_01"), result.length());
                    result = result.substring(result.indexOf("<td"));
                    result = result.substring(result.indexOf("<a"));
                    String link = "";
                    if(result.contains("/tmois/wszhcx_getLikeCondition.xhtml?")) {
                        result = result.substring(result.indexOf("/tmois/wszhcx_getLikeCondition.xhtml?"));
                        link = result.substring(0, result.indexOf("')"));
                    }
                    result = result.substring(result.indexOf(">"));
                    String name = result.substring(1, result.indexOf("</a>"));
                    TrademarkIntegratedResultItem item1 = new TrademarkIntegratedResultItem(null, categoryNum, name, null);
                    item1.setLink(link);
                    items.add(item1);
                    hasResult = true;
                    if (result.contains("list_02")) {
                        result = result.substring(result.indexOf("list_02"), result.length());
                        result = result.substring(result.indexOf("<td"));
                        result = result.substring(result.indexOf("<a"));
                        link = "";
                        if(result.contains("/tmois/wszhcx_getLikeCondition.xhtml?")) {
                            result = result.substring(result.indexOf("/tmois/wszhcx_getLikeCondition.xhtml?"));
                            link = result.substring(0, result.indexOf("')"));
                        }
                        result = result.substring(result.indexOf(">"));
                        String name2 = result.substring(1, result.indexOf("</a>"));
                        TrademarkIntegratedResultItem item2 = new TrademarkIntegratedResultItem(null, categoryNum, name2, null);
                        item2.setLink(link);
                        items.add(item2);
                    }
                }
            }
        } catch (Exception e){
            Log.e("Error onPost", e.getMessage());
        }
        finally {
            progressDialog.dismiss();
        }
        if(hasResult) {
            Intent intent_result_page = new Intent(mActivity.getBaseContext(), TradeMarkIntegratedResultListActivity.class);
            intent_result_page.putExtra("result_list", items);
            intent_result_page.putExtra("result_sort_num", resultSort);
            intent_result_page.putExtra("search_by", searchBy);
            mActivity.startActivity(intent_result_page);
        }
    }
}

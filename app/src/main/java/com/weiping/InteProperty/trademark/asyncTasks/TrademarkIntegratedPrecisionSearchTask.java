package com.weiping.InteProperty.trademark.asyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.weiping.InteProperty.trademark.activity.TradeMarkSimilarDetailActivity;
import com.weiping.InteProperty.trademark.adapters.TrademarkIntegratedPrecisionResultListAdapter;
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

import platform.tyk.weping.com.weipingplatform.R;

public class TrademarkIntegratedPrecisionSearchTask extends AsyncTask<String, Void, String> {
    private Activity mActivity;
    protected ProgressDialog progressDialog;
    private String categoryNum;
    private String trademarkName;
    private String resultSort;
    private String searchBy;
    private HttpGet httpGet;
    private String link;

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            httpGet.abort();
        }
    };

    public TrademarkIntegratedPrecisionSearchTask(Activity c, String categoryNum, String trademarkName, String resultSort, String searchBy){
        super();
        mActivity = c;
        this.categoryNum = categoryNum;
        this.trademarkName = trademarkName;
        this.resultSort = resultSort;
        this.searchBy = searchBy;
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
            String url_encode = "";
            if(!StringUtility.isEmptyString(link)){
                if (searchBy.equalsIgnoreCase("appEnName")) {
                    url_encode = URLEncoder.encode(link, "UTF-8");
                    url = "http://sbcx.saic.gov.cn:9080"+url_encode;
                } else {
                    if(searchBy.equalsIgnoreCase("appCnName") && link.contains("appCnName")){
                        String sub_link = link.substring(link.indexOf("appCnName"));
                        String code = sub_link.substring(10, sub_link.indexOf("&"));
                        url_encode = URLEncoder.encode(URLEncoder.encode(code, "UTF-8"), "UTF-8");
                        url += (searchBy + "=" + url_encode + "&intCls=" + categoryNum + "&paiType=" + resultSort);
                    }
                }
            } else {
                if (searchBy.equalsIgnoreCase("appEnName")) {
                    url_encode = URLEncoder.encode(trademarkName, "UTF-8");
                } else {
                    url_encode = URLEncoder.encode(URLEncoder.encode(trademarkName, "UTF-8"), "UTF-8");
                }
                url += (searchBy + "=" + url_encode + "&intCls=" + categoryNum + "&paiType=" + resultSort);
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
        if(result == null){
            mActivity.onBackPressed();
            return;
        }
        try {
            ArrayList<TrademarkIntegratedResultItem> items = new ArrayList();
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
            TrademarkIntegratedPrecisionResultListAdapter adapter = new TrademarkIntegratedPrecisionResultListAdapter(mActivity, items);
            ListView listView = (ListView)mActivity.findViewById(R.id.listView_trademark_integrated_precision_result);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ArrayList<TrademarkIntegratedResultItem> trademarkList = ((TrademarkIntegratedPrecisionResultListAdapter) parent.getAdapter()).getItemArrayList();
                    String categoryNum = trademarkList.get(position).getCategoryNum();
                    String regNum = trademarkList.get(position).getRegNum();
                    Intent intent = new Intent(mActivity.getBaseContext(), TradeMarkSimilarDetailActivity.class);
                    intent.putExtra(Constants.JSON_NAME_CATEGORY_NUM, categoryNum);
                    intent.putExtra(Constants.JSON_NAME_REG_NUM, regNum);
                    intent.putExtra(Constants.JSON_NAME_NAME, trademarkList.get(position).getName());
                    mActivity.startActivity(intent);
                }
            });
            adapter.notifyDataSetChanged();
        } catch (Exception e){
            Log.e("Error onPost", e.getMessage());
        }
        finally {
            progressDialog.dismiss();
        }
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}

package com.weiping.InteProperty.patent.asyncTasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.weiping.InteProperty.patent.activity.PatentIntegratedSearchActivity;
import com.weiping.InteProperty.patent.adapter.PatentISearchAdapter;
import com.weiping.InteProperty.patent.base.Constants;
import com.weiping.InteProperty.patent.model.PatentIntegratedItem;
import com.weiping.common.StringUtility;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class PatentIntegratedSearchTask extends AsyncTask<String, Void, String> {

    private Activity mActivity;
    private AbstractHttpClient mClient;
    private HttpGet httpGet;

    private PatentISearchAdapter adapter;

    private String content;
    private int page = 1;
    private int sort = 0;
    private boolean isInvent;
    private boolean isUtility;
    private boolean isDesign;
    private boolean isGrant;

    public PatentIntegratedSearchTask(Activity a, String content, int page, int sort){
        super();
        mActivity = a;
        this.content = content;
        this.page = page;
        this.sort = sort;
        mClient = new DefaultHttpClient();
    }

    @Override
    protected String doInBackground(String... params) {
        try{
            String url = params[0];
            url += "SearchWord=";
            url += URLEncoder.encode(URLEncoder.encode(content, "UTF-8"), "UTF-8");
            if(isUtility){
                url += "&SYXX=Y";
            }
            if(isDesign){
                url += "&WGZL=Y";
            }
            if(isInvent){
                url += "&FMZL=Y";
            }
            if(isGrant){
                url += "&FMSQ=Y";
            }
            url += "&page="+page;
            url += "&s=";
            url += String.valueOf(sort);

            httpGet = new HttpGet(url);
            HttpResponse response = mClient.execute(httpGet);
            return EntityUtils.toString(response.getEntity());
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if(StringUtility.isEmptyString(result)){
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_data_updating), Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<PatentIntegratedItem> items = new ArrayList<>();
        try {
            if (adapter != null) {
                items = adapter.getItemArrayList();
                if(items == null){
                    items = new ArrayList<>();
                }
            }
            JSONObject jsonData = new JSONObject(result.trim());
            JSONArray jsonArray = (JSONArray)jsonData.get("list");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray patentType_array = (JSONArray) jsonObject.get(Constants.JNAME_PATENT_TYPE);
                ArrayList<String> patentTypeList = new ArrayList<>();
                for (int j = 0; j < patentType_array.length(); j++) {
                    patentTypeList.add(patentType_array.getString(j));
                }
                String title = jsonObject.getString(Constants.JNAME_TITLE);
                String patentId = jsonObject.getString(Constants.JNAME_PATENT_ID);
                JSONArray patentStatus_array = (JSONArray) jsonObject.get(Constants.JNAME_PATENT_STATUS);
                ArrayList<String> patentStatusList = new ArrayList<>();
                for (int j = 0; j < patentStatus_array.length(); j++) {
                    patentStatusList.add(patentStatus_array.getString(j));
                }
                String applicant = jsonObject.getString(Constants.JNAME_APPLICANT);
                String applyDate = jsonObject.getString(Constants.JNAME_APPLY_DATE);
                String categoryCode = jsonObject.getString(Constants.JNAME_CATEGORY_CODE);
                String summary = jsonObject.getString(Constants.JNAME_SUMMARY);

                PatentIntegratedItem item = new PatentIntegratedItem();
                item.setPatentTypeList(patentTypeList);
                item.setTitle(title);
                item.setPatentId(patentId);
                item.setPatentStatusList(patentStatusList);
                item.setApplicant(applicant);
                item.setApplyDate(applyDate);
                item.setCategoryCode(categoryCode);
                item.setSummary(summary);

                items.add(item);
            }
            int totalResult = jsonData.getInt(Constants.JNAME_TOTAL_RESULT);
            adapter.setTotalItems(totalResult);
            adapter.notifyDataSetChanged();

            int page = jsonData.getInt(Constants.JNAME_CURRENT_PAGE);
            int totalPage = jsonData.getInt(Constants.JNAME_TOTAL_PAGE);
            if(page < totalPage){
                ((PatentIntegratedSearchActivity)mActivity).setPage(++page);
            }else{
                ((PatentIntegratedSearchActivity)mActivity).setPage(0);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }finally {
            ((PatentIntegratedSearchActivity)mActivity).getProgressBar().setVisibility(View.GONE);
            ((PatentIntegratedSearchActivity)mActivity).setLoading(false);
        }
    }

    public boolean isInvent() {
        return isInvent;
    }

    public void setIsInvent(boolean isInvent) {
        this.isInvent = isInvent;
    }

    public boolean isUtility() {
        return isUtility;
    }

    public void setIsUtility(boolean isUtility) {
        this.isUtility = isUtility;
    }

    public boolean isDesign() {
        return isDesign;
    }

    public void setIsDesign(boolean isDesign) {
        this.isDesign = isDesign;
    }

    public boolean isGrant() {
        return isGrant;
    }

    public void setIsGrant(boolean isGrant) {
        this.isGrant = isGrant;
    }

    public PatentISearchAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(PatentISearchAdapter adapter) {
        this.adapter = adapter;
    }
}

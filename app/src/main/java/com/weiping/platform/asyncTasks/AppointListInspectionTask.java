package com.weiping.platform.asyncTasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.weiping.common.StringUtility;
import com.weiping.membership.common.Constants;
import com.weiping.platform.adapter.AppointedListAdapter;
import com.weiping.platform.model.AppointedListItem;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class AppointListInspectionTask extends AsyncTask<String, Void, String> {
    private Context context;
    private AppointedListAdapter adapter;
    private AbstractHttpClient mClient;
    private HttpPost httpPost;
    public AppointListInspectionTask(Context context, AppointedListAdapter adapter){
        this.context = context;
        this.adapter = adapter;
        mClient = new DefaultHttpClient();
    }
    @Override
    protected String doInBackground(String... params) {
        try{
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
            String AUTH_SESSION_TOKEN = sharedPreferences.getString(Constants.HEAD_AUTH_TOKEN, "");

            httpPost = new HttpPost(Constants.URL_MEMBER_QUALITY_APPOINTED_INSPECTION);
            httpPost.setHeader(Constants.HEAD_AUTH_TOKEN, AUTH_SESSION_TOKEN);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse response = mClient.execute(httpPost);

            return EntityUtils.toString(response.getEntity());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    protected void onPostExecute(String result){
        try {
            if (!StringUtility.isEmptyString(result)) {
                ArrayList<AppointedListItem> items = new ArrayList<>();
                if (adapter != null) {
                    items = adapter.getItemArrayList();
                    if (items == null) {
                        items = new ArrayList<>();
                    }
                }
                JSONObject jsonObject = new JSONObject(result.trim());
                if (jsonObject.has("isAppliedWood") && Constants.YES.equalsIgnoreCase((String) jsonObject.get("isAppliedWood"))) {
                    items.add(new AppointedListItem(R.mipmap.cs_btn_icon_quality, "木业检测", null, "质量检测"));
                }
                if (jsonObject.has("isAppliedKids") && Constants.YES.equalsIgnoreCase((String) jsonObject.get("isAppliedKids"))) {
                    items.add(new AppointedListItem(R.mipmap.cs_btn_icon_quality, "童装检测", null, "质量检测"));
                }
                if (jsonObject.has("isAppliedTextile") && Constants.YES.equalsIgnoreCase((String) jsonObject.get("isAppliedTextile"))) {
                    items.add(new AppointedListItem(R.mipmap.cs_btn_icon_quality, "纺织品检测", null, "质量检测"));
                }
                adapter.notifyDataSetChanged();
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}

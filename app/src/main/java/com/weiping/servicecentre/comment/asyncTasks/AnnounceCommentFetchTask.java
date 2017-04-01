package com.weiping.servicecentre.comment.asyncTasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.weiping.common.StringUtility;
import com.weiping.servicecentre.Constants;
import com.weiping.servicecentre.comment.adapter.AnnounceCommentListAdapter;
import com.weiping.servicecentre.comment.model.AnnounceCommentListItem;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class AnnounceCommentFetchTask extends AsyncTask<String, Void, String> {
    private Activity mActivity;
    private AbstractHttpClient mClient;
    private HttpPost httpPost;
    private AnnounceCommentListAdapter adapter;
    private ListView listView;
    private View moreView;
    private String announceId;
    private int numbers = 20;

    public AnnounceCommentFetchTask(Activity mActivity, String announceId, ListView listView, AnnounceCommentListAdapter adapter,View moreView){
        super();
        this.mActivity = mActivity;
        this.announceId = announceId;
        this.adapter = adapter;
        this.moreView = moreView;
        this.listView = listView;
        mClient = new DefaultHttpClient();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            httpPost = new HttpPost(params[0]);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            JSONObject jsonObject = new JSONObject();
            if(!StringUtility.isEmptyString(announceId))
                jsonObject.put("announceId", announceId);
            StringEntity se = new StringEntity(jsonObject.toString());
            httpPost.setEntity(se);
            HttpResponse response = mClient.execute(httpPost);
            return EntityUtils.toString(response.getEntity());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (StringUtility.isEmptyString(result)) {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_data_updating), Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            JSONObject jsonData = new JSONObject(result.trim());
            if (jsonData != null) {
                JSONArray jsonItems = jsonData.getJSONArray(Constants.JSON_NAME_RESPONSE_OBJECT);
                ArrayList<AnnounceCommentListItem> items = new ArrayList<>();
                if (adapter != null) {
                    items = adapter.getItemArrayList();
                    if(items == null){
                        items = new ArrayList<>();
                    }
                }
                if(jsonItems != null && jsonItems.length() > 0){
                    for(int i=0; i<jsonItems.length(); i++){
                        JSONObject row = jsonItems.getJSONObject(i);
                        String id = "";
                        if(!StringUtility.isEmptyString(row.getString(Constants.ID))){
                            id = row.getString(com.weiping.InteProperty.trademark.core.Constants.ID);
                        }
                        String custName = "";
                        if(!StringUtility.isEmptyString(row.getString(Constants.CUST_NAME))){
                            custName = row.getString(Constants.CUST_NAME);
                        }
                        String comment = "";
                        if(!StringUtility.isEmptyString(row.getString(Constants.COMMENT))){
                            comment = row.getString(Constants.COMMENT);
                        }
                        String announceId = "";
                        if(!StringUtility.isEmptyString(row.getString(Constants.ANNOUNCE_ID))){
                            announceId = row.getString(Constants.ANNOUNCE_ID);
                        }
                        String commentTime = "";
                        if(!StringUtility.isEmptyString(row.getString(Constants.COMMENT_TIME))){
                            commentTime = row.getString(Constants.COMMENT_TIME);
                        }
                        String sessionId = "";
                        if(!StringUtility.isEmptyString(row.getString(Constants.SESSION_ID))){
                            sessionId = row.getString(Constants.SESSION_ID);
                        }

                        int numberOfLike = row.getInt(Constants.NUMBER_OF_LIKE);
                        int numberOfComment = row.getInt(Constants.NUMBER_OF_COMMENT);

                        String[] comment_array = new String[numberOfComment];
                        String[] username_array = new String[numberOfComment];
                        if(numberOfComment > 1) {
                            JSONArray comment_list = row.getJSONArray(Constants.COMMENT_LIST);
                            JSONArray username_list = row.getJSONArray(Constants.CUSTOMER_NAME_LIST);
                            if (comment_list != null && comment_list.length() > 1){
                                for (int j=1; j<comment_list.length(); j++){
                                    String commentRow = comment_list.getString(j);
                                    comment_array[j] = commentRow;
                                }
                            }
                            if (username_list != null && username_list.length() > 1){
                                for (int j=1; j<username_list.length(); j++){
                                    String usernameRow = username_list.getString(j);
                                    username_array[j] = usernameRow;
                                }
                            }
                        }

                        items.add(new AnnounceCommentListItem(id, custName, comment, announceId, commentTime, sessionId, numberOfLike, numberOfComment, comment_array, username_array));
                    }
                    adapter.setItemArrayList(items);
                    listView.setAdapter(adapter);
                    if (jsonItems.length() < numbers){
                        noMoreItems();
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void noMoreItems(){
        listView.removeFooterView(moreView);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }
}

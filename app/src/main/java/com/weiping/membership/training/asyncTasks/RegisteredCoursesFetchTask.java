package com.weiping.membership.training.asyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import com.weiping.membership.common.Constants;
import com.weiping.membership.training.adapter.RegisteredCourseListAdapter;
import com.weiping.membership.training.model.RegisteredCourseListItem;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class RegisteredCoursesFetchTask extends AsyncTask<String, Void, JSONObject> {

    private Activity mActivity;
    protected ProgressDialog progressDialog;
    private AbstractHttpClient mClient;
    private HttpPost httpPost;

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            if (httpPost != null)
                httpPost.abort();
            mActivity.onBackPressed();
        }
    };

    public RegisteredCoursesFetchTask(Activity activity){
        super();
        mActivity = activity;
        mClient = new DefaultHttpClient();
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        progressDialog = ProgressDialog.show(mActivity, null, mActivity.getResources().getString(R.string.dialog_loading));
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(progressDialogCancelListener);
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {

            SharedPreferences sharedPreferences = mActivity.getSharedPreferences(com.weiping.servicecentre.Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
            String AUTH_SESSION_TOKEN = sharedPreferences.getString(com.weiping.servicecentre.Constants.HEAD_AUTH_TOKEN, "");

            httpPost = new HttpPost(params[0]);
            httpPost.setHeader(Constants.HEAD_AUTH_TOKEN, AUTH_SESSION_TOKEN);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse response = mClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity());

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
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_data_updating), Toast.LENGTH_SHORT).show();
            mActivity.onBackPressed();
            return;
        }

        ArrayList<RegisteredCourseListItem> items = new ArrayList<>();
        try{
            JSONArray jsonItems = jsonData.getJSONArray(Constants.JSON_NAME_RESPONSE_OBJECT);
            for(int i=0; i<jsonItems.length(); i++){
                JSONObject row = jsonItems.getJSONObject(i);
                String title = row.getString(com.weiping.servicecentre.Constants.JSON_TITLE);
                String id = row.getString(com.weiping.servicecentre.Constants.JSON_CS_TRAIN_COURSE_ID);
                String typeId = row.getString(com.weiping.servicecentre.Constants.JSON_CS_TRAIN_COURSE_TYPE_ID);
                int[] categoryIDMap = com.weiping.servicecentre.Constants.COURSE_CATEGORY_ID_MAP;
                int index = -1;
                for(int j=0; j<categoryIDMap.length; j++) {
                    if (Integer.valueOf(typeId) == categoryIDMap[j]){
                        index = j;
                        break;
                    }
                }
                String categoryName = "";
                if(index >= 0){
                    String[] grid_name = mActivity.getResources().getStringArray(R.array.cs_course_grid_name);
                    if(index < grid_name.length){
                        categoryName = grid_name[index];
                    }
                }
                items.add(new RegisteredCourseListItem(title, id, categoryName));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }finally {
            progressDialog.dismiss();
        }

        RegisteredCourseListAdapter adapter = new RegisteredCourseListAdapter(mActivity, items);


        ListView v = (ListView)mActivity.findViewById(R.id.list_view_member_registered_course);
        v.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}

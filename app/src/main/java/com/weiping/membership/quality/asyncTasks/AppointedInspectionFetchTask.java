package com.weiping.membership.quality.asyncTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import com.weiping.membership.common.Constants;
import com.weiping.membership.quality.adapter.AppointedInspectionListAdapter;
import com.weiping.membership.quality.model.AppointedInspectionListItem;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class AppointedInspectionFetchTask extends AsyncTask<String, Void, JSONObject> {
    private Activity mActivity;
    protected ProgressDialog progressDialog;
    private AbstractHttpClient mClient;
    private HttpPost httpPost;

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
            httpPost.abort();
            mActivity.onBackPressed();
        }
    };

    public AppointedInspectionFetchTask(Activity activity){
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

        AlertDialog.Builder notLoginDialogBuilder = new AlertDialog.Builder(mActivity);
        notLoginDialogBuilder.setCancelable(false);

        ArrayList<AppointedInspectionListItem> items = new ArrayList<>();
        boolean appointed = false;
        try{
            if(jsonData.has("error") && Constants.UNAUTHORIZED.equalsIgnoreCase((String)jsonData.get("error"))){
                notLoginDialogBuilder
                        .setMessage("请登录！")
                        .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                mActivity.onBackPressed();
                            }
                        });
                AlertDialog notLoginDialog = notLoginDialogBuilder.create();
                notLoginDialog.show();
                return;
            }
            if(jsonData.has("isAppliedWood") && Constants.YES.equalsIgnoreCase((String) jsonData.get("isAppliedWood"))){
                items.add(new AppointedInspectionListItem(mActivity.getResources().getString(R.string.title_activity_wood_inspection), Constants.INSPECTION_TYPE_WOOD));
                appointed = true;
            }
            if(jsonData.has("isAppliedKids") && Constants.YES.equalsIgnoreCase((String) jsonData.get("isAppliedKids"))){
                items.add(new AppointedInspectionListItem(mActivity.getResources().getString(R.string.title_activity_kids_wear_inspection), Constants.INSPECTION_TYPE_KIDS));
                appointed = true;
            }
            if(jsonData.has("isAppliedTextile") && Constants.YES.equalsIgnoreCase((String) jsonData.get("isAppliedTextile"))){
                items.add(new AppointedInspectionListItem(mActivity.getResources().getString(R.string.title_activity_textile_inspection), Constants.INSPECTION_TYPE_TEXTILE));
                appointed = true;
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }finally {
            progressDialog.dismiss();
        }

        if(!appointed){
            notLoginDialogBuilder
                    .setMessage("您尚未预约！")
                    .setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            mActivity.onBackPressed();
                        }
                    });
            AlertDialog notLoginDialog = notLoginDialogBuilder.create();
            notLoginDialog.show();
        }

        AppointedInspectionListAdapter adapter = new AppointedInspectionListAdapter(mActivity, items);
        ListView v = (ListView)mActivity.findViewById(R.id.list_view_member_appointed_inspection);
        v.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}

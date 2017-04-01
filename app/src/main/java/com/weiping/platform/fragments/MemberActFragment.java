package com.weiping.platform.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.common.StringUtility;
import com.weiping.platform.MainMemberActivity;
import com.weiping.servicecentre.Constants;
import com.weiping.servicecentre.comment.adapter.AnnounceCommentListAdapter;
import com.weiping.servicecentre.comment.asyncTasks.AddAnnounceCommentTask;
import com.weiping.servicecentre.comment.asyncTasks.AnnounceCommentFetchTask;
import com.weiping.servicecentre.comment.asyncTasks.AnnounceCommentMoreFetchTask;
import com.weiping.servicecentre.comment.model.AnnounceCommentListItem;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class MemberActFragment extends MainMemberActivity.PlaceholderFragment  {
    public View rootView;

    private AddAnnounceCommentTask addAnnounceCommentTask;
    private AnnounceCommentFetchTask fetchAnnounceTask;
    private AnnounceCommentMoreFetchTask moreCommentTask;

    private ArrayList<AnnounceCommentListItem> itemArrayList;
    private AnnounceCommentListAdapter adapter;
    private ListView listView;
    private View moreView;
    private int lastItem;
    public int count=20;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_member_act, container, false);
        setupUI(rootView);
        initListView();
        fetchData();
        Button btn = (Button)rootView.findViewById(R.id.btn_submit_comment);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onClickSubmitAnnounceComment(v);
            }
        });
        return rootView;
    }

    private void initListView(){
        itemArrayList = new ArrayList<>();
        listView = (ListView)rootView.findViewById(R.id.lv_comment);
        adapter = new AnnounceCommentListAdapter(getActivity(), itemArrayList);
        moreView = getActivity().getLayoutInflater().inflate(R.layout.listview_footer_loading, null);
        moreView.setVisibility(View.GONE);
        listView.addFooterView(moreView);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (lastItem == count && scrollState == SCROLL_STATE_IDLE) {
                    if (!adapter.isDownloading()) {
                        adapter.setDownloading(true);
                        moreView.setVisibility(view.VISIBLE);
                        ArrayList<AnnounceCommentListItem> arrayList = adapter.getItemArrayList();
                        String id = arrayList.get(lastItem - 1).getSession_id();
                        loadMoreData(id);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem = firstVisibleItem + visibleItemCount - 1;
                if (totalItemCount - 1 != count) {
                    count = totalItemCount - 1;
                }
            }
        });
    }
    public void loadMoreData(String startId) {
        if (!networkConnect()) {
            Toast.makeText(getActivity(), getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }
        moreCommentTask = new AnnounceCommentMoreFetchTask(getActivity(), null, listView, adapter, moreView, Integer.valueOf(startId));
        moreCommentTask.execute(Constants.URL_ANNOUNCE_COMMENT_FETCH);
    }

    public void fetchData() {
        if (!networkConnect()) {
            Toast.makeText(getActivity(), getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }
        fetchAnnounceTask = new AnnounceCommentFetchTask(getActivity(), null, listView, adapter, moreView);
        fetchAnnounceTask.execute(Constants.URL_ANNOUNCE_COMMENT_FETCH);
    }
    public void onClickSubmitAnnounceComment(View view){

        EditText et_comment = (EditText)rootView.findViewById(R.id.et_announce_comment);
        String str_comment = et_comment.getText().toString();
        if(StringUtility.isEmptyString(str_comment)){
            return;
        }
        if(!networkConnect()){
            Toast.makeText(getActivity(), getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(com.weiping.membership.common.Constants.IS_USER_LOGIN, false);
        if(isLoggedIn) {
            String session_id = ((TextView)rootView.findViewById(R.id.txt_hide_session_id)).getText().toString();
            addAnnounceCommentTask = new AddAnnounceCommentTask(getActivity(), str_comment, session_id, null);
            addAnnounceCommentTask.setRefresh(false);
            addAnnounceCommentTask.setFragmentManager(getFragmentManager());
            addAnnounceCommentTask.execute(Constants.URL_ANNOUNCE_COMMENT);
        } else {
            loginAlert();
        }
    }

    public void setupUI(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(getActivity());
                    return false;
                }

            });
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        EditText et_comment = (EditText)rootView.findViewById(R.id.et_announce_comment);
        et_comment.setHint(getResources().getString(R.string.say_something));
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES || newConfig.keyboardHidden == Configuration.KEYBOARDHIDDEN_YES) {
            EditText et_comment = (EditText)rootView.findViewById(R.id.et_announce_comment);
            et_comment.setHint(getResources().getString(R.string.say_something));
        }
    }

    public boolean networkConnect() {
        ConnectivityManager connMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    private void loginAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder
                .setMessage("请登录！")
                .setPositiveButton(getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

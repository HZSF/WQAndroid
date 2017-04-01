package com.weiping.servicecentre.comment.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weiping.common.Constants;
import com.weiping.common.StringUtility;
import com.weiping.servicecentre.comment.asyncTasks.LikeCommentTask;
import com.weiping.servicecentre.comment.model.AnnounceCommentListItem;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class AnnounceCommentListAdapter extends ArrayAdapter<AnnounceCommentListItem> {
    private Context context;
    private ArrayList<AnnounceCommentListItem> itemArrayList;
    private boolean downloading = false;

    public AnnounceCommentListAdapter(Context context, ArrayList<AnnounceCommentListItem> itemArrayList) {
        super(context, R.layout.announce_comment_list_row, itemArrayList);
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.announce_comment_list_row, parent, false);

        TextView custName_tv = (TextView) rowView.findViewById(R.id.txt_username);
        TextView commentTime_tv = (TextView) rowView.findViewById(R.id.txt_comment_time);

        TextView commentBody_tv = (TextView) rowView.findViewById(R.id.txt_comment_body);
        custName_tv.setText(itemArrayList.get(position).getCustomer_name());
        String commentTime = itemArrayList.get(position).getComment_time();
        if(commentTime.length() > 5) {
            commentTime = commentTime.substring(0, commentTime.length()-5);
        }
        commentTime_tv.setText(commentTime);
        commentBody_tv.setText(itemArrayList.get(position).getComment());
        int comments_number = itemArrayList.get(position).getNumberOfComments();
        if (comments_number > 1) {
            RelativeLayout rl_sub = (RelativeLayout) (rowView.findViewById(R.id.insert_point));
            for (int subCommentNum = 1; subCommentNum<itemArrayList.get(position).getUsername_array().length; subCommentNum++) {
                LayoutInflater sub_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                RelativeLayout subRowView = (RelativeLayout)sub_inflater.inflate(R.layout.sub_comment_row, null, false);
                TextView txt_subC_name = (TextView) subRowView.findViewById(R.id.txt_subC_name);
                txt_subC_name.setText(itemArrayList.get(position).getUsername_array()[subCommentNum] + ": ");
                TextView txt_subC_content = (TextView) subRowView.findViewById(R.id.txt_subC_content);
                txt_subC_content.setText(itemArrayList.get(position).getComment_array()[subCommentNum]);
                subRowView.setId(R.id.rl_sub_com + subCommentNum);
                RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                p.addRule(RelativeLayout.BELOW, R.id.rl_sub_com + subCommentNum -1);
                subRowView.setLayoutParams(p);
                rl_sub.addView(subRowView);
            }
        }

        TextView likeCount_tv = (TextView) rowView.findViewById(R.id.txt_like_count);
        TextView commentCount_tv = (TextView) rowView.findViewById(R.id.txt_comment_count);
        int commentCount = 0;
        likeCount_tv.setText(String.valueOf(itemArrayList.get(position).getNumberOfLike()));
        commentCount_tv.setText(String.valueOf(commentCount));

        rowView.findViewById(R.id.ll_like).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(com.weiping.membership.common.Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
                boolean isLoggedIn = sharedPreferences.getBoolean(com.weiping.membership.common.Constants.IS_USER_LOGIN, false);
                if (isLoggedIn) {
                    if(!itemArrayList.get(position).isLiked()){
                        TextView likeCount_tv = (TextView) v.findViewById(R.id.txt_like_count);
                        likeCount_tv.setText(String.valueOf(Integer.valueOf(likeCount_tv.getText().toString()) + 1));
                        LikeCommentTask task = new LikeCommentTask(v.getContext(), itemArrayList.get(position).getSession_id(), true, likeCount_tv);
                        task.execute(Constants.URL_ANNOUNCE_COMMENT_LIKE);
                        itemArrayList.get(position).setLiked(true);
                    } else {
                        TextView likeCount_tv = (TextView) v.findViewById(R.id.txt_like_count);
                        if(Integer.valueOf(likeCount_tv.getText().toString()) > 0){
                            likeCount_tv.setText(String.valueOf(Integer.valueOf(likeCount_tv.getText().toString()) - 1));
                        }
                        LikeCommentTask task = new LikeCommentTask(v.getContext(), itemArrayList.get(position).getSession_id(), false, likeCount_tv);
                        task.execute(Constants.URL_ANNOUNCE_COMMENT_LIKE);
                        itemArrayList.get(position).setLiked(false);
                    }
                } else {
                    loginAlert();
                }
            }
        });
        rowView.findViewById(R.id.ll_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(com.weiping.membership.common.Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
                boolean isLoggedIn = sharedPreferences.getBoolean(com.weiping.membership.common.Constants.IS_USER_LOGIN, false);
                if (isLoggedIn) {
                    final View view = parent.getRootView();
                    EditText et = (EditText)view.findViewById(R.id.et_announce_comment);
                    et.requestFocus();
                    ((TextView)view.findViewById(R.id.txt_hide_session_id)).setText(itemArrayList.get(position).getSession_id());
                    et.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            InputMethodManager keyboard = (InputMethodManager)
                                    view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            EditText etText = (EditText)view.findViewById(R.id.et_announce_comment);
                            keyboard.showSoftInput(etText, 0);
                            etText.setHint("@" + itemArrayList.get(position).getCustomer_name());
                        }
                    }, 200);
                } else {
                    loginAlert();
                }
            }
        });
        return rowView;
    }

    private void loginAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder
                .setMessage("请登录！")
                .setPositiveButton(context.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public ArrayList<AnnounceCommentListItem> getItemArrayList() {
        return itemArrayList;
    }
    public void setItemArrayList(ArrayList<AnnounceCommentListItem> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }

    public boolean isDownloading() {
        return downloading;
    }

    public void setDownloading(boolean downloading) {
        this.downloading = downloading;
    }
}

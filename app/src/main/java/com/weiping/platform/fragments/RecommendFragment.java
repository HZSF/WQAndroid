package com.weiping.platform.fragments;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.weiping.common.Constants;
import com.weiping.platform.MainMemberActivity;
import com.weiping.thirdParty.wechat.Util;

import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import platform.tyk.weping.com.weipingplatform.R;

public class RecommendFragment extends MainMemberActivity.PlaceholderFragment {
    public View rootView;
    public String share_url = "http://120.55.96.224/appDownload.htm";
    public String share_content = "我在用微企一站通手机客户端";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recommend, container, false);
        rootView.findViewById(R.id.txt_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });/*
        rootView.findViewById(R.id.btn_wechat).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                CallWeChat(1);
            }
        });
        rootView.findViewById(R.id.btn_moments).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                CallWeChat(2);
            }
        });
        rootView.findViewById(R.id.btn_favorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallWeChat(3);
            }
        });
        rootView.findViewById(R.id.btn_weibo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CallWeibo();
                showShare();
            }
        });
        rootView.findViewById(R.id.btn_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Intent.ACTION_SEND);*/
                /*List<ResolveInfo> resInfo = getActivity().getPackageManager().queryIntentActivities(intent, 0);
                if (!resInfo.isEmpty()) {
                    List<Intent> targetedShareIntents = new ArrayList<>();
                    for (ResolveInfo info : resInfo) {
                        intent.setType("image/*");
                        ActivityInfo activityInfo = info.activityInfo;

                        // judgments : activityInfo.packageName, activityInfo.name, etc.
                        if (activityInfo.packageName.contains("bluetooth") || activityInfo.name.contains("bluetooth")) {
                            continue;
                        } else if (activityInfo.packageName.contains("com.tencent.mm")){
                            intent.putExtra(Intent.EXTRA_TEXT, share_content);
                        } else {
                            intent.putExtra(Intent.EXTRA_TEXT, share_content+"1");
                        }
                        intent.setPackage(activityInfo.packageName);
                        targetedShareIntents.add(intent);
                    }
                    Intent chooserIntent = Intent.createChooser(intent, "Select app to share");

                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[] {}));
                    try {
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(chooserIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getActivity(), "Can't find share component to share", Toast.LENGTH_SHORT).show();
                    }
                }*/
                /*intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.putExtra(Intent.EXTRA_TEXT, "专注于中小微企业服务");
                intent.putExtra(Intent.EXTRA_TITLE, share_content);
                Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                final Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), thumb, null, null));
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getActivity().getTitle()));*/
              /*  Intent intent = new Intent();
                //ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
                //intent.setComponent(comp);
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.setType("image/*");

                intent.putExtra("Kdescription", share_content);

                ArrayList<Uri> uris = new ArrayList<Uri>();
                Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                final Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), thumb, null, null));
                uris.add(uri);
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                startActivity(intent);
            }
        });*/
        return rootView;
    }

    private void CallWeChat(int where){
        IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), Constants.Wechat_APP_ID, false);
        api.registerApp(Constants.Wechat_APP_ID);
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = share_url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = share_content;
        msg.description = "专注于中小微企业服务";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.czb);
        msg.thumbData = Util.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("weiwei");
        req.message = msg;
        switch (where){
            case 1:
                req.scene = SendMessageToWX.Req.WXSceneSession;
                break;
            case 2:
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                break;
            case 3:
                req.scene = SendMessageToWX.Req.WXSceneFavorite;
                break;
        }
        boolean resp = api.sendReq(req);
        Log.i("req", resp ? "true" : "false");
    }
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /*private void CallWeibo(){
        IWeiboShareAPI mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(getActivity(),Constants.Webo_APP_KEY);
        mWeiboShareAPI.registerApp();
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        TextObject textObject = new TextObject();
        textObject.text = share_content;
        weiboMessage.textObject = textObject;
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        mWeiboShareAPI.sendRequest(getActivity(), request);
    }*/

    private void showShare() {
        ShareSDK.initSDK(getActivity());
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.txt_recommend_us));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(share_url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(share_content);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        final Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), thumb, null, null));
        oks.setImagePath(uri.toString());//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(share_url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(share_url);

// 启动分享GUI
        oks.show(getActivity());
    }
}

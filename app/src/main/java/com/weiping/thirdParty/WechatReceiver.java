package com.weiping.thirdParty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WechatReceiver extends BroadcastReceiver {
    public WechatReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        final IWXAPI api = WXAPIFactory.createWXAPI(context, null);
        api.registerApp("wxd930ea5d5a258f4f");
    }
}

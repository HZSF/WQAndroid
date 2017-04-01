package com.weiping.credential.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import platform.tyk.weping.com.weipingplatform.R;

public class MemberAgreementActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_member_agreement));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_member_agreement);
        ((TextView)findViewById(R.id.txt_member_agreement)).setText(Html.fromHtml(getResources().getString(R.string.register_agreement)));
    }

    public void onClickAgreeAgreement(View view){
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        onBackPressed();
    }

    public void onClickCancelAgreement(View view){
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        onBackPressed();
    }

}

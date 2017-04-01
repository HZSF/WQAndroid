package com.weiping.servicecentre.property.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.weiping.common.Constants;
import com.weiping.servicecentre.property.adapter.PropertyAdapter;
import com.weiping.servicecentre.property.buy.activity.PropertySellingListActivity;
import com.weiping.servicecentre.property.favorites.activity.FavoriteCentreActivity;
import com.weiping.servicecentre.property.lend.activity.PropertyLendFormActivity;
import com.weiping.servicecentre.property.rent.activity.PropertyLendingListActivity;
import com.weiping.servicecentre.property.sell.activity.PropertySellFormActivity;

import platform.tyk.weping.com.weipingplatform.R;

public class PropertyActivity extends Activity {

    private String[] gridTitle;
    private TypedArray gridIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);
        gridTitle = getResources().getStringArray(R.array.property_items);
        gridIcons = getResources().obtainTypedArray(R.array.property_icons);
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(com.weiping.membership.common.Constants.IS_USER_LOGIN, false);
        if(isLoggedIn) {
            String username = sharedPreferences.getString(Constants.SHARE_PREFERENCE_USERNAME, "");
            ((TextView) findViewById(R.id.txt_username)).setText(username);
        }
        setGridView();
    }
    public void setGridView() {
        GridView gridview = (GridView) findViewById(R.id.gv_property);
        gridview.setAdapter(new PropertyAdapter(this, gridTitle, gridIcons));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(getBaseContext(), PropertySellFormActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getBaseContext(), PropertySellingListActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getBaseContext(), PropertyLendingListActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(getBaseContext(), PropertyLendFormActivity.class);
                        startActivity(intent);
                        break;
                    case 4:

                        break;
                    case 5:
                        intent = new Intent(getBaseContext(), FavoriteCentreActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

}

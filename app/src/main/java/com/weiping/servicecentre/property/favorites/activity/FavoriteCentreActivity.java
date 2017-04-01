package com.weiping.servicecentre.property.favorites.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.weiping.servicecentre.property.favorites.adapter.FavoriteGridAdapter;
import com.weiping.servicecentre.property.favorites.lend.activity.PropertyFavoriteLendListActivity;
import com.weiping.servicecentre.property.favorites.sell.activity.PropertyFavoriteSellListActivity;

import platform.tyk.weping.com.weipingplatform.R;

public class FavoriteCentreActivity extends Activity {
    private String[] gridTitle;
    private TypedArray gridIcons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_favorite_centre));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_favorite_centre);
        setGridView();
    }

    public void setGridView() {
        GridView gridview = (GridView)findViewById(R.id.gv_favorite);
        gridTitle = getResources().getStringArray(R.array.property_favorite_items);
        gridIcons = getResources().obtainTypedArray(R.array.property_favorite_icons);
        gridview.setAdapter(new FavoriteGridAdapter(this, gridTitle, gridIcons));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(getBaseContext(), PropertyFavoriteSellListActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(getBaseContext(), PropertyFavoriteLendListActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

}

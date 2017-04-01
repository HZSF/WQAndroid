package com.weiping.platform.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiping.platform.model.NavDrawerItem;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class NavDrawerListAdapter extends ArrayAdapter<NavDrawerItem> {
    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;

    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems) {
        super(context, R.layout.drawer_list_item, navDrawerItems);
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public NavDrawerItem getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.drawer_item_icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.drawer_item_text);
        TextView txtCount = (TextView) convertView.findViewById(R.id.drawer_item_counter);
        imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
        txtTitle.setText(navDrawerItems.get(position).getTitle());
        if (navDrawerItems.get(position).isCounterVisible()){
            txtCount.setText(navDrawerItems.get(position).getTitle());
        } else {
            txtCount.setVisibility(View.GONE);
        }
        return convertView;
    }
}

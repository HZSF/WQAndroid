package com.weiping.platform.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import platform.tyk.weping.com.weipingplatform.R;

public class DashboardAdapter extends BaseAdapter {
    private Context mContext;

    private String[] dashNames;
    private int[] drawRes;

    public DashboardAdapter(Context c, String[] strings, int[] ints){
        mContext = c;
        dashNames = strings;
        drawRes = ints;
    }

    @Override
    public int getCount() {
        return dashNames.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout ll;
        if(convertView == null){
            ll = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.dashboard_grid_cell, parent, false);
            TextView textView = (TextView)ll.findViewById(R.id.txt_dashboard_cell);
            textView.setText(dashNames[position]);
            ImageView imageView = (ImageView)ll.findViewById(R.id.img_dashboard_cell);
            imageView.setImageResource(drawRes[position]);
        }else{
            ll = (LinearLayout)convertView;
        }
        ll.setId(position);

        return ll;
    }
}

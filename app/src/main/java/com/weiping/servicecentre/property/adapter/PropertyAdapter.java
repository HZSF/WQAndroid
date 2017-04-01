package com.weiping.servicecentre.property.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import platform.tyk.weping.com.weipingplatform.R;

public class PropertyAdapter extends BaseAdapter {
    private Context mContext;

    private String[] dashNames;
    private TypedArray drawRes;

    public PropertyAdapter(Context c, String[] strings, TypedArray ints){
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
        ViewGroup ll;
        if(convertView == null){
            ll = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.property_grid_cell, parent, false);
            TextView textView = (TextView)ll.findViewById(R.id.cell_text);
            textView.setText(dashNames[position]);
            ImageView imageView = (ImageView)ll.findViewById(R.id.cell_image);
            imageView.setImageResource(drawRes.getResourceId(position, -1));
        }else{
            ll = (ViewGroup)convertView;
        }
        ll.setId(position);

        return ll;
    }
}

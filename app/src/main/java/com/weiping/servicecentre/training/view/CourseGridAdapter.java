package com.weiping.servicecentre.training.view;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import platform.tyk.weping.com.weipingplatform.R;

public class CourseGridAdapter extends BaseAdapter {

    private Context mContext;
    private String[] grid_name;
    private int[] grid_drawable;

    public CourseGridAdapter(Context c, String[] strings, int[] ints){
        super();
        mContext = c;
        grid_name = strings;
        grid_drawable = ints;
    }

    @Override
    public int getCount() {
        return grid_drawable.length;
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

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout cellView;
        if (convertView == null) {
            cellView = (LinearLayout)inflater.inflate(R.layout.cs_course_grid_cell, null);

            TextView textView = (TextView)cellView.findViewById(R.id.cs_course_text_cell);
            textView.setText(grid_name[position]);
            ImageView imageView = (ImageView)cellView.findViewById(R.id.cs_course_image_cell);
            imageView.setImageResource(grid_drawable[position]);

            int size_width, size_height;
            if (Build.VERSION.SDK_INT < 16) {
                size_width = (int)mContext.getResources().getDimension(R.dimen.cs_grid_width);
                size_height = (int)mContext.getResources().getDimension(R.dimen.cs_grid_height);
            }
            else
            {
                size_width = size_height = ((GridView) parent).getColumnWidth();
            }
            cellView.setLayoutParams(new GridView.LayoutParams(size_width, size_height));
            cellView.setPadding(size_width/5, size_height/10, size_width/5, size_height/10);
        }else{
            cellView = (LinearLayout)convertView;
        }

        return cellView;
    }
}

package com.weiping.servicecentre.training.course;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class CourseListAdapter extends ArrayAdapter<CourseListItem> {
    private Context context;
    private ArrayList<CourseListItem> itemArrayList;

    public CourseListAdapter(Context context, ArrayList<CourseListItem> itemArrayList){
        super(context, R.layout.cs_course_list_row, itemArrayList);
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.cs_course_list_row, parent, false);
        TextView titleView = (TextView) rowView.findViewById(R.id.cs_course_list_title);
        titleView.setText(itemArrayList.get(position).getTitle());

        return rowView;
    }

    public void setItemArrayList(ArrayList<CourseListItem> items){
        itemArrayList = items;
    }
    public ArrayList<CourseListItem> getItemArrayList(){
        return itemArrayList;
    }
}

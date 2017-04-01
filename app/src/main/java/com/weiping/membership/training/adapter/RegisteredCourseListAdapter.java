package com.weiping.membership.training.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.weiping.membership.training.model.RegisteredCourseListItem;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class RegisteredCourseListAdapter extends ArrayAdapter<RegisteredCourseListItem> {

    private Context context;
    private ArrayList<RegisteredCourseListItem> itemArrayList;

    public RegisteredCourseListAdapter(Context context, ArrayList<RegisteredCourseListItem> itemArrayList){
        super(context, R.layout.membership_training_registered_course_list_row, itemArrayList);
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.membership_training_registered_course_list_row, parent, false);
        TextView titleView = (TextView) rowView.findViewById(R.id.member_registered_course_list_title);
        titleView.setText(itemArrayList.get(position).getTitle());
        TextView categoryView = (TextView) rowView.findViewById(R.id.member_registered_course_list_category_name);
        categoryView.setText(itemArrayList.get(position).getCategoryName());

        return rowView;
    }

    public void setItemArrayList(ArrayList<RegisteredCourseListItem> items){
        itemArrayList = items;
    }
    public ArrayList<RegisteredCourseListItem> getItemArrayList(){
        return itemArrayList;
    }
}

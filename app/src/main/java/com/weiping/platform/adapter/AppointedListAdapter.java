package com.weiping.platform.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiping.platform.model.AppointedListItem;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class AppointedListAdapter extends ArrayAdapter<AppointedListItem> {
    private Context context;
    private ArrayList<AppointedListItem> itemArrayList;
    public AppointedListAdapter(Context context, ArrayList<AppointedListItem> itemArrayList){
        super(context, R.layout.appointment_list_row, itemArrayList);
        this.context = context;
        this.itemArrayList = itemArrayList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.appointment_list_row, parent, false);
        ImageView icon = (ImageView) rowView.findViewById(R.id.img_appoint);
        icon.setImageResource(itemArrayList.get(position).getImg_src());
        TextView titleView = (TextView) rowView.findViewById(R.id.txt_appoint_title);
        titleView.setText(itemArrayList.get(position).getTitle());
        //TextView categoryView = (TextView) rowView.findViewById(R.id.txt_appoint_category);
        //categoryView.setText(itemArrayList.get(position).getCategory());

        return rowView;
    }

    public void setItemArrayList(ArrayList<AppointedListItem> items){
        itemArrayList = items;
    }
    public ArrayList<AppointedListItem> getItemArrayList(){
        return itemArrayList;
    }
}

package com.weiping.servicecentre.announcement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class NewsListAdapter extends ArrayAdapter<NewsItem> {
    private Context context;
    private ArrayList<NewsItem> itemArrayList;

    public NewsListAdapter(Context context, ArrayList<NewsItem> itemArrayList){
        super(context, R.layout.cs_announcement_row, itemArrayList);
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.cs_announcement_row, parent, false);
        TextView titleView = (TextView) rowView.findViewById(R.id.announce_title);
        TextView subTitleView = (TextView)rowView.findViewById(R.id.announce_subtitle);
        titleView.setText(itemArrayList.get(position).getTitle());
        subTitleView.setText(itemArrayList.get(position).getSubtitle());

        return rowView;
    }

    public void setItemArrayList(ArrayList<NewsItem> items){
        itemArrayList = items;
    }
    public ArrayList<NewsItem> getItemArrayList(){
        return itemArrayList;
    }
}

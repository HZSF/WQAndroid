package com.weiping.platform.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiping.platform.model.ContactUsItem;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class ContactUsListAdapter extends ArrayAdapter<ContactUsItem> {
    private Context context;
    private ArrayList<ContactUsItem> list;
    public ContactUsListAdapter(Context context, ArrayList<ContactUsItem> objects) {
        super(context, R.layout.list_row_contact_us, objects);
        this.context = context;
        list = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_row_contact_us, parent, false);
        ImageView img = (ImageView) rowView.findViewById(R.id.img_contact_us_row);
        img.setImageResource(list.get(position).getRes_img());
        TextView titleView = (TextView) rowView.findViewById(R.id.txt_contact_us_title);
        titleView.setText(list.get(position).getContent());
        return rowView;
    }
}

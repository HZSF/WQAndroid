package com.weiping.servicecentre.property.favorites.lend.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weiping.servicecentre.Constants;
import com.weiping.servicecentre.property.asyncTasks.PropertyCancelFavoritesTask;
import com.weiping.servicecentre.property.favorites.adapter.PropertyFavoriteListAdapter;
import com.weiping.servicecentre.property.rent.model.PropertyLendingListItem;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class PropertyFavoriteLendListAdapter extends PropertyFavoriteListAdapter<PropertyLendingListItem> {
    private Context context;
    private ArrayList<PropertyLendingListItem> itemArrayList;
    private boolean downloading = false;
    public PropertyFavoriteLendListAdapter(Context context, ArrayList<PropertyLendingListItem> itemArrayList){
        super(context, R.layout.property_favorite_lend_list_row, itemArrayList);
        this.context = context;
        this.itemArrayList = itemArrayList;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.property_favorite_lend_list_row, parent, false);
        TextView tv_title = (TextView)rowView.findViewById(R.id.txt_title);
        tv_title.setText(itemArrayList.get(position).getAddress_area());
        String subTitle = "面积：" + itemArrayList.get(position).getArea() + "  ";
        subTitle += "楼层数：" + itemArrayList.get(position).getLevels();
        TextView tv_sub_title = (TextView)rowView.findViewById(R.id.txt_sub_title);
        tv_sub_title.setText(subTitle);
        TextView tv_price = (TextView)rowView.findViewById(R.id.txt_price);
        String price = "价格：" + itemArrayList.get(position).getAsk_price() + context.getResources().getString(R.string.lbl_cny_month);
        tv_price.setText(price);
        TextView tv_submit_time = (TextView)rowView.findViewById(R.id.txt_submit_time);
        String submitTime = "发布时间：" + itemArrayList.get(position).getSubmit_time();
        tv_submit_time.setText(submitTime);
        TextView tv_description = (TextView)rowView.findViewById(R.id.txt_description);
        tv_description.setText(itemArrayList.get(position).getDescription());
        TextView btn_add_favorite = (TextView)rowView.findViewById(R.id.btn_add_favorite);
        btn_add_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PropertyCancelFavoritesTask task = new PropertyCancelFavoritesTask(PropertyFavoriteLendListAdapter.this, v.getContext(), itemArrayList.get(position).getId(), 2, position);
                task.execute(Constants.URL_PROPERTY_CANCEL_FAVORITE);
            }
        });
        return rowView;
    }

    public ArrayList<PropertyLendingListItem> getItemArrayList() {
        return itemArrayList;
    }

    public void setItemArrayList(ArrayList<PropertyLendingListItem> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }

    public boolean isDownloading() {
        return downloading;
    }

    public void setDownloading(boolean downloading) {
        this.downloading = downloading;
    }
}

package com.weiping.InteProperty.trademark.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.weiping.InteProperty.trademark.base.TrademarkTradeItem;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class TrademarkTradeListAdapter extends ArrayAdapter<TrademarkTradeItem> {
    private Context context;
    private ArrayList<TrademarkTradeItem> itemArrayList;
    private boolean downloading = false;

    public TrademarkTradeListAdapter(Context context, ArrayList<TrademarkTradeItem> itemArrayList) {
        super(context, R.layout.trademark_trade_list_row, itemArrayList);
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.trademark_trade_list_row, parent, false);

        TextView nameView = (TextView) rowView.findViewById(R.id.txt_trdmk_name);
        TextView regNumView = (TextView)rowView.findViewById(R.id.txt_trdmk_regNum);
        TextView categoryNumView = (TextView)rowView.findViewById(R.id.txt_trdmk_categoryNum);
        TextView price = (TextView)rowView.findViewById(R.id.txt_trdmk_price);
        nameView.setText(itemArrayList.get(position).getName());
        regNumView.setText("注册号："+itemArrayList.get(position).getRegNum());
        categoryNumView.setText("第"+itemArrayList.get(position).getCategoryNum()+"类");
        price.setText("价格："+itemArrayList.get(position).getPrice() + "元");

        return rowView;
    }

    public ArrayList<TrademarkTradeItem> getItemArrayList() {
        return itemArrayList;
    }

    public void setItemArrayList(ArrayList<TrademarkTradeItem> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }

    public boolean isDownloading() {
        return downloading;
    }

    public void setDownloading(boolean downloading) {
        this.downloading = downloading;
    }
}

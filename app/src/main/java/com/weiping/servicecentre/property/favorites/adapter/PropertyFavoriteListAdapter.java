package com.weiping.servicecentre.property.favorites.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;


public abstract class PropertyFavoriteListAdapter<T> extends ArrayAdapter<T> {
    private List itemArrayList;

    public PropertyFavoriteListAdapter(Context context, int resource, List itemArrayList) {
        super(context, resource, itemArrayList);
    }


    public List getItemArrayList() {
        return itemArrayList;
    }

    public void setItemArrayList(List itemArrayList) {
        this.itemArrayList = itemArrayList;
    }
}

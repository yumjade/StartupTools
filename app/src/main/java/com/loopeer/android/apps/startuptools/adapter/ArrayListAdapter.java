package com.loopeer.android.apps.startuptools.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by KorHsien on 2015/3/24.
 */
public abstract class ArrayListAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected ArrayList<T> mData;

    public void setData(ArrayList<T> data) {
        mData = data;
    }

    public ArrayListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    abstract public View getView(int position, View convertView, ViewGroup parent);
}

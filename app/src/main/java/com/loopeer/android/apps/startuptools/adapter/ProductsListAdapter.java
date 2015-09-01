package com.loopeer.android.apps.startuptools.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.laputapp.utilities.ViewHolder;
import com.loopeer.android.apps.startuptools.R;
import com.loopeer.android.apps.startuptools.model.Product;
import com.loopeer.android.apps.startuptools.util.ServiceUtils;

/**
 * Created by KorHsien on 2015/3/24.
 */
public class ProductsListAdapter extends ArrayListAdapter<Product> {

    public ProductsListAdapter(Context context) {
        super(context);
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_products, parent, false);
        }

        ImageView logoImageView = ViewHolder.get(convertView, R.id.products_imageView_logo);
        ServiceUtils.getExternalPicasso().load(mData.get(position).logoUrl).placeholder(R.mipmap.logo_placeholder).into(logoImageView);
        TextView titleTextView = ViewHolder.get(convertView, R.id.products_textView_title);
        titleTextView.setText(mData.get(position).name);
        TextView subtitleTextView = ViewHolder.get(convertView, R.id.products_textView_subtitle);
        subtitleTextView.setText(mData.get(position).desc);

        return convertView;
    }
}

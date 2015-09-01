package com.loopeer.android.apps.startuptools.adapter;

import android.content.Context;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.laputapp.utilities.ViewHolder;
import com.loopeer.android.apps.startuptools.R;
import com.loopeer.android.apps.startuptools.StartUpToolsApp;

/**
 * Created by tudou on 15-3-23.
 */
public class PrimaryListAdapter extends BaseAdapter {

    private String[] titles;
    private int[] colors;

    private Drawable mShapeDrawable = getBackgroundShapeDrawable(true);

    private Context mContext;
    private int mCheckPosition;

    public PrimaryListAdapter(Context context) {
        mContext = context;
        titles = StartUpToolsApp.getAppResources().getStringArray(R.array.primary_list_strings);
        colors = StartUpToolsApp.getAppResources().getIntArray(R.array.primary_list_colors);
    }

    @Override
    public int getCount() {
        return titles == null ? 0 : titles.length;
    }

    @Override
    public Object getItem(int position) {
        return titles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_primary, parent, false);
        }

        TextView textView = ViewHolder.get(convertView, R.id.list_item_primary);
        textView.setText(titles[position]);

        Drawable drawable;
        if (mCheckPosition == position) {
            drawable = mShapeDrawable.getConstantState().newDrawable();
            ((ShapeDrawable) drawable).getPaint().setColor(colors[position]);
        } else {
            drawable = new ColorDrawable(colors[position]);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((RippleDrawable) textView.getBackground()).setDrawableByLayerId(R.id.item_background_drawable, drawable);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            textView.setBackground(drawable);
        } else {
            textView.setBackgroundDrawable(drawable);
        }

        return convertView;
    }

    public void setCheckPosition(int position) {
        mCheckPosition = position;
        notifyDataSetChanged();
    }

    private static ShapeDrawable getBackgroundShapeDrawable(boolean isShowTriangle) {
        final int stdSize = 300;

        int mTriangle_height = stdSize / 10;

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.addRect(0, 0, stdSize, stdSize, Path.Direction.CW);
        if (isShowTriangle) {
            path.moveTo(stdSize - mTriangle_height, stdSize / 2);
            path.lineTo(stdSize, stdSize / 2 - mTriangle_height);
            path.lineTo(stdSize, stdSize / 2 + mTriangle_height);
            path.lineTo(stdSize - mTriangle_height, stdSize / 2);
        }

        return new ShapeDrawable(new PathShape(path, stdSize, stdSize));
    }
}

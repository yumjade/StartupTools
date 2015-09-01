package com.loopeer.android.apps.startuptools.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laputapp.utilities.ViewHolder;
import com.loopeer.android.apps.startuptools.R;
import com.loopeer.android.apps.startuptools.StartUpToolsApp;
import com.loopeer.android.apps.startuptools.model.Category;

/**
 * Created by KorHsien on 2015/3/24.
 */
public class SecondaryListAdapter extends ArrayListAdapter<Category> {

    private Drawable mHighlightDrawable;
    private Drawable mListItemBackgroundLight;
    private Drawable mListItemBackgroundDark;

    public SecondaryListAdapter(Context context) {
        super(context);

        mListItemBackgroundLight = new ColorDrawable(StartUpToolsApp.getAppResources().getColor(R.color.background_light));
        mListItemBackgroundDark = new ColorDrawable(StartUpToolsApp.getAppResources().getColor(R.color.background_dark));
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_secondary, parent, false);
        }

        TextView textView = ViewHolder.get(convertView, R.id.secondary_textView);
        textView.setText(mData.get(position).title);

        Drawable backgroundDrawable = textView.getBackground();
        Drawable colorDrawable = (position % 2 == 0 ? mListItemBackgroundLight : mListItemBackgroundDark)
                .getConstantState().newDrawable();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((RippleDrawable) backgroundDrawable).setDrawableByLayerId(R.id.item_background_drawable, colorDrawable);
        } else {
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, getHighlightDrawable());
            stateListDrawable.addState(new int[]{}, colorDrawable);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                textView.setBackground(stateListDrawable);
            } else {
                textView.setBackgroundDrawable(stateListDrawable);
            }
        }

        return convertView;
    }

    private Drawable getHighlightDrawable() {
        if (mHighlightDrawable == null) {
            TypedValue typedValue = new TypedValue();
            mContext.getTheme().resolveAttribute(android.R.attr.colorPressedHighlight, typedValue, true);
            mHighlightDrawable = new ColorDrawable(typedValue.data);
        }

        return mHighlightDrawable;
    }
}

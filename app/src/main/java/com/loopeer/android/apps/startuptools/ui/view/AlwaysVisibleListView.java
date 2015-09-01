package com.loopeer.android.apps.startuptools.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * Created by KorHsien on 2015/4/7.
 */
public class AlwaysVisibleListView extends ListView {
    public AlwaysVisibleListView(Context context) {
        super(context);
    }

    public AlwaysVisibleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlwaysVisibleListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * http://stackoverflow.com/questions/22964934/android-swiperefreshlayout-with-empty-textview/23025336#23025336
     *
     * @param visibility
     */
    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.GONE && getCount() == 0) {
            return;
        }
        super.setVisibility(visibility);
    }
}

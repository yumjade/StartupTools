package com.loopeer.android.apps.startuptools.ui.activity;

import android.os.Bundle;

import com.loopeer.android.apps.startuptools.R;

import butterknife.ButterKnife;

/**
 * Created by KorHsien on 2015/3/23.
 */
public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.inject(this);

        setSwipeBackEnable(false);

        setupActionBarToolbar();
    }

    protected void setupActionBarToolbar() {
        super.setupActionBarToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActionBarToolbar.setNavigationIcon(R.mipmap.ic_ab_back);
    }
}

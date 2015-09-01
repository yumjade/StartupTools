package com.loopeer.android.apps.startuptools.ui.activity;

import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.loopeer.android.apps.startuptools.NavUtils;
import com.loopeer.android.apps.startuptools.R;
import com.loopeer.android.apps.startuptools.StartUpToolsApp;
import com.loopeer.android.apps.startuptools.adapter.ProductPagerAdapter;
import com.loopeer.android.apps.startuptools.model.Product;
import com.loopeer.android.apps.startuptools.util.ThemeUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by KorHsien on 2015/3/23.
 */
public class ProductActivity extends BaseActivity {

    @InjectView(R.id.product_viewPager)
    ViewPager mViewPager;

    private ProductPagerAdapter mProductPagerAdapter;

    private int mTintColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        ButterKnife.inject(this);

        int parentItemPosition = getIntent().getIntExtra(NavUtils.EXTRA_PARENT_ITEM_POSITION, 0);
        setTheme(ThemeUtils.getThemeByParentItemPosition(parentItemPosition));
        mTintColor = StartUpToolsApp.getAppResources().getIntArray(R.array.primary_list_colors)[parentItemPosition];

        setupActionBarToolbar();
        setupViewPager();
    }

    protected void setupActionBarToolbar() {
        super.setupActionBarToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActionBarToolbar.setNavigationIcon(R.mipmap.ic_ab_back);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mActionBarToolbar.getNavigationIcon().setTint(mTintColor);
        } else {
            mActionBarToolbar.getNavigationIcon().setColorFilter(mTintColor, PorterDuff.Mode.SRC_IN);
        }
    }

    private void setupViewPager() {
        mProductPagerAdapter = new ProductPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mProductPagerAdapter);

        final ArrayList<Product> products = ((StartUpToolsApp) StartUpToolsApp.getInstance()).getProducts();
        mProductPagerAdapter.setProducts(products);
        mProductPagerAdapter.notifyDataSetChanged();

        int currentItem = getIntent().getIntExtra(NavUtils.EXTRA_PRODUCT_SORT, 0);
        mViewPager.setCurrentItem(currentItem);
        setTitle(products.get(currentItem).name);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setTitle(products.get(position).name);
            }
        });
    }
}

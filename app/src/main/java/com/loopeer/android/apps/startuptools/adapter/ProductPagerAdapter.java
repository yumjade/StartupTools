package com.loopeer.android.apps.startuptools.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.loopeer.android.apps.startuptools.model.Product;
import com.loopeer.android.apps.startuptools.ui.fragment.ProductFragment;

import java.util.ArrayList;

/**
 * Created by KorHsien on 2015/3/23.
 */
public class ProductPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Product> mProducts;

    public void setProducts(ArrayList<Product> products) {
        mProducts = products;
    }

    public ProductPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return ProductFragment.newInstance(i);
    }

    @Override
    public int getCount() {
        return mProducts == null ? 0 : mProducts.size();
    }
}
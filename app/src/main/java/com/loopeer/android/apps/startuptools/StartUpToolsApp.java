package com.loopeer.android.apps.startuptools;

import com.laputapp.Laputapp;
import com.loopeer.android.apps.startuptools.model.Product;

import java.util.ArrayList;

/**
 * Created by tudou on 15-3-20.
 */
public class StartUpToolsApp extends Laputapp {

    private ArrayList<Product> mProducts;

    public ArrayList<Product> getProducts() {
        return mProducts;
    }

    public void setProducts(ArrayList<Product> products) {
        mProducts = products;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}

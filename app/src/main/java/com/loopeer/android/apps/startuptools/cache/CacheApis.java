/**
 * Created by YuGang Yang on September 17, 2014.
 * Copyright 2007-2015 Loopeer.com. All rights reserved.
 */
package com.loopeer.android.apps.startuptools.cache;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.loopeer.android.apps.startuptools.StartUpToolsApp;
import com.loopeer.android.apps.startuptools.model.Category;
import com.loopeer.android.apps.startuptools.model.Product;
import com.loopeer.android.apps.startuptools.util.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

//import com.loopeer.android.apps.startuptools.util.AccountUtils;

public class CacheApis {

    private static CacheApis instance;
    private ArrayList<DataCache> mCacheArray = new ArrayList<>();

    private static final int Category_CACHE_INDEX = 0;
    private static final int Product_CACHE_INDEX = 1;

    private CacheApis() {
        mCacheArray.add(Category_CACHE_INDEX, new CategoryCache());
        mCacheArray.add(Product_CACHE_INDEX, new ProductCache());
    }

    public static CacheApis getInstance() {
        if (instance == null) {
            instance = new CacheApis();
        }
        return instance;
    }

    public DataCache getByIndex(int index) {
        return mCacheArray.get(index);
    }

    public static DataCache getCategoryCache() {
        return getInstance().getByIndex(Category_CACHE_INDEX);
    }

    public class CategoryCache extends DataCache.DataBaseCache<Category> {
        @Override
        public void saveInternal(final ArrayList<Category> dataArray, Object... params) {
            final DatabaseHelper helper = OpenHelperManager.getHelper(
                StartUpToolsApp.getAppContext(), DatabaseHelper.class);
            try {
                final Dao<Category, String> dao = helper.getCategoryDao();
                dao.callBatchTasks(new Callable<Void>() {

                    public Void call() throws Exception {
                        for (Category item : dataArray) {
                            dao.createOrUpdate(item);
                        }
                        return null;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                OpenHelperManager.releaseHelper();
            }
        }

        @Override
        public ArrayList<Category> readInternal(Object... params) {
            final DatabaseHelper helper = OpenHelperManager.getHelper(StartUpToolsApp.getAppContext(), DatabaseHelper.class);
            List<Category> categories = null;

            try {
                final Dao<Category, String> dao = helper.getCategoryDao();
                categories = dao.query(dao.queryBuilder()
                        .orderBy(Category.FIELD_SORT, true)
                        .where()
                        .eq(Category.FIELD_PARENT_ID, params[0])
                        .prepare());
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                OpenHelperManager.releaseHelper();
            }
            return categories == null ? new ArrayList<Category>(0) : new ArrayList<>(categories);
        }
    }

    public static DataCache getProductCache() {
        return getInstance().getByIndex(Product_CACHE_INDEX);
    }

    public class ProductCache extends DataCache.DataBaseCache<Product> {
        @Override
        public void saveInternal(final ArrayList<Product> dataArray, Object... params) {
            final DatabaseHelper helper = OpenHelperManager.getHelper(
                    StartUpToolsApp.getAppContext(), DatabaseHelper.class);
            try {
                final Dao<Product, String> dao = helper.getProductDao();
                dao.callBatchTasks(new Callable<Void>() {

                    public Void call() throws Exception {
                        for (Product item : dataArray) {
                            dao.createOrUpdate(item);
                        }
                        return null;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                OpenHelperManager.releaseHelper();
            }
        }

        @Override
        public ArrayList<Product> readInternal(Object... params) {
            final DatabaseHelper helper = OpenHelperManager.getHelper(StartUpToolsApp.getAppContext(), DatabaseHelper.class);
            List<Product> products = null;

            try {
                final Dao<Product, String> dao = helper.getProductDao();
                products = dao.query(dao.queryBuilder()
                        .orderBy(Product.FIELD_SORT, true)
                        .where()
                        .eq(Product.FIELD_CATEGORY_ID, params[0])
                        .prepare());
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                OpenHelperManager.releaseHelper();
            }
            return products == null ? new ArrayList<Product>(0) : new ArrayList<>(products);
        }
    }
}

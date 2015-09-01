/**
 * Created by YuGang Yang on September 16, 2014.
 * Copyright 2007-2015 Loopeer.com. All rights reserved.
 */
package com.loopeer.android.apps.startuptools.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.loopeer.android.apps.startuptools.model.Category;
import com.loopeer.android.apps.startuptools.model.Product;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final Class<?>[] DATA_CLASSES = {Category.class, Product.class};

    public static final String DATABASE_NAME = "mobilelogistics.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Category, String> mCategoryDao = null;
    private Dao<Product, String> mProductDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is first created. Usually you should call createTable
     * statements here to create the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            for (Class<?> dataClass : DATA_CLASSES) {
                TableUtils.createTable(connectionSource, dataClass);
            }

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows
     * you to adjust the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion,
                          int newVersion) {
        try {
            for (Class<?> dataClass : DATA_CLASSES) {
                TableUtils.dropTable(connectionSource, dataClass, true);
            }

            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    public Dao<Category, String> getCategoryDao() throws SQLException {
        if (mCategoryDao == null) {
            mCategoryDao = getDao(Category.class);
        }
        return mCategoryDao;
    }

    public Dao<Product, String> getProductDao() throws SQLException {
        if (mProductDao == null) {
            mProductDao = getDao(Product.class);
        }
        return mProductDao;
    }

}

/**
 * Created by YuGang Yang on September 19, 2014.
 * Copyright 2007-2015 Loopeer.com. All rights reserved.
 */
package com.loopeer.android.apps.startuptools.cache;


import android.os.AsyncTask;

import java.util.ArrayList;

public interface DataCache<Data> {

    void save(final ArrayList<Data> result, Object... params);
    public void clear();
    public void read(final Callbacks.RequestCallback<ArrayList<Data>> callback, Object... params);


    public abstract static class DataBaseCache<Data> implements DataCache<Data> {

        @Override
        public void save(final ArrayList<Data> datas, Object... params) {
            if (datas == null || datas.isEmpty()) return;

            AsyncTask<Object, Integer, Integer> task = new AsyncTask<Object, Integer, Integer>() {
                @Override
                protected Integer doInBackground(Object... params) {
                    saveInternal(datas, params);
                    return 0;
                }
            };
            task.execute(params);
        }

        @Override
        public void clear() {

        }

        @Override
        public void read(final Callbacks.RequestCallback<ArrayList<Data>> callback, Object... params) {
            AsyncTask<Object, Integer, ArrayList<Data>> task = new AsyncTask<Object, Integer, ArrayList<Data>>() {
                @Override
                protected ArrayList<Data> doInBackground(Object... params) {
                    return readInternal(params);
                }

                @Override
                protected void onPostExecute(ArrayList<Data> datas) {
                    if (callback != null) {
                        callback.onCacheLoaded(datas);
                    }
                }
            };
            task.execute(params);
        }

        public abstract void saveInternal(ArrayList<Data> dataArray, Object...params);
        public abstract ArrayList<Data> readInternal(Object...params);
    }

}

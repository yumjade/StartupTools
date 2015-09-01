/**
 * Created by YuGang Yang on September 19, 2014.
 * Copyright 2007-2015 Loopeer.com. All rights reserved.
 */
package com.loopeer.android.apps.startuptools.cache;

import com.laputapp.http.Response;

import retrofit.RetrofitError;

public class Callbacks {

    public interface RequestCallback<Data> {
        public void onRequestComplete(Response<Data> result);
        public void onRequestFailure(Response<Data> result);
        public void onRequestFailure(RetrofitError error);
        public void onRequestNetworkError();
        public void onFinish();

        public boolean canCache();
        public void handleLocalCache(Data data);
        public void onCacheLoaded(Data localData);
    }

    public static class ApiBaseCallback<Data> implements RequestCallback<Data> {
        @Override
        public void onRequestComplete(Response<Data> result) {}
        @Override
        public void onRequestFailure(Response<Data> result) {}
        @Override
        public void onRequestFailure(RetrofitError error) {}

        @Override
        public void onRequestNetworkError() {}
        @Override
        public void onFinish() {}

        @Override
        public boolean canCache() {
            return false;
        }
        @Override
        public void handleLocalCache(Data data) {}
        @Override
        public void onCacheLoaded(Data localData) {}
    }

}

package com.loopeer.android.apps.startuptools.api;

import android.app.Application;

import com.loopeer.android.apps.startuptools.R;
import com.loopeer.android.apps.startuptools.StartUpToolsApp;
import com.loopeer.android.apps.startuptools.api.service.ConnectService;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;

import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;

/**
 * Created by tudou on 15-3-20.
 */
public class ApiService  {

    public static final String API_URL = StartUpToolsApp.getAppResources().getString(R.string.api_url);

    private boolean isDebug;
    private RestAdapter restAdapter;
    private ApiHeaders mApiHeaders;

    public ApiService setIsDebug(boolean isDebug) {
        this.isDebug = isDebug;
        if (restAdapter != null) {
            restAdapter.setLogLevel(isDebug ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE);
        }
        return this;
    }

    private ApiHeaders getApiHeaders() {
        if (mApiHeaders == null) {
            mApiHeaders = new ApiHeaders();
        }

        return mApiHeaders;
    }

    private Client getClient() {
        return new OkClient(createOkHttpClient(StartUpToolsApp.getInstance()));
    }

    static final int DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB
    static OkHttpClient createOkHttpClient(Application app) {
        OkHttpClient client = new OkHttpClient();

        // Install an HTTP cache in the application cache directory.
        try {
            File cacheDir = new File(app.getCacheDir(), "http");
            Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);
            client.setCache(cache);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return client;
    }


    protected RestAdapter.Builder newRestAdapterBuilder() {
        return new RestAdapter.Builder();
    }

    protected RestAdapter getRestAdapter() {
        if (restAdapter == null) {
            RestAdapter.Builder builder = newRestAdapterBuilder();
            builder.setClient(getClient());
            builder.setEndpoint(API_URL);
            builder.setRequestInterceptor(getApiHeaders());
            if (isDebug) {
                builder.setLogLevel(RestAdapter.LogLevel.FULL);
            }
            restAdapter = builder.build();
        }

        return restAdapter;
    }

    public ConnectService connectService() {
        return getRestAdapter().create(ConnectService.class);
    }
}

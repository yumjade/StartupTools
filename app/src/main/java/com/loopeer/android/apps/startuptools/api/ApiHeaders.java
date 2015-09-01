package com.loopeer.android.apps.startuptools.api;

import com.loopeer.android.apps.startuptools.StartUpToolsApp;

import retrofit.RequestInterceptor;

/**
 * Created by tudou on 15-3-20.
 */
public class ApiHeaders implements RequestInterceptor {

    private static final String mVersion = StartUpToolsApp.getAppInfo().version;
    private static final String mVersionCode = StartUpToolsApp.getAppInfo().versionCode;
    private static final String mDeviceId = StartUpToolsApp.getAppInfo().deviceId;
    private static final String mChannelId = StartUpToolsApp.getAppInfo().channel;

    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("version-code", mVersionCode);
        request.addHeader("version-name", mVersion);
        request.addHeader("device-id", mDeviceId);
        request.addHeader("channel-id", mChannelId);
        request.addHeader("platform", "android");
    }

}

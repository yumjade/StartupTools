/**
 * Created by YuGang Yang on January 05, 2015.
 * Copyright 2007-2015 Loopeer.com. All rights reserved.
 */
package com.loopeer.android.apps.startuptools.util;

import android.content.Context;

import com.loopeer.android.apps.startuptools.BuildConfig;
import com.loopeer.android.apps.startuptools.StartUpToolsApp;
import com.loopeer.android.apps.startuptools.api.ApiService;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ServiceUtils {

    private static final String IMAGE_CACHE = "offline-cache";

    private static ApiService sApiService;
    private static Picasso sPicasso;
    private static Picasso sExternalPicasso;

    public static synchronized Picasso getPicasso(Context context) {
        if (sPicasso == null) {
            sPicasso =
                    new Picasso.Builder(context).downloader(new LocalOnlyOkHttpDownloader(context)).build();
        }
        return sPicasso;
    }

    /**
     * Returns a {@link com.squareup.picasso.Picasso} instance caching images in a larger external
     * cache directory. This is useful for show posters and episode images which remain valid for
     * long time periods and should be accessible offline.
     *
     * <p> This may return {@code null} if the external cache directory is currently not
     * accessible.
     */
    public static synchronized Picasso getExternalPicasso(Context context) {
        if (sExternalPicasso == null) {
            File externalCacheDir = context.getExternalCacheDir();
            if (externalCacheDir == null) {
                // external storage currently unavailable
                // return null;
                return getPicasso(context);
            }

            File cache = new File(externalCacheDir, IMAGE_CACHE);
            if (!cache.exists()) {
                //noinspection ResultOfMethodCallIgnored
                cache.mkdirs();
            }

            sExternalPicasso =
                    new Picasso.Builder(context).downloader(new LocalOnlyOkHttpDownloader(context, cache))
                            .build();
        }
        return sExternalPicasso;
    }

    public static synchronized Picasso getExternalPicasso() {
        final Context context = StartUpToolsApp.getAppContext();
        if (sExternalPicasso == null) {
            File externalCacheDir = context.getExternalCacheDir();
            if (externalCacheDir == null) {
                // external storage currently unavailable
                // return null;
                return getPicasso(context);
            }

            File cache = new File(externalCacheDir, IMAGE_CACHE);
            if (!cache.exists()) {
                //noinspection ResultOfMethodCallIgnored
                cache.mkdirs();
            }

            sExternalPicasso =
                    new Picasso.Builder(context).downloader(new LocalOnlyOkHttpDownloader(context, cache))
                            .build();
        }
        return sExternalPicasso;
    }

    public static synchronized ApiService getApiService() {
        if (sApiService == null) {
            sApiService = new ApiService();
            sApiService.setIsDebug(BuildConfig.DEBUG);
        }
        return sApiService;
    }
}

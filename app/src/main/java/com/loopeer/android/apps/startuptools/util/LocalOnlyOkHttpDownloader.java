/**
 * Created by YuGang Yang on August 19, 2014.
 * Copyright 2007-2015 Loopeer.com. All rights reserved.
 */
package com.loopeer.android.apps.startuptools.util;

import android.content.Context;
import android.net.Uri;

import com.squareup.picasso.OkHttpDownloader;

import java.io.File;
import java.io.IOException;

/**
 * Custom {@link com.squareup.picasso.OkHttpDownloader} that loads only from local cache if user
 * wishes to conserve mobile data.
 */
public class LocalOnlyOkHttpDownloader extends OkHttpDownloader {

    private final Context mContext;

    public LocalOnlyOkHttpDownloader(Context context) {
        super(context);
        mContext = context.getApplicationContext();
    }

    public LocalOnlyOkHttpDownloader(Context context, File cacheDir) {
        super(cacheDir);
        mContext = context.getApplicationContext();
    }

    @Override
    public Response load(Uri uri, boolean localCacheOnly) throws IOException {
        // TODO
//        if (!Utils.isAllowedLargeDataConnection(mContext, false)) {
//            localCacheOnly = true;
//        }
        return super.load(uri, localCacheOnly);
    }
}

package com.sean.android.example.ui.main.router;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.sean.android.example.base.util.ToastMaker;

/**
 * Created by sean on 2017. 3. 15..
 */

public class GalleryRouterImpl implements GalleryRouter {

    private final Context context;

    public GalleryRouterImpl(Context context) {
        this.context = context;
    }

    @Override
    public void navigateGalleryDetailWeb(String uri) {
        if(context != null && !TextUtils.isEmpty(uri)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uri));
            context.startActivity(intent);
        }
    }
}

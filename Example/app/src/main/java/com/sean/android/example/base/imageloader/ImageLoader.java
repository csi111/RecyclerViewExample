package com.sean.android.example.base.imageloader;

import android.text.TextUtils;
import android.widget.ImageView;

/**
 * Created by Seonil on 2017-03-13.
 */

public class ImageLoader {


    private ImageLoadingListener defaultImageListener;

    private ImageLoader() {
        defaultImageListener = new DefaultImageLoadingListener();
    }

    public static ImageLoader getInstance() {
        return ImageLoaderHolder.instance;
    }

    private static class ImageLoaderHolder {
        public static final ImageLoader instance = new ImageLoader();
    }


    public void loadImage(String uri, ImageView imageView, ImageLoadingListener imageLoadingListener) {
        loadImage(uri, new ImageViewWrapper(imageView), imageLoadingListener);

    }

    public void loadImage(String uri, ImageViewWrapper imageViewWrapper, ImageLoadingListener imageLoadingListener) {
        if (imageLoadingListener == null) {
            imageLoadingListener = defaultImageListener;
        }


        if(TextUtils.isEmpty(uri)) {
            //TODO Cancel Image Load
        }
    }

}

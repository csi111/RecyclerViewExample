package com.sean.android.example.base.imageloader.view;

import android.view.View;

/**
 * Created by Seonil on 2017-03-15.
 */

public interface ViewWrapper<VIEW extends View> {
    VIEW getWrappedView();

    int getWidth();

    int getHeight();

    int getId();

    boolean isGarbageCollected();
}

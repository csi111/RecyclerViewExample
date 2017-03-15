package com.sean.android.example.ui.main.viewmodel;

import android.text.TextUtils;

import com.sean.android.example.domain.GettyImage;

/**
 * Created by sean on 2017. 3. 11..
 */

public class GalleryItemViewModelImpl implements GalleryItemViewModel {

    private final GettyImage gettyImage;

    public GalleryItemViewModelImpl(GettyImage gettyImage) {
        this.gettyImage = gettyImage;
    }

    @Override
    public String getTitle() {
        return gettyImage.getName();
    }

    @Override
    public String getImageUrl() {

        String imagePath = gettyImage.getImagePath();

        if (imagePath == null || imagePath.length() == 0) {
            return "";
        }

        if (imagePath.startsWith("http")) {
            return imagePath;
        }

        StringBuilder stringBuilder = new StringBuilder(gettyImage.getBaseUri());
        stringBuilder.append(imagePath);
        return stringBuilder.toString();
    }

    @Override
    public String getLinkUrl() {
        String linkPath = gettyImage.getLinkPath();

        if (linkPath == null || linkPath.length() == 0) {
            return "";
        }

        if (linkPath.startsWith("http")) {
            return linkPath;
        }

        StringBuilder stringBuilder = new StringBuilder(gettyImage.getBaseUri());
        stringBuilder.append(linkPath);
        return stringBuilder.toString();
    }

    @Override
    public boolean checkVisibleInformation() {
        return !TextUtils.isEmpty(gettyImage.getName());
    }
}

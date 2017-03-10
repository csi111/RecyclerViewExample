package com.sean.android.example.domain;

import com.sean.android.example.base.domain.AppModel;

/**
 * Created by Seonil on 2017-03-10.
 */

public class GettyImage extends AppModel {
    private final String name;
    private final String linkPath;
    private final String imagePath;

    private final String baseUri;

    public GettyImage(String imgPath) {
        this("", "", imgPath);
    }

    public GettyImage(String link, String imgPath) {
        this("", link, imgPath);
    }

    public GettyImage(String name, String link, String imgPath) {
        this("", name, link, imgPath);
    }

    public GettyImage(String baseUri, String name, String link, String imgPath) {
        this.baseUri = baseUri;
        this.name = name;
        this.linkPath = link;
        this.imagePath = imgPath;
    }

    public String getName() {
        return name;
    }

    public String getLinkPath() {
        return linkPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getBaseUri() {
        return baseUri;
    }

    public String getLinkUrl() {
        if (linkPath == null || linkPath.length() == 0) {
            throw new NullPointerException("ImagePath is null!!!");
        }

        if (linkPath.startsWith("http")) {
            return linkPath;
        }

        StringBuilder stringBuilder = new StringBuilder(baseUri);
        stringBuilder.append(linkPath);
        return stringBuilder.toString();
    }

    public String getImageUrl() {
        if (imagePath == null || imagePath.length() == 0) {
            throw new NullPointerException("ImagePath is null!!!");
        }

        if (linkPath.startsWith("http")) {
            return linkPath;
        }

        StringBuilder stringBuilder = new StringBuilder(baseUri);
        stringBuilder.append(linkPath);
        return stringBuilder.toString();
    }
}

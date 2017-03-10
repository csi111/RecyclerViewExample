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


    @Override
    public String toString() {
        return "GettyImage{" +
                "name='" + name + '\'' +
                ", linkPath='" + linkPath + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", baseUri='" + baseUri + '\'' +
                '}';
    }
}

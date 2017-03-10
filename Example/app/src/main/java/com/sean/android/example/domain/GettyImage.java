package com.sean.android.example.domain;

import com.sean.android.example.base.domain.AppModel;

/**
 * Created by Seonil on 2017-03-10.
 */

public class GettyImage extends AppModel{
    private final String name;
    private final String link;
    private final String imageUrl;

    public GettyImage(String imageUrl) {
        this("", "", imageUrl);
    }

    public GettyImage(String link, String imageUrl) {
        this("", link, imageUrl);
    }

    public GettyImage(String name, String link, String imageUrl) {
        this.name = name;
        this.link = link;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

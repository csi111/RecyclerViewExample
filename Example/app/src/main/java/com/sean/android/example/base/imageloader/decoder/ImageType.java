package com.sean.android.example.base.imageloader.decoder;

/**
 * Created by Seonil on 2017-03-15.
 */

public enum ImageType {
    FILE("file://"), HTTP("http://"), HTTPS("https://"), ASSET("assets://"), UNKNOWN("unknown://");

    private String scheme;

    ImageType(String scheme) {
        this.scheme = scheme;
    }

    public String getScheme() {
        return scheme;
    }

    public static String getPathWithoutScheme(String uri) {
        ImageType imageType = findImageType(uri);
        return uri.toLowerCase().replace(imageType.scheme, "");
    }

    public static ImageType findImageType(String uri) {
        for (ImageType imageType : values()) {
            if (uri.toLowerCase().contains(imageType.getScheme())) {
                return imageType;
            }
        }

        return UNKNOWN;
    }
}

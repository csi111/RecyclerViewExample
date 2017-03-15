package com.sean.android.example.base.imageloader.decoder;

/**
 * Created by Seonil on 2017-03-15.
 * Do not used
 */

public class ImageDecoderFactory implements DecoderFactory {
    @Override
    public ImageDecoder newDecoder(String imageUri) {
        ImageType imageType = ImageType.findImageType(imageUri);

        switch (imageType) {
            case FILE:
                return new ImageFileDecoder();
            case HTTP:
            case HTTPS:
                break;
            case ASSET:
                break;
            default:
                break;

        }

        return new DefaultImageDecoder();
    }
}

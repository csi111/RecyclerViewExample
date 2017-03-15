package com.sean.android.example.base.imageloader.decoder;

/**
 * Created by Seonil on 2017-03-15.
 */

public interface DecoderFactory {

    ImageDecoder newDecoder(String imageUri);

}

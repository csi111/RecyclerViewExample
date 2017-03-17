package com.sean.android.example.base.protocol.data;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Seonil on 2017-03-09.
 */

public interface Input {

    String getContentType();

    InputStream in() throws IOException;

    long length();

}

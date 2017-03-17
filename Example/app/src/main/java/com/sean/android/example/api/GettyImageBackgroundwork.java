package com.sean.android.example.api;

import com.sean.android.example.base.asynctask.HttpBackgroundResult;
import com.sean.android.example.base.asynctask.HttpBackgroundWork;
import com.sean.android.example.base.asynctask.HttpUrlPath;
import com.sean.android.example.base.protocol.HttpRequest;
import com.sean.android.example.base.protocol.data.ResponseData;
import com.sean.android.example.base.util.Logger;
import com.sean.android.example.domain.GettyImageParser;
import com.sean.android.example.domain.GettyImages;

import java.io.IOException;

/**
 * Created by Seonil on 2017-03-10.
 */

@HttpUrlPath(host = "http://www.gettyimagesgallery.com", path = "/collections/archive/slim-aarons.aspx")
public class GettyImageBackgroundwork extends HttpBackgroundWork<GettyImages> {

    @Override
    protected HttpBackgroundResult<GettyImages> onCompleteHttpTransation(HttpRequest httpRequest, ResponseData responseData) {
        HttpBackgroundResult<GettyImages> httpBackgroundResult = new HttpBackgroundResult<>(responseData);


        GettyImages gettyImages = new GettyImages();

        long startParsingTimeMills = System.currentTimeMillis();

        try {
            GettyImageParser gettyImageParser = new GettyImageParser();
            gettyImageParser.parse(getHost(), responseData.getResponseBody().in(), gettyImages);

            long endParsingTimeMills = System.currentTimeMillis();
            Logger.d(this, "the end parsingTime = [" + endParsingTimeMills + "] cost Time = [" + (endParsingTimeMills - startParsingTimeMills) + "]");
            httpBackgroundResult.setData(gettyImages);

            return httpBackgroundResult;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.onCompleteHttpTransation(httpRequest, responseData);
    }
}

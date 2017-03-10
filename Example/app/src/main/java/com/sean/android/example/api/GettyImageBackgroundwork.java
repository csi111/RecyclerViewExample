package com.sean.android.example.api;

import com.sean.android.example.base.asynctask.HttpBackgroundResult;
import com.sean.android.example.base.asynctask.HttpBackgroundWork;
import com.sean.android.example.base.asynctask.HttpUrlPath;
import com.sean.android.example.base.protocol.ConnectException;
import com.sean.android.example.base.protocol.HttpRequest;
import com.sean.android.example.base.protocol.ResponseData;
import com.sean.android.example.base.util.Logger;
import com.sean.android.example.domain.GettyImages;

import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Seonil on 2017-03-10.
 */

@HttpUrlPath(host = "http://www.gettyimagesgallery.com", path = "/collections/archive/slim-aarons.aspx")
public class GettyImageBackgroundwork extends HttpBackgroundWork<GettyImages> {


    @Override
    protected HttpBackgroundResult<GettyImages> onCompleteHttpTransation(HttpRequest httpRequest, ResponseData responseData) {
        HttpBackgroundResult<GettyImages> httpBackgroundResult = new HttpBackgroundResult<>(responseData);

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(responseData.getResponseBody().in()));

            StringBuilder stringBuilder = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
                Logger.d(this, inputLine);
            }


//          httpBackgroundResult.setData(stringBuilder.toString());

            return httpBackgroundResult;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.onCompleteHttpTransation(httpRequest, responseData);
    }
}

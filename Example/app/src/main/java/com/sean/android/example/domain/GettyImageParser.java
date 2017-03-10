package com.sean.android.example.domain;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by Seonil on 2017-03-10.
 */

public class GettyImageParser {
    private final String htmlString;
    private Document document;


    public GettyImageParser(String htmlString) {
        this.htmlString = htmlString;
    }


    public void parse() {
        document = Jsoup.parse(htmlString);
    }


}

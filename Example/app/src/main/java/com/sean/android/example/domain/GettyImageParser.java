package com.sean.android.example.domain;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Seonil on 2017-03-10.
 */

public class GettyImageParser {

    private final String ELEMENT_DIV_NAME_GALLERY_ITEM_GROUP = "gallery-item-group exitemrepeater";
    private final String ELEMENT_SELECT_CAPTION = "div.gallery-item-caption p a";
    private final String ELEMENT_CLASS_NAME_PICTURE = "picture";
    private final String ELEMENT_ATTRIBUTE_NAME_PICTURE = "src";
    private final String ELEMENT_ATTRIBUTE_NAME_LINK = "href";

    private Document document;

    public GettyImageParser() {

    }


    public void parse(String baseUri, InputStream in, GettyImages gettyImages) {
        try {
            document = Jsoup.parse(in, "UTF-8", baseUri);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Elements galleryGroups = document.getElementsByClass(ELEMENT_DIV_NAME_GALLERY_ITEM_GROUP);
        for (Element element : galleryGroups) {
            String srcPath = element.getElementsByClass(ELEMENT_CLASS_NAME_PICTURE).attr(ELEMENT_ATTRIBUTE_NAME_PICTURE);
            String linkUrl = element.select(ELEMENT_SELECT_CAPTION).attr(ELEMENT_ATTRIBUTE_NAME_LINK);
            String caption = element.select(ELEMENT_SELECT_CAPTION).text();
            gettyImages.add(new GettyImage(baseUri, caption, linkUrl, srcPath));

        }

    }


}

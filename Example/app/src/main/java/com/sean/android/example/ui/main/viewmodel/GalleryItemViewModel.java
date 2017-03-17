package com.sean.android.example.ui.main.viewmodel;

/**
 * Created by sean on 2017. 3. 11..
 */

public interface GalleryItemViewModel extends ViewModel{

    String getTitle();

    String getImageUrl();

    String getLinkUrl();

    boolean checkVisibleInformation();
}

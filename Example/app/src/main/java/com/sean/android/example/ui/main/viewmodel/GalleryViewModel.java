package com.sean.android.example.ui.main.viewmodel;

import java.util.Collection;

/**
 * Created by sean on 2017. 3. 11..
 */

public interface GalleryViewModel {

    int itemCount();

    void navigateGalleryDetail(int position);

    GalleryItemViewModel get(int position);

    void add(GalleryItemViewModel viewModel);

    void addAll(Collection<GalleryItemViewModel> viewModels);

    void setNotification(Notification notification);

    void changeRecyclerViewMode();

    int displayGalleryModeMenuIcon();

    interface Notification {
        void onNotifyItemInserted(int startPosition, int size);

        void onNotifyItemInserted(int position);

        void onNotifyItemRemoved(int position);

        void onChangedLayoutType(GalleryViewType galleryViewType);

    }
}

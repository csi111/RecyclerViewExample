package com.sean.android.example.ui.main.viewmodel;

import com.sean.android.example.ui.main.router.GalleryRouter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by sean on 2017. 3. 15..
 */

public class GalleryViewModelImpl implements GalleryViewModel {
    private List<GalleryItemViewModel> galleryItemViewModels;

    private final GalleryRouter router;

    private Notification notification;

    public GalleryViewModelImpl(GalleryRouter galleryRouter) {
        this.galleryItemViewModels = new ArrayList<>();
        this.router = galleryRouter;
    }

    @Override
    public int itemCount() {
        return galleryItemViewModels.size();
    }

    @Override
    public void navigateGalleryDetail(int position) {
        router.navigateGalleryDetailWeb(galleryItemViewModels.get(position).getLinkUrl());
    }

    @Override
    public GalleryItemViewModel get(int position) {
        return galleryItemViewModels.get(position);
    }

    @Override
    public void add(GalleryItemViewModel galleryItemViewModel) {
        galleryItemViewModels.add(galleryItemViewModel);
        if(notification != null) {
            notification.onNotifyItemInserted(galleryItemViewModels.indexOf(galleryItemViewModel));
        }
    }

    @Override
    public void addAll(Collection<GalleryItemViewModel> galleryItemViewModels) {
        int currentCount = this.galleryItemViewModels.size();
        this.galleryItemViewModels.addAll(galleryItemViewModels);
        if(notification != null) {
            notification.onNotifyItemInserted(currentCount, this.galleryItemViewModels.size());
        }
    }

    @Override
    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}

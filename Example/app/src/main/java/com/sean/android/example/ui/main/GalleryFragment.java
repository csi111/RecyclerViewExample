package com.sean.android.example.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sean.android.example.R;
import com.sean.android.example.base.imageloader.ImageLoader;
import com.sean.android.example.ui.main.viewmodel.GalleryViewModel;
import com.sean.android.example.ui.main.viewmodel.GalleryViewType;
import com.sean.android.example.ui.main.viewmodel.ViewBinder;

/**
 * A placeholder fragment containing a simple view.
 * Thumnail Image Load
 * http://www.gettyimagesgallery.com/collections/archive/slim-aarons.aspx
 */
public class GalleryFragment extends Fragment implements ViewBinder<GalleryViewModel>, GalleryViewModel.Notification {
    private static final int SPAN_COUNT = 5;

    private GalleryViewModel galleryViewModel;

    private RecyclerView recyclerView;

    public GalleryFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.gallery_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL));
    }

    @Override
    public void onBind(GalleryViewModel galleryViewModel) {
        if (this.galleryViewModel == null) {
            galleryViewModel.setNotification(this);
            this.galleryViewModel = galleryViewModel;
        }

        changeLayoutManager(this.galleryViewModel.getGalleryViewType());
        recyclerView.setAdapter(new GalleryAdapter(this.galleryViewModel));
    }

    private void changeLayoutManager(GalleryViewType viewType) {
        switch (viewType) {
            case GRID:
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
                break;
            case LIST:
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onNotifyItemInserted(int startPosition, int size) {
        recyclerView.getAdapter().notifyItemRangeChanged(startPosition, size);
    }

    @Override
    public void onNotifyItemInserted(int position) {
        recyclerView.getAdapter().notifyItemInserted(position);
    }

    @Override
    public void onNotifyItemRemoved(int position) {
        recyclerView.getAdapter().notifyItemRemoved(position);
    }
}

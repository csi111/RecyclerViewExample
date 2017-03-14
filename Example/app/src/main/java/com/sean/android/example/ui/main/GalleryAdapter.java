package com.sean.android.example.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sean.android.example.R;
import com.sean.android.example.base.view.OnItemClickListener;
import com.sean.android.example.ui.main.viewmodel.GalleryViewModel;

/**
 * Created by sean on 2017. 3. 9..
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder> implements OnItemClickListener {

    private GalleryViewModel galleryViewModel;

    public GalleryAdapter(GalleryViewModel galleryViewModel) {
        this.galleryViewModel = galleryViewModel;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GalleryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_gallery, null), this);
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        if (holder == null) {
            return;
        }

        holder.onBind(galleryViewModel.get(position));
    }


    @Override
    public int getItemCount() {
        return galleryViewModel.itemCount();
    }


    @Override
    public void onClickItem(int position) {
        galleryViewModel.navigateGalleryDetail(position);
    }
}

package com.sean.android.example.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sean.android.example.R;
import com.sean.android.example.base.view.OnItemClickListener;
import com.sean.android.example.ui.main.viewmodel.AdapterViewModel;
import com.sean.android.example.ui.main.viewmodel.GalleryItemViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by sean on 2017. 3. 9..
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder> implements OnItemClickListener, AdapterViewModel<GalleryItemViewModel> {

    private List<GalleryItemViewModel> galleryItemViewModels;

    public GalleryAdapter() {
        this(new ArrayList<GalleryItemViewModel>());
    }

    public GalleryAdapter(List<GalleryItemViewModel> galleryItemViewModels) {
        this.galleryItemViewModels = galleryItemViewModels;
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

        holder.onBind(galleryItemViewModels.get(position));
    }


    @Override
    public int getItemCount() {
        return galleryItemViewModels.size();
    }


    @Override
    public void onClickItem(int position) {

    }

    @Override
    public void add(GalleryItemViewModel galleryItemViewModel) {
        int currentItemCount = getItemCount();
        this.galleryItemViewModels.add(galleryItemViewModel);
        notifyItemInserted(currentItemCount + 1);
    }

    @Override
    public void addAll(Collection<GalleryItemViewModel> galleryItemViewModels) {
        int currentItemCount = getItemCount();
        this.galleryItemViewModels.addAll(galleryItemViewModels);
//        notifyItemRangeInserted(currentItemCount, galleryItemViewModels.size());
        notifyItemInserted(0);
    }
}

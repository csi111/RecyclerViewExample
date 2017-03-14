package com.sean.android.example.ui.main;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sean.android.example.R;
import com.sean.android.example.base.imageloader.ImageLoader;
import com.sean.android.example.base.imageloader.ImageLoadingListener;
import com.sean.android.example.base.util.Logger;
import com.sean.android.example.base.view.OnItemClickListener;
import com.sean.android.example.ui.main.viewmodel.GalleryItemViewModel;
import com.sean.android.example.ui.main.viewmodel.ViewBinder;

/**
 * Created by sean on 2017. 3. 11..
 */

public class GalleryViewHolder extends RecyclerView.ViewHolder implements ViewBinder<GalleryItemViewModel> {


    private OnItemClickListener onItemClickListener;

    private TextView titleTextView;
    private ImageView galleryImageView;

    public GalleryViewHolder(View itemView, OnItemClickListener onItemClickListener) {
        super(itemView);

        titleTextView = (TextView) itemView.findViewById(R.id.item_title_textView);
        galleryImageView = (ImageView) itemView.findViewById(R.id.item_imageView);

        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBind(final GalleryItemViewModel galleryItemViewModel) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION)
                    onItemClickListener.onClickItem(getAdapterPosition());
            }
        });

        titleTextView.setText(galleryItemViewModel.getTitle());
        ImageLoader.getInstance().loadImage(galleryItemViewModel.getImageUrl(), galleryImageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri) {
                Logger.d(this, "Image Loading Started uri =[" + imageUri + "]");
            }

            @Override
            public void onLoadingComplete(String imageUri, ImageView imageView, Bitmap loadedImage) {
                Logger.d(this, "Image Loading Complete uri =[" + imageUri + "]");
            }

            @Override
            public void onLoadingFailed(String imageUri, ImageView imageView, Throwable cause) {
                Logger.d(this, "Image Loading Failed uri =[" + imageUri + "]");
            }

            @Override
            public void onLoadingCancelled(String imageUri, ImageView imageView) {
                Logger.d(this, "Image Loading Cancelled uri =[" + imageUri + "]");
            }
        });
    }
}

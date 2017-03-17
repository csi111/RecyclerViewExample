package com.sean.android.example.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sean.android.example.R;
import com.sean.android.example.base.imageloader.ImageLoader;
import com.sean.android.example.base.view.OnItemClickListener;
import com.sean.android.example.ui.main.viewmodel.GalleryItemViewModel;
import com.sean.android.example.ui.main.viewmodel.ViewBinder;
import com.sean.android.example.ui.main.viewmodel.ViewModel;

/**
 * Created by sean on 2017. 3. 11..
 */

public class GalleryViewHolder extends RecyclerView.ViewHolder implements ViewBinder {


    private OnItemClickListener onItemClickListener;

    private TextView titleTextView;
    private ImageView galleryImageView;
    private LinearLayout informationView;

    public GalleryViewHolder(View itemView, OnItemClickListener onItemClickListener) {
        super(itemView);

        titleTextView = (TextView) itemView.findViewById(R.id.item_title_textView);
        galleryImageView = (ImageView) itemView.findViewById(R.id.item_imageView);
        informationView = (LinearLayout) itemView.findViewById(R.id.informationView);

        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBind(ViewModel viewModel) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION)
                    onItemClickListener.onClickItem(getAdapterPosition());
            }
        });

        if (viewModel != null && viewModel instanceof GalleryItemViewModel) {
            GalleryItemViewModel galleryItemViewModel = (GalleryItemViewModel) viewModel;
            titleTextView.setText(galleryItemViewModel.getTitle());
            informationView.setVisibility(galleryItemViewModel.checkVisibleInformation() ? View.VISIBLE : View.INVISIBLE);
            ImageLoader.getInstance().loadImage(galleryItemViewModel.getImageUrl(), galleryImageView);
        }


    }
}

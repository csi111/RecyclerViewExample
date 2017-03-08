package com.sean.android.example.ui.main;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sean.android.example.R;

/**
 * A placeholder fragment containing a simple view.
 * Thumnail Image Load
 * http://www.gettyimagesgallery.com/collections/archive/slim-aarons.aspx
 *
 */
public class GalleryFragment extends Fragment {

    private RecyclerView mRecyclerView;

    public GalleryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.gallery_recyclerview);

    }
}

package com.sean.android.example.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sean.android.example.R;
import com.sean.android.example.base.protocol.HttpRequest;
import com.sean.android.example.base.protocol.UrlConnectionClient;
import com.sean.android.example.ui.main.viewmodel.GalleryItemViewModel;
import com.sean.android.example.ui.main.viewmodel.ViewBinder;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 * Thumnail Image Load
 * http://www.gettyimagesgallery.com/collections/archive/slim-aarons.aspx
 */
public class GalleryFragment extends Fragment implements ViewBinder<List<GalleryItemViewModel>> {

    private RecyclerView recyclerView;

    private GalleryAdapter galleryAdapter;

    public GalleryFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        galleryAdapter = new GalleryAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.gallery_recyclerview);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        linearLayoutManager.generateLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        recyclerView.setAdapter(galleryAdapter);
    }

    @Override
    public void onBind(List<GalleryItemViewModel> galleryItemViewModels) {
        if (isVisible()) {
            galleryAdapter.addAll(galleryItemViewModels);
        }
    }
}

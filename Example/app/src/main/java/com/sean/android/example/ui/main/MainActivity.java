package com.sean.android.example.ui.main;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.sean.android.example.R;
import com.sean.android.example.api.GettyImageBackgroundwork;
import com.sean.android.example.base.activity.BaseActivity;
import com.sean.android.example.base.asynctask.BackgroundWorker;
import com.sean.android.example.base.asynctask.HttpBackgroundResult;
import com.sean.android.example.base.imageloader.ImageLoader;
import com.sean.android.example.base.util.Logger;
import com.sean.android.example.domain.GettyImage;
import com.sean.android.example.domain.GettyImages;
import com.sean.android.example.ui.main.router.GalleryRouter;
import com.sean.android.example.ui.main.router.GalleryRouterImpl;
import com.sean.android.example.ui.main.viewmodel.GalleryItemViewModel;
import com.sean.android.example.ui.main.viewmodel.GalleryItemViewModelImpl;
import com.sean.android.example.ui.main.viewmodel.GalleryViewModel;
import com.sean.android.example.ui.main.viewmodel.GalleryViewModelImpl;
import com.sean.android.example.ui.main.viewmodel.GalleryViewType;
import com.sean.android.example.ui.main.viewmodel.ViewBinder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final int TRANSACTION_ID_GET_GETTYIMAGE = 1000;

    private GalleryRouter galleryRouter;

    private GalleryViewModel galleryViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        galleryRouter = new GalleryRouterImpl(this);
        galleryViewModel = new GalleryViewModelImpl(galleryRouter);
        bindViewFragment(R.id.fragment, galleryViewModel);
        executeBackgroundWork(TRANSACTION_ID_GET_GETTYIMAGE, new GettyImageBackgroundwork());
    }

    @Override
    protected void onBackgroundWorkComplete(int transactionId, List<BackgroundWorker.BackgroundWorkResult> results) {
        super.onBackgroundWorkComplete(transactionId, results);
        if (transactionId == TRANSACTION_ID_GET_GETTYIMAGE) {
            HttpBackgroundResult<GettyImages> httpBackgroundResult = (HttpBackgroundResult) results.get(0).getResult();
            GettyImages gettyImages = httpBackgroundResult.getData();
            Logger.d(this, "GettyImage count = [" + gettyImages.count() + "], [" + gettyImages.toString() + "]");

            List<GalleryItemViewModel> viewModels = new ArrayList<>();

            for (GettyImage gettyImage : gettyImages.getModels()) {
                viewModels.add(new GalleryItemViewModelImpl(gettyImage));
            }

            galleryViewModel.addAll(viewModels);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_layout_type);
        menuItem.setIcon(getMenuIconRes(galleryViewModel.getGalleryViewType()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_layout_type) {
            galleryViewModel.changeRecyclerViewMode();
            item.setIcon(getMenuIconRes(galleryViewModel.getGalleryViewType()));
            bindViewFragment(R.id.fragment, galleryViewModel);
            return true;
        } else if(id == R.id.action_refresh) {
            ImageLoader.getInstance().clearCache();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void bindViewFragment(int id, Object param) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(id);
        if (fragment != null) {
            if (ViewBinder.class.isAssignableFrom(fragment.getClass())) {
                ViewBinder viewBinder = (ViewBinder) fragment;
                viewBinder.onBind(param);
            }
        }
    }

    private int getMenuIconRes(GalleryViewType galleryViewType) {
        switch (galleryViewType) {
            case GRID:
                return R.drawable.ic_action_grid;
            case LIST:
                return R.drawable.ic_action_list;
            default:
                return R.drawable.ic_action_grid;
        }
    }
}



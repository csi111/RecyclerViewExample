package com.sean.android.example.ui.main.viewmodel;

import java.util.Collection;

/**
 * Created by sean on 2017. 3. 11..
 */

public interface AdapterViewModel<ViewModel> {

    void add(ViewModel viewModel);

    void addAll(Collection<ViewModel> viewModels);

}

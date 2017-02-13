package com.instainsight;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.instainsight.viewmodels.IViewModel;


/**
 * For all Activities that have ViewModels
 */
public abstract class ViewModelActivity extends BaseActivity {
    protected IViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create the ViewModel
        createViewModel();

        // run the viewModel methods
        mViewModel.onCreate(new Handler(Looper.getMainLooper()));
    }

    abstract protected void createViewModel();

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mViewModel.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.onDestroy();
    }
}

package com.instainsight.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.instainsight.BaseFragment;
import com.instainsight.R;

/**
 * Created by SONY on 17-12-2016.
 */

public class PaidProfileFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frgmnt_profilepaid, container, false);
    }
}
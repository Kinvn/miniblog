package com.kinvn.miniblog.module.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kinvn.miniblog.R;
import com.kinvn.miniblog.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by Kinvn on 2017/9/6.
 */

public class MessageFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, v);
        return v;
    }
}

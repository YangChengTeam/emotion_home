package com.yc.emotion.home.base.ui.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yc.emotion.home.utils.WeakHandler;

import org.jetbrains.annotations.NotNull;

/**
 * Created by mayn on 2019/4/23.
 */

public abstract class BaseFragment extends Fragment  {

    private String fragmentTag = "";
//    public String LOG_TAG_FRAGMENT_NAME;


//    protected WeakHandler mHandler ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        LOG_TAG_FRAGMENT_NAME = getClass().getName() + " ";
        Log.d("ClassName", "onCreate: ------Fragment :" + getClass().getName());
//        mHandler= new WeakHandler(this);
        return onFragmentCreateView(inflater, container, savedInstanceState);
    }

    protected abstract View onFragmentCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public String getFragmentTag() {
        return fragmentTag;
    }

    public void setFragmentTag(String fragmentTag) {
        this.fragmentTag = fragmentTag;
    }



}
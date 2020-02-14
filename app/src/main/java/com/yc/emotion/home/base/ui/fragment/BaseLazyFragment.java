package com.yc.emotion.home.base.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yc.emotion.home.base.presenter.BasePresenter;
import com.yc.emotion.home.base.view.IView;

/**
 * Created by mayn on 2019/4/24.
 */

public abstract class BaseLazyFragment<P extends BasePresenter> extends BaseFragment implements IView {

    protected View rootView;
    private boolean isInitView = false;
    private boolean isVisible = false;
    protected P mPresenter;

//    protected BasePresenter mPresenter;


    @Override
    protected View onFragmentCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutId(), container, false);

        return rootView;
    }

    protected void initBundle() {

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initBundle();
        initViews();
        isInitView = true;
        isCanLoadData();

        if (null != mPresenter) {
            mPresenter.subscribe();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见，获取该标志记录下来
        if (isVisibleToUser) {
            isVisible = true;
            isCanLoadData();
        } else {
            isVisible = false;
        }
    }

    private void isCanLoadData() {
        //所以条件是view初始化完成并且对用户可见
        if (isInitView && isVisible) {
            lazyLoad();

            //防止重复加载数据
            isInitView = false;
            isVisible = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mPresenter) {
            mPresenter.unSubscribe();
        }
    }

    /**
     * 加载要显示的数据
     */
    protected abstract void lazyLoad();


}

package com.yc.emotion.home.mine.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.base.ui.fragment.BaseFragment

/**
 *
 * Created by suns  on 2020/4/23 11:34.
 */
abstract class BaseNavFragment<P : BasePresenter<*, *>?> : BaseFragment<P>() {

    private var isNavigationViewInit = false//记录是否已经初始化过一次视图
    private var lastView: View? = null//记录上次创建的view

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (lastView == null) {
            lastView = super.onCreateView(inflater, container, savedInstanceState)
        }
        return lastView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (!isNavigationViewInit) {//初始化过视图则不再进行view和data初始化
            super.onViewCreated(view, savedInstanceState)
            isNavigationViewInit = true
        }
    }
}
package com.yc.emotion.home.base.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.mine.ui.activity.CollectActivity

/**
 * Created by mayn on 2019/5/5.
 */
abstract class BaseCollectFragment : BaseFragment<BasePresenter<*, *>>() {
    protected lateinit var mCollectActivity: CollectActivity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mCollectActivity = activity as CollectActivity
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}
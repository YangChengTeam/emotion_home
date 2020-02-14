package com.yc.emotion.home.index.ui.fragment

import android.os.Bundle
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.base.ui.fragment.BaseLazyFragment
import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.index.ui.activity.EmotionSearchActivity

/**
 *
 * Created by suns  on 2019/10/28 14:18.
 */
abstract class BaseEmotionSearchFragment : BaseLazyFragment<BasePresenter<IModel, IView>>() {
    var mMainActivity: EmotionSearchActivity? = null


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            mMainActivity = activity as EmotionSearchActivity?
        }

    }
}
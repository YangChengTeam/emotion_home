package com.yc.emotion.home.mine.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.base.ui.fragment.BaseLazyFragment
import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.mine.ui.activity.RegisterMainActivity


/**
 *
 * Created by suns  on 2019/10/21 11:40.
 */
abstract class BaseRegisterPhoneFragment : BaseLazyFragment<BasePresenter<IModel, IView>>() {

    var mMainActivity: RegisterMainActivity? = null


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            mMainActivity = activity as RegisterMainActivity?
        }

    }

}
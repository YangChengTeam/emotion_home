package com.yc.emotion.home.mine.ui.fragment

import android.os.Bundle
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.mine.ui.activity.RegisterMainActivityNew


/**
 *
 * Created by suns  on 2019/10/21 11:40.
 */
abstract class BaseRegisterPhoneFragment : BaseNavFragment<BasePresenter<IModel, IView>>() {

    var mMainActivity: RegisterMainActivityNew? = null


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            mMainActivity = activity as RegisterMainActivityNew?
        }

    }

}
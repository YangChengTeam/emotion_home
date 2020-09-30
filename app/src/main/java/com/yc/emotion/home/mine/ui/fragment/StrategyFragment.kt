package com.yc.emotion.home.mine.ui.fragment

import com.yc.emotion.home.R
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.base.ui.fragment.common.ShareAppFragment
import com.yc.emotion.home.utils.clickWithTrigger
import kotlinx.android.synthetic.main.fragment_strategy.*

/**
 *
 * Created by suns  on 2020/8/28 20:14.
 */
class StrategyFragment : BaseFragment<BasePresenter<*, *>>() {


    override fun getLayoutId(): Int {
        return R.layout.fragment_strategy
    }


    override fun lazyLoad() {

    }

    override fun initViews() {
        tv_share_link.clickWithTrigger {
            activity?.let {
                val shareAppFragment = ShareAppFragment()
                shareAppFragment.show(it.supportFragmentManager, "")
            }

        }
    }
}
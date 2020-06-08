package com.yc.emotion.home.index.ui.fragment

import android.webkit.WebSettings
import com.yc.emotion.home.R
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.base.view.IView
import kotlinx.android.synthetic.main.fragment_tutor_detail_desc.*


/**
 *
 * Created by suns  on 2019/10/12 13:42.
 */
class TutorDetailDescFragment : BaseFragment<BasePresenter<IModel,IView>>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_tutor_detail_desc
    }

    override fun initViews() {

        val content = arguments?.getString("content")


        val settings = webview_tutor_desc.settings

        settings.loadWithOverviewMode = true//设置WebView是否使用预览模式加载界面。
        webview_tutor_desc.isVerticalScrollBarEnabled = false//不能垂直滑动
        webview_tutor_desc.isHorizontalScrollBarEnabled = false//不能水平滑动

        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN//支持内容重新布局
        val data = (activity as BaseActivity).formatting(content)

        webview_tutor_desc.loadDataWithBaseURL(null, data, "text/html", "utf-8", null)


    }

    override fun lazyLoad() {

    }
}
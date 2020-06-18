package com.yc.emotion.home.index.ui.fragment

import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.yc.emotion.home.R
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.base.view.IView
import kotlinx.android.synthetic.main.fragment_tutor_service_detail_desc.*

/**
 *
 * Created by suns  on 2019/10/14 09:32.
 */
class TutorServiceDetailDescFragment : BaseFragment<BasePresenter<IModel, IView>>() {


    override fun getLayoutId(): Int {
        return R.layout.fragment_tutor_service_detail_desc
    }


    override fun initViews() {
        tv_buy_intro.text = String.format(getString(R.string.buy_course_intro), getString(R.string.app_name))

        val content = arguments?.getString("content")


        val settings = webView.settings


//        settings.textZoom = 200;//通过设置WebSettings，改变HTML中文字的大小

        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN//支持内容重新布局


//        webView.loadUrl("http://www.baidu.com")

        webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null)


        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                //                webView.loadUrl("javascript:window.android.resize(document.body.getBoundingClientRect().height)");
//                webView.loadUrl("javascript:window.APP.resize(document.body.getScrollHeight())")
            }
        }


    }

    override fun lazyLoad() {

    }


}
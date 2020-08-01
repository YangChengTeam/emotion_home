package com.yc.emotion.home.index.ui.fragment

import android.webkit.WebSettings
import com.yc.emotion.home.R
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.utils.UIUtils
import kotlinx.android.synthetic.main.fragment_tutor_detail_desc.*


/**
 *
 * Created by suns  on 2019/10/12 13:42.
 */
class TutorDetailDescFragment : BaseFragment<BasePresenter<IModel, IView>>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_tutor_detail_desc
    }

    override fun initViews() {

        tv_tutor_intro.text = String.format(getString(R.string.tutor_intro), UIUtils.getAppName(activity))

        val content = arguments?.getString("content")


        val settings = webview_tutor_desc.settings

        settings.loadWithOverviewMode = true//设置WebView是否使用预览模式加载界面。
        webview_tutor_desc.isVerticalScrollBarEnabled = false//不能垂直滑动
        webview_tutor_desc.isHorizontalScrollBarEnabled = false//不能水平滑动

        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN//支持内容重新布局
        val data = formatting(content)
//
        webview_tutor_desc.loadDataWithBaseURL(null, data, "text/html", "utf-8", null)


    }

    fun formatting(data: String?): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("<html>")
//        stringBuilder.append("<html><head><meta charset=\"utf-8\" /><meta content=\"yes\" name=\"apple-mobile-web-app-capable\" /><meta content=\"yes\" name=\"apple-touch-fullscreen\" /><meta content=\"telephone=no,email=no\" name=\"format-detection\" /><meta name=\"App-Config\"  content=\"fullscreen=yes,useHistoryState=yes,transition=yes\" /><meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no\" /><style> html,body{overflow:hidden; font-size:16px; line-height: 1.6;} img { width:100%; height:auto; overflow:hidden;}</style></head><body><section><section><section><section><section><section><section><section><span style=\"font-size: 15px; letter-spacing: 1px;\">~</span><br/></section></section></section></section><p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px; max-width: 100%; clear: both; min-height: 1em; line-height: 2em; letter-spacing: 1px; box-sizing: border-box !important; overflow-wrap: break-word !important;\"><br/></p></section></section></section></section><p><section><section><section><section><section><section><section><section><section><section><section><section><section><section><section><img class=\"\" src=\"https://mmbiz.qpic.cn/mmbiz_png/DOVVfocNpPabu4FcFibxsfAy2iaDs4uWS9K1MDOzXYoqVWdfBZrDETLPCQdLXsteAIfbCfhGfAbXWVuUhn8AaGvA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1\"/></section></section></section><section><section><section><img class=\"\" src=\"https://mmbiz.qpic.cn/mmbiz_png/DOVVfocNpPabu4FcFibxsfAy2iaDs4uWS9z8KQTY7718czYyiazZW8OxUu5jBsFZ8PH05DNyFVU8bAy0IBKqd0dLg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1\"/></section></section></section><section><section><section><section><br/></section></section></section></section><section><section><section><img class=\"\" src=\"https://mmbiz.qpic.cn/mmbiz_png/DOVVfocNpPabu4FcFibxsfAy2iaDs4uWS95uBnTz0k1c6o52UKce02ibXsURmPUsXQfdeviazxQiaiaovaS1jfdw6WJg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1\"/></section></section></section><section><section><section><section><br/></section></section></section></section><section><section><section><img class=\"\" src=\"https://mmbiz.qpic.cn/mmbiz_png/DOVVfocNpPabu4FcFibxsfAy2iaDs4uWS9lTauBkk1adqLm5VeQuZkh5jJ8O6WvmkvTDqh5S3h1qcN4abxuFG5Kw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1\"/></section></section></section><section><section><section><section><br/></section></section></section></section><section><section><section><img class=\"\" src=\"https://mmbiz.qpic.cn/mmbiz_png/DOVVfocNpPabu4FcFibxsfAy2iaDs4uWS9adVHa1WN6hLnDs3eiaBaokNBOwoa3PnNKZxbwC7sTRQSUMkO4OYr8Pg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1\"/></section></section></section><section><section><section><section><br/></section></section></section></section><section><section><section><img class=\"\" src=\"https://mmbiz.qpic.cn/mmbiz_png/DOVVfocNpPabu4FcFibxsfAy2iaDs4uWS9fkmRnYibJ0liclsnUC1sCekXVIVUBukabIJT1jqpahHia2Rg6pImVB3lQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1\"/></section></section></section><section><section><section><section><br/></section></section></section></section><section><section><section><img class=\"\" src=\"https://mmbiz.qpic.cn/mmbiz_png/DOVVfocNpPabu4FcFibxsfAy2iaDs4uWS9HhrofNKMw5U1CjDJgVia9zIYx7Abw6S6ic2s0kgBUMsgjicDCykmDhVAA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1\"/></section></section></section><section><section><section><section><br/></section></section></section></section><section><section><section><img class=\"\" src=\"https://mmbiz.qpic.cn/mmbiz_png/DOVVfocNpPabu4FcFibxsfAy2iaDs4uWS9rcLdVeOCcR7R6NQhexrkjkgibPGcTMcR27mG6om4f5PEoDQl6Nz2bJQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1\"/></section></section></section><section><section><section><section><br/></section></section></section></section><section><section><section><img class=\"\" src=\"https://mmbiz.qpic.cn/mmbiz_png/DOVVfocNpPabu4FcFibxsfAy2iaDs4uWS9wXERK44n92XxDT8Fz91RYVuss1VKaB8qflESBDMKpvPehlfZUiaoUfw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1\"/></section></section></section><section><section><section><section><br/></section></section></section></section><section><section><section><img class=\"\" src=\"https://mmbiz.qpic.cn/mmbiz_png/DOVVfocNpPabu4FcFibxsfAy2iaDs4uWS94FsDsfjibXDjMl3uicBgYbiaicS5JgcbCg7Q3uDElLviaXlAelJXpOdORRA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1\"/></section></section></section><section><section><section><section><br/></section></section></section></section><section><section><section><img class=\"\" src=\"https://mmbiz.qpic.cn/mmbiz_png/DOVVfocNpPabu4FcFibxsfAy2iaDs4uWS9DyS5ga0pW5t7fXkZOd0f4hGWy0Igm3hF1LXwloruxs5RVpnoMbvpicA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1\"/></section></section></section><section><section><section><section><br/></section></section></section></section><section><section><section><img class=\"\" src=\"https://mmbiz.qpic.cn/mmbiz_png/DOVVfocNpPabu4FcFibxsfAy2iaDs4uWS9WqXSLKua2oJdHAB8bQxJZ3uu7mNa1tFrBnt3vLLYhDia04iae2on8xWA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1\"/></section></section></section><section><section><section><section><br/></section></section></section></section><section><section><section><img class=\"\" src=\"https://mmbiz.qpic.cn/mmbiz_png/DOVVfocNpPabu4FcFibxsfAy2iaDs4uWS9PBOQ8kvU1zbGicYShVy53qicJ80JOjKiaTtyWrQf7aGrodxK1yARd3ZBA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1\"/></section></section></section><section><section><section><section><br/></section></section></section></section><section><section><section><img class=\"\" src=\"https://mmbiz.qpic.cn/mmbiz_png/DOVVfocNpPabu4FcFibxsfAy2iaDs4uWS9ylPQ33JQmrkBjL1ubrZvVicozAWqSQj0JZsPicsc7PTkON7zEibwfcPgg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1\"/></section></section></section><section><section><section><section><br/></section></section></section></section><section><section><section><img class=\"\" src=\"https://mmbiz.qpic.cn/mmbiz_png/DOVVfocNpPabu4FcFibxsfAy2iaDs4uWS99w6AI46Io7fDlba2U9TFuTWorZXq52V0taH7wLpCBGFrvT2pLLB8Jw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1\"/></section></section></section><section><section><section><section><br/></section></section></section></section><section><section><section><img class=\"\" src=\"https://mmbiz.qpic.cn/mmbiz_png/DOVVfocNpPabu4FcFibxsfAy2iaDs4uWS9FhfZp1TbTIx3pJNvtuThiafl0vKCyulico1rdcziclJwQeibxa7hWGomwA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1\"/></section></section></section><section><section><section><section><br/></section></section></section></section><section><section><section><img class=\"\" src=\"https://mmbiz.qpic.cn/mmbiz_png/DOVVfocNpPabu4FcFibxsfAy2iaDs4uWS9ZuSzgGUG4FnF2p9y01vs2hr9lcn1S5MqiaViaUlP3gxI2ow6guFiaFpZg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1\"/></section></section></section><section><section><section><section><br/></section></section></section></section><section><section><section><img class=\"\" src=\"https://mmbiz.qpic.cn/mmbiz_png/DOVVfocNpPabu4FcFibxsfAy2iaDs4uWS9j49icQnjPhjGoErCywiazxt8J7ictMSh8P1nI061iavIuO3EmjBQFopsCA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1\"/></section></section></section><section><section><section><section><br/></section></section></section></section><section><section><section><img class=\"\" src=\"https://mmbiz.qpic.cn/mmbiz_png/DOVVfocNpPabu4FcFibxsfAy2iaDs4uWS9Jp3SQS58ZDRAy5Mk178GianyVDsWHtCIk10PPWAwt1ibYLc2IbF3bcgQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1\"/></section></section></section><section><section><section><section><br/></section></section></section></section><section><section><section><img class=\"\" src=\"https://mmbiz.qpic.cn/mmbiz_png/DOVVfocNpPabu4FcFibxsfAy2iaDs4uWS9GA9ca84IuS7tbBobC26KicRmJPTkD8lLaEQRIck8alRcibjHJK3Xb2WA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1\"/></section></section></section><section><section><section><section><br/></section></section></section></section><section><section><section><img class=\"\" src=\"https://mmbiz.qpic.cn/mmbiz_png/DOVVfocNpPabu4FcFibxsfAy2iaDs4uWS9ckendtnhWsLhO4Q5FMSj5WpQxugTa9mwZiaadWhAFhxBvicFYeNqIFVA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1\"/></section></section></section><section><section><section><section><br/></section></section></section></section><section><section><section><img class=\"\" src=\"https://mmbiz.qpic.cn/mmbiz_png/DOVVfocNpPabu4FcFibxsfAy2iaDs4uWS9hQqzLbqshzNHrFL20mdfib5aiafGzrZZ0LZxVuVCkB4OXNDlN4ChLRzA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1\"/></section></section></section><section><section><section><section><br/></section></section></section></section><section><section><section><img class=\"\" src=\"https://mmbiz.qpic.cn/mmbiz_png/DOVVfocNpPabu4FcFibxsfAy2iaDs4uWS9CqgcKfQKzEYotwpcibmicXS9tKnTia0z5z99EuLWwGMbWN56Y8oMLjlTw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1\"/></section></section></section></section></section></section></section></section></section></section></section></section></section></section></section></p><p><br/></p></body></html>")
        stringBuilder.append("<head><style>img{max-width: 100%!important;height:auto!important;}body{background:#fff;position: relative;line-height:1.6;font-size:40px;font-family:Microsoft YaHei,Helvetica,Tahoma,Arial,\\5FAE\\8F6F\\96C5\\9ED1,sans-serif}</style></head>")
        stringBuilder.append("<body>")
        stringBuilder.append(data)
        stringBuilder.append("</body></html>")
        return stringBuilder.toString()
    }

    override fun lazyLoad() {

    }
}
package com.yc.emotion.home.community.ui.activity

import android.os.Bundle
import android.webkit.WebSettings
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.community.presenter.CommunityPresenter
import com.yc.emotion.home.community.view.CommunityView
import com.yc.emotion.home.model.bean.CommunityInfo
import com.yc.emotion.home.model.bean.TopTopicInfo
import com.yc.emotion.home.utils.DateUtils
import com.yc.emotion.home.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_community_notice_detail.*

/**
 * Created by suns  on 2019/8/28 17:36.
 */
class CommunityNoticeDetailActivity : BaseSameActivity(), CommunityView {
    override fun shoCommunityNewestCacheInfos(datas: List<CommunityInfo>?) {

    }


    private val content = "<p>各位小伙伴大家好，为保障社区能够文明有序进行，特针对本社区制定如下相关规定，请认真查看并遵守，如违反规定将对发帖或账号进行封禁限制，希望大家配合。</p><p style='text-align:center;'><img src=\"http://ytx.wk2.com/static/ueditor/php/upload1//20190308/15520293621027.jpg\"/></p><p><strong>【发贴标准】</strong></p><p>发帖内容要以情感解惑、婚姻生活为主且内容健康、积极向上；</p><p>严禁发布宣传Q群、微信群、各种招人性质的帖子及评论；</p><p>严禁发布包含有色情，赌博，恐怖等内容的帖子及评论；</p><p>严禁发布包含盈利商业广告交易信息内容的帖子及评论；</p><p>严禁发布涉及挑衅、侮辱、威胁等人身攻击内容的帖子及评论；</p><p>严禁发布使用他人照片信息等侵害他人隐私内容的帖子及评论；</p><p><strong>【删帖标准】</strong></p><p>【非法类】：涉黄、涉黑、涉及国家政治等信息内容；</p><p>【隐私类】：利用他人照片等信息发布虚假内容；</p><p>【广告类】：任何广告、商业信息、交易帖、不安全网址。</p><p>【不文明】：恶意人身攻击、侮辱、诽谤他人。</p><p>【灌水、重复】：恶意灌水，无意义的发帖回帖行为、标题党、大量重复发贴、与本话题无关的帖子。</p><p>【头像、昵称、标题】：头像不雅、昵称含有广告、Q号宣传性内容。昵称、头像、标题不允许带联系方式、广告、涉黄、擦边、交易等信息。</p><p><strong>以上就是所有规则，如有改动将继续更新，最后祝大家在社区玩的愉快！</strong></p>"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_community_notice_detail
    }

    override fun initViews() {

        mPresenter = CommunityPresenter(this, this)
        getData()

    }

    private fun getData() {

        (mPresenter as? CommunityPresenter)?.getTopTopicInfos()
//        mLoadingDialog?.showLoadingDialog()
//        mLoveEngine?.topTopicInfos?.subscribe(object : Subscriber<ResultInfo<TopTopicInfo>>() {
//            override fun onCompleted() {
//                mLoadingDialog?.dismissLoadingDialog()
//            }
//
//            override fun onError(e: Throwable) {
//
//
//            }
//
//            override fun onNext(topTopicInfoResultInfo: ResultInfo<TopTopicInfo>?) {
//                if (topTopicInfoResultInfo != null && topTopicInfoResultInfo.code == HttpConfig.STATUS_OK && topTopicInfoResultInfo.data.topic_info != null) {
//                    val topic_info = topTopicInfoResultInfo.data.topic_info
//
//
//                }
//            }
//        })

    }

    private fun initWebView() {


        webview.isClickable = true
        val settings = webview.settings

        settings.builtInZoomControls = true
//        settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        settings.defaultTextEncodingName = "gb2312"

        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN

        //不显示webview缩放按钮
        settings.displayZoomControls = false


        webview.loadDataWithBaseURL(null, formatting1(content), "text/html", "utf-8", null)
    }


    private fun formatting1(data: String): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("<html>")
        stringBuilder.append("<head><style>img{max-width: 100%!important;height:auto!important;}body{margin:0 auto;background:#fff;position: relative;line-height:1.3;font-size:2.8em;font-family:Microsoft YaHei,Helvetica,Tahoma,Arial,\\5FAE\\8F6F\\96C5\\9ED1,sans-serif}</style></head>")
        stringBuilder.append("<body>")
        stringBuilder.append(data)
        stringBuilder.append("</body></html>")
        return stringBuilder.toString()
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val layoutParams = webview.layoutParams as LinearLayout.LayoutParams
        var bottom = 0
        if (StatusBarUtil.isNavigationBarExist(this)) {
            bottom = StatusBarUtil.getNavigationBarHeight(this)
        }
        layoutParams.bottomMargin = bottom
        webview.layoutParams = layoutParams
    }

    private fun initData(data: CommunityInfo) {
        Glide.with(this).load(R.mipmap.ic_launcher).apply(RequestOptions().circleCrop()).into(iv_icon)
        //        tvName.setText(data.name);
        tv_content.text = data.content
        tv_date.text = DateUtils.formatTimeToStr(data.create_time)
        Glide.with(this).load(R.mipmap.community_detail_bg).into(iv_community_pic)
        initWebView()
    }

    override fun showCommunityNoticeInfo(data: TopTopicInfo?) {
        data?.let {
            initData(data.topic_info)
        }
    }

    override fun offerActivityTitle(): String {
        return getString(R.string.notice)
    }
}

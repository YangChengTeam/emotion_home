package com.yc.emotion.home.community.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.Html
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.kk.utils.ToastUtil
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.community.adapter.CommunityFollowAdapter
import com.yc.emotion.home.model.bean.CommunityDetailInfo
import com.yc.emotion.home.model.bean.CommunityInfo
import com.yc.emotion.home.mine.ui.activity.UserInfoActivity
import com.yc.emotion.home.base.ui.activity.BaseSlidingActivity
import com.yc.emotion.home.base.ui.widget.LoadDialog
import com.yc.emotion.home.community.presenter.CommunityPresenter
import com.yc.emotion.home.community.view.CommunityView
import com.yc.emotion.home.model.util.SizeUtils
import com.yc.emotion.home.utils.DateUtils
import com.yc.emotion.home.utils.StatusBarUtil
import com.yc.emotion.home.utils.UserInfoHelper
import kotlinx.android.synthetic.main.activity_community_detail.*
import kotlinx.android.synthetic.main.common_topbar_view.*
import rx.Subscriber
import java.util.*

/**
 * Created by suns  on 2019/8/29 11:23.
 */
class CommunityDetailActivity : BaseSlidingActivity(), View.OnClickListener, CommunityView {
    override fun shoCommunityNewestCacheInfos(datas: List<CommunityInfo>?) {

    }


    private var communityMainAdapter: CommunityFollowAdapter? = null

    private var result: String? = null//输入结果


    private var tile: String? = null
    private var id: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置fitsSystemWindows属性后添加
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(getLayoutId())
        initViews()
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_community_detail
    }

    override fun initViews() {
        invadeStatusBar() //侵入状态栏
        setAndroidNativeLightStatusBar() //状态栏字体颜色改变

        mPresenter = CommunityPresenter(this, this)

        initView()
        initListener()
    }


    private fun initView() {
        val intent = intent
        if (intent.hasExtra("title")) {
            tile = intent.getStringExtra("title")
        }
        if (intent.hasExtra("id")) {
            id = intent.getStringExtra("id")
        }


        activity_base_same_tv_title.text = tile

        detail_recyclerView.layoutManager = LinearLayoutManager(this)
        communityMainAdapter = CommunityFollowAdapter(null)
        detail_recyclerView.adapter = communityMainAdapter

        getData()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val layoutParams = ll_bottom.layoutParams as RelativeLayout.LayoutParams
        val rcvLayoutParams = detail_recyclerView.layoutParams as LinearLayout.LayoutParams
        var bottom = 0
        val rcvBottom: Int
        if (StatusBarUtil.isNavigationBarExist(this)) {
            bottom = StatusBarUtil.getNavigationBarHeight(this)
            rcvBottom = SizeUtils.dp2px(this, 80f)
        } else {
            rcvBottom = SizeUtils.dp2px(this, 40f)
        }
        layoutParams.bottomMargin = bottom
        ll_bottom.layoutParams = layoutParams

        rcvLayoutParams.bottomMargin = rcvBottom

        detail_recyclerView.layoutParams = rcvLayoutParams

    }

    private fun initListener() {
        activity_base_same_iv_back.setOnClickListener(this)
        tv_send.setOnClickListener(this)
        iv_wx_help.setOnClickListener(this)
        et_input_comment.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                result = s.toString().trim { it <= ' ' }
                //                Log.e(TAG, "afterTextChanged: " + result);
                if (TextUtils.isEmpty(result)) {
                    tv_send.background = ContextCompat.getDrawable(this@CommunityDetailActivity, R.drawable.community_gray_send_selector)
                } else {
                    tv_send.background = ContextCompat.getDrawable(this@CommunityDetailActivity, R.drawable.community_red_send_selector)
                }

            }
        })
        communityMainAdapter?.setOnItemChildClickListener { adapter, view, position ->
            val communityInfo = communityMainAdapter?.getItem(position)


            communityInfo?.let {
                when (view.id) {
                    R.id.iv_like, R.id.ll_like ->
                        //
                        if (communityInfo.is_dig == 0) {
                            like(communityInfo, position)
                        }
                }
            }
        }

    }

    private fun like(communityInfo: CommunityInfo, position: Int) {

        (mPresenter as? CommunityPresenter)?.commentLike(communityInfo, position)

    }

    private fun getData() {
//        val userId = UserInfoHelper.instance.getUid()


        (mPresenter as? CommunityPresenter)?.getCommunityDetailInfo(id)

    }

    private fun initData(detailInfo: CommunityDetailInfo) {
        val communityInfo = detailInfo.topic_info
        Glide.with(this).load(communityInfo.pic).apply(RequestOptions().circleCrop().error(R.mipmap.main_icon_default_head).placeholder(R.mipmap.main_icon_default_head))
                .into(iv_icon)
        tv_name.text = communityInfo.name
        tv_date.text = DateUtils.formatTimeToStr(communityInfo.create_time, "yyyy-MM-dd")
        val html = Html.fromHtml("<font color='#ff2d55'>#" + communityInfo.tag + "#</font>" + communityInfo.content)
        tv_content.text = html
        tv_like_num.text = communityInfo.like_num.toString()
        tv_comment_num.text = communityInfo.comment_num.toString()
        iv_like.setImageResource(if (communityInfo.is_dig == 0) R.mipmap.community_like else R.mipmap.community_like_selected)
        tv_like_num.setTextColor(if (communityInfo.is_dig == 0) ContextCompat.getColor(this, R.color.gray_999) else ContextCompat.getColor(this, R.color.red_crimson))

        communityMainAdapter?.setNewData(detailInfo.comment_list)


    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.activity_base_same_iv_back -> finish()
            R.id.tv_send -> {
                if (TextUtils.isEmpty(result)) {
                    ToastUtil.toast2(this, "评论内容不能为空")
                    return
                }
                // TODO: 2019/8/30 提交到服务器
                submitComment(id, result)
            }

            R.id.iv_wx_help -> {
                showToWxServiceDialog()
                MobclickAgent.onEvent(this, "WeChat_help+id", "社区微信求助")
            }
        }
    }

    private fun submitComment(topicId: String?, content: String?) {



        (mPresenter as? CommunityPresenter)?.createComment(topicId, content)

    }


    private fun saveToList(content: String?) {

        val ycSingle = UserInfoHelper.instance
        val communityInfos = communityMainAdapter?.data

        //        SimpleDateFormat sd = new SimpleDateFormat("MM月dd日", Locale.getDefault());

        val communityInfo = CommunityInfo(ycSingle.getUserInfo()?.nick_name, Date().time / 1000, content, "", 0, 0)
        communityInfo.pic = ycSingle.getUserInfo()?.face
        communityInfos?.add(0, communityInfo)
        communityMainAdapter?.setNewData(communityInfos)


    }

    override fun showCommunityDetailInfo(detailInfo: CommunityDetailInfo?) {
        detailInfo?.let {

            initData(it)
        }
    }


    override fun showCommunityDetailSuccess(communityInfo: CommunityInfo, position: Int) {
        communityInfo.is_dig = 1
        communityInfo.like_num = communityInfo.like_num + 1

        communityMainAdapter?.notifyItemChanged(position)
    }

    override fun createCommunityResult(t: ResultInfo<String>, content: String?) {
        if (t.code == HttpConfig.STATUS_OK) {
            hindKeyboard(et_input_comment)
            et_input_comment.setText("")
            saveToList(content)
        } else if (t.code == 2) {
            startActivity(Intent(this@CommunityDetailActivity, UserInfoActivity::class.java))
        }
    }


    companion object {

        private val TAG = "CommunityDetailActivity"

        fun StartActivity(context: Context?, title: String, id: String) {
            val intent = Intent(context, CommunityDetailActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("id", id)
            context?.startActivity(intent)
        }
    }

}

package com.yc.emotion.home.community.ui.activity

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.base.ui.widget.LoadDialog
import com.yc.emotion.home.community.adapter.PublishTagAdapter
import com.yc.emotion.home.community.presenter.CommunityPresenter
import com.yc.emotion.home.community.view.CommunityView
import com.yc.emotion.home.mine.ui.fragment.ExitPublishFragment
import com.yc.emotion.home.model.bean.CommunityTagInfo
import com.yc.emotion.home.model.bean.CommunityTagInfoWrapper
import com.yc.emotion.home.model.bean.event.CommunityPublishSuccess
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.utils.Preference
import com.yc.emotion.home.utils.SnackBarUtils
import com.yc.emotion.home.utils.UserInfoHelper
import kotlinx.android.synthetic.main.activity_community_publish.*
import org.greenrobot.eventbus.EventBus
import rx.Subscriber

/**
 * Created by suns  on 2019/8/30 18:12.
 */
class CommunityPublishActivity : BaseSameActivity(), CommunityView {


    private var tagAdapter: PublishTagAdapter? = null
    private var tagInfo: CommunityTagInfo? = null
    private var pos by Preference(ConstantKey.TAG_POSTION, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())


        initViews()


    }

    override fun getLayoutId(): Int {
        return R.layout.activity_community_publish
    }

    override fun initViews() {
        invadeStatusBar() //侵入状态栏
        setAndroidNativeLightStatusBar() //状态栏字体颜色改变

        mPresenter = CommunityPresenter(this, this)

        et_community.requestFocus()//直接让editText获取焦点

        mBaseSameTvSub.text = getString(R.string.publish)

        tag_recyclerView.layoutManager = GridLayoutManager(this, 3)

        tagAdapter = PublishTagAdapter(null)
        tag_recyclerView.adapter = tagAdapter

        getTag()
        initListener()

    }

    private fun initListener() {
        tagAdapter?.setOnItemClickListener { adapter, view, position ->
            tagInfo = tagAdapter?.getItem(position)
            tagAdapter?.resetView()
            tagAdapter?.setViewState(position)
        }
        ivBack.setOnClickListener { v -> showExitFragment() }
        mBaseSameTvSub.setOnClickListener { v ->
            val content = et_community.text.toString().trim { it <= ' ' }
            hindKeyboard(et_community)
            if (TextUtils.isEmpty(content)) {
                SnackBarUtils.tips(this@CommunityPublishActivity, "请输入您的问题")
                return@setOnClickListener
            }
            publishComment(content)

        }


    }


    private fun publishComment(content: String) {


        (mPresenter as? CommunityPresenter)?.publishCommunityInfo(tagInfo?.id, content)


    }

    private fun getTag() {

        (mPresenter as? CommunityPresenter)?.getCommunityTagInfos()

    }

    override fun offerActivityTitle(): String {
        return getString(R.string.edit_content)
    }


    override fun onBackPressed() {

        showExitFragment()
    }

    private fun showExitFragment() {
        val exitPublishFragment = ExitPublishFragment()
        exitPublishFragment.show(supportFragmentManager, "")
        exitPublishFragment.setOnConfirmListener { this.finish() }
    }


    override fun showCommunityTagInfos(list: List<CommunityTagInfo>) {

        if (list.isNotEmpty()) {
            tagInfo = list[pos]
        }
        tagAdapter?.setNewData(list)
    }

    override fun publishCommunitySuccess(message: String?) {
        SnackBarUtils.tips(this@CommunityPublishActivity, message)
        mLoadingDialog?.dismissLoadingDialog()
        EventBus.getDefault().post(CommunityPublishSuccess())
        finish()
    }
}

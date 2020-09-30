package com.yc.emotion.home.community.ui.activity

import android.os.Bundle

import android.text.TextUtils
import androidx.recyclerview.widget.GridLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.community.adapter.PublishTagAdapter
import com.yc.emotion.home.community.presenter.CommunityPresenter
import com.yc.emotion.home.community.view.CommunityView
import com.yc.emotion.home.mine.ui.fragment.ExitPublishFragment
import com.yc.emotion.home.model.bean.CommunityInfo
import com.yc.emotion.home.model.bean.CommunityTagInfo
import com.yc.emotion.home.model.bean.event.CommunityPublishSuccess
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.utils.Preference
import com.yc.emotion.home.utils.SnackBarUtils
import com.yc.emotion.home.utils.clickWithTrigger
import kotlinx.android.synthetic.main.activity_community_publish.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by suns  on 2019/8/30 18:12.
 */
class CommunityPublishActivity : BaseSameActivity(), CommunityView {
    override fun shoCommunityNewestCacheInfos(datas: List<CommunityInfo>?) {

    }


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
        ivBack.clickWithTrigger { v -> showExitFragment() }
        mBaseSameTvSub.clickWithTrigger { v ->
            val content = et_community.text.toString().trim { it <= ' ' }
            hindKeyboard(et_community)
            if (TextUtils.isEmpty(content)) {
                SnackBarUtils.tips(this@CommunityPublishActivity, "请输入您的问题")
                return@clickWithTrigger
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
        val exitPublishFragment = ExitPublishFragment.newInstance("")
        exitPublishFragment.show(supportFragmentManager, "")
        exitPublishFragment.setOnConfirmListener (object :ExitPublishFragment.OnConfirmListener{
            override fun onConfirm() {
               finish()
            }
        })
    }


    override fun showCommunityTagInfos(list: List<CommunityTagInfo>) {

        if (list.isNotEmpty()) {
            tagInfo = list[pos]
        }
        tagAdapter?.setNewData(list)
    }

    override fun publishCommunitySuccess(message: String) {
        SnackBarUtils.tips(this@CommunityPublishActivity, message)
        mLoadingDialog?.dismissLoadingDialog()
        EventBus.getDefault().post(CommunityPublishSuccess())
        finish()
    }
}

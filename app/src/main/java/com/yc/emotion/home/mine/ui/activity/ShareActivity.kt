package com.yc.emotion.home.mine.ui.activity

import android.os.Bundle
import android.text.TextUtils
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.base.ui.fragment.common.ShareAppFragment
import com.yc.emotion.home.model.bean.ShareInfo
import com.yc.emotion.home.utils.StatusBarUtil
import com.yc.emotion.home.utils.UIUtils
import com.yc.emotion.home.utils.UserInfoHelper
import com.yc.emotion.home.utils.clickWithTrigger
import kotlinx.android.synthetic.main.activity_share.*

/**
 *
 * Created by suns  on 2019/10/18 15:36.
 */
class ShareActivity : BaseSameActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_share
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        initViews()
    }


    override fun initViews() {


        val instance = UserInfoHelper.instance
        val face = instance.getUserInfo()?.face
        if (!TextUtils.isEmpty(face)) {

            Glide.with(this).load(face).apply(RequestOptions().circleCrop().error(R.mipmap.ic_launcher)).into(iv_share_face)
        } else {
            Glide.with(this).load(R.mipmap.ic_launcher).apply(RequestOptions().circleCrop()).into(iv_share_face)
        }
        var nick_name = instance.getUserInfo()?.nick_name
        if (TextUtils.isEmpty(nick_name)) {
            nick_name = UIUtils.getAppName(this)
        }
        tv_share_name.text = nick_name
        tv_invite_friend.text = String.format(getString(R.string.invitate_friend), UIUtils.getAppName(this))
        tv_share_desc.text = "邀请你一起加入${UIUtils.getAppName(this)}"
        initListener()
    }

    private fun initListener() {
        tv_invite_friend.clickWithTrigger {
            val shareFragment = ShareAppFragment()
            window.decorView.isDrawingCacheEnabled = true
            val bmp = window.decorView.drawingCache
            val sharInfo = ShareInfo()
            sharInfo.bitmap = bmp
            shareFragment.setShareInfo(sharInfo)
            shareFragment.show(supportFragmentManager, "")
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val layoutParams = tv_invite_friend.layoutParams as RelativeLayout.LayoutParams
        var bottom = 0
        if (StatusBarUtil.isNavigationBarExist(this)) {
            bottom = StatusBarUtil.getNavigationBarHeight(this)
        }

        layoutParams.bottomMargin = bottom
        tv_invite_friend.layoutParams = layoutParams
    }

    override fun offerActivityTitle(): String {
        return UIUtils.getAppName(this)
    }
}
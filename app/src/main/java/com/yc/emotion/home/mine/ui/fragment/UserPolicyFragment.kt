package com.yc.emotion.home.mine.ui.fragment

import android.widget.ImageView
import com.kk.utils.ScreenUtil
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.fragment.BaseDialogFragment

/**
 * Created by suns  on 2020/4/17 16:28.
 */
class UserPolicyFragment : BaseDialogFragment() {
    var ivClose: ImageView? = null
    override val width: Float
        protected get() = 0.7f

    override val animationId: Int
        get() = R.style.share_anim

    override val height: Int
        get() = ScreenUtil.getHeight(activity) * 3 / 5

    override fun getLayoutId(): Int {
        return R.layout.fragment_user_policy
    }

    override fun initViews() {
        ivClose = rootView?.findViewById(R.id.iv_close)
        ivClose?.setOnClickListener { dismiss() }
    }


}
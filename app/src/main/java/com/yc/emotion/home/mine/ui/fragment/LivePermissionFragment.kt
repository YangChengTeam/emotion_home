package com.yc.emotion.home.mine.ui.fragment

import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.BaseDialogFragment
import com.yc.emotion.home.mine.domain.bean.LiveInfo
import com.yc.emotion.home.mine.presenter.LivePresenter
import com.yc.emotion.home.mine.view.LiveView
import com.yc.emotion.home.utils.clickWithTrigger

/**
 * Created by suns  on 2020/6/2 15:52.
 */
class LivePermissionFragment : BaseDialogFragment(), LiveView {
    private lateinit var livePresenter: LivePresenter


    override val width: Float
        protected get() = 0.7f

    override val animationId: Int
        get() = 0

    override val height: Int
        get() = ViewGroup.LayoutParams.WRAP_CONTENT

    fun init() {
        livePresenter = LivePresenter(activity, this)
        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)
        val etAccount = rootView?.findViewById<EditText>(R.id.et_account)
        val etPassword = rootView?.findViewById<EditText>(R.id.et_password)

        rootView?.findViewById<TextView>(R.id.tv_submit)?.clickWithTrigger {
            val account = etAccount?.text.toString().trim()
            val password = etPassword?.text.toString().trim()
            livePresenter.liveLogin(account, password)

        }
    }

    override fun showLoginSuccess(data: LiveInfo) {
//        LiveRoomActivity.startActivity(activity, data)
        dismiss()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_live_permisson
    }


    override fun initViews() {
        init()
    }

    override fun showLoadingDialog() {
        activity?.let {
            (it as BaseActivity).showLoadingDialog()
        }
    }

    override fun hideLoadingDialog() {
        activity?.let {
            (it as BaseActivity).hideLoadingDialog()
        }
    }
}
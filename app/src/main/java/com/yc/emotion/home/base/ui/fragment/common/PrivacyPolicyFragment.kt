package com.yc.emotion.home.base.ui.fragment.common

import android.content.Intent
import android.view.ViewGroup
import android.widget.TextView
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.fragment.BaseDialogFragment
import com.yc.emotion.home.mine.ui.activity.PrivacyStatementActivity
import com.yc.emotion.home.mine.ui.activity.UserPolicyActivity
import com.yc.emotion.home.utils.UIUtils
import com.yc.emotion.home.utils.clickWithTrigger
import kotlin.system.exitProcess

/**
 *
 * Created by suns  on 2020/5/15 14:38.
 */
class PrivacyPolicyFragment : BaseDialogFragment() {

    override val width: Float
        get() = 0.8f
    override val animationId: Int
        get() = 0
    override val height: Int
        get() = ViewGroup.LayoutParams.WRAP_CONTENT

    override fun getLayoutId(): Int {
        return R.layout.fragment_privacy_policy
    }

    override fun initViews() {
        init()
    }

    fun init() {
        isCancelable = false
        val tvServiceClause = rootView?.findViewById<TextView>(R.id.tv_service_clause)
        val tvPrivacy = rootView?.findViewById<TextView>(R.id.tv_privacy)
        val tvKnow = rootView?.findViewById<TextView>(R.id.tv_agree)
        val tvNotAgree = rootView?.findViewById<TextView>(R.id.tv_not_agree)
        val tvDesc = rootView?.findViewById<TextView>(R.id.tv_desc)
        tvDesc?.text = String.format(getString(R.string.privacy_desc, UIUtils.getAppName(activity)))
        tvServiceClause?.clickWithTrigger {
            startActivity(Intent(mContext, UserPolicyActivity::class.java))

        }
        tvPrivacy?.clickWithTrigger {
            startActivity(Intent(mContext, PrivacyStatementActivity::class.java))
        }
        tvKnow?.clickWithTrigger {
            onClickBtnListener?.onBtnClick()
            dismiss()
        }
        tvNotAgree?.clickWithTrigger {
            dismiss()
            activity?.finish()
            exitProcess(0)
        }

    }

    private var onClickBtnListener: OnClickBtnListener? = null

    fun setOnClickBtnListener(listener: OnClickBtnListener) {
        this.onClickBtnListener = listener
    }

    interface OnClickBtnListener {
        fun onBtnClick()
    }
}
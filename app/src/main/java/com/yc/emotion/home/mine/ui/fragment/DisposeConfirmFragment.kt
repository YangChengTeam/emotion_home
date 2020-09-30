package com.yc.emotion.home.mine.ui.fragment

import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.fragment.BaseDialogFragment
import com.yc.emotion.home.utils.clickWithTrigger

/**
 *
 * Created by suns  on 2020/8/26 14:28.
 */
class DisposeConfirmFragment : BaseDialogFragment() {
    private var name: String? = ""
    private var account: String? = ""
    private var money: String? = ""

    override val width: Float
        get() = 0.75f
    override val animationId: Int
        get() = R.style.share_anim
    override val height: Int
        get() = ViewGroup.LayoutParams.WRAP_CONTENT

    override fun getLayoutId(): Int {
        return R.layout.fragment_dispose_confirm
    }

    override fun initViews() {
        arguments?.let {
            name = it.getString("name")
            account = it.getString("account")
            money = it.getString("money")
        }

        val tvDisposeName = rootView?.findViewById<TextView>(R.id.tv_dispose_name)
        val tvDisposeAccount = rootView?.findViewById<TextView>(R.id.tv_dispose_account)
        val tvDisposeMoney = rootView?.findViewById<TextView>(R.id.tv_dispose_money)
        val tvModify = rootView?.findViewById<TextView>(R.id.tv_modify)
        val tvConfirmSubmit = rootView?.findViewById<TextView>(R.id.tv_confirm_submit)

        tvDisposeName?.text = name
        tvDisposeAccount?.text = account
        tvDisposeMoney?.text = money

        tvModify?.clickWithTrigger {
            dismiss()
        }

        tvConfirmSubmit?.clickWithTrigger {
            dismiss()
            listener?.onSubmit()
        }

    }


    var listener: OnSubmitApplyListener? = null

    fun setOnSubmitApplyListener(listener: OnSubmitApplyListener?) {
        this.listener = listener
    }

    interface OnSubmitApplyListener {
        fun onSubmit()
    }

    companion object {
        fun newInstance(name: String, account: String, money: String): DisposeConfirmFragment {
            val disposeConfirmFragment = DisposeConfirmFragment()
            val bundle = Bundle()
            bundle.putString("name", name)
            bundle.putString("account", account)
            bundle.putString("money", money)

            disposeConfirmFragment.arguments = bundle
            return disposeConfirmFragment
        }
    }
}
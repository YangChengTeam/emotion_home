package com.yc.emotion.home.index.ui.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.BaseDialogFragment
import com.yc.emotion.home.utils.ToastUtils
import com.yc.emotion.home.utils.clickWithTrigger

/**
 * Created by suns  on 2019/9/6 16:41.
 */
class PaySuccWxFragment : BaseDialogFragment() {

    private var mWx = ""


    private fun initView() {

        val tvWx = rootView?.findViewById<TextView>(R.id.tv_copy_wx)
        val tvCopyWx = rootView?.findViewById(R.id.tv_copy_wx) as TextView
        val ivClose = rootView?.findViewById(R.id.iv_close) as ImageView
//        if (!TextUtils.isEmpty(mWx)) tvWx?.text = mWx

        ivClose.clickWithTrigger { v: View? -> dismiss() }

        if (activity is BaseActivity) {
            (activity as BaseActivity).showToWxServiceDialog(position = "vip", listener = object : BaseActivity.OnWxListener {
                override fun onWx(wx: String) {
                    Log.e("TAG", "onWx: $wx")
                    mWx = wx
                    tvWx?.text = "复制微信($wx)"

                }

            })
        }

        tvCopyWx.clickWithTrigger { v: View? ->
            val myClipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val myClip = ClipData.newPlainText("text", mWx)
            myClipboard.setPrimaryClip(myClip)
            ToastUtils.showCenterToast("微信复制成功")
            if (activity is BaseActivity) {
                (activity as BaseActivity).openWeiXin()
            }
            dismiss()
        }
    }

    override val width: Float
        protected get() = 0.75f

    override val animationId: Int
        get() = R.style.share_anim

    override val height: Int
        get() = ViewGroup.LayoutParams.WRAP_CONTENT

    override val gravity: Int
        get() = Gravity.CENTER

    override fun getLayoutId(): Int {
        return R.layout.frament_pay_succ_add_wx
    }

    override fun initViews() {
        initView()
    }

    fun setWX(wx: String) {

        mWx = wx
    }

    private var listener: OnToWxListener? = null
    fun setListener(listener: OnToWxListener?) {
        this.listener = listener
    }


    interface OnToWxListener {
        fun onToWx()
    }
}
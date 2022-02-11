package com.yc.emotion.home.pay.ui.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.BaseDialogFragment
import com.yc.emotion.home.utils.ToastUtils
import com.yc.emotion.home.utils.clickWithTrigger

/**
 *
 * Created by suns  on 2020/6/30 16:31.
 */
class PaySuccessFragment : BaseDialogFragment() {

    private var mWx = "pai201807"
    override val width: Float
        get() = 1.0f
    override val animationId: Int
        get() = R.style.share_anim
    override val height: Int
        get() = ViewGroup.LayoutParams.WRAP_CONTENT

    override fun getLayoutId(): Int {
        return R.layout.fragment_pay_success
    }

    override fun initViews() {


        if (activity is BaseActivity) {
            (activity as BaseActivity).showToWxServiceDialog(position = "audio", listener = object : BaseActivity.OnWxListener {
                override fun onWx(wx: String) {
//                    Log.e(TAG, "onWx: $wx")
                    mWx = wx
                }

            })
        }
        rootView?.findViewById<ImageView>(R.id.iv_cancel)?.clickWithTrigger {
            dismiss()
        }

        rootView?.findViewById<ImageView>(R.id.iv_copy_wx)?.clickWithTrigger {
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
}
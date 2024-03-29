package com.yc.emotion.home.utils

import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.yc.emotion.home.R
import com.yc.emotion.home.base.EmApplication

object ToastUtils {
    private var sMTv_text: TextView? = null
    private var centerToast: Toast? = null
    private var ivOk: ImageView? = null

    @JvmOverloads
    fun showCenterToast(text: String?, showImg: Boolean = false) {
        if (null == centerToast) {
            centerToast = Toast(EmApplication.instance)
            centerToast?.duration = Toast.LENGTH_SHORT
            centerToast?.setGravity(Gravity.CENTER, 0, 0)
            val view = View.inflate(EmApplication.instance, R.layout.toast_center_layout, null)
            ivOk = view.findViewById(R.id.iv_ok)
            sMTv_text = view.findViewById(R.id.tv_text)
            sMTv_text?.text = if (TextUtils.isEmpty(text)) "null" else text
            ivOk?.visibility = if (showImg) View.VISIBLE else View.GONE
            centerToast?.view = view
        } else {
            sMTv_text?.text = if (TextUtils.isEmpty(text)) "null" else text
            ivOk?.visibility = if (showImg) View.VISIBLE else View.GONE
        }
        centerToast?.show()
    }
}
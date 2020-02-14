package com.yc.emotion.home.index.ui.fragment

import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.fragment.BaseBottomSheetDialogFragment
import kotlinx.android.synthetic.main.layout_vip_pay_way.*

/**
 *
 * Created by suns  on 2019/10/16 11:35.
 */
class VipPayWayFragment : BaseBottomSheetDialogFragment() {

    private var icClose: ImageView? = null
    private var ivAli: ImageView? = null
    private var ivWx: ImageView? = null

    private var rlAliPay: RelativeLayout? = null
    private var rlWxiPay: RelativeLayout? = null

    private var tvPayBtn: TextView? = null

    private var payWayName = "alipay"
    override fun getLayoutId(): Int {
        return R.layout.layout_vip_pay_way

    }


    override fun initView() {
        icClose = rootView?.findViewById(R.id.iv_payway_close)
        rlAliPay = rootView?.findViewById(R.id.rl_ali_pay)
        rlWxiPay = rootView?.findViewById(R.id.rl_wx_pay)
        ivAli = rootView?.findViewById(R.id.iv_ali_select)
        ivWx = rootView?.findViewById(R.id.iv_wx_select)
        tvPayBtn = rootView?.findViewById(R.id.tv_pay_btn)

        initListener()
    }


    private fun initListener() {

        icClose?.setOnClickListener { dismiss() }

        rlAliPay?.setOnClickListener {
            payWayName = "alipay"
            ivAli?.setImageResource(R.mipmap.icon_circle_sel)
            ivWx?.setImageResource(R.mipmap.icon_circle_default)
        }

        rlWxiPay?.setOnClickListener {
            payWayName = "wxpay"
            ivWx?.setImageResource(R.mipmap.icon_circle_sel)
            ivAli?.setImageResource(R.mipmap.icon_circle_default)
        }
        tvPayBtn?.setOnClickListener {

            onPayWaySelectListener?.onPayWaySelect(payWayName)
            dismiss()

        }
    }

    private var onPayWaySelectListener: OnPayWaySelectListener? = null
    fun setOnPayWaySelectListener(listener: OnPayWaySelectListener) {
        this.onPayWaySelectListener = listener
    }


    interface OnPayWaySelectListener {
        fun onPayWaySelect(payway: String)
    }
}
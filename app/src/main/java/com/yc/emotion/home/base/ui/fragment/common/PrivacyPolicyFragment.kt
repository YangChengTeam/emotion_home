package com.yc.emotion.home.base.ui.fragment.common

import android.content.Intent
import android.text.method.LinkMovementMethod
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.umeng.commonsdk.UMConfigure
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.fragment.BaseDialogFragment
import com.yc.emotion.home.mine.ui.activity.PrivacyStatementActivity
import com.yc.emotion.home.mine.ui.activity.UserPolicyActivity
import com.yc.emotion.home.utils.SpanUtils
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

        val link = """
            为了加强对您个人信息的保护,根据最新法律法规要求,更新了隐私政策,请您仔细阅读并确认"<a href="javascript:void(0)">隐私权相关政策</a>"(特别是加粗或下划线标注的内容),同时我们使用了友盟SDK来进行应用统计、应用分享，相关的友盟隐私政策如下:
            使用SDK名称：友盟SDK
            服务类型：友盟统计、分享
            收集个人信息类型：设备信息（IMEI/Mac/android ID/IDFA/OPENUDID/GUID/SIM卡IMSI/地理位置信息）
            如有疑问，请仔细阅读"<a href="https://www.umeng.com/page/policy">友盟隐私政策</a>",
            我们严格按照政策内容使用和保存您的个人信息,为您提供更好的服务,感谢您的信任。
            """.trimIndent()

        val format = String.format(getString(R.string.privacy_desc, UIUtils.getAppName(activity)))
//        tvDesc?.text = format

        tvDesc?.setLinkTextColor(ContextCompat.getColor(requireActivity(), R.color.app_color))
        tvDesc?.movementMethod = LinkMovementMethod.getInstance()

        tvDesc?.text = SpanUtils.getStringBuilder(activity, link , "隐私政策")

        tvServiceClause?.clickWithTrigger {
            startActivity(Intent(mContext, UserPolicyActivity::class.java))

        }
        tvPrivacy?.clickWithTrigger {
            startActivity(Intent(mContext, PrivacyStatementActivity::class.java))
        }
        tvKnow?.clickWithTrigger {
            UMConfigure.init(activity, "5da983e44ca357602b00046d", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null)
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
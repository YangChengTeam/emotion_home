package com.yc.emotion.home.base.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.alipay.sdk.app.PayTask
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.yc.emotion.home.base.ui.activity.PayActivity
import com.yc.emotion.home.model.bean.OrdersInitBean.ParamsBean
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.pay.AuthResult
import com.yc.emotion.home.pay.PayResult

/**
 * Created by mayn on 2019/5/14.
 */
abstract class PayActivity : BaseSlidingActivity() {
    private var mMsgApi: IWXAPI? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMsgApi = WXAPIFactory.createWXAPI(this, null)
        // 将该app注册到微信
//        mMsgApi.registerApp(ConstantKey.WX_APP_ID);
    }

    fun toWxPay(paramsBean: ParamsBean) {
        val request = PayReq()
        /*request.appId = "wxd930ea5d5a258f4f";
        request.partnerId = "1900000109";
        request.prepayId= "1101000000140415649af9fc314aa427";
        request.packageValue = "Sign=WXPay";
        request.nonceStr= "1101000000140429eb40476f8896f4c9";
        request.timeStamp= "1398746574";
        request.sign= "7FFECB600D7157C5AA49810D2D8F28BC2811827B";*/ConstantKey.WX_APP_ID = paramsBean.appid
        request.appId = paramsBean.appid
        request.partnerId = paramsBean.mch_id
        request.prepayId = paramsBean.prepay_id
        request.packageValue = "Sign=WXPay"
        request.nonceStr = paramsBean.nonce_str
        request.timeStamp = paramsBean.timestamp
        request.sign = paramsBean.sign
        mMsgApi?.registerApp(ConstantKey.WX_APP_ID)
        Log.d("mylog", "toWxPay: request.appId " + request.appId)
        Log.d("mylog", "toWxPay: request.partnerId " + request.partnerId)
        Log.d("mylog", "toWxPay: request.prepayId " + request.prepayId)
        Log.d("mylog", "toWxPay: request.packageValue " + request.packageValue)
        Log.d("mylog", "toWxPay: request.nonceStr " + request.nonceStr)
        Log.d("mylog", "toWxPay: request.timeStamp " + request.timeStamp)
        Log.d("mylog", "toWxPay: request.sign " + request.sign)
        mMsgApi?.sendReq(request)
    }

    fun toZfbPay(orderInfo: String?) {
        val payRunnable = Runnable {
            val alipay = PayTask(this@PayActivity)
            val result = alipay.payV2(orderInfo, true)
            val msg = Message()
            msg.what = SDK_PAY_FLAG
            msg.obj = result
            handler.sendMessage(msg)
        }
        // 必须异步调用
        val payThread = Thread(payRunnable)
        payThread.start()
    }

    @SuppressLint("HandlerLeak")
    private var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SDK_PAY_FLAG -> {
                    val payResult = PayResult(msg.obj as Map<String?, String?>)

                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    val resultInfo = payResult.result // 同步返回需要验证的信息
                    val resultStatus = payResult.resultStatus
                    Log.d("mylog", "handleMessage: resultStatus $resultStatus")

                    // 判断resultStatus 为9000则代表支付成功
                    when {
                        TextUtils.equals(resultStatus, "9000") -> {
                            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                            onZfbPauResult(true, "支付成功")
                            //                        showAlert(PayActivity.this, "001 Payment success:" + payResult);
                        }
                        TextUtils.equals(resultStatus, "6001") -> {
                            onZfbPauResult(false, "支付取消")
                        }
                        else -> {
                            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                            onZfbPauResult(false, "支付失败")
                            //                        showAlert(PayActivity.this, "002 Payment failed:" + payResult);  //用户取消
                        }
                    }
                }
                SDK_AUTH_FLAG -> {
                    val authResult = AuthResult(msg.obj as Map<String?, String?>, true)
                    val resultStatus = authResult.resultStatus

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.resultCode, "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        showAlert(this@PayActivity, "003 Authentication success:$authResult")
                    } else {
                        // 其他状态值则为授权失败
                        showAlert(this@PayActivity, "004 Authentication failed:$authResult")
                    }
                }
                else -> {
                }
            }
        }
    }

    protected abstract fun onZfbPauResult(result: Boolean, des: String?)
    private fun showAlert(ctx: Context, info: String, onDismiss: DialogInterface.OnDismissListener? = null) {
        AlertDialog.Builder(ctx)
                .setMessage(info)
                .setPositiveButton("Confirm", null)
                .setOnDismissListener(onDismiss)
                .show()
    }

    companion object {
        private const val SDK_PAY_FLAG = 1
        private const val SDK_AUTH_FLAG = 2
    }
}
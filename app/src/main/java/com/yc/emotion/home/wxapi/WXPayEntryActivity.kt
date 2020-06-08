package com.yc.emotion.home.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.yc.emotion.home.R
import com.yc.emotion.home.model.bean.event.EventBusWxPayResult
import com.yc.emotion.home.model.constant.ConstantKey
import org.greenrobot.eventbus.EventBus

/**
 * Created by Administrator on 2018/7/4.
 */
class WXPayEntryActivity : Activity(), IWXAPIEventHandler {
    private lateinit var api: IWXAPI
    public override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_entry)
        Log.e("mylog", "onCreate: " + ConstantKey.WX_APP_ID)
        api = WXAPIFactory.createWXAPI(this, ConstantKey.WX_APP_ID)
        api.handleIntent(this.intent, this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        api.handleIntent(intent, this)
    }

    override fun onReq(baseReq: BaseReq) {}
    override fun onResp(resp: BaseResp) {
        Log.d("mylog", "onPayFinish, resp.errCode  " + resp.errCode + " resp.transaction " + resp.transaction
                + " resp.errStr " + resp.errStr + " resp.openId " + resp.openId)
        /*if (resp.getType() == 5) {
            Builder builder = new Builder(this);
            builder.setTitle("2131165188");
            builder.setMessage(String.valueOf(resp.errCode));
            builder.show();
        }*/
        var mess = "未知错误"
        when (resp.errCode) {
            0 -> mess = "支付成功"
            -1 -> mess = "支付失败"
            -2 -> mess = "支付取消"
            else -> {
            }
        }
        EventBus.getDefault().post(EventBusWxPayResult(resp.errCode, mess))
        finish()
    }
}
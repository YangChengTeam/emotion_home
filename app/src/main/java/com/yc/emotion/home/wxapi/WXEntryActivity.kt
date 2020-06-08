package com.yc.emotion.home.wxapi

import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.umeng.weixin.callback.WXCallbackActivity

class WXEntryActivity : WXCallbackActivity() {
    fun onResp(resp: BaseResp) {
        if (resp.type == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
            val launchMiniProResp = resp as WXLaunchMiniProgram.Resp
            val extraData = launchMiniProResp.extMsg // 对应JsApi navigateBackApplication中的extraData字段数据
        }
    }
}
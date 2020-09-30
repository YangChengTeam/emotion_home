package com.yc.emotion.home.base.domain.model

import android.content.Context
import com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext
import com.yc.emotion.home.base.httpinterface.HttpRequestInterface
import yc.com.rthttplibrary.request.RetrofitHttpRequest

/**
 *
 * Created by suns  on 2019/11/7 15:37.
 * 所有model的父类
 */
open class IModel(open var context: Context?) {

    protected var request: HttpRequestInterface = RetrofitHttpRequest.get(mContext).create(HttpRequestInterface::class.java)

}
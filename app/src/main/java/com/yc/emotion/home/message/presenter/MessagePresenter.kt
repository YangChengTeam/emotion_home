package com.yc.emotion.home.message.presenter

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.emotion.home.R
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.message.domain.model.MessageModel
import com.yc.emotion.home.message.view.MessageView
import com.yc.emotion.home.model.bean.MessageInfo
import com.yc.emotion.home.utils.CommonInfoHelper
import rx.Subscriber
import java.util.ArrayList

/**
 *
 * Created by suns  on 2019/11/15 14:02.
 */
class MessagePresenter(context: Context?, view: MessageView) : BasePresenter<MessageModel, MessageView>(context, view) {

    init {
        mModel = MessageModel(context)

    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {

    }

    override fun getCache() {

    }

    fun getMessageCache() {
        CommonInfoHelper.getO<List<MessageInfo>>(mContext, "main_message_info", object : TypeReference<List<MessageInfo>>() {}.type) { messageInfos ->
            messageInfos?.let {
                if (it.isNotEmpty()) {
                    mView.showMessageInfos(it)
                }
            }
        }
        getMessageInfoList()
    }


    fun getMessageInfoList() {

        mView.showLoadingDialog()
        val subscription = mModel?.getMessageInfoList()?.subscribe(object : Subscriber<ResultInfo<List<MessageInfo>>>() {
            override fun onNext(t: ResultInfo<List<MessageInfo>>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
//                        createNewData(t.data)
                        mView.showMessageInfos(t.data)
                        CommonInfoHelper.setO(mContext, t.data, "main_message_info")
                    }
                }
            }

            override fun onCompleted() {
                mView.hideLoadingDialog()
                mView.onComplete()
            }

            override fun onError(e: Throwable?) {

            }

        })
        subScriptions?.add(subscription)
    }

    fun getNotificationDetail(id: String) {
        mView.showLoadingDialog()

        mModel?.getNotificationDetail(id)?.subscribe(object : Subscriber<ResultInfo<MessageInfo>>() {
            override fun onNext(t: ResultInfo<MessageInfo>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showNotificationDetail(t.data)
                    }
                }
            }

            override fun onCompleted() {
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable?) {

            }

        })
    }
}
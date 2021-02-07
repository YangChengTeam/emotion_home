package com.yc.emotion.home.message.presenter

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.message.domain.model.MessageModel
import com.yc.emotion.home.message.view.MessageView
import com.yc.emotion.home.model.bean.MessageInfo
import com.yc.emotion.home.utils.CommonInfoHelper
import io.reactivex.observers.DisposableObserver
import yc.com.rthttplibrary.config.HttpConfig

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
        CommonInfoHelper.getO(mContext, "main_message_info", object : TypeReference<List<MessageInfo>>() {}.type,
                object : CommonInfoHelper.OnParseListener<List<MessageInfo>> {
                    override fun onParse(o: List<MessageInfo>?) {
                        o?.let {
                            if (it.isNotEmpty()) {
                                mView.showMessageInfos(it)
                            }
                        }
                    }
                })
        getMessageInfoList()
    }


    fun getMessageInfoList() {

        mModel?.getMessageInfoList()?.getData(mView, { it, _ ->
            it?.let {
                mView.showMessageInfos(it)
                CommonInfoHelper.setO(mContext, it, "main_message_info")
            }
        }, { _, _ -> mView.onComplete() })


    }

    fun getNotificationDetail(id: String) {

        mModel?.getNotificationDetail(id)?.getData(mView, { it, _ ->
            it?.let {
                mView.showNotificationDetail(it)
            }
        }, { _, _ -> })

    }
}
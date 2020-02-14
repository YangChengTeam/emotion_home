package com.yc.emotion.home.message.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.model.bean.MessageInfo

/**
 *
 * Created by suns  on 2019/9/28 18:12.
 */
class NotificationAdapter(mDatas: List<MessageInfo>?) : BaseQuickAdapter<MessageInfo, BaseViewHolder>(R.layout.notification_item_view, mDatas) {


    override fun convert(helper: BaseViewHolder?, item: MessageInfo?) {

        helper?.let {
            item?.let {

                helper.setText(R.id.tv_message_name, item.name)
                        .setText(R.id.tv_message_desc, item.desc)
            }


        }
    }
}
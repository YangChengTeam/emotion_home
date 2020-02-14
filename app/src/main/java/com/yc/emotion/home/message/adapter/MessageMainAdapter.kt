package com.yc.emotion.home.message.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.model.bean.MessageInfo

/**
 *
 * Created by suns  on 2019/9/28 18:12.
 */
class MessageMainAdapter(mDatas: List<MessageInfo>?) : BaseMultiItemQuickAdapter<MessageInfo, BaseViewHolder>(mDatas) {

    init {
        addItemType(MessageInfo.TYPE_MESSAGE, R.layout.message_item_view)
        addItemType(MessageInfo.TYPE_NOTIFICATION, R.layout.message_item_view)
        addItemType(MessageInfo.TYPE_COMMUNITY, R.layout.message_item_view)
    }

    override fun convert(helper: BaseViewHolder?, item: MessageInfo?) {

        helper?.let {
            item?.let {
                when (item.itemType) {
                    MessageInfo.TYPE_MESSAGE -> {
                        helper.setGone(R.id.view_big_divider, true)
                                .setGone(R.id.view_small_divider, false)
                                .setText(R.id.tv_message_name, item.name)
                                .setText(R.id.tv_message_desc, item.desc)
                                .setImageResource(R.id.iv_message_icon, item.img)
                    }
                    MessageInfo.TYPE_NOTIFICATION, MessageInfo.TYPE_COMMUNITY -> {
                        helper.setGone(R.id.view_big_divider, false)
                                .setGone(R.id.view_small_divider, true)
                                .setText(R.id.tv_message_name, item.name)
                                .setText(R.id.tv_message_desc, item.desc)
                                .setImageResource(R.id.iv_message_icon, item.img)
                    }
                    else -> {
                    }
                }


            }
        }
    }
}
package com.yc.emotion.home.index.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.index.domain.bean.AIItem

/**
 *
 * Created by suns  on 2020/8/11 18:07.
 */
class AIChatAdapter(datas:List<AIItem>?):BaseMultiItemQuickAdapter<AIItem,BaseViewHolder>(datas) {

    init {
        addItemType(AIItem.TYPE_FROM_ME,R.layout.chat_item_my_view)
        addItemType(AIItem.TYPE_FROM_OTHER,R.layout.chat_item_other_view)
    }
    override fun convert(helper: BaseViewHolder, item: AIItem?) {
        item?.let {
            when(item.itemType){
                AIItem.TYPE_FROM_ME-> {
                    helper.setText(R.id.tv_chat_message, item.content)
                    helper.setImageResource(R.id.iv_avtor,R.mipmap.person_head)
                            .setGone(R.id.tv_chat_name,false)
                            .addOnClickListener(R.id.tv_chat_message)
                }
                AIItem.TYPE_FROM_OTHER-> {
                    helper.setText(R.id.tv_chat_message, item.content)
                    helper.setImageResource(R.id.iv_avtor,R.mipmap.ai_header)
                            .setGone(R.id.tv_chat_name,false)
                            .addOnClickListener(R.id.tv_chat_message)
                }
                else->{}
            }
        }
    }
}
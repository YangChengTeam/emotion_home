package com.yc.emotion.home.index.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.index.domain.bean.AIItem
import com.yc.emotion.home.index.domain.bean.SmartChatItem
import com.yc.emotion.home.utils.ToastUtils
import com.yc.emotion.home.utils.UserInfoHelper

/**
 *
 * Created by suns  on 2020/9/1 10:26.
 */
class SmartChatVerbalAdapter(mDatas: List<SmartChatItem>?) : BaseMultiItemQuickAdapter<SmartChatItem, BaseViewHolder>(mDatas) {

    var face: String? = null

    init {
        addItemType(SmartChatItem.CHAT_ITEM_SELF, R.layout.chat_item_my_view)
        addItemType(SmartChatItem.CHAT_ITEM_VERBAL, R.layout.chat_item_verbal_view)
        addItemType(SmartChatItem.CHAT_ITEM_VIP, R.layout.chat_item_vip_view)
        face = UserInfoHelper.instance.getUserInfo()?.face
    }

    override fun convert(helper: BaseViewHolder, item: SmartChatItem?) {

        item?.let {
            when (item.itemType) {
                SmartChatItem.CHAT_ITEM_SELF -> {
                    helper.setText(R.id.tv_chat_message, item.content)

                    Glide.with(mContext).load(face).error(R.drawable.default_avatar_72).circleCrop().into(helper.getView(R.id.iv_avtor))
                }

                SmartChatItem.CHAT_ITEM_VERBAL -> {
                    if (item.aiItems != null && item.aiItems.size > 0) {
                        val recyclerViewAi = helper.getView<RecyclerView>(R.id.recyclerview_ai)
                        recyclerViewAi.layoutManager = LinearLayoutManager(mContext)
                        val verbalAiItemAdapter = VerbalAiItemAdapter(item.aiItems)
                        recyclerViewAi.adapter = verbalAiItemAdapter
                        recyclerViewAi.itemAnimator = DefaultItemAnimator()
                        verbalAiItemAdapter.setOnItemChildClickListener { adapter, view, position ->
                            val aiItem = verbalAiItemAdapter.getItem(position)
                            if (view.id == R.id.iv_praise) {
                                aiItemClickListener?.onPraise(aiItem)
                                view.findViewById<View>(R.id.iv_praise).isSelected = true
                            } else if (view.id == R.id.iv_collect) {
                                aiItemClickListener?.onCollect(aiItem)
                                view.findViewById<View>(R.id.iv_collect).isSelected = true
                            }
                        }
                        verbalAiItemAdapter.setOnItemClickListener { adapter, view, position ->
                            val aiItem = verbalAiItemAdapter.getItem(position)
                            copyText(aiItem?.content)
                        }

                    }
                    if (item.loveHealDetBeans != null && item.loveHealDetBeans.size > 0) {
                        val recyclerViewVerbal = helper.getView<RecyclerView>(R.id.recyclerview_verbal)
                        recyclerViewVerbal.layoutManager = LinearLayoutManager(mContext)

                        val verbalVbsAdapter = VerbalVbsAdapter(item.loveHealDetBeans)
                        recyclerViewVerbal.adapter = verbalVbsAdapter
                        verbalVbsAdapter.setOnItemChildClickListener { adapter, view, position ->
                            if (view.id == R.id.iv_praise) {
                                view.findViewById<View>(R.id.iv_praise).isSelected = true
                            } else if (view.id == R.id.iv_collect) {
                                view.findViewById<View>(R.id.iv_collect).isSelected = true
                            }
                        }

                    }
                    helper.addOnClickListener(R.id.ll_ai_change)
                            .addOnClickListener(R.id.ll_verbal_change)
                }
                SmartChatItem.CHAT_ITEM_VIP -> {
                    helper.addOnClickListener(R.id.tv_open_vip)
                }
                else -> {
                }
            }
        }

    }

    var aiItemClickListener: OnAiItemClickListener? = null

    fun setOnAiItemClickListener(listener: OnAiItemClickListener?) {
        this.aiItemClickListener = listener
    }

    interface OnAiItemClickListener {
        fun onPraise(aiItem: AIItem?)
        fun onCollect(aiItem: AIItem?)
    }


    private fun copyText(text: String?) {
        val myClipboard = mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val myClip = ClipData.newPlainText("text", text)
        myClipboard.setPrimaryClip(myClip)
        ToastUtils.showCenterToast("内容已复制", true)
    }
}
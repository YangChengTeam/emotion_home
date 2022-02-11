package com.yc.emotion.home.index.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseViewHolder
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.BaseQuickImproAdapter
import com.yc.emotion.home.model.bean.LoveHealDetBean
import com.yc.emotion.home.model.bean.LoveHealDetDetailsBean
import com.yc.emotion.home.utils.ToastUtils

/**
 *
 * Created by suns  on 2020/9/1 17:58.
 */
class VerbalVbsAdapter(datas: List<LoveHealDetBean>?) : BaseQuickImproAdapter<LoveHealDetBean, BaseViewHolder>(R.layout.verbal_vb_out_view, datas) {
    override fun convert(helper: BaseViewHolder, item: LoveHealDetBean?) {
        item?.let {

            var details: List<LoveHealDetDetailsBean>? = item.details
            val recyclerView = helper.getView<RecyclerView>(R.id.item_love_heal_rv)
            val layoutManager = LinearLayoutManager(mContext)
            recyclerView.layoutManager = layoutManager
            layoutManager.orientation = LinearLayoutManager.VERTICAL

            recyclerView.itemAnimator = DefaultItemAnimator()
            val pos = helper.adapterPosition
            if (pos == mData.size - 1) {
                helper.setGone(R.id.verbal_divider, false)
            }

            if (details == null || details.isEmpty()) {
                details = item.detail
            }

            val verbalVbItemAdapter = VerbalVbItemAdapter(details)

            recyclerView.adapter = verbalVbItemAdapter


            verbalVbItemAdapter.setOnItemClickListener { adapter, view, position ->
                val item1 = verbalVbItemAdapter.getItem(position)
                if (item1 != null) {
//                    item1.setTitle(mTitle)
                    MobclickAgent.onEvent(mContext, "copy_dialogue_id", "复制恋爱话术")
                    copyText(item1.content)
                }
            }

            helper.addOnClickListener(R.id.iv_praise)
                    .addOnClickListener(R.id.iv_collect)

            verbalVbItemAdapter.setOnItemChildClickListener { adapter, view, position ->
                val item2 = verbalVbItemAdapter.getItem(position)
                item2?.let {
                    when (view.id) {
                        R.id.iv_praise -> view.findViewById<View>(R.id.iv_praise).isSelected = true
                        R.id.iv_collect -> view.findViewById<View>(R.id.iv_collect).isSelected = true
                    }
                }


            }
        }
    }

    private fun copyText(text: String?) {
        val myClipboard = mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val myClip = ClipData.newPlainText("text", text)
        myClipboard.setPrimaryClip(myClip)
        ToastUtils.showCenterToast("内容已复制", true)
    }
}
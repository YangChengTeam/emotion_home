package com.yc.emotion.home.index.adapter

import android.text.TextUtils
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.BaseQuickImproAdapter
import com.yc.emotion.home.model.bean.LoveHealDetDetailsBean

/**
 *
 * Created by suns  on 2020/9/1 14:02.
 */
class VerbalVbItemAdapter(mDatas: List<LoveHealDetDetailsBean>?) : BaseQuickImproAdapter<LoveHealDetDetailsBean, BaseViewHolder>(R.layout.verbal_vb_item_view, mDatas) {


    override fun convert(helper: BaseViewHolder, item: LoveHealDetDetailsBean?) {
        item?.let {
//            helper.setText(R.id.item_details_bean_tv_name, item.content)

            val ansSex = item.ans_sex
            if (!TextUtils.isEmpty(ansSex)) {

                when (ansSex) {
                    in "0", "1" -> {//1男2女0bi'a
                        helper.setGone(R.id.ll_right_container, true)
                                .setGone(R.id.ll_left_container, false)
                                .setText(R.id.tv_details_content, item.content)

//                        helper.setImageDrawable(R.id.item_details_bean_iv_sex, ContextCompat.getDrawable(mContext, R.mipmap.icon_dialogue_men))
                    }
                    "2" -> {
                        helper.setGone(R.id.ll_right_container, false)
                                .setGone(R.id.ll_left_container, true)
                                .setText(R.id.item_details_bean_tv_name, item.content)
//                        helper.setImageDrawable(R.id.item_details_bean_iv_sex, ContextCompat.getDrawable(mContext, R.mipmap.icon_dialogue_women))
                    }
                    else -> {
                        helper.setImageDrawable(R.id.item_details_bean_iv_sex, ContextCompat.getDrawable(mContext, R.mipmap.icon_dialogue_nothing))
                    }

                }
            }
        }

    }
}
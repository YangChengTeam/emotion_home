package com.yc.emotion.home.index.adapter

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.model.bean.EmotionTestInfo

/**
 *
 * Created by suns  on 2019/10/9 14:02.
 */
class EmotionTestAdapter(mDatas: List<EmotionTestInfo>?) : BaseQuickAdapter<EmotionTestInfo, BaseViewHolder>(R.layout.layout_emotion_test_item, mDatas) {
    override fun convert(helper: BaseViewHolder?, item: EmotionTestInfo?) {
        helper?.let {
            item?.let {

                Glide.with(mContext).load(item.img).apply(RequestOptions().error(R.mipmap.index_example_icon)
                        .diskCacheStrategy(DiskCacheStrategy.DATA).skipMemoryCache(false))
                        .thumbnail(0.1f)
                        .into(helper.getView(R.id.roundCornerImg_emotion))
                helper.setText(R.id.tv_emotion_title, item.title)
                        .setText(R.id.tv_emotion_test_count, "${item.people}人已测试")

            }
        }
    }
}
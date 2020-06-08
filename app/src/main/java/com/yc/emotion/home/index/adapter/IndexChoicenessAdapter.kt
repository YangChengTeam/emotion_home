package com.yc.emotion.home.index.adapter

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.CommonMoreAdapter
import com.yc.emotion.home.model.bean.ArticleDetailInfo
import com.yc.emotion.home.base.ui.widget.RoundCornerImg

/**
 *
 * Created by suns  on 2019/9/19 09:10.
 * 首页精选adapter
 */
class IndexChoicenessAdapter(mDatas: ArrayList<ArticleDetailInfo>?, isIndex: Boolean, isMore: Boolean) : CommonMoreAdapter<ArticleDetailInfo, BaseViewHolder>(R.layout.item_index_choiceness, mDatas) {

    private val mIsIndex = isIndex

    private val mIsMore = isMore
    override fun convert(helper: BaseViewHolder?, item: ArticleDetailInfo?) {
        helper?.let {
            item?.let {
                helper.setText(R.id.tv_content, item.post_title)
                        .setText(R.id.tv_reuse, "${item.feeluseful}人觉得有用")
                        .setText(R.id.tv_tag, item.tag)

                val roundCornerImg = helper.getView<RoundCornerImg>(R.id.roundCornerImg)

                Glide.with(mContext).asBitmap().load(item.image).thumbnail(0.1f).apply(RequestOptions().error(R.mipmap.index_example_icon)
                        .diskCacheStrategy(DiskCacheStrategy.DATA).skipMemoryCache(false)).into(roundCornerImg)

                if (mIsIndex) {
                    val pos = helper.adapterPosition
                    if (pos == mData.size - 1) {
                        helper.setGone(R.id.view_divider, false)
                    }
                }
                if (mIsMore) {
                    helper.setGone(R.id.tv_tag, false)
                }

            }

        }

    }
}
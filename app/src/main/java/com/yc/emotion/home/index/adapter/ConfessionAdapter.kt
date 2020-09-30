package com.yc.emotion.home.index.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.widget.CustomLoadMoreView
import com.yc.emotion.home.model.bean.confession.ConfessionDataBean

/**
 * Created by mayn on 2019/5/5.
 */
class ConfessionAdapter(data: List<ConfessionDataBean?>?) : BaseMultiItemQuickAdapter<ConfessionDataBean?, BaseViewHolder?>(data) {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    init {
        addItemType(ConfessionDataBean.VIEW_TITLE, R.layout.recycler_view_item_title_t2_view)
        addItemType(ConfessionDataBean.VIEW_ITEM, R.layout.recycler_view_item_confession)
        setLoadMoreView(CustomLoadMoreView())
    }

    override fun convert(helper: BaseViewHolder, item: ConfessionDataBean?) {


            item?.let {
                when (item.itemType) {
                    ConfessionDataBean.VIEW_TITLE -> {
                        helper.setImageResource(R.id.roundCornerImg,R.mipmap.main_bg_title_t2)
                    }
                    ConfessionDataBean.VIEW_ITEM -> {
                        val ivIcon = helper.getView<ImageView>(R.id.iv_zb_thumb)
                        helper.setText(R.id.tv_zb_title, item.title)
                        helper.setText(R.id.tv_zb_des, item.desp)
                        helper.setText(R.id.tv_use_count, item.build_num)
                        val ivSrc = item.small_img
                        Glide.with(mContext).asBitmap().load(ivSrc).apply(RequestOptions().error(R.mipmap.acts_default).placeholder(R.mipmap.acts_default)).into(ivIcon)
                    }
                    else -> {
                    }

                }
            }


        }



}
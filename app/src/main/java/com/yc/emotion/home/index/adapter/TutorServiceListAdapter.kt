package com.yc.emotion.home.index.adapter

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.CommonMoreAdapter
import com.yc.emotion.home.model.bean.TutorServiceInfo

/**
 *
 * Created by suns  on 2019/10/12 17:03.
 */
class TutorServiceListAdapter(mDatas: List<TutorServiceInfo>?) : CommonMoreAdapter<TutorServiceInfo, BaseViewHolder>(R.layout.tutor_service_list_item, mDatas) {
    override fun convert(helper: BaseViewHolder, item: TutorServiceInfo?) {


            item?.let {
                helper.setText(R.id.tv_tutor_service_list_title, item.name)
                        .setText(R.id.tv_tutor_service_list_price, item.price)
                        .setText(R.id.tv_tutor_service_list_buy_count, "${item.buy_count}人已购")
                        .setText(R.id.tv_tutor_service_list_comment_count, "${item.comment_count}好评")

                Glide.with(mContext).load(item.img).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)
                        .error(R.mipmap.service_picture_one)).thumbnail(0.1f).into(helper.getView(R.id.iv_tutor_service_list_pic))

        }

    }
}
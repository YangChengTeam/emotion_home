package com.yc.emotion.home.index.adapter

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.model.bean.TutorInfo

/**
 *
 * Created by suns  on 2019/10/9 14:02.
 */
class TutorListAdapter(mDatas: List<TutorInfo>?) : BaseQuickAdapter<TutorInfo, BaseViewHolder>(R.layout.layout_tutor_list_item, mDatas) {
    override fun convert(helper: BaseViewHolder?, item: TutorInfo?) {
        helper?.let {
            item?.let {

                helper.setText(R.id.tv_tutor_name, item.name)
                        .setText(R.id.tv_tutor_desc, item.profession)

                        .addOnClickListener(R.id.iv_add_wx)


                val starId = when (item.level) {
                    1 -> R.mipmap.star_one
                    2 -> R.mipmap.star_two
                    3 -> R.mipmap.star_three
                    4 -> R.mipmap.star_four
                    5 -> R.mipmap.star_five
                    else -> R.mipmap.star_one
                }
                helper.setImageResource(R.id.iv_tutor_star, starId)

                Glide.with(mContext).load(item.img).apply(RequestOptions().circleCrop().error(R.mipmap.tutor_head)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)).into(helper.getView(R.id.iv_tutor_face))
            }
        }
    }
}
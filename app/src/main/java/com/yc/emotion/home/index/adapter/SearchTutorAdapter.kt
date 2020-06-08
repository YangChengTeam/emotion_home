package com.yc.emotion.home.index.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.CommonMoreAdapter
import com.yc.emotion.home.model.bean.TutorInfo

/**
 *
 * Created by suns  on 2019/10/9 14:02.
 */
class SearchTutorAdapter(mDatas: List<TutorInfo>?) : CommonMoreAdapter<TutorInfo, BaseViewHolder>(R.layout.layout_tutor_search_item, mDatas) {
    override fun convert(helper: BaseViewHolder?, item: TutorInfo?) {
        helper?.let {
            item?.let {

//                helper.setText(R.id.tv_efficient_course_title, item.title)
//                        .setText(R.id.tv_efficient_price, "$bd/年")
//                        .setText(R.id.tv_efficient_teacher_name, item.name)
//                        .setText(R.id.tv_efficient_teach_count, "${item.count}人已学习")
                helper.addOnClickListener(R.id.iv_add_wx)
            }
        }
    }
}
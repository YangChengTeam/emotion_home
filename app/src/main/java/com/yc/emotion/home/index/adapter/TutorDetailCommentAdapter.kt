package com.yc.emotion.home.index.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.model.bean.CommunityInfo

/**
 *
 * Created by suns  on 2019/10/12 14:40.
 */
class TutorDetailCommentAdapter(mDatas: List<CommunityInfo>?) : BaseQuickAdapter<CommunityInfo, BaseViewHolder>(R.layout.tutor_detail_comment_item, mDatas) {
    override fun convert(helper: BaseViewHolder?, item: CommunityInfo?) {

        helper?.let {
            item?.let {
//                helper.setText(R.id.tv_tutor_detail_comment_name, item.name)
//                        .setText(R.id.tv_tutor_detail_comment_content, item.content)
            }
        }
    }
}
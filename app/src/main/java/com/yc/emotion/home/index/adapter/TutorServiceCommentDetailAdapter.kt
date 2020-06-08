package com.yc.emotion.home.index.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.CommonMoreAdapter
import com.yc.emotion.home.model.bean.CommunityInfo

/**
 *
 * Created by suns  on 2019/10/14 16:12.
 */
class TutorServiceCommentDetailAdapter(mDatas: List<CommunityInfo>?) : CommonMoreAdapter<CommunityInfo, BaseViewHolder>(R.layout.tutor_service_comment_detail_item, mDatas) {
    override fun convert(helper: BaseViewHolder?, item: CommunityInfo?) {
        helper?.let {
            item?.let {

                helper.addOnClickListener(R.id.tv_once_again_comment)
            }
        }
    }

}
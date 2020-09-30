package com.yc.emotion.home.index.adapter

import android.view.View
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.CommonMoreAdapter
import com.yc.emotion.home.model.bean.AudioDataInfo
import com.yc.emotion.home.model.util.SPUtils

/**
 * Created by wanglin  on 2019/7/22 09:13.
 */
class AudioFilterAdapter(data: List<AudioDataInfo?>?) : CommonMoreAdapter<AudioDataInfo?, BaseViewHolder?>(R.layout.filter_item_view, data) {
    override fun convert(helper: BaseViewHolder, item: AudioDataInfo?) {

            item?.let {
                helper.setText(R.id.tv_name, item.title)
                val pos = SPUtils.get(mContext, SPUtils.FILTER_POS, 0) as Int
                if (helper.adapterPosition == pos) helper.getView<View>(R.id.tv_name).isSelected = true
            }


    }
}
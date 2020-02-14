package com.yc.emotion.home.index.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.emotion.home.R;
import com.yc.emotion.home.model.bean.AudioDataInfo;
import com.yc.emotion.home.model.util.SPUtils;

import java.util.List;

/**
 * Created by wanglin  on 2019/7/22 09:13.
 */
public class AudioFilterAdapter extends BaseQuickAdapter<AudioDataInfo, BaseViewHolder> {
    public AudioFilterAdapter(@Nullable List<AudioDataInfo> data) {
        super(R.layout.filter_item_view, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AudioDataInfo item) {
        helper.setText(R.id.tv_name, item.getTitle());

        int pos = (int) SPUtils.get(mContext, SPUtils.FILTER_POS, 0);
        if (helper.getAdapterPosition() == pos) helper.getView(R.id.tv_name).setSelected(true);
    }
}

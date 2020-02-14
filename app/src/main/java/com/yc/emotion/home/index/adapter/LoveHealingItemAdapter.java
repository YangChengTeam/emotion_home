package com.yc.emotion.home.index.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.emotion.home.R;
import com.yc.emotion.home.model.bean.LoveHealingBean;

import java.util.List;


public class LoveHealingItemAdapter extends BaseQuickAdapter<LoveHealingBean, BaseViewHolder> {
    public LoveHealingItemAdapter(@Nullable List<LoveHealingBean> data) {
        super(R.layout.recycler_view_item_love_healing, data);
    }



    @Override
    protected void convert(BaseViewHolder helper, LoveHealingBean item) {
        TextView tvName = helper.getView(R.id.item_love_healing_tv_name);
        tvName.setSingleLine(true);
        tvName.setEllipsize(TextUtils.TruncateAt.END);
        tvName.setText(item.chat_name);
    }
}
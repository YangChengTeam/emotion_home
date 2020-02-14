package com.yc.emotion.home.index.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.emotion.home.R;
import com.yc.emotion.home.model.bean.CommunityTagInfo;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

/**
 * Created by suns  on 2019/8/31 09:30.
 */
public class EfficientCourseTagAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private SparseArray<View> itemSparseArray;
    private SparseArray<View> viewSparseArray;

    public EfficientCourseTagAdapter(@Nullable List<String> data) {
        super(R.layout.item_community_tag, data);
        itemSparseArray = new SparseArray<>();
        viewSparseArray = new SparseArray<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        int position = helper.getAdapterPosition();
        setItemParams(helper, position);
        helper.itemView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.community_tag_selector));

        helper.setText(R.id.tv_content, item);
        if (position == 0) {
            helper.itemView.setSelected(true);
            helper.getView(R.id.tv_content).setSelected(true);
        }

        itemSparseArray.put(position, helper.itemView);
        viewSparseArray.put(position, helper.getView(R.id.tv_content));
    }

    private void setItemParams(BaseViewHolder helper, int position) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) helper.itemView.getLayoutParams();

        if (position < 3) {
            layoutParams.topMargin = UIUtil.dip2px(mContext, 10);
        }

        if (position % 3 == 0) {
            layoutParams.rightMargin = UIUtil.dip2px(mContext, 5);
            layoutParams.leftMargin = UIUtil.dip2px(mContext, 5);
        } else if (position % 3 == 1) {
            layoutParams.leftMargin = UIUtil.dip2px(mContext, 5);
            layoutParams.rightMargin = UIUtil.dip2px(mContext, 5);
        } else {
            layoutParams.leftMargin = UIUtil.dip2px(mContext, 5);
            layoutParams.rightMargin = UIUtil.dip2px(mContext, 5);
        }
        helper.itemView.setLayoutParams(layoutParams);
    }

    public void resetView() {
        for (int i = 0; i < itemSparseArray.size(); i++) {
            itemSparseArray.get(i).setSelected(false);
            viewSparseArray.get(i).setSelected(false);
        }
    }

    public void setViewState(int position) {
        itemSparseArray.get(position).setSelected(true);
        viewSparseArray.get(position).setSelected(true);
    }

}

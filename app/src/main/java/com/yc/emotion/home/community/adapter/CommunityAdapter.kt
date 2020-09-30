package com.yc.emotion.home.community.adapter


import android.text.Html
import android.text.TextUtils
import android.util.Log
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.CommonMoreAdapter
import com.yc.emotion.home.model.bean.CommunityInfo
import com.yc.emotion.home.utils.DateUtils
import com.yc.emotion.home.utils.UIUtils

/**
 * Created by suns  on 2019/8/28 15:44.
 */
class CommunityAdapter(data: List<CommunityInfo>?, private val mIsMy: Boolean) : CommonMoreAdapter<CommunityInfo, BaseViewHolder>(R.layout.item_community_view, data) {

    override fun convert(helper: BaseViewHolder, item: CommunityInfo?) {


        item?.let {

            if (TextUtils.isEmpty(item.tag)) {
                item.tag = ""
            }
            val html = Html.fromHtml("<font color='#ff2d55'>#" + item.tag + "#</font>" + item.content)

            helper.setText(R.id.tv_name, item.name)
                    .setText(R.id.tv_content, html)
                    .setText(R.id.tv_message_num, "${item.comment_num}")
                    .setText(R.id.tv_like_num, "${item.like_num}")
                    .addOnClickListener(R.id.iv_like)
                    .addOnClickListener(R.id.ll_like)
                    .addOnClickListener(R.id.iv_avator)
                    .addOnClickListener(R.id.iv_del)


            //        Log.e(TAG, "convert: " + helper.getAdapterPosition() + " --" + item.is_dig);
            helper.setImageResource(R.id.iv_like, if (item.is_dig == 0) R.mipmap.community_like else R.mipmap.community_like_selected)
            helper.setTextColor(R.id.tv_like_num, if (item.is_dig == 0) ContextCompat.getColor(mContext, R.color.gray_999) else ContextCompat.getColor(mContext, R.color.red_crimson))

            Glide.with(mContext).load(item.pic).apply(RequestOptions()
                    .circleCrop().placeholder(R.mipmap.main_icon_default_head)
                    .error(R.mipmap.main_icon_default_head).diskCacheStrategy(DiskCacheStrategy.DATA))
                    .into(helper.getView(R.id.iv_avator))
            try {
                helper.setText(R.id.tv_date, DateUtils.formatTimeToStr(item.create_time, "yyyy-MM-dd"))
            } catch (e: Exception) {
                Log.e(TAG, "convert: " + e.message)
            }

            val commentInfo = item.detail
            if (null != commentInfo) {

                helper.setGone(R.id.ll_comment, true)
                var color = "#427FC3"
                var commentName = commentInfo.name
                if (!TextUtils.isEmpty(commentName)) {
                    if (commentInfo.name.contains("情感导师") || commentInfo.name.contains(UIUtils.getAppName(mContext))) {
                        color = "#1d99f4"
                    }
                } else {
                    commentName = "***** "
                }
                val comment = "<font color=" + color + ">" + commentName + "：</font>" + commentInfo.content
                helper.setText(R.id.tv_comment, Html.fromHtml(comment))
            } else {
                helper.setGone(R.id.ll_comment, false)
            }
            val pos = helper.adapterPosition

            helper.setGone(R.id.iv_del, mIsMy)

            //        if (mIsEnd && pos == mData.size() - 1) {
            //            helper.setGone(R.id.view_divider, false);
            //        } else {
            //            helper.setGone(R.id.view_divider, true);
            //        }
        }
    }


}

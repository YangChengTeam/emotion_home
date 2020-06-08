package com.yc.emotion.home.index.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.widget.CustomLoadMoreView
import com.yc.emotion.home.base.ui.widget.RoundCornerImg
import com.yc.emotion.home.model.bean.SearchContentInfo
import com.yc.emotion.home.model.util.SizeUtils

/**
 *
 * Created by suns  on 2019/10/15 11:14.
 */
class EmotionSearchContentAdapter(mDatas: List<SearchContentInfo>?) : BaseMultiItemQuickAdapter<SearchContentInfo, BaseViewHolder>(mDatas) {

    init {
        addItemType(SearchContentInfo.ITEM_TYPE_ARTICLE, R.layout.layout_search_content_article_item)

        addItemType(SearchContentInfo.ITEM_TYPE_COURSE, R.layout.layout_search_content_course_item)

        addItemType(SearchContentInfo.ITEM_TYPE_EMOTION_TEST, R.layout.layout_search_content_emotion_test_item)

        addItemType(SearchContentInfo.ITEM_TYPE_TUTOR_SERVICE, R.layout.layout_search_content_tutor_service_item)

        addItemType(SearchContentInfo.ITEM_TYPE_DIVIDER, R.layout.item_divider_f2)
        setLoadMoreView(CustomLoadMoreView())
    }

    override fun convert(helper: BaseViewHolder?, item: SearchContentInfo?) {
        helper?.let {
            item?.let {
                val pos = helper.adapterPosition
                when (item.type) {

                    SearchContentInfo.ITEM_TYPE_ARTICLE -> {
                        val articleInfo = item.articleDetailInfo
                        if (item.isShow) {
                            helper.setGone(R.id.rl_search_content_top_title, true)
                        } else {
                            helper.setGone(R.id.rl_search_content_top_title, false)
                        }

                        helper.setText(R.id.tv_content, articleInfo?.post_title)
                                .setText(R.id.tv_reuse, "${articleInfo?.feeluseful}人觉得有用")
                                .setText(R.id.tv_tag, articleInfo?.tag)
                                .addOnClickListener(R.id.tv_all)

                        Glide.with(mContext).load(articleInfo?.image)
                                .apply(RequestOptions().error(R.mipmap.index_example_icon).diskCacheStrategy(DiskCacheStrategy.DATA))
                                .thumbnail(0.1f).into(helper.getView(R.id.roundCornerImg))
                    }

                    SearchContentInfo.ITEM_TYPE_COURSE -> {
                        val courseInfo = item.courseInfo
                        when {

                            item.isShow -> {
                                helper.setVisible(R.id.tv_search_content_classify_title, true)
                                helper.setVisible(R.id.tv_search_content_look_all, false)
                            }
                            item.isExtra -> {
                                helper.setVisible(R.id.tv_search_content_classify_title, false)
                                helper.setVisible(R.id.tv_search_content_look_all, true)
                            }
                            else -> helper.setGone(R.id.rl_search_content_top_title, false)


                        }

                        val layoutParams = helper.itemView.layoutParams as ViewGroup.MarginLayoutParams

                        if (pos % 2 == 0) {
                            layoutParams.leftMargin = SizeUtils.dp2px(mContext, 15f)
                            layoutParams.rightMargin = SizeUtils.dp2px(mContext, 5f)

                        } else {
                            layoutParams.leftMargin = SizeUtils.dp2px(mContext, 5f)
                            layoutParams.rightMargin = SizeUtils.dp2px(mContext, 15f)
                        }
                        helper.itemView.layoutParams = layoutParams

                        val roundCornerImg = helper.getView<RoundCornerImg>(R.id.course_roundImg)


                        courseInfo?.img?.let {
                            Glide.with(mContext).load(courseInfo.img).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)).thumbnail(0.1f).into(roundCornerImg)
                        }


                        helper.setText(R.id.tv_course_count, "${courseInfo?.count}人已学习")
                                .setText(R.id.tv_course_title, courseInfo?.title)
                                .setText(R.id.tv_course_price, courseInfo?.price)
                                .setText(R.id.tv_course_tutor, courseInfo?.name)
                                .addOnClickListener(R.id.tv_search_content_look_all)

                    }

                    SearchContentInfo.ITEM_TYPE_EMOTION_TEST -> {
                        val emotionTestInfo = item.emotionTestInfo
                        if (item.isShow) {
                            helper.setGone(R.id.rl_search_content_top_title, true)
                        } else {
                            helper.setGone(R.id.rl_search_content_top_title, false)
                        }
                        Glide.with(mContext).load(emotionTestInfo?.img).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA))
                                .thumbnail(0.1f).into(helper.getView(R.id.roundCornerImg_emotion))
                        helper.setText(R.id.tv_emtion_title, emotionTestInfo?.title)
                                .setText(R.id.tv_emotion_test_count, "${emotionTestInfo?.people}人已通过测试")


                    }

                    SearchContentInfo.ITEM_TYPE_TUTOR_SERVICE -> {
                        if (pos == 10) {
                            helper.setGone(R.id.rl_search_content_top_title, true)
                        } else {
                            helper.setGone(R.id.rl_search_content_top_title, false)
                        }
                    }
                    else -> {
                    }
                }
            }
        }
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager?

        gridLayoutManager?.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {

                val searchContentInfo = getItem(position)
                if (searchContentInfo != null)
                    if (searchContentInfo.itemType == SearchContentInfo.ITEM_TYPE_COURSE) {
                        return 1
                    }
                return 2


            }
        }

    }


}
package com.yc.emotion.home.message.adapter

import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.widget.CustomLoadMoreView
import com.yc.emotion.home.base.ui.widget.RoundCornerImg
import com.yc.emotion.home.index.ui.activity.LiveWebActivity
import com.yc.emotion.home.message.domain.bean.VideoBannerInfo
import com.yc.emotion.home.message.domain.bean.VideoItemInfo
import com.yc.emotion.home.utils.GlideImageLoader
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import yc.com.rthttplibrary.util.ScreenUtil
import java.util.*


/**
 * Created by suns  on 2020/8/3 17:19.
 */
class VideoItemAdapter(data: List<VideoItemInfo?>?, count: Int) : BaseMultiItemQuickAdapter<VideoItemInfo, BaseViewHolder>(data) {

    private var mCount = count

    init {
        addItemType(VideoItemInfo.ITEM_TOP_BANNER, R.layout.video_layout_banner)
        addItemType(VideoItemInfo.ITEM_TITLE, R.layout.video_layout_title)
        addItemType(VideoItemInfo.ITEM_COURSE, R.layout.index_course_item)
        addItemType(VideoItemInfo.ITEM_VIDEO, R.layout.video_item_view)
        addItemType(VideoItemInfo.ITEM_DIVIDER, R.layout.video_layout_divider)
        setLoadMoreView(CustomLoadMoreView())

    }

    fun setCount(count: Int) {
        this.mCount = count
    }


    override fun convert(helper: BaseViewHolder, item: VideoItemInfo?) {
        item?.let {
            when (it.itemType) {
                VideoItemInfo.ITEM_TOP_BANNER -> {
                    val banner = helper.getView<Banner>(R.id.top_video_banner)
                    val banners = item.banners

                    initBanner(banner, banners)
                }
                VideoItemInfo.ITEM_TITLE -> {
                    helper.setText(R.id.tv_course_title, it.title)
                    if (TextUtils.equals("高效课程", it.title)) {
                        helper.setGone(R.id.tv_more_course, true)
                    } else {
                        helper.setGone(R.id.tv_more_course, false)
                    }
                    helper.addOnClickListener(R.id.tv_more_course)
                }
                VideoItemInfo.ITEM_COURSE -> {
                    val courseInfo = item.courseInfo
                    val itemView = helper.itemView
                    val layoutParams = itemView.layoutParams as ViewGroup.MarginLayoutParams
                    val position = helper.adapterPosition


                    if (position % 2 == 0) {
                        layoutParams.leftMargin = ScreenUtil.dip2px(mContext, 15f)
                        layoutParams.rightMargin = ScreenUtil.dip2px(mContext, 7f)
                    } else {
                        layoutParams.leftMargin = ScreenUtil.dip2px(mContext, 7f)
                        layoutParams.rightMargin = ScreenUtil.dip2px(mContext, 15f)
                    }

                    itemView.layoutParams = layoutParams

                    val roundCornerImg = helper.getView<RoundCornerImg>(R.id.course_roundImg)

                    Glide.with(mContext).load(courseInfo.img)
                            .apply(RequestOptions().error(R.mipmap.home_course_one).diskCacheStrategy(DiskCacheStrategy.DATA))
                            .thumbnail(0.1f).into(roundCornerImg)

                    val imgLayoutParams = roundCornerImg.layoutParams
                    imgLayoutParams.height = ScreenUtil.dip2px(mContext, 120f)
                    roundCornerImg.layoutParams = imgLayoutParams

                    helper.setText(R.id.tv_course_count, "${courseInfo.count}人已学习")
                            .setText(R.id.tv_course_title, courseInfo.title)
                            .setText(R.id.tv_course_price, courseInfo.price)
                            .setText(R.id.tv_course_tutor, courseInfo.name)
                }
                VideoItemInfo.ITEM_VIDEO -> {
                    val videoIem = item.videoItem
                    val ivCover = helper.getView<ImageView>(R.id.iv_video_cover)
                    Glide.with(mContext).load(videoIem.picCover).into(ivCover)
                    helper.setText(R.id.tv_play_count, "${videoIem.playCount}")
                            .setText(R.id.tv_tutor_name, videoIem.tutor_name)
                            .setText(R.id.tv_video_title, videoIem.title)
                    val ivFace = helper.getView<ImageView>(R.id.iv_icon_face)
                    Glide.with(mContext).load(videoIem.tutor_face).error(R.mipmap.tutor_head).apply(RequestOptions().circleCrop())
                            .into(ivFace)

                    val itemView = helper.itemView
                    val layoutParams = itemView.layoutParams as ViewGroup.MarginLayoutParams
                    val position = helper.adapterPosition
                    if (mCount % 2 == 0) {
                        if (position % 2 == 0) {
                            layoutParams.leftMargin = ScreenUtil.dip2px(mContext, 15f)
                            layoutParams.rightMargin = ScreenUtil.dip2px(mContext, 7f)
//                        layoutParams.height = ScreenUtil.dip2px(mContext, 224f)
                        } else {
                            layoutParams.leftMargin = ScreenUtil.dip2px(mContext, 7f)
                            layoutParams.rightMargin = ScreenUtil.dip2px(mContext, 15f)
//                        layoutParams.height = ScreenUtil.dip2px(mContext, 274f)
                        }
                    } else {
                        if (position % 2 == 1) {
                            layoutParams.leftMargin = ScreenUtil.dip2px(mContext, 15f)
                            layoutParams.rightMargin = ScreenUtil.dip2px(mContext, 7f)
//                        layoutParams.height = ScreenUtil.dip2px(mContext, 224f)
                        } else {
                            layoutParams.leftMargin = ScreenUtil.dip2px(mContext, 7f)
                            layoutParams.rightMargin = ScreenUtil.dip2px(mContext, 15f)
//                        layoutParams.height = ScreenUtil.dip2px(mContext, 274f)
                        }
                    }

                    layoutParams.topMargin = ScreenUtil.dip2px(mContext, 12f)
                    itemView.layoutParams = layoutParams


                }
                else -> {
                }
            }
        }
    }

    private fun initBanner(banner: Banner, banners: List<VideoBannerInfo>?) {

        banners?.let {
            val imageList = mutableListOf<String>()
            banners.forEach { bs ->
                imageList.add(bs.cover)
            }

            banner.let {
                //设置banner样式
                it.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                //设置图片加载器
                it.setImageLoader(GlideImageLoader())
                //设置图片集合
                it.setImages(imageList)

                //设置banner动画效果
                it.setBannerAnimation(Transformer.Default)

                //设置自动轮播，默认为true
                it.isAutoPlay(true)
                //设置轮播时间
                it.setDelayTime(2500)
                //设置指示器位置（当banner模式中有指示器时）
                it.setIndicatorGravity(BannerConfig.CENTER)

                //banner设置方法全部调用完毕时最后调用
                it.setOnBannerListener { position ->
                    MobclickAgent.onEvent(mContext, "video_banner_click", "视频页banner点击")
                    //todo banner点击事件
                    val bannerInfo = banners[position]
                    when (bannerInfo.type) {
                        1 -> {
                            val online = bannerInfo.is_online
                            if (online == 0) {//回放
                                val liveRebackUrls = bannerInfo.live_reback_url
                                val index = Random().nextInt(liveRebackUrls.size)
                                LiveWebActivity.startActivity(mContext, liveRebackUrls[index].url, liveRebackUrls[index].cover, bannerInfo.wx)
                            } else if (online == 1) {//直播

                                LiveWebActivity.startActivity(mContext, bannerInfo.jump_url, "", bannerInfo.wx)
                            }

                        }
                        2 -> {
                            gotoView(bannerInfo.jump_url)
                        }

                    }

                }
                it.start()
            }

        }

    }

    private fun gotoView(action: String) {
        try {
            val uri = Uri.parse(action)
            //String android.intent.action.VIEW 比较通用，会根据用户的数据类型打开相应的Activity。如:浏览器,电话,播放器,地图
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            mContext.startActivity(intent)
        } catch (e: Exception) {

        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager?
//        gridLayoutManager.spanCount=object :

//        for (i in 0..gridLayoutManager.childCount) {
//            val videoItemInfo = getItem(i)
//            if (videoItemInfo != null)
//                if (videoItemInfo.itemType == VideoItemInfo.ITEM_COURSE || videoItemInfo.itemType == VideoItemInfo.ITEM_VIDEO) {
//                    gridLayoutManager.spanCount = 3
//                }
//            gridLayoutManager.spanCount = 1
//        }


        gridLayoutManager?.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {

                val videoItemInfo = getItem(position)
                if (videoItemInfo != null)
                    if (videoItemInfo.itemType == VideoItemInfo.ITEM_COURSE || videoItemInfo.itemType == VideoItemInfo.ITEM_VIDEO) {
                        return 1
                    }
                return 2


            }
        }

    }
}
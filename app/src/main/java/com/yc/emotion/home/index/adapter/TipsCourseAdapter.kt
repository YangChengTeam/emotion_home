package com.yc.emotion.home.index.adapter

import android.content.Intent
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.MainActivity
import com.yc.emotion.home.base.ui.widget.CustomLoadMoreView
import com.yc.emotion.home.factory.MainFragmentFactory
import com.yc.emotion.home.index.ui.activity.AudioActivity
import com.yc.emotion.home.model.bean.MainT3Bean
import com.yc.emotion.home.utils.GlideImageLoader
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import java.util.*

/**
 * Created by mayn on 2019/4/30.
 */
class TipsCourseAdapter(data: List<MainT3Bean?>?) : BaseMultiItemQuickAdapter<MainT3Bean?, BaseViewHolder?>(data) {
    private val images: List<Int?>

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    init {
        images = listOf(R.mipmap.main_bg_t3_title, R.mipmap.main_bg_t3_title_audio)
        addItemType(MainT3Bean.LOVE_HEAL_TYPE_TITLE, R.layout.recycler_view_item_t3title)
        addItemType(MainT3Bean.LOVE_HEAL_TYPE_ITEM_TITLE, R.layout.recycler_view_item_t3item_title)
        addItemType(MainT3Bean.LOVE_HEAL_TYPE_ITEM, R.layout.recycler_view_item_t3item)
        addItemType(MainT3Bean.LOVE_HEAL_TYPE_ITEM_LOCALITY, R.layout.recycler_view_item_t3item)
        setLoadMoreView(CustomLoadMoreView())
    }

    override fun convert(helper: BaseViewHolder?, item: MainT3Bean?) {

        helper?.let {
            item?.let {
                when (item.type) {
                    MainT3Bean.LOVE_HEAL_TYPE_TITLE -> {
                        val banner = helper.getView<Banner>(R.id.banner)
                        //设置banner样式
                        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                        //设置图片加载器
                        banner.setImageLoader(GlideImageLoader())
                        //设置图片集合
                        banner.setImages(images)
                        //设置banner动画效果
                        banner.setBannerAnimation(Transformer.Accordion)

                        //设置自动轮播，默认为true
                        banner.isAutoPlay(true)
                        //设置轮播时间
                        banner.setDelayTime(1500)
                        //设置指示器位置（当banner模式中有指示器时）
                        banner.setIndicatorGravity(BannerConfig.CENTER)
                        //banner设置方法全部调用完毕时最后调用
                        banner.setOnBannerListener { position: Int ->
                            if (position == 0) {
                                val intent = Intent(mContext, MainActivity::class.java)
                                intent.putExtra("pos", MainFragmentFactory.MAIN_FRAGMENT_3)
                                mContext.startActivity(intent)
                                MobclickAgent.onEvent(mContext, "pattern_expression_id", "花样表白")
                            } else if (position == 1) {
                                MobclickAgent.onEvent(mContext, "audio_id", "音频")
                                mContext.startActivity(Intent(mContext, AudioActivity::class.java))
                            }
                        }
                        banner.start()
                        helper.addOnClickListener(R.id.item_t3title_iv_title)
                                .addOnClickListener(R.id.item_t3title_tv_icon_01)
                                .addOnClickListener(R.id.item_t3title_tv_icon_02)
                                .addOnClickListener(R.id.item_t3title_tv_icon_03)
                                .addOnClickListener(R.id.item_t3title_tv_icon_04)
                                .addOnClickListener(R.id.item_t3title_tv_icon_05) //                            .addOnClickListener(R.id.iv_practice_teach)
                                .addOnClickListener(R.id.iv_practice_love)
                    }
                    MainT3Bean.LOVE_HEAL_TYPE_ITEM_TITLE -> helper.setText(R.id.item_t3item_title_tv_name, item.titleName)
                    MainT3Bean.LOVE_HEAL_TYPE_ITEM -> {
                        val imageView = helper.getView<ImageView>(R.id.item_t3item_iv)
                        helper.setText(R.id.item_t3item_tv_title, item.name)
                                .setText(R.id.item_t3item_tv_des, item.desp)
                        val image = item.image

                        //设置图片圆角角度
                        val roundedCorners = RoundedCorners(6)
                        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
                        val options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300)
                        Glide.with(mContext).asBitmap().load(image).apply(options.diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(R.mipmap.main_bg_t3_placeholder)
                                .error(R.mipmap.main_bg_t3_placeholder).dontAnimate()).thumbnail(0.1f).into(imageView)
                    }
                    MainT3Bean.LOVE_HEAL_TYPE_ITEM_LOCALITY -> {

                        helper.setText(R.id.item_t3item_tv_title, item.name)
                                .setText(R.id.item_t3item_tv_des, item.desp)
                        val imageResourceld = item.imageResourceld
                        if (imageResourceld > 0) {
                            var imageResourceldId: Drawable? = null
                            try {
                                imageResourceldId = mContext.resources.getDrawable(imageResourceld)
                            } catch (e: Exception) {
                            }
                            if (imageResourceldId != null) {
                                helper.setImageDrawable(R.id.item_t3item_iv, imageResourceldId)
                            } else {
                            }
                        } else {
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
        gridLayoutManager!!.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val mainT3Bean = mData[position]!!
                var spansize = 1
                when (mainT3Bean.type) {
                    MainT3Bean.LOVE_HEAL_TYPE_TITLE, MainT3Bean.LOVE_HEAL_TYPE_ITEM_TITLE -> spansize = 2 //占据2列
                }
                return spansize
            }
        }
    }


}
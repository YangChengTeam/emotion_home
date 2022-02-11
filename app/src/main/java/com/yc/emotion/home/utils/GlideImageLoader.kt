package com.yc.emotion.home.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.widget.RoundCornerImg
import com.youth.banner.loader.ImageLoader

/**
 * Created by suns  on 2019/9/4 15:08.
 */
class GlideImageLoader(private val corner: Int=20) : ImageLoader() {
    override fun displayImage(context: Context, path: Any, imageView: ImageView) {
        //Glide 加载图片简单用法
        //设置图片圆角角度
//        RoundedCorners roundedCorners = new RoundedCorners(30);
//        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
//        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
        Glide.with(context).load(path).apply(RequestOptions().error(R.mipmap.home_banner).diskCacheStrategy(DiskCacheStrategy.DATA)).into(imageView)
    }

    override fun createImageView(context: Context): ImageView {
        val roundCornerImg = RoundCornerImg(context, null)
        roundCornerImg.setCorner(corner)
        return roundCornerImg
    }
}
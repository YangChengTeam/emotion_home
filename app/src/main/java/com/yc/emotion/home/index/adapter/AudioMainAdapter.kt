package com.yc.emotion.home.index.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseViewHolder
import com.music.player.lib.bean.MusicInfo
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.CommonMoreAdapter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by wanglin  on 2019/7/20 10:08.
 */
class AudioMainAdapter(data: List<MusicInfo?>?) : CommonMoreAdapter<MusicInfo?, BaseViewHolder?>(R.layout.audio_main_item, data) {
    override fun convert(helper: BaseViewHolder?, item: MusicInfo?) {

        helper?.let {
            item?.let {
                helper.setText(R.id.tv_audio_title, item.title)
                        .setText(R.id.tv_audio_desc, item.desp)
                        .setText(R.id.tv_audio_play_num, "播放量" + item.play_num)
                val sd = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
                try {
                    helper.setText(R.id.tv_audio_time, sd.format(Date(item.add_time * 1000)) + "上新")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val ivCover = helper.getView<ImageView>(R.id.iv_audio_cover)

//            //设置图片圆角角度
//            RoundedCorners roundedCorners = new RoundedCorners(15);
//
//            //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
//            RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
                Glide.with(mContext).asBitmap().load(item.img).apply(RequestOptions().error(R.mipmap.audio_cover)
                        .placeholder(R.mipmap.audio_holder).diskCacheStrategy(DiskCacheStrategy.DATA).skipMemoryCache(false))
                        .thumbnail(0.1f).into(ivCover)
            }
        }


    }
}
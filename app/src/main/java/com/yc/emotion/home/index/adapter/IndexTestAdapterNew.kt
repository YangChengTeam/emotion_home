package com.yc.emotion.home.index.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.BaseQuickImproAdapter
import com.yc.emotion.home.model.bean.EmotionTestInfo

/**
 * Created by suns  on 2019/9/28 10:57.
 */
class IndexTestAdapterNew(emotionTestInfos: List<EmotionTestInfo>?) : BaseQuickImproAdapter<EmotionTestInfo, BaseViewHolder>(R.layout.fragment_index_test, emotionTestInfos) {


    override fun convert(helper: BaseViewHolder, item: EmotionTestInfo?) {
        val imageView = helper.getView<ImageView>(R.id.image)


//        cardView.setOnClickListener { v: View? ->
//            EmotionTestDescActivity.startActivity(mContext, emotionTestInfo.id)
//            MobclickAgent.onEvent(mContext, "emotion_test_click", "情感测试点击")
//        }
        helper.setText(R.id.tv_test_title, item?.title)
                .setText(R.id.tv_test_count, String.format(mContext.getString(R.string.test_count), item?.people))

//        Log.e("TAG", "instantiateItem: "+imageUrl );
        Glide.with(mContext).load(item?.img).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA).circleCrop()).into(imageView)
    }

}
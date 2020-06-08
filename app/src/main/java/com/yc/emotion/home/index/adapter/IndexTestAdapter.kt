package com.yc.emotion.home.index.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.yc.emotion.home.R
import com.yc.emotion.home.index.ui.activity.EmotionTestDescActivity.Companion.startActivity
import com.yc.emotion.home.model.bean.EmotionTestInfo

/**
 * Created by suns  on 2019/9/28 10:57.
 */
class IndexTestAdapter(private val mContext: Context, private val emotionTestInfos: List<EmotionTestInfo>) : PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val emotionTestInfo = emotionTestInfos[position % emotionTestInfos.size]
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_index_test, null)
        val imageView = view.findViewById<ImageView>(R.id.image)
        val tvTestTitle = view.findViewById<TextView>(R.id.tv_test_title)
        val tvTestCount = view.findViewById<TextView>(R.id.tv_test_count)
        val cardView: CardView = view.findViewById(R.id.cardView_emotion_test)
        tvTestTitle.text = emotionTestInfo.title
        tvTestCount.text = String.format(mContext.getString(R.string.test_count), emotionTestInfo.people)
        cardView.setOnClickListener { v: View? -> startActivity(mContext, emotionTestInfo.id) }

//        Log.e("TAG", "instantiateItem: "+imageUrl );
        Glide.with(container.context).load(emotionTestInfo.img).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA).circleCrop()).into(imageView)
        container.addView(view)
        return view
    }

    override fun getCount(): Int {
        return emotionTestInfos.size
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}
package com.yc.emotion.home.base.ui.adapter

import android.app.Activity
import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.yc.emotion.home.R
import com.yc.emotion.home.index.ui.activity.SelectSexActivity
import com.yc.emotion.home.mine.ui.activity.LoginMainActivity
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.utils.Preference

/**
 *
 * Created by suns  on 2019/10/31 14:40.
 */
class GuideAdapter(context: Activity, images: List<Int>) : PagerAdapter() {
    private val mImages = images
    private val mContext = context

    private var isFirstLogin by Preference(ConstantKey.IS_FIRST_LOGIN, true)

    private var sex by Preference(ConstantKey.SEX, 0)

    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1
    }

    override fun getCount(): Int {
        return this.mImages.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_guide_view, container, false)

        val image = mImages[position]
        val ivGuideBtn = view.findViewById<ImageView>(R.id.iv_guide_btn)
        val ivGuide = view.findViewById<ImageView>(R.id.iv_guide)
        ivGuide.setImageResource(image)

        if (position == 2) {
            ivGuideBtn.visibility = View.VISIBLE
        } else {
            ivGuideBtn.visibility = View.GONE
        }
        ivGuideBtn.setOnClickListener {
            //            if (isFirstLogin) {
//                val intent = Intent(mContext, LoginMainActivity::class.java)
//                mContext.startActivity(intent)
//                mContext.finish()
//                isFirstLogin = false
//            }

            if (sex == 0) {
                val intent = Intent(mContext, SelectSexActivity::class.java)
                mContext.startActivity(intent)
                mContext.finish()
            }
        }


        container.addView(view)

        return view


    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
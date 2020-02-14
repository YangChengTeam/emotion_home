package com.yc.emotion.home.base.ui.activity

import android.os.Bundle
import android.view.View
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.GuideAdapter
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.utils.Preference
import kotlinx.android.synthetic.main.activity_guide.*

/**
 *
 * Created by suns  on 2019/10/31 14:34.
 */
class GuideActivity : BaseSameActivity() {


//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_guide
    }


    private val imagesArr = intArrayOf(R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3)

    override fun initViews() {
//        ivBack.visibility = View.GONE
        val guideAdapter = GuideAdapter(this, imagesArr.toList())
        guide_viewpager.adapter = guideAdapter
    }

    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    override fun hindActivityBar(): Boolean {
        return true
    }

    override fun hindActivityTitle(): Boolean {
        return true
    }

    override fun offerActivityTitle(): String {
        return ""
    }
}
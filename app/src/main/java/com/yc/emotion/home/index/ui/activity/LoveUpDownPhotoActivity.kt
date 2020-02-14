package com.yc.emotion.home.index.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import cn.youngkaaa.yviewpager.YViewPager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.base.ui.adapter.CommonMainPageVerticalAdapter
import com.yc.emotion.home.index.ui.fragment.LoveUpDownPhotoFragment
import kotlinx.android.synthetic.main.activity_love_up_down_photo.*
import java.util.*

class LoveUpDownPhotoActivity : BaseSameActivity() {



    private var mClickPosition: Int = 0
    private var mChildUrl: String? = null




    override fun initIntentData() {
        val intent = intent
        mClickPosition = intent.getIntExtra("position", -1)
        mChildUrl = intent.getStringExtra("childUrl")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        initViews()
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_love_up_down_photo
    }

    override fun initViews() {

        val datas = ArrayList<String>()
        val fragmentList = ArrayList<Fragment>()
        for (i in 0..9999) {
            datas.add("itme $i")
            fragmentList.add(LoveUpDownPhotoFragment.newInstance(mChildUrl, "itme $i", i))
        }
        val viewPager = findViewById<YViewPager>(R.id.love_up_down_photo_viewpager)


        val commonMainPageAdapter = CommonMainPageVerticalAdapter(supportFragmentManager, fragmentList)

        //        LoveUpDownPhotoPagerAdapter loveUpDownPhotoPagerAdapter = new LoveUpDownPhotoPagerAdapter(getSupportFragmentManager(), mChildUrl, datas);
        viewPager.adapter = commonMainPageAdapter
        viewPager.currentItem = mClickPosition


        comp_main_iv_to_wx.setOnClickListener(this)
    }


    override fun offerActivityTitle(): String {
        return "1"
    }

    override fun hindActivityTitle(): Boolean {
        return true
    }

    override fun hindActivityBar(): Boolean {
        return true
    }


    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {

            R.id.comp_main_iv_to_wx -> showToWxServiceDialog()
        }//                collectAudio(musicInfo);
    }

    companion object {

        fun startLoveUpDownPhotoActivity(context: Context, position: Int, childUrl: String) {
            val intent = Intent(context, LoveUpDownPhotoActivity::class.java)
            intent.putExtra("position", position)
            intent.putExtra("childUrl", childUrl)
            context.startActivity(intent)
        }
    }
}

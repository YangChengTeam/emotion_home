package com.yc.emotion.home.base.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.yc.emotion.home.R

/**
 * Created by mayn on 2019/4/30.
 */
abstract class BaseSameActivity : BaseSlidingActivity(), View.OnClickListener {

    protected lateinit var mBaseSameTvSub: TextView
    public lateinit var mTvTitle: TextView
    protected lateinit var ivBack: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initIntentData()
        super.setContentView(R.layout.activity_base_same)
        if (!isTaskRoot) {
            /* If this is not the root activity,finish it.*/
            val intent = intent
            val action = intent.action
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN == action) {
                Log.d("mylog", "Activity is not the root. Finishing Activity instead of launching.")
                finish()
                return
            }
        }
        invadeStatusBar() //侵入状态栏
        setAndroidNativeLightStatusBar() //状态栏字体颜色改变
    }

    override fun setContentView(layoutId: Int) {
        setContentView(View.inflate(this, layoutId, null))
    }

    override fun setContentView(view: View) {
        val linearLayout = findViewById<LinearLayout>(R.id.activity_base_same_linear_layout)
        val viewBar = findViewById<View>(R.id.activity_base_same_view_bar)
        mTvTitle = findViewById(R.id.activity_base_same_tv_title)
        val rlTitleCon = findViewById<RelativeLayout>(R.id.activity_base_same_rl_title_con)
        ivBack = findViewById(R.id.activity_base_same_iv_back)
        mBaseSameTvSub = findViewById(R.id.activity_base_same_tv_sub)
        val isHind = hindActivityTitle()
        if (isHind) {
            rlTitleCon.visibility = View.GONE
        } else {
            rlTitleCon.visibility = View.VISIBLE
        }
        if (hindActivityBar()) {
            viewBar.visibility = View.GONE
        } else {
            setStateBarHeight(viewBar)
        }
        mTvTitle.text = offerActivityTitle()
        mBaseSameTvSub.text = offerActivitySubTitle()
        ivBack.setOnClickListener(View.OnClickListener { v: View? ->
            val onBack = childDisposeOnBack()
            if (onBack) {
                return@OnClickListener
            }
            finish()
        })
        //        tvSub.setOnClickListener(this);
        linearLayout.addView(view, ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT))
    }

    /*  protected void setSubIcon(@DrawableRes int right) {
        mBaseSameTvSub.setCompoundDrawablesWithIntrinsicBounds(0, 0, right, 0);
    }*/
    fun setBarTitle(barTitle: String?) {
        mTvTitle.text = barTitle
    }

    protected open fun hindActivityTitle() = false

    protected open fun hindActivityBar() = false


    protected fun offerActivitySubTitle() = ""


    protected fun offerActivitySubTitleView() = mBaseSameTvSub


    protected abstract fun offerActivityTitle(): String?
    protected open fun initIntentData() {}
    override fun onClick(v: View) {}
    protected open fun childDisposeOnBack(): Boolean {
        return false
    }

    override fun initViews() {}
}
package com.yc.emotion.home.base.ui.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.yc.emotion.home.R

/**
 * Created by mayn on 2019/4/27.
 */
class MainNavigationLin(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private var mTvDes: TextView? = null
    private fun initView(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MainNavigationLin)
        val text = typedArray.getString(R.styleable.MainNavigationLin_textName)
        val inflate = LayoutInflater.from(context).inflate(R.layout.layout_main_navigation_lin, this, true)
        mTvDes = inflate.findViewById(R.id.main_navigation_lin_tv_des)
        mTvDes.setText(text)
    }

    fun setDes(des: String?) {
        if (!TextUtils.isEmpty(des)) {
            mTvDes!!.text = des
        }
    }

    init {
        initView(context, attrs)
    }
}
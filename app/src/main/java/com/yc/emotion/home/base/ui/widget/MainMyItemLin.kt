package com.yc.emotion.home.base.ui.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.yc.emotion.home.R

/**
 * Created by mayn on 2019/4/28.
 */
class MainMyItemLin(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private lateinit var mTvSub: TextView

    private fun initView(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MainMyItemLin)
        try {
            val text = typedArray.getString(R.styleable.MainMyItemLin_textNameMyItem)
            val textSub = typedArray.getString(R.styleable.MainMyItemLin_textSubMyItem)
            val imgSrc = typedArray.getDrawable(R.styleable.MainMyItemLin_imgSrc)
            val isAddIntervalTop = typedArray.getBoolean(R.styleable.MainMyItemLin_isAddIntervalTop, false)
            val isAddIntervalBom = typedArray.getBoolean(R.styleable.MainMyItemLin_isAddIntervalBom, false)
            val inflate = LayoutInflater.from(context).inflate(R.layout.layout_main_my_item_lin, this, true)
            val tvDes = inflate.findViewById<TextView>(R.id.main_my_item_lin_tv_des)
            mTvSub = inflate.findViewById(R.id.main_my_item_lin_tv_sub)
            val viewLineTop = inflate.findViewById<View>(R.id.main_my_item_lin_view_line_top)
            val viewLineBom = inflate.findViewById<View>(R.id.main_my_item_lin_view_line_bom)
            val ivIcon = inflate.findViewById<ImageView>(R.id.main_my_item_lin_iv_icon)
            if (!isAddIntervalTop) {
                viewLineTop.visibility = View.GONE
            }
            if (!isAddIntervalBom) {
                viewLineBom.visibility = View.GONE
            }
            tvDes.text = text
            mTvSub.text = textSub
            ivIcon.setImageDrawable(imgSrc)
        } catch (e: Exception) {
        } finally {
            typedArray.recycle()
        }
    }

    /*    public void setDes(String des) {
            if (!TextUtils.isEmpty(des)) {
                mTvDes.setText(des);
            }
        }*/
    fun setSub(des: String?) {
        if (!TextUtils.isEmpty(des)) {
            mTvSub.text = des
        }
    }

    //    private TextView mTvDes;
    init {
        initView(context, attrs)
    }
}
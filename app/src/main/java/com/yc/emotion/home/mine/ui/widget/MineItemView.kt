package com.yc.emotion.home.mine.ui.widget

import android.content.Context
import android.support.annotation.Nullable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.widget.BaseView

/**
 *
 * Created by suns  on 2019/10/16 19:10.
 */
class MineItemView(@Nullable context: Context, @Nullable attrs: AttributeSet) : BaseView(context, attrs) {

    private var moreTv: TextView? = null

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.MineItemView)
        try {
            val text = ta.getString(R.styleable.MineItemView_item_title)

            val isShowMoreText = ta.getBoolean(R.styleable.MineItemView_is_show_more_text, false)
            val isAddIntervalBom = ta.getBoolean(R.styleable.MineItemView_is_show_divider, true)
            val isShowArrow = ta.getBoolean(R.styleable.MineItemView_is_show_arrow, true)

            val moreText = ta.getString(R.styleable.MineItemView_more_text)


            val tvDes = getView(R.id.main_mine_item_title) as TextView

            val divider = getView(R.id.main_mine_divider) as View
            moreTv = getView(R.id.tv_more_text) as TextView
            val ivArrow = getView(R.id.iv_right_arrow) as ImageView

            divider.visibility = if (isAddIntervalBom) View.VISIBLE else View.GONE
            moreTv?.visibility = if (isShowMoreText) View.VISIBLE else View.GONE
            ivArrow.visibility = if (isShowArrow) View.VISIBLE else View.GONE

            tvDes.text = text
            if (!TextUtils.isEmpty(moreText))
                moreTv?.text = moreText

        } catch (e: Exception) {
        } finally {
            ta.recycle()
        }

    }


    fun setMoreText(text: String?) {
        moreTv?.text = text
    }

    fun getMoreText(): CharSequence? {
        return moreTv?.text
    }

    override fun getLayoutId(): Int {

        return R.layout.layout_main_mine_item_lin
    }
}
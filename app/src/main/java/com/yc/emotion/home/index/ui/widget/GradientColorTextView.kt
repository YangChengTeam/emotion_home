package com.yc.emotion.home.index.ui.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.yc.emotion.home.R

/**
 * Created by suns  on 2019/10/15 19:12.
 */
class GradientColorTextView(context: Context, attrs: AttributeSet?) : AppCompatTextView(context, attrs) {
    private var mLinearGradient: LinearGradient? = null
    private lateinit var mPaint: Paint
    private var mViewWidth = 0
    private val mTextBound = Rect()
    private val startColor: Int
    private val endColor: Int
    override fun onDraw(canvas: Canvas) {
        mViewWidth = measuredWidth
        mPaint = paint
        val mTipText = text.toString()
        mPaint.getTextBounds(mTipText, 0, mTipText.length, mTextBound)
        mLinearGradient = LinearGradient(0f, 0f, mViewWidth.toFloat(), 0f, intArrayOf(startColor, endColor),
                null, Shader.TileMode.REPEAT)
        mPaint.shader = mLinearGradient
        canvas.drawText(mTipText, measuredWidth / 2 - mTextBound.width() / 2.toFloat(), measuredHeight / 2 + mTextBound.height() / 2.toFloat(), mPaint)
    }

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.GradientColorTextView)
        //        0xFFFFEABA
//        0xFFBE8B49
        startColor = ta.getColor(R.styleable.GradientColorTextView_startColor, 0xFFFFEABA.toInt())
        endColor = ta.getColor(R.styleable.GradientColorTextView_endColor, -0x4174b7)
        ta.recycle()
    }
}
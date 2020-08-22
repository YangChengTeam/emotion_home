package com.yc.emotion.home.base.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PaintFlagsDrawFilter
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.yc.emotion.home.R

/**
 * Created by mayn on 2019/4/28.
 */
class RoundCornerImg(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {
    private var width = 0f
    private var height = 0f
    private var corners = 12
    private var pfd: PaintFlagsDrawFilter? = null

    private fun init(context: Context, attrs: AttributeSet) {
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
        val ta = context.obtainStyledAttributes(attrs, R.styleable.RoundCornerImg)

//        try {
//            corners = ta.getDimensionPixelSize(R.styleable.RoundCornerImg_cornerRadius, 12);
//        } finally {
//            ta.recycle();
//        }
        pfd = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        ta.recycle()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        width = getWidth().toFloat()
        height = getHeight().toFloat()
    }

    fun setCorner(corner: Int) {
        corners = corner
    }

    override fun onDraw(canvas: Canvas) {
        if (width >= 12 && height > 12) {
            val path = Path()
            //四个圆角
            path.moveTo(corners.toFloat(), 0f)
            path.lineTo(width - corners, 0f)
            path.quadTo(width, 0f, width, corners.toFloat())
            path.lineTo(width, height - corners)
            path.quadTo(width, height, width - corners, height)
            path.lineTo(corners.toFloat(), height)
            path.quadTo(0f, height, 0f, height - corners)
            path.lineTo(0f, corners.toFloat())
            path.quadTo(0f, 0f, corners.toFloat(), 0f)
            path.close()
            canvas.drawFilter = pfd
            canvas.clipPath(path)
        }
        super.onDraw(canvas)
    }

    init {
        init(context, attrs)
    }
}
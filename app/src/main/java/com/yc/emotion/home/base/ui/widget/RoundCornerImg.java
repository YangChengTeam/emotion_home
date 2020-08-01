package com.yc.emotion.home.base.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.yc.emotion.home.R;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Created by mayn on 2019/4/28.
 */

public class RoundCornerImg extends AppCompatImageView {
    private float width, height;
    private int corners = 12;
    private PaintFlagsDrawFilter pfd;

    public RoundCornerImg(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundCornerImg);

//        try {
//            corners = ta.getDimensionPixelSize(R.styleable.RoundCornerImg_cornerRadius, 12);
//        } finally {
//            ta.recycle();
//        }

        pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    public void setCorner(int corner) {
        this.corners = corner;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (width >= 12 && height > 12) {
            Path path = new Path();
            //四个圆角
            path.moveTo(corners, 0);
            path.lineTo(width - corners, 0);
            path.quadTo(width, 0, width, corners);

            path.lineTo(width, height - corners);
            path.quadTo(width, height, width - corners, height);
            path.lineTo(corners, height);
            path.quadTo(0, height, 0, height - corners);
            path.lineTo(0, corners);
            path.quadTo(0, 0, corners, 0);
            path.close();
            canvas.setDrawFilter(pfd);
            canvas.clipPath(path);

        }
        super.onDraw(canvas);
    }
}


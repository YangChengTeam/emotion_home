package com.yc.emotion.home.index.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.yc.emotion.home.R;


/**
 * Created by suns  on 2019/10/15 19:12.
 */
public class GradientColorTextView extends AppCompatTextView {

    private LinearGradient mLinearGradient;
    private Paint mPaint;
    private int mViewWidth = 0;
    private Rect mTextBound = new Rect();
    private final int startColor;
    private final int endColor;

    public GradientColorTextView (Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta= context.obtainStyledAttributes(attrs,R.styleable.GradientColorTextView);

        startColor = ta.getColor(R.styleable.GradientColorTextView_startColor,0xFFFFEABA);
        endColor = ta.getColor(R.styleable.GradientColorTextView_endColor,0xFFBE8B49);

        ta.recycle();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        mViewWidth = getMeasuredWidth();
        mPaint = getPaint();
        String mTipText = getText().toString();
        mPaint.getTextBounds(mTipText, 0, mTipText.length(), mTextBound);
        mLinearGradient = new LinearGradient(0, 0, mViewWidth, 0,
                new int[]{startColor, endColor},
                null, Shader.TileMode.REPEAT);
        mPaint.setShader(mLinearGradient);
        canvas.drawText(mTipText, getMeasuredWidth() / 2 - mTextBound.width() / 2, getMeasuredHeight() / 2 + mTextBound.height() / 2, mPaint);
    }
}


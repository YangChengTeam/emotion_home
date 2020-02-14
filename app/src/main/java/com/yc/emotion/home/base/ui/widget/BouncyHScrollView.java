package com.yc.emotion.home.base.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.HorizontalScrollView;

/**
 * Created by suns  on 2019/9/25 17:16.
 */
public class BouncyHScrollView extends HorizontalScrollView {

    private static final int MAX_X_OVERSCROLL_DISTANCE = 50;
    private Context mContext;
    private int mMaxXOverscrollDistance;

    public BouncyHScrollView(Context context) {
        super(context);

        mContext = context;
        initBounceDistance();
    }

    public BouncyHScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        initBounceDistance();
    }

    public BouncyHScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mContext = context;
        initBounceDistance();
    }

    private void initBounceDistance() {
        final DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        mMaxXOverscrollDistance = (int) (metrics.density * MAX_X_OVERSCROLL_DISTANCE);

//        getChildCount()
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        //这块是关键性代码
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, mMaxXOverscrollDistance, maxOverScrollY, isTouchEvent);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (changeListener != null) {
            changeListener.onScrolled(l, t, oldl, oldt);
        }
    }


    private onScrollChangeListener changeListener;

    public void setOnScrollChangeListener(onScrollChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public interface onScrollChangeListener {
        void onScrolled(int l, int t, int oldl, int oldt);
    }

}


package com.yc.emotion.home.message.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.yc.emotion.home.R;




public class ProgressView extends View {
    public static final int MODE_DETERMINATE = 0;
    public static final int MODE_INDETERMINATE = 1;
    private Drawable mProgressDrawable;
    private int mProgressId;

    public ProgressView(Context context) {
        this(context, null, 0, 0);
    }

    private void init(Context context, AttributeSet attributeSet, int i, int i2) {
        applyStyle(context, attributeSet, i, i2);
    }

    private boolean needCreateProgress(boolean z) {
        Drawable drawable = this.mProgressDrawable;
        if (drawable == null) {
            return true;
        }
        return true;
    }

    /* Access modifiers changed, original: protected */
    public void applyStyle(Context context, AttributeSet attributeSet, int i, int i2) {
        if (needCreateProgress(true)) {
            this.mProgressId = 0;
            int i3 = R.style.CircularProgress;
            this.mProgressId = i3;
            CircularProgressDrawable build = new CircularProgressDrawable.Builder(context, i3, isInEditMode()).build();
            this.mProgressDrawable = build;
            setBackground(build);
        } else if (this.mProgressId != 0) {
            this.mProgressId = 0;
            ((CircularProgressDrawable) this.mProgressDrawable).applyStyle(context, 0);
        }
    }

    public void start() {
        Drawable drawable = this.mProgressDrawable;
        if (drawable != null) {
            ((Animatable) drawable).start();
        }
    }

    public void stop() {
        Drawable drawable = this.mProgressDrawable;
        if (drawable != null) {
            ((Animatable) drawable).stop();
        }
    }

    public ProgressView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0);
    }

    public ProgressView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    @TargetApi(16)
    public ProgressView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i);
        init(context, attributeSet, i, i2);
    }
}
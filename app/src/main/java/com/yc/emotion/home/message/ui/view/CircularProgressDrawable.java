package com.yc.emotion.home.message.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.yc.emotion.home.R;
import com.yc.emotion.home.utils.ColorUtil;


import androidx.core.graphics.ColorUtils;
import androidx.core.view.ViewCompat;


public class CircularProgressDrawable extends Drawable implements Animatable {
    private static final int PROGRESS_STATE_HIDE = -1;
    private static final int PROGRESS_STATE_KEEP_SHRINK = 3;
    private static final int PROGRESS_STATE_KEEP_STRETCH = 1;
    private static final int PROGRESS_STATE_SHRINK = 2;
    private static final int PROGRESS_STATE_STRETCH = 0;
    private static final int RUN_STATE_RUNNING = 3;
    private static final int RUN_STATE_STARTED = 2;
    private static final int RUN_STATE_STARTING = 1;
    private static final int RUN_STATE_STOPPED = 0;
    private static final int RUN_STATE_STOPPING = 4;
    private static TypedValue value;
    private int mInAnimationDuration;
    private int[] mInColors;
    private float mInStepPercent;
    private float mInitialAngle;
    private int mKeepDuration;
    private long mLastProgressStateTime;
    private long mLastRunStateTime;
    private long mLastUpdateTime;
    private float mMaxSweepAngle;
    private float mMinSweepAngle;
    private int mOutAnimationDuration;
    private int mPadding;
    private Paint mPaint;
    private int mProgressMode;
    private float mProgressPercent;
    private int mProgressState;
    private RectF mRect;
    private boolean mReverse;
    private int mRotateDuration;
    private int mRunState;
    private float mSecondaryProgressPercent;
    private float mStartAngle;
    private int mStrokeColorIndex;
    private int[] mStrokeColors;
    private int mStrokeSecondaryColor;
    private int mStrokeSize;
    private float mSweepAngle;
    private int mTransformDuration;
    private Interpolator mTransformInterpolator;
    private final Runnable mUpdater;

    public static class Builder {
        protected boolean isInEditMode;
        private int mInAnimationDuration;
        private int[] mInColors;
        private float mInStepPercent;
        private float mInitialAngle;
        private int mKeepDuration;
        private float mMaxSweepAngle;
        private float mMinSweepAngle;
        private int mOutAnimationDuration;
        private int mPadding;
        private int mProgressMode;
        private float mProgressPercent;
        private boolean mReverse;
        private int mRotateDuration;
        private float mSecondaryProgressPercent;
        private int[] mStrokeColors;
        private int mStrokeSecondaryColor;
        private int mStrokeSize;
        private int mTransformDuration;
        private Interpolator mTransformInterpolator;

        public CircularProgressDrawable build() {
            if (this.mStrokeColors == null) {
                this.mStrokeColors = new int[]{-16737793};
            }
            if (this.mInColors == null && this.mInAnimationDuration > 0) {
                this.mInColors = new int[]{-4860673, -2168068, -327682};
            }
            if (this.mTransformInterpolator == null) {
                this.mTransformInterpolator = new DecelerateInterpolator();
            }

            return new CircularProgressDrawable(this.mPadding, this.mInitialAngle, this.mProgressPercent, this.mSecondaryProgressPercent, this.mMaxSweepAngle, this.mMinSweepAngle, this.mStrokeSize, this.mStrokeColors, this.mStrokeSecondaryColor, this.mReverse, this.mRotateDuration, this.mTransformDuration, this.mKeepDuration, this.mTransformInterpolator, this.mProgressMode, this.mInAnimationDuration, this.mInStepPercent, this.mInColors, this.mOutAnimationDuration);
        }

        public Builder inAnimDuration(int i) {
            this.mInAnimationDuration = i;
            return this;
        }

        public Builder inStepColors(int... iArr) {
            this.mInColors = iArr;
            return this;
        }

        public Builder inStepPercent(float f) {
            this.mInStepPercent = f;
            return this;
        }

        public Builder initialAngle(float f) {
            this.mInitialAngle = f;
            return this;
        }

        public Builder keepDuration(int i) {
            this.mKeepDuration = i;
            return this;
        }

        public Builder maxSweepAngle(float f) {
            this.mMaxSweepAngle = f;
            return this;
        }

        public Builder minSweepAngle(float f) {
            this.mMinSweepAngle = f;
            return this;
        }

        public Builder outAnimDuration(int i) {
            this.mOutAnimationDuration = i;
            return this;
        }

        public Builder padding(int i) {
            this.mPadding = i;
            return this;
        }

        public Builder progressMode(int i) {
            this.mProgressMode = i;
            return this;
        }

        public Builder progressPercent(float f) {
            this.mProgressPercent = f;
            return this;
        }

        public Builder reverse(boolean z) {
            this.mReverse = z;
            return this;
        }

        public Builder rotateDuration(int i) {
            this.mRotateDuration = i;
            return this;
        }

        public Builder secondaryProgressPercent(float f) {
            this.mSecondaryProgressPercent = f;
            return this;
        }

        public Builder strokeColors(int... iArr) {
            this.mStrokeColors = iArr;
            return this;
        }

        public Builder strokeSecondaryColor(int i) {
            this.mStrokeSecondaryColor = i;
            return this;
        }

        public Builder strokeSize(int i) {
            this.mStrokeSize = i;
            return this;
        }

        public Builder transformDuration(int i) {
            this.mTransformDuration = i;
            return this;
        }

        public Builder transformInterpolator(Interpolator interpolator) {
            this.mTransformInterpolator = interpolator;
            return this;
        }

        public Builder(Context context, int i, boolean z) {
            this(context, null, 0, i, z);
        }

        public Builder reverse() {
            return reverse(true);
        }

        public Builder(Context context, AttributeSet attributeSet, int i, int i2, boolean z) {
            TypedArray obtainTypedArray;
            int[] iArr;
            this.isInEditMode = z;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.CircularProgressDrawable, i, i2);
            padding(obtainStyledAttributes.getDimensionPixelSize(R.styleable.CircularProgressDrawable_cpd_padding, 0));
            initialAngle((float) obtainStyledAttributes.getInteger(R.styleable.CircularProgressDrawable_cpd_initialAngle, 0));
            progressPercent(obtainStyledAttributes.getFloat(R.styleable.CircularProgressDrawable_pv_progress, 0.0f));
            secondaryProgressPercent(obtainStyledAttributes.getFloat(R.styleable.CircularProgressDrawable_pv_secondaryProgress, 0.0f));
            maxSweepAngle((float) obtainStyledAttributes.getInteger(R.styleable.CircularProgressDrawable_cpd_maxSweepAngle, 270));
            minSweepAngle((float) obtainStyledAttributes.getInteger(R.styleable.CircularProgressDrawable_cpd_minSweepAngle, 1));
            strokeSize(obtainStyledAttributes.getDimensionPixelSize(R.styleable.CircularProgressDrawable_cpd_strokeSize, z ? 8 : CircularProgressDrawable.dp2px(context, 4.0f)));
            strokeColors(obtainStyledAttributes.getColor(R.styleable.CircularProgressDrawable_cpd_strokeColor, CircularProgressDrawable.colorPrimary(context, ViewCompat.MEASURED_STATE_MASK)));
            i = obtainStyledAttributes.getResourceId(R.styleable.CircularProgressDrawable_cpd_strokeColors, 0);
            if (i != 0) {
                obtainTypedArray = context.getResources().obtainTypedArray(i);
                iArr = new int[obtainTypedArray.length()];
                for (int i3 = 0; i3 < obtainTypedArray.length(); i3++) {
                    iArr[i3] = obtainTypedArray.getColor(i3, 0);
                }
                obtainTypedArray.recycle();
                strokeColors(iArr);
            }
            strokeSecondaryColor(obtainStyledAttributes.getColor(R.styleable.CircularProgressDrawable_cpd_strokeSecondaryColor, 0));
            reverse(obtainStyledAttributes.getBoolean(R.styleable.CircularProgressDrawable_cpd_reverse, false));
            rotateDuration(obtainStyledAttributes.getInteger(R.styleable.CircularProgressDrawable_cpd_rotateDuration, context.getResources().getInteger(17694722)));
            transformDuration(obtainStyledAttributes.getInteger(R.styleable.CircularProgressDrawable_cpd_transformDuration, context.getResources().getInteger(17694721)));
            keepDuration(obtainStyledAttributes.getInteger(R.styleable.CircularProgressDrawable_cpd_keepDuration, context.getResources().getInteger(17694720)));
            i = obtainStyledAttributes.getResourceId(R.styleable.CircularProgressDrawable_cpd_transformInterpolator, 0);
            if (i != 0) {
                transformInterpolator(AnimationUtils.loadInterpolator(context, i));
            }
            progressMode(obtainStyledAttributes.getInteger(R.styleable.CircularProgressDrawable_pv_progressMode, ProgressView.MODE_INDETERMINATE));
            inAnimDuration(obtainStyledAttributes.getInteger(R.styleable.CircularProgressDrawable_cpd_inAnimDuration, context.getResources().getInteger(17694721)));
            i = obtainStyledAttributes.getResourceId(R.styleable.CircularProgressDrawable_cpd_inStepColors, 0);
            if (i != 0) {
                obtainTypedArray = context.getResources().obtainTypedArray(i);
                iArr = new int[obtainTypedArray.length()];
                for (int i4 = 0; i4 < obtainTypedArray.length(); i4++) {
                    iArr[i4] = obtainTypedArray.getColor(i4, 0);
                }
                obtainTypedArray.recycle();
                inStepColors(iArr);
            }
            inStepPercent(obtainStyledAttributes.getFloat(R.styleable.CircularProgressDrawable_cpd_inStepPercent, 0.5f));
            outAnimDuration(obtainStyledAttributes.getInteger(R.styleable.CircularProgressDrawable_cpd_outAnimDuration, context.getResources().getInteger(17694721)));
            obtainStyledAttributes.recycle();
        }
    }

    @TargetApi(21)
    public static int colorPrimary(Context context, int i) {
        if (VERSION.SDK_INT >= 21) {
            return getColor(context, 16843827, i);
        }
        return getColor(context, 16843827, i);
    }

    private static int dp2px(Context context, float f) {
        return (int) ((context.getResources().getDisplayMetrics().density * f) + 0.5f);
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x009b  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x009b  */
    /* JADX WARNING: Removed duplicated region for block: B:33:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:33:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x009b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void drawDeterminate(Canvas canvas) {
        float min;
        int min2 = 0;
        int i = 0;
        float f;
        Rect bounds = getBounds();
        int i2 = this.mRunState;
        if (i2 == 1) {
            min = (((float) this.mStrokeSize) * ((float) Math.min((long) this.mInAnimationDuration, SystemClock.uptimeMillis() - this.mLastRunStateTime))) / ((float) this.mInAnimationDuration);
            if (min > 0.0f) {
                min2 = Math.min(bounds.width(), bounds.height()) - (this.mPadding * 2);
                i = this.mStrokeSize;
            }
            f = 0.0f;
            if (f <= 0.0f) {
                float f2 = ((float) (bounds.left + bounds.right)) / 2.0f;
                float f3 = ((float) (bounds.top + bounds.bottom)) / 2.0f;
                this.mPaint.setStrokeWidth(min);
                this.mPaint.setStyle(Style.STROKE);
                min = this.mProgressPercent;
                if (min == 1.0f) {
                    this.mPaint.setColor(this.mStrokeColors[0]);
                    canvas.drawCircle(f2, f3, f, this.mPaint);
                    return;
                } else if (min == 0.0f) {
                    this.mPaint.setColor(this.mStrokeSecondaryColor);
                    canvas.drawCircle(f2, f3, f, this.mPaint);
                    return;
                } else {
                    int i3 = -360;
                    float f4 = this.mProgressPercent * ((float) (this.mReverse ? -360 : 360));
                    this.mRect.set(f2 - f, f3 - f, f2 + f, f3 + f);
                    this.mPaint.setColor(this.mStrokeSecondaryColor);
                    RectF rectF = this.mRect;
                    f = this.mInitialAngle + f4;
                    if (!this.mReverse) {
                        i3 = 360;
                    }
                    canvas.drawArc(rectF, f, ((float) i3) - f4, false, this.mPaint);
                    this.mPaint.setColor(this.mStrokeColors[0]);
                    canvas.drawArc(this.mRect, this.mInitialAngle, f4, false, this.mPaint);
                    return;
                }
            }
            return;
        }
        if (i2 == 4) {
            min = (((float) this.mStrokeSize) * ((float) Math.max(0, (((long) this.mOutAnimationDuration) - SystemClock.uptimeMillis()) + this.mLastRunStateTime))) / ((float) this.mOutAnimationDuration);
            if (min > 0.0f) {
                min2 = Math.min(bounds.width(), bounds.height()) - (this.mPadding * 2);
                i = this.mStrokeSize;
            }
        } else if (i2 != 0) {
            min = (float) this.mStrokeSize;
            f = (float) ((Math.min(bounds.width(), bounds.height()) - (this.mPadding * 2)) - this.mStrokeSize);
            f /= 2.0f;
            if (f <= 0.0f) {
            }
        } else {
            min = 0.0f;
        }
        f = 0.0f;
        f = ((float) (min2 - (i * 2))) + min;
        f /= 2.0f;
    }

    private void drawIndeterminate(Canvas canvas) {
        Canvas canvas2 = canvas;
        int i = this.mRunState;
        float f = 0.0f;
        float f2 = 2.0f;
        Rect bounds;
        float f3;
        float f4;
        float min;
        if (i == 1) {
            bounds = getBounds();
            f3 = ((float) (bounds.left + bounds.right)) / 2.0f;
            f4 = ((float) (bounds.top + bounds.bottom)) / 2.0f;
            min = ((float) (Math.min(bounds.width(), bounds.height()) - (this.mPadding * 2))) / 2.0f;
            float f5 = 1.0f;
            float uptimeMillis = ((float) (SystemClock.uptimeMillis() - this.mLastRunStateTime)) / ((float) this.mInAnimationDuration);
            float length = uptimeMillis / (1.0f / ((this.mInStepPercent * ((float) (this.mInColors.length + 2))) + 1.0f));
            int floor = (int) Math.floor((double) length);
            float f6 = 0.0f;
            while (floor >= 0) {
                float min2 = Math.min(f5, (length - ((float) floor)) * this.mInStepPercent) * min;
                int[] iArr = this.mInColors;
                if (floor < iArr.length) {
                    if (f6 != f) {
                        if (min2 <= f6) {
                            break;
                        }
                        float f7 = (f6 + min2) / f2;
                        this.mRect.set(f3 - f7, f4 - f7, f3 + f7, f4 + f7);
                        this.mPaint.setStrokeWidth(min2 - f6);
                        this.mPaint.setStyle(Style.STROKE);
                        this.mPaint.setColor(this.mInColors[floor]);
                        canvas2.drawCircle(f3, f4, f7, this.mPaint);
                    } else {
                        this.mPaint.setColor(iArr[floor]);
                        this.mPaint.setStyle(Style.FILL);
                        canvas2.drawCircle(f3, f4, min2, this.mPaint);
                    }
                }
                floor--;
                f6 = min2;
                f = 0.0f;
                f2 = 2.0f;
                f5 = 1.0f;
            }
            if (this.mProgressState != -1) {
                min -= ((float) this.mStrokeSize) / 2.0f;
                this.mRect.set(f3 - min, f4 - min, f3 + min, f4 + min);
                this.mPaint.setStrokeWidth((float) this.mStrokeSize);
                this.mPaint.setStyle(Style.STROKE);
                this.mPaint.setColor(getIndeterminateStrokeColor());
                canvas.drawArc(this.mRect, this.mStartAngle, this.mSweepAngle, false, this.mPaint);
            } else if (length >= 1.0f / this.mInStepPercent || uptimeMillis >= 1.0f) {
                resetAnimation();
            }
        } else if (i == 4) {
            min = (((float) this.mStrokeSize) * ((float) Math.max(0, (((long) this.mOutAnimationDuration) - SystemClock.uptimeMillis()) + this.mLastRunStateTime))) / ((float) this.mOutAnimationDuration);
            if (min > 0.0f) {
                Rect bounds2 = getBounds();
                f2 = (((float) ((Math.min(bounds2.width(), bounds2.height()) - (this.mPadding * 2)) - (this.mStrokeSize * 2))) + min) / 2.0f;
                f4 = ((float) (bounds2.left + bounds2.right)) / 2.0f;
                f = ((float) (bounds2.top + bounds2.bottom)) / 2.0f;
                this.mRect.set(f4 - f2, f - f2, f4 + f2, f + f2);
                this.mPaint.setStrokeWidth(min);
                this.mPaint.setStyle(Style.STROKE);
                this.mPaint.setColor(getIndeterminateStrokeColor());
                canvas.drawArc(this.mRect, this.mStartAngle, this.mSweepAngle, false, this.mPaint);
            }
        } else if (i != 0) {
            bounds = getBounds();
            f = ((float) ((Math.min(bounds.width(), bounds.height()) - (this.mPadding * 2)) - this.mStrokeSize)) / 2.0f;
            f3 = ((float) (bounds.left + bounds.right)) / 2.0f;
            min = ((float) (bounds.top + bounds.bottom)) / 2.0f;
            this.mRect.set(f3 - f, min - f, f3 + f, min + f);
            this.mPaint.setStrokeWidth((float) this.mStrokeSize);
            this.mPaint.setStyle(Style.STROKE);
            this.mPaint.setColor(getIndeterminateStrokeColor());
            canvas.drawArc(this.mRect, this.mStartAngle, this.mSweepAngle, false, this.mPaint);

        }
    }

    private static int getColor(Context context, int i, int i2) {
        if (value == null) {
            value = new TypedValue();
        }
        try {
            Theme theme = context.getTheme();
            if (theme != null && theme.resolveAttribute(i, value, true)) {
                if (value.type >= 16 && value.type <= 31) {
                    return value.data;
                }
                if (value.type == 3) {
                    return context.getResources().getColor(value.resourceId);
                }
            }
        } catch (Exception unused) {
        }
        return i2;
    }

    private int getIndeterminateStrokeColor() {
        if (this.mProgressState != 3 || this.mStrokeColors.length == 1) {
            return this.mStrokeColors[this.mStrokeColorIndex];
        }
        float max = Math.max(0.0f, Math.min(1.0f, ((float) (SystemClock.uptimeMillis() - this.mLastProgressStateTime)) / ((float) this.mKeepDuration)));
        int i = this.mStrokeColorIndex;
        if (i == 0) {
            i = this.mStrokeColors.length;
        }
        i--;
        int[] iArr = this.mStrokeColors;
        return ColorUtil.getMiddleColor(iArr[i], iArr[this.mStrokeColorIndex], max);
    }

    private void resetAnimation() {
        long uptimeMillis = SystemClock.uptimeMillis();
        this.mLastUpdateTime = uptimeMillis;
        this.mLastProgressStateTime = uptimeMillis;
        this.mStartAngle = this.mInitialAngle;
        this.mStrokeColorIndex = 0;
        this.mSweepAngle = this.mReverse ? -this.mMinSweepAngle : this.mMinSweepAngle;
        this.mProgressState = 0;
    }

    private void update() {
        int i = this.mProgressMode;
        if (i == 0) {
            updateDeterminate();
        } else if (i == 1) {
            updateIndeterminate();
        }
    }

    private void updateDeterminate() {
        long uptimeMillis = SystemClock.uptimeMillis();
        int i = this.mRunState;
        if (i == 1) {
            if (uptimeMillis - this.mLastRunStateTime > ((long) this.mInAnimationDuration)) {
                this.mRunState = 2;
                return;
            }
        } else if (i == 4 && uptimeMillis - this.mLastRunStateTime > ((long) this.mOutAnimationDuration)) {
            stop(false);
            return;
        }
        if (isRunning()) {
            scheduleSelf(this.mUpdater, SystemClock.uptimeMillis() + 16);
        }
        invalidateSelf();
    }

    private void updateIndeterminate() {
        long uptimeMillis = SystemClock.uptimeMillis();
        float f = (((float) (uptimeMillis - this.mLastUpdateTime)) * 360.0f) / ((float) this.mRotateDuration);
        if (this.mReverse) {
            f = -f;
        }
        this.mLastUpdateTime = uptimeMillis;
        int i = this.mProgressState;
        float f2;
        float f3;
        float f4;
        if (i == 0) {
            i = this.mTransformDuration;
            if (i <= 0) {
                this.mSweepAngle = this.mReverse ? -this.mMinSweepAngle : this.mMinSweepAngle;
                this.mProgressState = 1;
                this.mStartAngle += f;
                this.mLastProgressStateTime = uptimeMillis;
            } else {
                f2 = ((float) (uptimeMillis - this.mLastProgressStateTime)) / ((float) i);
                f3 = this.mReverse ? -this.mMaxSweepAngle : this.mMaxSweepAngle;
                f4 = this.mReverse ? -this.mMinSweepAngle : this.mMinSweepAngle;
                this.mStartAngle += f;
                this.mSweepAngle = (this.mTransformInterpolator.getInterpolation(f2) * (f3 - f4)) + f4;
                if (f2 > 1.0f) {
                    this.mSweepAngle = f3;
                    this.mProgressState = 1;
                    this.mLastProgressStateTime = uptimeMillis;
                }
            }
        } else if (i == 1) {
            this.mStartAngle += f;
            if (uptimeMillis - this.mLastProgressStateTime > ((long) this.mKeepDuration)) {
                this.mProgressState = 2;
                this.mLastProgressStateTime = uptimeMillis;
            }
        } else if (i == 2) {
            i = this.mTransformDuration;
            if (i <= 0) {
                this.mSweepAngle = this.mReverse ? -this.mMinSweepAngle : this.mMinSweepAngle;
                this.mProgressState = 3;
                this.mStartAngle += f;
                this.mLastProgressStateTime = uptimeMillis;
                this.mStrokeColorIndex = (this.mStrokeColorIndex + 1) % this.mStrokeColors.length;
            } else {
                f2 = ((float) (uptimeMillis - this.mLastProgressStateTime)) / ((float) i);
                f3 = this.mReverse ? -this.mMaxSweepAngle : this.mMaxSweepAngle;
                f4 = this.mReverse ? -this.mMinSweepAngle : this.mMinSweepAngle;
                float interpolation = ((1.0f - this.mTransformInterpolator.getInterpolation(f2)) * (f3 - f4)) + f4;
                this.mStartAngle += (f + this.mSweepAngle) - interpolation;
                this.mSweepAngle = interpolation;
                if (f2 > 1.0f) {
                    this.mSweepAngle = f4;
                    this.mProgressState = 3;
                    this.mLastProgressStateTime = uptimeMillis;
                    this.mStrokeColorIndex = (this.mStrokeColorIndex + 1) % this.mStrokeColors.length;
                }
            }
        } else if (i == 3) {
            this.mStartAngle += f;
            if (uptimeMillis - this.mLastProgressStateTime > ((long) this.mKeepDuration)) {
                this.mProgressState = 0;
                this.mLastProgressStateTime = uptimeMillis;
            }
        }
        int i2 = this.mRunState;
        if (i2 == 1) {
            if (uptimeMillis - this.mLastRunStateTime > ((long) this.mInAnimationDuration)) {
                this.mRunState = 3;
                if (this.mProgressState == -1) {
                    resetAnimation();
                }
            }
        } else if (i2 == 4 && uptimeMillis - this.mLastRunStateTime > ((long) this.mOutAnimationDuration)) {
            stop(false);
            return;
        }
        if (isRunning()) {
            scheduleSelf(this.mUpdater, SystemClock.uptimeMillis() + 16);
        }
        invalidateSelf();
    }

    public void applyStyle(Context context, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(i, R.styleable.CircularProgressDrawable);
        int indexCount = obtainStyledAttributes.getIndexCount();
        int[] iArr = null;
        Object obj = null;
        int i2 = 0;
        for (int i3 = 0; i3 < indexCount; i3++) {
            int index = obtainStyledAttributes.getIndex(i3);
            if (index == R.styleable.CircularProgressDrawable_cpd_padding) {
                this.mPadding = obtainStyledAttributes.getDimensionPixelSize(index, 0);
            } else if (index == R.styleable.CircularProgressDrawable_cpd_initialAngle) {
                this.mInitialAngle = (float) obtainStyledAttributes.getInteger(index, 0);
            } else if (index == R.styleable.CircularProgressDrawable_pv_progress) {
                setProgress(obtainStyledAttributes.getFloat(index, 0.0f));
            } else if (index == R.styleable.CircularProgressDrawable_pv_secondaryProgress) {
                setSecondaryProgress(obtainStyledAttributes.getFloat(index, 0.0f));
            } else if (index == R.styleable.CircularProgressDrawable_cpd_maxSweepAngle) {
                this.mMaxSweepAngle = (float) obtainStyledAttributes.getInteger(index, 0);
            } else if (index == R.styleable.CircularProgressDrawable_cpd_minSweepAngle) {
                this.mMinSweepAngle = (float) obtainStyledAttributes.getInteger(index, 0);
            } else if (index == R.styleable.CircularProgressDrawable_cpd_strokeSize) {
                this.mStrokeSize = obtainStyledAttributes.getDimensionPixelSize(index, 0);
            } else if (index == R.styleable.CircularProgressDrawable_cpd_strokeColor) {
                i2 = obtainStyledAttributes.getColor(index, 0);
                obj = 1;
            } else if (index == R.styleable.CircularProgressDrawable_cpd_strokeColors) {
                TypedArray obtainTypedArray = context.getResources().obtainTypedArray(obtainStyledAttributes.getResourceId(index, 0));
                int[] iArr2 = new int[obtainTypedArray.length()];
                for (index = 0; index < obtainTypedArray.length(); index++) {
                    iArr2[index] = obtainTypedArray.getColor(index, 0);
                }
                obtainTypedArray.recycle();
                iArr = iArr2;
            } else if (index == R.styleable.CircularProgressDrawable_cpd_strokeSecondaryColor) {
                this.mStrokeSecondaryColor = obtainStyledAttributes.getColor(index, 0);
            } else if (index == R.styleable.CircularProgressDrawable_cpd_reverse) {
                this.mReverse = obtainStyledAttributes.getBoolean(index, false);
            } else if (index == R.styleable.CircularProgressDrawable_cpd_rotateDuration) {
                this.mRotateDuration = obtainStyledAttributes.getInteger(index, 0);
            } else if (index == R.styleable.CircularProgressDrawable_cpd_transformDuration) {
                this.mTransformDuration = obtainStyledAttributes.getInteger(index, 0);
            } else if (index == R.styleable.CircularProgressDrawable_cpd_keepDuration) {
                this.mKeepDuration = obtainStyledAttributes.getInteger(index, 0);
            } else if (index == R.styleable.CircularProgressDrawable_cpd_transformInterpolator) {
                this.mTransformInterpolator = AnimationUtils.loadInterpolator(context, obtainStyledAttributes.getResourceId(index, 0));
            } else if (index == R.styleable.CircularProgressDrawable_pv_progressMode) {
                this.mProgressMode = obtainStyledAttributes.getInteger(index, 0);
            } else if (index == R.styleable.CircularProgressDrawable_cpd_inAnimDuration) {
                this.mInAnimationDuration = obtainStyledAttributes.getInteger(index, 0);
            } else if (index == R.styleable.CircularProgressDrawable_cpd_inStepColors) {
                TypedArray obtainTypedArray2 = context.getResources().obtainTypedArray(obtainStyledAttributes.getResourceId(index, 0));
                this.mInColors = new int[obtainTypedArray2.length()];
                for (index = 0; index < obtainTypedArray2.length(); index++) {
                    this.mInColors[index] = obtainTypedArray2.getColor(index, 0);
                }
                obtainTypedArray2.recycle();
            } else if (index == R.styleable.CircularProgressDrawable_cpd_inStepPercent) {
                this.mInStepPercent = obtainStyledAttributes.getFloat(index, 0.0f);
            } else if (index == R.styleable.CircularProgressDrawable_cpd_outAnimDuration) {
                this.mOutAnimationDuration = obtainStyledAttributes.getInteger(index, 0);
            }
        }
        obtainStyledAttributes.recycle();
        if (iArr != null) {
            this.mStrokeColors = iArr;
        } else if (obj != null) {
            this.mStrokeColors = new int[]{i2};
        }
        if (this.mStrokeColorIndex >= this.mStrokeColors.length) {
            this.mStrokeColorIndex = 0;
        }
        invalidateSelf();
    }

    public void draw(Canvas canvas) {
        int i = this.mProgressMode;
        if (i == 0) {
            drawDeterminate(canvas);
        } else if (i == 1) {
            drawIndeterminate(canvas);
        }
    }

    public int getOpacity() {
        return -3;
    }

    public float getProgress() {
        return this.mProgressPercent;
    }

    public int getProgressMode() {
        return this.mProgressMode;
    }

    public float getSecondaryProgress() {
        return this.mSecondaryProgressPercent;
    }

    public boolean isRunning() {
        return this.mRunState != 0;
    }

    public void scheduleSelf(Runnable runnable, long j) {
        if (this.mRunState == 0) {
            this.mRunState = this.mInAnimationDuration > 0 ? 1 : 3;
        }
        super.scheduleSelf(runnable, j);
    }

    public void setAlpha(int i) {
        this.mPaint.setAlpha(i);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }

    public void setProgress(float f) {
        f = Math.min(1.0f, Math.max(0.0f, f));
        if (this.mProgressPercent != f) {
            this.mProgressPercent = f;
            if (isRunning()) {
                invalidateSelf();
            } else if (this.mProgressPercent != 0.0f) {
                start();
            }
        }
    }

    public void setProgressMode(int i) {
        if (this.mProgressMode != i) {
            this.mProgressMode = i;
            invalidateSelf();
        }
    }

    public void setSecondaryProgress(float f) {
        f = Math.min(1.0f, Math.max(0.0f, f));
        if (this.mSecondaryProgressPercent != f) {
            this.mSecondaryProgressPercent = f;
            if (isRunning()) {
                invalidateSelf();
            } else if (this.mSecondaryProgressPercent != 0.0f) {
                start();
            }
        }
    }

    public void start() {
        start(this.mInAnimationDuration > 0);
    }

    public void stop() {
        stop(this.mOutAnimationDuration > 0);
    }

    private CircularProgressDrawable(int i, float f, float f2, float f3, float f4, float f5, int i2, int[] iArr, int i3, boolean z, int i4, int i5, int i6, Interpolator interpolator, int i7, int i8, float f6, int[] iArr2, int i9) {
        this.mRunState = 0;
        this.mUpdater = () -> {

        };
        this.mPadding = i;
        this.mInitialAngle = f;
        float f7 = f2;
        setProgress(f2);
        f7 = f3;
        setSecondaryProgress(f3);
        this.mMaxSweepAngle = f4;
        this.mMinSweepAngle = f5;
        this.mStrokeSize = i2;
        this.mStrokeColors = iArr;
        this.mStrokeSecondaryColor = i3;
        this.mReverse = z;
        this.mRotateDuration = i4;
        this.mTransformDuration = i5;
        this.mKeepDuration = i6;
        this.mTransformInterpolator = interpolator;
        this.mProgressMode = i7;
        this.mInAnimationDuration = i8;
        this.mInStepPercent = f6;
        this.mInColors = iArr2;
        this.mOutAnimationDuration = i9;
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setAntiAlias(true);
        this.mPaint.setStrokeCap(Cap.ROUND);
        this.mPaint.setStrokeJoin(Join.ROUND);
        this.mRect = new RectF();
    }

    private void start(boolean z) {
        if (!isRunning()) {
            if (z) {
                this.mRunState = 1;
                this.mLastRunStateTime = SystemClock.uptimeMillis();
                this.mProgressState = -1;
            } else {
                resetAnimation();
            }
            scheduleSelf(this.mUpdater, SystemClock.uptimeMillis() + 16);
            invalidateSelf();
        }
    }

    private void stop(boolean z) {
        if (isRunning()) {
            if (z) {
                this.mLastRunStateTime = SystemClock.uptimeMillis();
                if (this.mRunState == 2) {
                    scheduleSelf(this.mUpdater, SystemClock.uptimeMillis() + 16);
                    invalidateSelf();
                }
                this.mRunState = 4;
            } else {
                this.mRunState = 0;
                unscheduleSelf(this.mUpdater);
                invalidateSelf();
            }
        }
    }
}
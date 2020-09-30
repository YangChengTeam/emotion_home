package com.yc.emotion.home.message.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.yc.emotion.home.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import androidx.annotation.ColorInt;
import androidx.appcompat.widget.AppCompatTextView;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.Intrinsics;

public final class RoundTextView extends AppCompatTextView {
    @NotNull
    private ColorStateList bgColor;
    private boolean isCircle;
    private Paint paint;
    private float radius;
    @NotNull
    private ColorStateList strokeColor;
    private Paint strokePaint;
    private float strokeWidth;


    @JvmOverloads
    public RoundTextView(@NotNull Context context) {
        this(context, null);
    }

    //
    @JvmOverloads
    public RoundTextView(@NotNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }


    public Paint getNPaint() {
        Paint paint = new Paint(1);
        paint.setStyle(Paint.Style.FILL);
        return paint;

    }

    private Paint getStrokePaint() {
        Paint strokePaint = new Paint(1);
        strokePaint.setStyle(Paint.Style.STROKE);
        return strokePaint;

    }

    @NotNull
    public final ColorStateList getBgColor() {
        return this.bgColor;
    }

    public final float getRadius() {
        return this.radius;
    }

    @NotNull
    public final ColorStateList getStrokeColor() {
        return this.strokeColor;
    }

    public final float getStrokeWidth() {
        return this.strokeWidth;
    }

    public final boolean isCircle() {
        return this.isCircle;
    }

    /* Access modifiers changed, original: protected */
    @SuppressLint({"DrawAllocation"})
    public void onDraw(@NotNull Canvas canvas) {
        getPaint().setColor(this.bgColor.getColorForState(getDrawableState(), 0));
        getStrokePaint().setColor(this.strokeColor.getColorForState(getDrawableState(), 0));
        float f;
        float f2;
        if (this.isCircle) {
            int measuredWidth = getMeasuredWidth();
            int measuredHeight = getMeasuredHeight();
            f = (float) 2;
            float min = (((float) Math.min(measuredWidth, measuredHeight)) - (this.strokeWidth * f)) / f;
            float f3 = ((float) measuredWidth) / 2.0f;
            f2 = ((float) measuredHeight) / 2.0f;
            canvas.drawCircle(f3, f2, min, getPaint());
            canvas.drawCircle(f3, f2, min + (this.strokeWidth / f), getStrokePaint());
        } else {
            f2 = this.strokeWidth;
            f = (float) 2;
            RectF rectF = new RectF(f2 / f, f2 / f, ((float) getMeasuredWidth()) - (this.strokeWidth / f), ((float) getMeasuredHeight()) - (this.strokeWidth / f));
            f = this.radius;
            canvas.drawRoundRect(rectF, f, f, getPaint());
            f = this.radius;
//            canvas.drawRoundRect(rectF, f, f, getStrokePaint());
        }
        super.onDraw(canvas);
    }

    public final void setBackColor(@ColorInt int i) {
        ColorStateList valueOf = ColorStateList.valueOf(i);
        Intrinsics.checkExpressionValueIsNotNull(valueOf, "ColorStateList.valueOf(color)");
        setBgColor(valueOf);
    }

    public final void setBgColor(@NotNull ColorStateList colorStateList) {
        this.bgColor = colorStateList;
        invalidate();
    }

    public final void setCircle(boolean z) {
        this.isCircle = z;
        invalidate();
    }

    public final void setRadius(float f) {
        this.radius = f;
        invalidate();
    }

    public final void setStrokeColor(@NotNull ColorStateList colorStateList) {
        this.strokeColor = colorStateList;
        invalidate();
    }

    public final void setStrokeWidth(float f) {
        this.strokeWidth = f;
        getStrokePaint().setStrokeWidth(f);
        invalidate();
    }

    @JvmOverloads
    public RoundTextView(@NotNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.paint = getNPaint();
        this.strokePaint = getStrokePaint();
        ColorStateList valueOf = ColorStateList.valueOf(0);


        this.bgColor = valueOf;
        valueOf = ColorStateList.valueOf(0);

        this.strokeColor = valueOf;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.RoundTextView);
        ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(R.styleable.RoundTextView_back_color);
        if (colorStateList == null) {
            colorStateList = ColorStateList.valueOf(0);

        }
        setBgColor(colorStateList);
        colorStateList = obtainStyledAttributes.getColorStateList(R.styleable.RoundTextView_stroke_color);
        if (colorStateList == null) {
            colorStateList = ColorStateList.valueOf(0);

        }
        setStrokeColor(colorStateList);
        setStrokeWidth(obtainStyledAttributes.getDimension(R.styleable.RoundTextView_stroke_width, 0.0f));
        setRadius(obtainStyledAttributes.getDimension(R.styleable.RoundTextView_stroke_radius, 0.0f));
        setCircle(obtainStyledAttributes.getBoolean(R.styleable.RoundTextView_is_circle, false));
        obtainStyledAttributes.recycle();
    }

    public final void setStrokeColor(@ColorInt int i) {
        ColorStateList valueOf = ColorStateList.valueOf(i);
        Intrinsics.checkExpressionValueIsNotNull(valueOf, "ColorStateList.valueOf(color)");
        setStrokeColor(valueOf);
    }
}
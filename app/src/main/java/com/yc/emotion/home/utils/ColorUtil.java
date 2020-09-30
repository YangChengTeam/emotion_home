package com.yc.emotion.home.utils;

import android.graphics.Color;

public class ColorUtil {
    public static int getColor(int i, float f) {
        return (i & 16777215) | (Math.round(((float) Color.alpha(i)) * f) << 24);
    }

    public static int getMiddleColor(int i, int i2, float f) {
        if (i == i2) {
            return i2;
        }
        if (f == 0.0f) {
            return i;
        }
        if (f == 1.0f) {
            return i2;
        }
        return Color.argb(getMiddleValue(Color.alpha(i), Color.alpha(i2), f), getMiddleValue(Color.red(i), Color.red(i2), f), getMiddleValue(Color.green(i), Color.green(i2), f), getMiddleValue(Color.blue(i), Color.blue(i2), f));
    }

    private static int getMiddleValue(int i, int i2, float f) {
        return Math.round(((float) i) + (((float) (i2 - i)) * f));
    }
}
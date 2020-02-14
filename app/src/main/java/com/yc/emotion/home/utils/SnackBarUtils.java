package com.yc.emotion.home.utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by suns  on 2019/8/30 09:08.
 */
public class SnackBarUtils {

    public static void tips(Activity activity, String mess) {
        tips(activity, mess, Snackbar.LENGTH_SHORT);

    }


    public static void tips(Activity activity, String mess, int duration) {
        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), mess, duration);
        if (!snackbar.isShown()) {
            snackbar.show();
        }

    }
}

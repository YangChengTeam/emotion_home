package com.yc.emotion.home.utils


import android.app.Activity
import android.view.ViewGroup.MarginLayoutParams
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.annotations.NotNull

/**
 * Created by suns  on 2019/8/30 09:08.
 */
object SnackBarUtils {
    @JvmOverloads
    fun tips(activity: Activity, @NotNull mess: String, duration: Int = Snackbar.LENGTH_SHORT) {
        val snackbar = Snackbar.make(activity.findViewById(android.R.id.content), mess, duration)
        val view = snackbar.view
        val layoutParams = view.layoutParams as MarginLayoutParams
        var bottom = 0
        if (StatusBarUtil.isNavigationBarExist(activity)) {
            bottom = StatusBarUtil.getNavigationBarHeight(activity)
        }
        layoutParams.bottomMargin = bottom
        view.layoutParams = layoutParams
        if (!snackbar.isShown) {
            snackbar.show()
        }
    }


}
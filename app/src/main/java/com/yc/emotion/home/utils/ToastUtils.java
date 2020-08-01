package com.yc.emotion.home.utils;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yc.emotion.home.R;
import com.yc.emotion.home.base.YcApplication;


public class ToastUtils {

    private static TextView sMTv_text;
    private static Toast centerToast;

    public static void showCenterToast(String text) {
        if (null==centerToast) {
            centerToast = new Toast(YcApplication.getInstance());
            centerToast.setDuration(Toast.LENGTH_LONG);
            centerToast.setGravity(Gravity.NO_GRAVITY, 0, 0);
            View view = View.inflate(YcApplication.getInstance(), R.layout.toast_center_layout, null);
            sMTv_text = view.findViewById(R.id.tv_text);
            sMTv_text.setText(TextUtils.isEmpty(text)?"null":text);
            centerToast.setView(view);
        } else {
            sMTv_text.setText(TextUtils.isEmpty(text)?"null":text);
        }
        centerToast.show();
    }
}

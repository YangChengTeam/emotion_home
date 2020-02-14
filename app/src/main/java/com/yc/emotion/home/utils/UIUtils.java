package com.yc.emotion.home.utils;


import android.os.Handler;
import android.os.Looper;

import com.music.player.lib.view.MarqueeTextView;
import com.sunfusheng.marqueeview.MarqueeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglin  on 2018/2/5 17:37.
 */

public class UIUtils {
    private static Handler handler = new Handler(Looper.getMainLooper());

    public static void post(Runnable runnable) {
        handler.post(runnable);
    }

    public static void postDelay(Runnable runnable, long i) {
        handler.postDelayed(runnable, i);
    }


    public static void setMarqueList(MarqueeView marqueeView) {
        post(() -> {
            List<String> messages = new ArrayList<>();
            messages.add("如何挽回那个人的心");
            messages.add("失恋怎么办");
            messages.add("老公不思上进怎么办");
            messages.add("生活没有激情怎么办");
            messages.add("挽回婚姻靠哪几招？");
            marqueeView.startWithList(messages);
        });

    }


}

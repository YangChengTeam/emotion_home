package com.yc.emotion.home.base.listener;

import com.yc.emotion.home.base.ui.widget.PaletteImageView;

/**
 * Created by suns  on 2019/8/28 10:30.
 */
public interface OnParseColorListener {
    void onComplete(PaletteImageView paletteImageView);

    void onFail();
}

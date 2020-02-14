package com.yc.emotion.home.base.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by wanglin  on 2018/2/27 11:45.
 */

public abstract class BaseView extends FrameLayout {

    protected Context mContext;
    protected View rootView;

    public BaseView(@NonNull Context context) {
        super(context);
        this.mContext = context;
        rootView = LayoutInflater.from(context).inflate(getLayoutId(), this);

        init();

    }


    protected abstract int getLayoutId();

    public BaseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        rootView = LayoutInflater.from(context).inflate(getLayoutId(), this);


        init();
    }


    public View getView(int resId) {
        return rootView.findViewById(resId);
    }

    public void init() {

    }
}

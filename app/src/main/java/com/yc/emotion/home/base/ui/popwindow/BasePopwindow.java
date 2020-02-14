package com.yc.emotion.home.base.ui.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.yc.emotion.home.base.presenter.BasePresenter;
import com.yc.emotion.home.utils.StatusBarUtil;
import com.yc.emotion.home.utils.UIUtils;

/**
 * Created by wanglin  on 2018/3/8 16:43.
 */

public abstract class BasePopwindow extends PopupWindow {
    protected Activity mContext;
    private ColorDrawable mBackgroundDrawable;
    protected View rootView;
    protected BasePresenter mPresenter;

    public BasePopwindow(Activity context) {
        mContext = context;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(getLayoutId(), null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(rootView);
        setWindowAlpha(1f);

        setBackgroundDrawable(new BitmapDrawable());

        int aid = getAnimationID();
        if (aid != 0) {
            setAnimationStyle(aid);
        }


        init();
        if (null != mPresenter) {
            mPresenter.subscribe();
        }
    }

    public abstract int getLayoutId();

    public abstract void init();

    private void setWindowAlpha(float alpha) {
        Window window = mContext.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = alpha;
        mContext.getWindow().setAttributes(lp);
    }

    @Override
    public void dismiss() {
        super.dismiss();


        UIUtils.postDelay(() -> setWindowAlpha(1f), 300);
        if (null != mPresenter) {
            mPresenter.unSubscribe();
        }
    }

    public int getAnimationID() {
        return 0;
    }

    @Override
    public void setContentView(View contentView) {
        if (contentView != null) {
            super.setContentView(contentView);
            setFocusable(true);
            setTouchable(true);
            contentView.setFocusable(true);
            contentView.setFocusableInTouchMode(true);
            contentView.setOnKeyListener((v, keyCode, event) -> {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        dismiss();
                        return true;
                    default:
                        break;
                }
                return false;
            });
        }
    }

    public void show() {
        show(mContext.getWindow().getDecorView().getRootView());
    }

    public void show(View view) {
        show(view, Gravity.BOTTOM);
    }

    public void show(View view, int gravity) {

        showAtLocation(view, gravity, 0, StatusBarUtil.getStatusBarHeight(mContext));
    }

    @Override
    public void setOutsideTouchable(boolean touchable) {
        super.setOutsideTouchable(touchable);
        if (touchable) {
            if (mBackgroundDrawable == null) {
                mBackgroundDrawable = new ColorDrawable(0x00000000);
            }
            super.setBackgroundDrawable(mBackgroundDrawable);
        } else {
            super.setBackgroundDrawable(null);
        }
    }
}

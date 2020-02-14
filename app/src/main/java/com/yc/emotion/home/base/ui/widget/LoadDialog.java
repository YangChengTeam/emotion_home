package com.yc.emotion.home.base.ui.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yc.emotion.home.R;


public class LoadDialog extends AlertDialog {


    private Context context;
    private String text;
    private TextView textView;

    public LoadDialog(Context context) {
        super(context, R.style.LoadingDialogTheme);//设置样式
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_view);
        textView = findViewById(R.id.loading_view_tv);
        setCancelable(true);
        initView();
    }

    private void initView() {

    }

    public void showLoadingDialog() {
        try {
            Activity activity = (Activity) context;

            if (activity != null && !activity.isFinishing()) {
                dismiss();
                if (!isShowing()) {
                    show();
                }
            }
        } catch (Exception e) {
            Log.d("mylog", "LoadDialog --showLoadingDialog: " + e.toString());
        }
    }

    public void dismissLoadingDialog() {
        try {
            Activity activity = (Activity) context;
            if (activity != null && !activity.isFinishing()) {
                dismiss();
                if (isShowing()) {
                    hide();
                }
            }
        } catch (Exception e) {
            Log.d("mylog", "LoadDialog --dismissDialog: " + e.toString());
        }
    }

    public void setText(String text) {
        this.text = text;
        if (textView != null) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }
    }
}

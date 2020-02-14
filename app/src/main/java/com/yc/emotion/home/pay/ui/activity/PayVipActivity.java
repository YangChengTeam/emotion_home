package com.yc.emotion.home.pay.ui.activity;

import android.os.Bundle;

import com.yc.emotion.home.R;
import com.yc.emotion.home.base.ui.activity.PayActivity;

public class PayVipActivity extends PayActivity {



    @Override
    protected void onZfbPauResult(boolean result, String des) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay_vip;
    }

    @Override
    public void initViews() {

    }
}

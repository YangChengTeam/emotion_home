package com.yc.emotion.home.base.view

/**
 *
 * Created by suns  on 2019/11/7 16:59.
 * 网络请求状态的默认实现
 */
interface StateDefaultImpl : IState {
    override fun onComplete() {
    }

    override fun onError() {
    }

    override fun onNoData() {
    }

    override fun onLoading() {
    }

    override fun onEnd() {}


}
package com.yc.emotion.home.base.view

/**
 *
 * Created by suns  on 2019/11/7 16:55.
 * 状态回调
 */
interface IState {

    fun onComplete() //加载完成

    fun onError() //网络错误或其他错误

    fun onNoData() //无数据

    fun onLoading() //正在加载


    fun onEnd()


}


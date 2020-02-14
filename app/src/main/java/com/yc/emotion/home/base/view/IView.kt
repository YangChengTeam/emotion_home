package com.yc.emotion.home.base.view

/**
 *
 * Created by suns  on 2019/11/7 15:38.
 * 所有view的父类
 */
interface IView {
    /**
     * 加载页面布局文件
     */
    fun getLayoutId(): Int

    /**
     * 让布局中的view与fragment中的变量建立起映射
     */
    fun initViews()
}
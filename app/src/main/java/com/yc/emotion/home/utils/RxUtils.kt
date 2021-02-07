package com.yc.emotion.home.utils


import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *
 * Created by suns  on 2021/1/5 17:04.
 */
object RxUtils {

    fun <T> rxSchedulerHelper(): FlowableTransformer<T, T> {
        return FlowableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }
}
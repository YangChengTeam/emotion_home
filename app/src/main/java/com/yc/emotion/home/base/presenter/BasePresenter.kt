package com.yc.emotion.home.base.presenter

import android.content.Context
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.utils.RxUtils
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.converter.BaseObserver
import yc.com.rthttplibrary.view.IDialog


/**
 *
 * Created by suns  on 2019/11/7 15:23.
 * mvp的核心思想在于mode（数据处理）和v(界面显示)不相关，彼此之间不联系，达到真正的解耦，这就必须靠中间件presenter
 * presenter同时和m,v打交道 统一调度数据和界面，让整个流程顺利完成。所以presenter 必须持有这两个对象，至于怎么持有??? 看各位的实现咯....
 * P
 */
abstract class BasePresenter<M : IModel, V : IView>(context: Context?, view: V) : IPresenter {


    var mModel: M? = null

    var mView: V = view

    constructor(view: V) : this(null, view)


    var mContext = context


    protected var isFirstLoad = true


    private var mDisposables: CompositeDisposable? = null


    fun loadData(isForceUI: Boolean) {
        loadData(isFirstLoad or isForceUI, true)
        if ((isFirstLoad or isForceUI) and isLoadingCache()) {
            getCache()
        }
        isFirstLoad = false


    }

    abstract fun loadData(isForceUI: Boolean, isLoading: Boolean)

    open fun isLoadingCache() = true


    abstract fun getCache()

    override fun subscribe() {

        loadData(false)
    }

    override fun unSubscribe() {
        mDisposables?.dispose()
    }

    protected open fun addSubscribe(disposable: Disposable?) {
        if (mDisposables == null) {
            mDisposables = CompositeDisposable()
        }
        disposable?.let { mDisposables?.add(it) }

    }


    protected inline fun <T, R : Flowable<ResultInfo<T>>, V : IDialog> R.getData(
            v: V?,
            crossinline block1: (T?,String?) -> Unit,
            crossinline block2: (Int, String?) -> Unit,
            isShowLoading: Boolean = true,
    ) {
        addSubscribe(compose(RxUtils.rxSchedulerHelper()).subscribeWith(object : BaseObserver<T, V>(v) {
            override fun onSuccess(data: T, message: String?) {
                block1(data, message)
            }

            override fun onFailure(code: Int, errorMsg: String?) {
                block2(code, errorMsg)
            }

            override fun isShow(): Boolean {
                return isShowLoading
            }

        }))
    }
}
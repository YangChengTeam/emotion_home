package com.yc.emotion.home.mine.presenter

import android.content.Context

import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.mine.domain.model.OrderModel
import com.yc.emotion.home.mine.view.OrderView
import com.yc.emotion.home.model.bean.OrderInfo
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.observers.DisposableObserver
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.config.HttpConfig

/**
 *
 * Created by suns  on 2019/11/18 15:41.
 */
class OrderPresenter(context: Context, view: OrderView) : BasePresenter<OrderModel, OrderView>(context, view) {

    init {
        mModel = OrderModel(context)
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {

    }

    override fun getCache() {

    }


    fun getOrderList(page: Int, pageSize: Int) {
        val userId = UserInfoHelper.instance.getUid()

        mModel?.getOrderList("$userId", page, pageSize)?.getData(mView, { it, _ ->
            if (it != null && it.isNotEmpty()) {
                mView.showOrderList(it)
            } else {
                if (page == 1) mView.onNoData()
                else {
                    mView.onEnd()
                }
            }
        }, { code, _ ->
            if (code == -1) {
                mView.onError()
            } else {
                mView.onNoData()
            }
        }, page == 1)

    }


}
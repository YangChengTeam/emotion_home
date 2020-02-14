package com.yc.emotion.home.mine.presenter

import android.content.Context
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.mine.domain.model.OrderModel
import com.yc.emotion.home.mine.view.OrderView
import com.yc.emotion.home.model.bean.OrderInfo
import com.yc.emotion.home.utils.UserInfoHelper
import rx.Subscriber

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

        if (page == 1) {
            mView.showLoadingDialog()
        }

        mModel?.getOrderList("$userId", page, pageSize)?.subscribe(object : Subscriber<ResultInfo<List<OrderInfo>>>() {
            override fun onNext(t: ResultInfo<List<OrderInfo>>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK) {
                        if (t.data != null) {
                            mView.showOrderList(t.data)
                        } else {
                            if (page == 1) mView.onNoData()
                        }
                    } else {
                        if (page == 1) mView.onNoData()
                    }
                }
            }

            override fun onCompleted() {
                if (page == 1) mView.hideLoadingDialog()
                mView.onComplete()
            }

            override fun onError(e: Throwable?) {
                if (page == 1) mView.hideLoadingDialog()

                mView.onError()
            }

        })
    }


}
package com.yc.emotion.home.pay.presenter

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.model.bean.AResultInfo
import com.yc.emotion.home.model.bean.GoodsInfo
import com.yc.emotion.home.model.bean.OrdersInitBean
import com.yc.emotion.home.pay.model.VipModel
import com.yc.emotion.home.pay.view.VipView
import com.yc.emotion.home.utils.CommonInfoHelper
import com.yc.emotion.home.utils.UserInfoHelper
import rx.Subscriber

/**
 *
 * Created by suns  on 2019/11/13 16:25.
 */
class VipPresenter(context: Context, view: VipView) : BasePresenter<VipModel, VipView>(context, view) {

    init {
        mModel = VipModel(context)
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {
        if (!isForceUI) return
    }

    override fun getCache() {
        CommonInfoHelper.getO<List<GoodsInfo>>(mContext, "vip_infos", object : TypeReference<List<GoodsInfo>>() {}.type) { goodInfos ->
            if (goodInfos != null && goodInfos.isNotEmpty()) {
                mView.showGoodInfoList(goodInfos)
            }
        }

        getGoodListInfo()
    }

    private fun getGoodListInfo() {
        mView.showLoadingDialog()
        val subscription = mModel?.getGoodListInfo()?.subscribe(object : Subscriber<AResultInfo<List<GoodsInfo>>>() {
            override fun onNext(t: AResultInfo<List<GoodsInfo>>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showGoodInfoList(t.data)
                        CommonInfoHelper.setO(mContext, t.data, "vip_infos")
                    }
                }
            }

            override fun onCompleted() {
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable?) {

            }

        })
        subScriptions?.add(subscription)
    }

    fun initOrders(pay_way_name: String, money: String, title: String, goodId: String) {
        val userId = UserInfoHelper.instance.getUid()

        mView.showLoadingDialog()

        val subscription = mModel?.initOrders("$userId", pay_way_name, money, title, goodId)?.subscribe(object : Subscriber<AResultInfo<OrdersInitBean>>() {
            override fun onCompleted() {
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable?) {

            }

            override fun onNext(t: AResultInfo<OrdersInitBean>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showOrderInfo(t.data, pay_way_name)
                    }
                }
            }

        })

        subScriptions?.add(subscription)
    }
}
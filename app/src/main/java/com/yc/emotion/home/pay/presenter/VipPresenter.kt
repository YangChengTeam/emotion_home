package com.yc.emotion.home.pay.presenter

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.model.bean.GoodsInfo
import com.yc.emotion.home.model.bean.OrdersInitBean
import com.yc.emotion.home.pay.model.VipModel
import com.yc.emotion.home.pay.view.VipView
import com.yc.emotion.home.utils.CommonInfoHelper
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.observers.DisposableObserver
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.config.HttpConfig

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
        CommonInfoHelper.getO(mContext, "vip_infos", object : TypeReference<List<GoodsInfo>>() {}.type,
                object : CommonInfoHelper.OnParseListener<List<GoodsInfo>> {
                    override fun onParse(o: List<GoodsInfo>?) {
                        if (o != null && o.isNotEmpty()) {
                            mView.showGoodInfoList(o)
                        }
                    }

                })

        getGoodListInfo()
    }

    private fun getGoodListInfo() {

        mModel?.getGoodListInfo()?.getData(mView, { it, _ ->
            it?.let {
                mView.showGoodInfoList(it)
                CommonInfoHelper.setO(mContext, it, "vip_infos")
            }
        }, { _, _ -> })

    }

    fun initOrders(pay_way_name: String, money: String, title: String, goodId: String) {
        val userId = UserInfoHelper.instance.getUid()

        mModel?.initOrders("$userId", pay_way_name, money, title, goodId)?.getData(mView, { it, _ ->
            it?.let {
                mView.showOrderInfo(it, pay_way_name)
            }
        }, { _, _ -> })


    }
}
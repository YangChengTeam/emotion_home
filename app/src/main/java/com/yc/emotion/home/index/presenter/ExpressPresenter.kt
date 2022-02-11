package com.yc.emotion.home.index.presenter

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.model.ExpressModel
import com.yc.emotion.home.index.view.ExpressView
import com.yc.emotion.home.model.bean.confession.ConfessionBean
import com.yc.emotion.home.model.bean.confession.ConfessionDataBean
import com.yc.emotion.home.utils.CommonInfoHelper
import com.yc.emotion.home.model.bean.ImageCreateBean
import io.reactivex.observers.DisposableObserver

import java.io.File

/**
 *
 * Created by suns  on 2019/11/20 14:46.
 */
class ExpressPresenter(context: Context?, view: ExpressView) : BasePresenter<ExpressModel, ExpressView>(context, view) {

    init {
        mModel = ExpressModel(context)
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {

    }

    override fun getCache() {

    }


    fun getExpressCache() {
        CommonInfoHelper.getO(mContext, "main3_new", object : TypeReference<List<ConfessionDataBean>>() {

        }.type, object : CommonInfoHelper.OnParseListener<List<ConfessionDataBean>> {
            override fun onParse(o: List<ConfessionDataBean>?) {
                if (o != null && o.isNotEmpty()) {
                    mView.showConfessionInfos(o)
                }
            }

//
        })
    }


    fun getExpressData(page: Int, pageSize: Int) {
        if (page == 1)
            mView.showLoading()
        mModel?.getExpressData(page)?.subscribe(object : DisposableObserver<ConfessionBean>() {
            override fun onNext(t: ConfessionBean) {
                t.let {
                    if (t.status) {
                        val confessionDataBeans = t.data
                        createNewData(confessionDataBeans, page, pageSize)
                    }
                }
            }

            override fun onComplete() {
                if (page == 1) mView.hideLoading()
                mView.onComplete()
            }

            override fun onError(e: Throwable) {
                if (page == 1) mView.hideLoading()
                mView.onComplete()
            }

        })


    }


    private fun createNewData(confessionDataBeans: List<ConfessionDataBean>?, page: Int, pageSize: Int) {
        val mConfessionDataBeans = arrayListOf<ConfessionDataBean>()

        if (confessionDataBeans != null && confessionDataBeans.isNotEmpty()) {
            for (confessionDataBean in confessionDataBeans) {
                confessionDataBean.itemType = ConfessionDataBean.VIEW_ITEM
                mConfessionDataBeans.add(confessionDataBean)
            }
        }

        if (page == 1) {
//            mConfessionDataBeans.add(0, ConfessionDataBean(ConfessionDataBean.VIEW_TITLE, "data_title"))

            CommonInfoHelper.setO<List<ConfessionDataBean>>(mContext, mConfessionDataBeans, "main3_new")
        }
        mView.showConfessionInfos(mConfessionDataBeans)

        if (confessionDataBeans != null && confessionDataBeans.size == pageSize) {
            mView.loadMoreComplete()
        } else {
            mView.loadEnd()
        }

    }


    fun netNormalData(requestMap: Map<String, String?>, requestUrl: String) {

        mView.showLoading()
        mModel?.netNormalData(requestMap, requestUrl)?.subscribe(object : DisposableObserver<ImageCreateBean>() {
            override fun onNext(t: ImageCreateBean) {
                t.let {
                    if (t.status) {
                        mView.showNormalDataSuccess(t.data)
                    }
                }
            }

            override fun onComplete() {
                mView.hideLoading()

            }

            override fun onError(e: Throwable) {

            }
        })


    }

    fun netUpFileNet(requestMap: Map<String, String?>, upFile: File, requestUrl: String) {
        mView.showLoading()
        mModel?.netUpFileNet(requestMap, upFile, requestUrl)?.subscribe(object : DisposableObserver<ImageCreateBean>() {
            override fun onNext(t: ImageCreateBean) {
                t.let {
                    if (t.status) {
                        mView.showNormalDataSuccess(t.data)
                    }
                }
            }

            override fun onComplete() {
                mView.hideLoading()
            }

            override fun onError(e: Throwable) {

            }

        })

    }

}
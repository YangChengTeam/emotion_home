package com.yc.emotion.home.index.presenter

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.model.LoveCaseModel
import com.yc.emotion.home.index.view.LoveCaseView
import com.yc.emotion.home.model.bean.ExampDataBean
import com.yc.emotion.home.model.bean.MainT2Bean
import com.yc.emotion.home.utils.CommonInfoHelper
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.observers.DisposableObserver
import yc.com.rthttplibrary.config.HttpConfig


/**
 *
 * Created by suns  on 2019/11/20 13:28.
 */
class LoveCasePresenter(context: Context, view: LoveCaseView) : BasePresenter<LoveCaseModel, LoveCaseView>(context, view) {

    init {
        mModel = LoveCaseModel(context)
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {

    }

    override fun getCache() {

    }


    fun getLoveCaseCache() {
        CommonInfoHelper.getO(mContext, "main2_example_lists", object : TypeReference<List<MainT2Bean>>() {

        }.type, object : CommonInfoHelper.OnParseListener<List<MainT2Bean>> {
            override fun onParse(o: List<MainT2Bean>?) {
                if (o != null && o.isNotEmpty()) {
                    //                initRecyclerViewData();
                    mView.showLoveCaseList(o)
                }
            }

//
        })
    }

    fun exampLists(page: Int, pageSize: Int) {
        val userId = UserInfoHelper.instance.getUid()
        if (page == 1) {
            mView.showLoadingDialog()
        }
        mModel?.exampLists("$userId", page, pageSize)?.subscribe(object : DisposableObserver<yc.com.rthttplibrary.bean.ResultInfo<ExampDataBean>>() {
            override fun onNext(t: yc.com.rthttplibrary.bean.ResultInfo<ExampDataBean>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        createNewData(t.data, page, pageSize)
                    }
                }
            }

            override fun onComplete() {
                if (page == 1) mView.hideLoadingDialog()
                mView.onComplete()
            }

            override fun onError(e: Throwable) {
                if (page == 1) mView.hideLoadingDialog()
                mView.onError()
            }

        })

    }

    private fun createNewData(exampDataBean: ExampDataBean, page: Int, pageSize: Int) {
        val isVip = exampDataBean.is_vip
        var mUserIsVip = false
        if (isVip > 0) {
            mUserIsVip = true
        }
        val exampListsBeans = exampDataBean.lists
        val mMainT2Beans = arrayListOf<MainT2Bean>()
        if (page == 1)
            mMainT2Beans.add(MainT2Bean("tit", MainT2Bean.VIEW_TITLE))
        if (exampListsBeans != null && exampListsBeans.size != 0) {
            for (i in exampListsBeans.indices) {
                val exampListsBean = exampListsBeans[i]
                mMainT2Beans.add(MainT2Bean(MainT2Bean.VIEW_ITEM, exampListsBean.create_time, exampListsBean.id, exampListsBean.image, exampListsBean.post_title))
            }
        }
        if (!mUserIsVip && mMainT2Beans.size > 6 && page == 1) {
            mMainT2Beans.add(6, MainT2Bean("vip", MainT2Bean.VIEW_VIP))
            mMainT2Beans.add(MainT2Bean("toPayVip", MainT2Bean.VIEW_TO_PAY_VIP, mMainT2Beans.size))
        }


        if (page == 1) {

            CommonInfoHelper.setO<List<MainT2Bean>>(mContext, mMainT2Beans, "main2_example_lists")
        }
        mView.showLoveCaseList(mMainT2Beans)

        if (exampListsBeans != null && exampListsBeans.size == pageSize) {
            mView.loadMoreComplete()
        } else {
            mView.loadEnd()
        }
//        if (love_case_swipe_refresh.isRefreshing) love_case_swipe_refresh.isRefreshing = false


    }


}
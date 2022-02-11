package com.yc.emotion.home.index.presenter

import android.content.Context
import com.alibaba.fastjson.TypeReference

import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.model.PracticeModel
import com.yc.emotion.home.index.view.PracticeView
import com.yc.emotion.home.model.bean.ExampDataBean
import com.yc.emotion.home.model.bean.MainT2Bean
import com.yc.emotion.home.utils.CommonInfoHelper
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.observers.DisposableObserver
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.config.HttpConfig

/**
 *
 * Created by suns  on 2019/11/13 17:11.
 * 实战
 */
class PracticePresenter(context: Context?, view: PracticeView) : BasePresenter<PracticeModel, PracticeView>(context, view) {

    init {
        mModel = PracticeModel(context)
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {
        if (!isForceUI) return
    }

    override fun getCache() {

        CommonInfoHelper.getO(mContext, "main2_example_lists", object : TypeReference<List<MainT2Bean>>() {

        }.type, object : CommonInfoHelper.OnParseListener<List<MainT2Bean>> {
            override fun onParse(o: List<MainT2Bean>?) {
                if (o != null && o.isNotEmpty()) {
                    mView.showPracticeInfoList(o)
                }
            }


        })
        getPracticeInfos(1, 10, false)

    }

    override fun isLoadingCache(): Boolean {
        return false
    }

    fun getPracticeInfos(page: Int, pageSize: Int, isRefresh: Boolean) {
        val userId = UserInfoHelper.instance.getUid()

        mModel?.getPracticeInfos("$userId", page, pageSize)?.getData(mView, { it, _ ->
            it?.let {
                createNewData(page, pageSize, it)
            }
        }, { _, _ -> mView.onError() }, page == 1 && !isRefresh)

    }


    private fun createNewData(page: Int, pageSize: Int, exampDataBean: ExampDataBean) {
        val isVip = exampDataBean.is_vip
        var mUserIsVip = false
        if (isVip > 0) {
            mUserIsVip = true
        }
        val exampListsBeans = exampDataBean.lists

        val mMainT2Beans = arrayListOf<MainT2Bean>()
//        if (page == 1) mMainT2Beans.add(MainT2Bean("tit", MainT2Bean.VIEW_TITLE))
        if (exampListsBeans != null && exampListsBeans.size > 0) {


            for (exampListsBean in exampListsBeans) {
//                var type = MainT2Bean.VIEW_ITEM
//                if (page > 1) {
//                    type = if (exampDataBean.is_vip > 0) MainT2Bean.VIEW_ITEM else MainT2Bean.VIEW_TO_PAY_VIP
//                }


//                val type = if (exampListsBean.is_vip > 0) MainT2Bean.VIEW_ITEM else MainT2Bean.VIEW_TO_PAY_VIP
                val type =  MainT2Bean.VIEW_ITEM


                mMainT2Beans.add(MainT2Bean(type, exampListsBean.create_time, exampListsBean.id, exampListsBean.image, exampListsBean.post_title))
            }

        }
//        if (!mUserIsVip && mMainT2Beans.size > 6 && page == 1) {
//            mMainT2Beans.add(6, MainT2Bean("vip", MainT2Bean.VIEW_VIP))
//        }

        if (page == 1) {
            CommonInfoHelper.setO<List<MainT2Bean>>(mContext, mMainT2Beans, "main2_example_lists")
        }
        mView.showPracticeInfoList(mMainT2Beans)

        if (exampListsBeans != null && exampListsBeans.size == pageSize) {
            mView.loadMoreComplete()
        } else {
            mView.loadMoreEnd()
        }

    }


}
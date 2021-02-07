package com.yc.emotion.home.index.presenter

import android.content.Context
import com.alibaba.fastjson.TypeReference

import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.model.TutorModel
import com.yc.emotion.home.index.view.TutorView
import com.yc.emotion.home.model.bean.*
import com.yc.emotion.home.utils.CommonInfoHelper
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.observers.DisposableObserver
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.config.HttpConfig


/**
 *
 * Created by suns  on 2019/11/12 16:47.
 */
class TutorPresenter(context: Context?, view: TutorView) : BasePresenter<TutorModel, TutorView>(context, view) {

    init {
        mModel = TutorModel(context)
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {
        if (!isForceUI) return
    }

    override fun getCache() {
        CommonInfoHelper.getO(mContext, "tutor_category_list", object : TypeReference<List<CourseInfo>>() {}.type,
                object : CommonInfoHelper.OnParseListener<List<CourseInfo>> {

                    override fun onParse(o: List<CourseInfo>?) {
                        o?.let {
                            if (o.isNotEmpty()) {
                                mView.showTutorCategory(o)
                            }
                        }
                    }

                })
    }

    override fun isLoadingCache(): Boolean {
        return false
    }

    fun getTutorDetailInfo(tutor_id: String?) {

        mModel?.getTutorDetailInfo(tutor_id)?.getData(mView, { it, _ ->
            it?.let {
                mView.showTutorDetailInfo(it)
            }
        }, { _, _ -> })

    }

    fun getTutorServices(tutor_id: String?, page: Int, page_sie: Int) {

        mModel?.getTutorServices(tutor_id, page, page_sie)?.getData(mView, { it, _ ->
            if (it != null && it.isNotEmpty()) {
                mView.showTutorServiceInfos(it)
            } else {
                if (page == 1) {
                    mView.onNoData()
                }
            }
        }, { _, _ -> mView.onComplete() })


    }


    fun getTutorServiceDetailInfo(service_id: String?) {
        mModel?.getTutorServiceDetailInfo(service_id)?.getData(mView, { it, _ ->
            it?.let {
                mView.showTutorServiceDetailInfo(it)
            }
        }, { _, _ -> })


    }


    fun initOrders(pay_way_name: String, money: String, title: String, goodId: String) {
        val userId = UserInfoHelper.instance.getUid()

        mModel?.initOrders("$userId", pay_way_name, money, title, goodId)?.getData(mView, { it, _ ->
            it?.let {
                mView.showTutorServiceOrder(pay_way_name, it)
            }
        }, { _, _ -> })

    }

    fun getApitudeInfo(tutor_id: String?) {

        mModel?.getApitudeInfo(tutor_id)?.getData(mView, { it, _ ->
            it?.let {
                mView.showTutorApitude(it)
            }
        }, { _, _ -> })

    }

    /**
     * 获取导师分类
     */
    fun getTutorCategory() {
        mModel?.getTutorCategory()?.getData(mView, { it, _ ->
            if (it != null) {
                mView.showTutorCategory(it)
                CommonInfoHelper.setO(mContext, it, "tutor_category_list")
            } else {
                getCache()
            }
        }, { _, _ -> getCache() }, false)

    }

    fun getTutorListInfo(catid: String?, page: Int, pageSize: Int) {

        mModel?.getTutorListInfo(catid, page, pageSize)?.getData(mView, { it, _ ->
            if (it != null && it.isNotEmpty()) {
                mView.showTutorListInfos(it)
            } else {
                mView.onNoData()

            }
        }, { _, _ -> mView.onNoData() })


    }
}
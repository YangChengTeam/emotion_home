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
        mView.showLoadingDialog()
        mModel?.getTutorDetailInfo(tutor_id)?.subscribe(object : DisposableObserver<ResultInfo<TutorDetailInfo>>() {
            override fun onNext(t: ResultInfo<TutorDetailInfo>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showTutorDetailInfo(t.data)
                    }
                }

            }

            override fun onComplete() {
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable) {

            }
        })

    }

    fun getTutorServices(tutor_id: String?, page: Int, page_sie: Int) {
        mView.showLoadingDialog()
        mModel?.getTutorServices(tutor_id, page, page_sie)?.subscribe(object : DisposableObserver<ResultInfo<List<TutorServiceInfo>>>() {
            override fun onNext(t: ResultInfo<List<TutorServiceInfo>>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK) {
                        val tutorServices = t.data
                        if (tutorServices != null && tutorServices.isNotEmpty()) {
                            mView.showTutorServiceInfos(tutorServices)
                        } else {
                            if (page == 1) {
                                mView.onNoData()
                            }
                        }

                    }
                }
            }

            override fun onComplete() {
                mView.onComplete()
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable) {

            }

        })

    }


    fun getTutorServiceDetailInfo(service_id: String?) {
        mView.showLoadingDialog()
        mModel?.getTutorServiceDetailInfo(service_id)?.subscribe(object : DisposableObserver<ResultInfo<TutorServiceDetailInfo>>() {
            override fun onNext(t: ResultInfo<TutorServiceDetailInfo>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showTutorServiceDetailInfo(t.data)
                    }
                }
            }

            override fun onComplete() {
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable) {

            }

        })

    }


    fun initOrders(pay_way_name: String, money: String, title: String, goodId: String) {
        val userId = UserInfoHelper.instance.getUid()
        mView.showLoadingDialog()
        mModel?.initOrders("$userId", pay_way_name, money, title, goodId)?.subscribe(object : DisposableObserver<ResultInfo<OrdersInitBean>>() {
            override fun onNext(t: ResultInfo<OrdersInitBean>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showTutorServiceOrder(pay_way_name, t.data)
                    }
                }
            }

            override fun onComplete() {
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable) {

            }

        })

    }

    fun getApitudeInfo(tutor_id: String?) {
        mView.showLoadingDialog()
        mModel?.getApitudeInfo(tutor_id)?.subscribe(object : DisposableObserver<ResultInfo<TutorInfoWrapper>>() {
            override fun onNext(t: ResultInfo<TutorInfoWrapper>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showTutorApitude(t.data)
                    }
                }
            }

            override fun onComplete() {
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable) {

            }

        })

    }

    /**
     * 获取导师分类
     */
    fun getTutorCategory() {
        mModel?.getTutorCategory()?.subscribe(object : DisposableObserver<ResultInfo<List<CourseInfo>>>() {
            override fun onNext(t: ResultInfo<List<CourseInfo>>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showTutorCategory(t.data)
                        CommonInfoHelper.setO(mContext, t.data, "tutor_category_list")
                    } else {
                        getCache()
                    }
                }
            }

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                getCache()
            }

        })

    }

    fun getTutorListInfo(catid: String?, page: Int, pageSize: Int) {
        mView.showLoadingDialog()
        mModel?.getTutorListInfo(catid, page, pageSize)?.subscribe(object : DisposableObserver<ResultInfo<List<TutorInfo>>>() {
            override fun onNext(t: ResultInfo<List<TutorInfo>>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK) {
                        if (t.data != null && t.data.isNotEmpty()) {
                            val tutorList = t.data
                            mView.showTutorListInfos(tutorList)
                        } else {
                            mView.onNoData()

                        }
                    } else {
                        mView.onNoData()

                    }


                }
            }

            override fun onComplete() {
                mView.hideLoadingDialog()
                mView.onComplete()
            }

            override fun onError(e: Throwable) {
            }
        })

    }
}
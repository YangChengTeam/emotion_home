package com.yc.emotion.home.index.presenter

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.model.TutorModel
import com.yc.emotion.home.index.view.TutorView
import com.yc.emotion.home.model.bean.*
import com.yc.emotion.home.utils.CommonInfoHelper
import com.yc.emotion.home.utils.UserInfoHelper
import rx.Subscriber

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
        CommonInfoHelper.getO<List<CourseInfo>>(mContext, "tutor_category_list", object : TypeReference<List<CourseInfo>>() {}.type) { courseInfos ->
            courseInfos?.let {
                if (courseInfos.isNotEmpty()) {
                    mView.showTutorCategory(courseInfos)
                }
            }

        }
    }

    override fun isLoadingCache(): Boolean {
        return false
    }

    fun getTutorDetailInfo(tutor_id: String?) {
        mView.showLoadingDialog()
        val subscription = mModel?.getTutorDetailInfo(tutor_id)?.subscribe(object : Subscriber<ResultInfo<TutorDetailInfo>>() {
            override fun onNext(t: ResultInfo<TutorDetailInfo>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showTutorDetailInfo(t.data)
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

    fun getTutorServices(tutor_id: String?, page: Int, page_sie: Int) {
        mView.showLoadingDialog()
        val subscription = mModel?.getTutorServices(tutor_id, page, page_sie)?.subscribe(object : Subscriber<ResultInfo<List<TutorServiceInfo>>>() {
            override fun onNext(t: ResultInfo<List<TutorServiceInfo>>?) {
                t?.let {
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

            override fun onCompleted() {
                mView.onComplete()
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable?) {

            }

        })
        subScriptions?.add(subscription)
    }


    fun getTutorServiceDetailInfo(service_id: String?) {
        mView.showLoadingDialog()
        val subscription = mModel?.getTutorServiceDetailInfo(service_id)?.subscribe(object : Subscriber<ResultInfo<TutorServiceDetailInfo>>() {
            override fun onNext(t: ResultInfo<TutorServiceDetailInfo>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showTutorServiceDetailInfo(t.data)
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
            override fun onNext(t: AResultInfo<OrdersInitBean>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showTutorServiceOrder(pay_way_name, t.data)
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

    fun getApitudeInfo(tutor_id: String?) {
        mView.showLoadingDialog()
        val subscription = mModel?.getApitudeInfo(tutor_id)?.subscribe(object : Subscriber<ResultInfo<TutorInfoWrapper>>() {
            override fun onNext(t: ResultInfo<TutorInfoWrapper>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showTutorApitude(t.data)
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

    /**
     * 获取导师分类
     */
    fun getTutorCategory() {
        val subscription = mModel?.getTutorCategory()?.subscribe(object : Subscriber<ResultInfo<List<CourseInfo>>>() {
            override fun onNext(t: ResultInfo<List<CourseInfo>>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showTutorCategory(t.data)
                        CommonInfoHelper.setO(mContext, t.data, "tutor_category_list")
                    } else {
                        getCache()
                    }
                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                getCache()
            }

        })
        subScriptions?.add(subscription)
    }

    fun getTutorListInfo(catid: String?, page: Int, pageSize: Int) {
        mView.showLoadingDialog()
        val subscription = mModel?.getTutorListInfo(catid, page, pageSize)?.subscribe(object : Subscriber<ResultInfo<List<TutorInfo>>>() {
            override fun onNext(t: ResultInfo<List<TutorInfo>>?) {
                t?.let {
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

            override fun onCompleted() {
                mView.hideLoadingDialog()
                mView.onComplete()
            }

            override fun onError(e: Throwable?) {
            }
        })
        subScriptions?.add(subscription)
    }
}
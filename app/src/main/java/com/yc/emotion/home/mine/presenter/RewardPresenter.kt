package com.yc.emotion.home.mine.presenter

import android.content.Context
import android.text.TextUtils
import android.view.TextureView
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.mine.domain.bean.DisposeDetailInfo
import com.yc.emotion.home.mine.domain.bean.RewardDetailInfo
import com.yc.emotion.home.mine.domain.bean.RewardInfo
import com.yc.emotion.home.mine.domain.model.RewardModel
import com.yc.emotion.home.mine.view.RewardView
import com.yc.emotion.home.model.bean.ShareInfo
import com.yc.emotion.home.utils.ToastUtils
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Field
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.config.HttpConfig

/**
 *
 * Created by suns  on 2020/8/26 10:20.
 */
class RewardPresenter(context: Context?, view: RewardView) : BasePresenter<RewardModel, RewardView>(context, view) {

    init {
        mModel = RewardModel(context)
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {

    }

    override fun getCache() {

    }

    fun getRewardInfo() {
        mModel?.getRewardInfo()?.subscribe(object : DisposableObserver<ResultInfo<RewardInfo>>() {
            override fun onComplete() {

            }

            override fun onNext(t: ResultInfo<RewardInfo>) {
                t.let {
                    if (it.code == HttpConfig.STATUS_OK && it.data != null) {
                        mView.showRewardInfo(it.data)
                    }
                }
            }

            override fun onError(e: Throwable) {

            }
        })
    }

    fun bindInvitationCode(code: String) {
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showCenterToast("邀请码不能为空")
            return
        }
        mView.showLoadingDialog()
        mModel?.bindInvitationCode(code)?.subscribe(object : DisposableObserver<ResultInfo<String>>() {
            override fun onComplete() {

            }

            override fun onNext(t: ResultInfo<String>) {
                mView.hideLoadingDialog()
                t.let {
                    if (t.code == HttpConfig.STATUS_OK) {
                        mView.showBindSuccess()
                        ToastUtils.showCenterToast(t.message)
                    } else {
                        ToastUtils.showCenterToast(t.message)
                    }
                }
            }

            override fun onError(e: Throwable) {
                mView.hideLoadingDialog()
            }
        })
    }

    fun getRewardDetailInfoList(page: Int, pageSize: Int) {
        mView.showLoadingDialog()
        mModel?.getRewardDetailInfoList(page, pageSize)?.subscribe(object : DisposableObserver<ResultInfo<List<RewardDetailInfo>>>() {
            override fun onComplete() {

            }

            override fun onNext(t: ResultInfo<List<RewardDetailInfo>>) {
                mView.hideLoadingDialog()
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null && t.data.isNotEmpty()) {
                        mView.showRewardDetailList(t.data)
                    } else {
                        if (page == 1) {
                            mView.onNoData()
                        }
                    }
                }
            }

            override fun onError(e: Throwable) {
                mView.hideLoadingDialog()
                if (page == 1)
                    mView.onError()
            }
        })
    }


    fun applyDispose(amount: String, alipay_number: String, truename: String) {

        mView.showLoadingDialog()

        mModel?.applyDispose(amount, alipay_number, truename)?.subscribe(object : DisposableObserver<ResultInfo<String>>() {
            override fun onComplete() {

            }

            override fun onNext(t: ResultInfo<String>) {
                mView.hideLoadingDialog()
                if (t.code == HttpConfig.STATUS_OK) {
                    mView.showDisposeApplySuccess()
                    ToastUtils.showCenterToast(t.message)
                } else {
                    ToastUtils.showCenterToast(t.message)
                }
            }

            override fun onError(e: Throwable) {
                mView.hideLoadingDialog()
            }
        })
    }

    fun getDisposeDetailInfoList(page: Int, pageSize: Int) {
        mView.showLoadingDialog()
        mModel?.getDisposeDetailInfoList(page, pageSize)?.subscribe(object : DisposableObserver<ResultInfo<List<DisposeDetailInfo>>>() {
            override fun onComplete() {

            }

            override fun onNext(t: ResultInfo<List<DisposeDetailInfo>>) {
                mView.hideLoadingDialog()
                if (t.code == HttpConfig.STATUS_OK && t.data != null && t.data.isNotEmpty()) {
                    mView.showDisposeDetailInfoList(t.data)
                } else {
                    if (page == 1)
                        mView.onNoData()
                }
            }

            override fun onError(e: Throwable) {
                mView.hideLoadingDialog()
            }
        })
    }

    fun getRewardMoneyList(page: Int, pageSize: Int) {
        mView.showLoadingDialog()
        mModel?.getRewardMoneyList(page, pageSize)?.subscribe(object : DisposableObserver<ResultInfo<List<RewardDetailInfo>>>() {
            override fun onComplete() {

            }

            override fun onNext(t: ResultInfo<List<RewardDetailInfo>>) {
                mView.hideLoadingDialog()
                if (t.code == HttpConfig.STATUS_OK) {
                    if (t.data != null && t.data.isNotEmpty()) {
                        mView.showRewardMoneyList(t.data)
                    }
                } else {
                    if (page == 1) {
                        mView.onNoData()
                    } else {
                        mView.onError()
                    }
                }
            }

            override fun onError(e: Throwable) {
                mView.hideLoadingDialog()
                mView.onError()
            }
        })
    }

    fun getRankingList(page: Int, pageSize: Int) {
        mView.showLoadingDialog()
        mModel?.getRankingList(page, pageSize)?.subscribe(object : DisposableObserver<ResultInfo<List<RewardDetailInfo>>>() {
            override fun onComplete() {

            }

            override fun onNext(t: ResultInfo<List<RewardDetailInfo>>) {
                mView.hideLoadingDialog()
                if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                    mView.showRankingList(t.data)
                } else {
                    if (page == 1) {
                        mView.onNoData()
                    }
                }
            }

            override fun onError(e: Throwable) {
                if (page == 1)
                    mView.onError()
            }
        })
    }
    fun isBindInvitation(){
        mModel?.isBindInvitation()?.subscribe(object :DisposableObserver<ResultInfo<RewardInfo>>(){
            override fun onComplete() {

            }

            override fun onNext(t: ResultInfo<RewardInfo>) {
                if (t.code==HttpConfig.STATUS_OK&&t.data!=null){
                    mView.showBindInvitation(t.data)
                }
            }

            override fun onError(e: Throwable) {

            }
        })
    }
}
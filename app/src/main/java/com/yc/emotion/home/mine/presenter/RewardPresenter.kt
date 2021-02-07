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
        mModel?.getRewardInfo()?.getData(mView, { it, _ ->
            it?.let {
                mView.showRewardInfo(it)
            }
        }, { _, _ -> }, false)

    }

    fun bindInvitationCode(code: String) {
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showCenterToast("邀请码不能为空")
            return
        }
        mModel?.bindInvitationCode(code)?.getData(mView, { it, msg ->
            mView.showBindSuccess()
            msg?.let {

                ToastUtils.showCenterToast(msg)
            }
        }, { _, msg -> ToastUtils.showCenterToast(msg) })

    }

    fun getRewardDetailInfoList(page: Int, pageSize: Int) {

        mModel?.getRewardDetailInfoList(page, pageSize)?.getData(mView, { it, _ ->
            if (it != null && it.isNotEmpty()) {
                mView.showRewardDetailList(it)
            } else {
                if (page == 1) {
                    mView.onNoData()
                }
            }
        }, { _, _ ->
            if (page == 1)
                mView.onError()
        })

    }


    fun applyDispose(amount: String, alipay_number: String, truename: String) {

        mModel?.applyDispose(amount, alipay_number, truename)?.getData(mView, { _, msg ->
            mView.showDisposeApplySuccess()
            ToastUtils.showCenterToast(msg)
        }, { _, msg -> ToastUtils.showCenterToast(msg) })

    }

    fun getDisposeDetailInfoList(page: Int, pageSize: Int) {

        mModel?.getDisposeDetailInfoList(page, pageSize)?.getData(mView, { it, _ ->
            if (it != null && it.isNotEmpty()) {
                mView.showDisposeDetailInfoList(it)
            } else {
                if (page == 1)
                    mView.onNoData()
            }
        }, { _, _ -> })

    }

    fun getRewardMoneyList(page: Int, pageSize: Int) {

        mModel?.getRewardMoneyList(page, pageSize)?.getData(mView, { it, _ ->
            if (it != null && it.isNotEmpty()) {
                mView.showRewardMoneyList(it)
            } else {
                if (page == 1) {
                    mView.onNoData()
                }
            }
        }, { _, _ -> mView.onError() })

    }

    fun getRankingList(page: Int, pageSize: Int) {

        mModel?.getRankingList(page, pageSize)?.getData(mView, { it, _ ->
            if (it != null) {
                mView.showRankingList(it)
            } else {
                if (page == 1) {
                    mView.onNoData()
                }
            }
        }, { _, _ ->
            if (page == 1)
                mView.onError()
        }, page == 1)

    }

    fun isBindInvitation() {
        mModel?.isBindInvitation()?.getData(mView, { it, _ ->
            it?.let {
                mView.showBindInvitation(it)
            }
        }, { _, _ -> }, false)
    }
}
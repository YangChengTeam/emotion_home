package com.yc.emotion.home.mine.domain.model

import android.content.Context
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.mine.domain.bean.DisposeDetailInfo
import com.yc.emotion.home.mine.domain.bean.RewardDetailInfo
import com.yc.emotion.home.mine.domain.bean.RewardInfo
import com.yc.emotion.home.model.bean.ShareInfo
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Field
import yc.com.rthttplibrary.bean.ResultInfo

/**
 *
 * Created by suns  on 2020/8/26 10:09.
 */
class RewardModel(override var context: Context?) : IModel(context) {

    fun getRewardInfo(): Observable<ResultInfo<RewardInfo>>? {
        return request.getRewardInfo("${UserInfoHelper.instance.getUid()}").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun bindInvitationCode(code: String): Observable<ResultInfo<String>>? {
        return request.bindInvitation("${UserInfoHelper.instance.getUid()}", code).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun getRewardDetailInfoList(page: Int, pageSize: Int): Observable<ResultInfo<List<RewardDetailInfo>>>? {
        return request.getRewardDetailInfo("${UserInfoHelper.instance.getUid()}", page, pageSize).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    fun applyDispose(amount: String, alipay_number: String, truename: String): Observable<ResultInfo<String>>? {
        return request.applyDispose("${UserInfoHelper.instance.getUid()}", amount, alipay_number, truename).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    fun getDisposeDetailInfoList(page: Int, pageSize: Int): Observable<ResultInfo<List<DisposeDetailInfo>>>? {
        return request.getDisposeDetailInfoList("${UserInfoHelper.instance.getUid()}", page, pageSize).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun getRewardMoneyList(page: Int, pageSize: Int): Observable<ResultInfo<List<RewardDetailInfo>>>? {
        return request.getRewardMoneyList("${UserInfoHelper.instance.getUid()}", page, pageSize).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun getRankingList(page: Int, pageSize: Int): Observable<ResultInfo<List<RewardDetailInfo>>>? {
        return request.getRankingList(page, pageSize).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun isBindInvitation(): Observable<ResultInfo<RewardInfo>>? {
        return request.isBindInvitation("${UserInfoHelper.instance.getUid()}").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}
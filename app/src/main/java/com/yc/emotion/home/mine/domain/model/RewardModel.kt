package com.yc.emotion.home.mine.domain.model

import android.content.Context
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.mine.domain.bean.DisposeDetailInfo
import com.yc.emotion.home.mine.domain.bean.RewardDetailInfo
import com.yc.emotion.home.mine.domain.bean.RewardInfo
import com.yc.emotion.home.model.bean.ShareInfo
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.Flowable
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

    fun getRewardInfo(): Flowable<ResultInfo<RewardInfo>> {
        return request.getRewardInfo("${UserInfoHelper.instance.getUid()}")
    }

    fun bindInvitationCode(code: String): Flowable<ResultInfo<String>> {
        return request.bindInvitation("${UserInfoHelper.instance.getUid()}", code)
    }

    fun getRewardDetailInfoList(page: Int, pageSize: Int): Flowable<ResultInfo<List<RewardDetailInfo>>> {
        return request.getRewardDetailInfo("${UserInfoHelper.instance.getUid()}", page, pageSize)
    }


    fun applyDispose(amount: String, alipay_number: String, truename: String): Flowable<ResultInfo<String>> {
        return request.applyDispose("${UserInfoHelper.instance.getUid()}", amount, alipay_number, truename)
    }


    fun getDisposeDetailInfoList(page: Int, pageSize: Int): Flowable<ResultInfo<List<DisposeDetailInfo>>> {
        return request.getDisposeDetailInfoList("${UserInfoHelper.instance.getUid()}", page, pageSize)
    }

    fun getRewardMoneyList(page: Int, pageSize: Int): Flowable<ResultInfo<List<RewardDetailInfo>>> {
        return request.getRewardMoneyList("${UserInfoHelper.instance.getUid()}", page, pageSize)
    }

    fun getRankingList(page: Int, pageSize: Int): Flowable<ResultInfo<List<RewardDetailInfo>>> {
        return request.getRankingList(page, pageSize)
    }

    fun isBindInvitation(): Flowable<ResultInfo<RewardInfo>> {
        return request.isBindInvitation("${UserInfoHelper.instance.getUid()}")
    }
}
package com.yc.emotion.home.mine.presenter

import android.content.Context
import android.text.TextUtils

import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.mine.domain.model.MineModel
import com.yc.emotion.home.mine.view.MineView
import com.yc.emotion.home.model.bean.UserInfo
import com.yc.emotion.home.model.bean.UserInterInfo
import com.yc.emotion.home.utils.ToastUtils
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_main_mine.*
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.config.HttpConfig

/**
 *
 * Created by suns  on 2019/11/15 15:04.
 */
class MinePresenter(context: Context?, view: MineView) : BasePresenter<MineModel, MineView>(context, view) {

    init {
        mModel = MineModel(context)
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {

    }

    override fun getCache() {

    }


    fun userInfo() {
        val userId = UserInfoHelper.instance.getUid()
        mModel?.userInfo("$userId")?.getData(mView, { it, _ ->
            it?.let {
                mView.showUserInfo(it)
            }
        }, { _, _ -> })


    }


    fun updateUserInfo(nickName: String?, face: String?, password: String, birthday: String, sex: Int, profession: String?, age: String?, signature: String?, interested_id: String?) {

        if (TextUtils.isEmpty(nickName) && (sex == 0) && TextUtils.isEmpty(profession) && TextUtils.isEmpty(age) && TextUtils.isEmpty(signature) && TextUtils.isEmpty(interested_id)) {
            ToastUtils.showCenterToast("请填写用户资料")
            return
        }

        val userId = UserInfoHelper.instance.getUid()

        mModel?.updateUserInfo("$userId", nickName, face, password, birthday, sex, profession, age, signature, interested_id)?.getData(mView, { it, _ ->
            it?.let {
                mView.showUpdateUserInfo(it)
            }
        }, { _, _ -> })


    }


    fun getUserInterseInfo() {
        mModel?.getUserInterseInfo()?.getData(mView, { it, _ ->
            it?.let {
                mView.showUserInterseInfo(it)
            }
        }, { _, _ -> }, false)

    }


    fun addSuggestion(content: String?, qq: String?, wechat: String?) {


        if (!UserInfoHelper.instance.goToLogin(mContext)) {

            val userId = UserInfoHelper.instance.getUid()

            mModel?.addSuggestion("$userId", content, qq, wechat, "user.suggestion/add")?.getData(mView, { it, message ->
                it?.let {
                    ToastUtils.showCenterToast(message)
                    mView.showSuggestionSuccess()
                }
            }, { _, _ -> })
        }
    }

    fun getRewardInfo() {
        mModel?.getRewardInfo()?.getData(mView, { it, _ ->
            it?.let {

                if (!TextUtils.isEmpty(it.code))
                    mView.showInvatationcode(it.code)

            }
        }, { _, _ -> }, false)
    }
}
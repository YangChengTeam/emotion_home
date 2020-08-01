package com.yc.emotion.home.mine.presenter

import android.content.Context
import android.text.TextUtils
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig

import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.mine.domain.model.MineModel
import com.yc.emotion.home.mine.view.MineView
import com.yc.emotion.home.model.bean.AResultInfo
import com.yc.emotion.home.model.bean.UserInfo
import com.yc.emotion.home.model.bean.UserInterInfo
import com.yc.emotion.home.utils.ToastUtils
import com.yc.emotion.home.utils.UserInfoHelper
import rx.Subscriber

/**
 *
 * Created by suns  on 2019/11/15 15:04.
 */
class MinePresenter(context: Context, view: MineView) : BasePresenter<MineModel, MineView>(context, view) {

    init {
        mModel = MineModel(context)
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {

    }

    override fun getCache() {

    }


    fun userInfo() {
        val userId = UserInfoHelper.instance.getUid()
        mView.showLoadingDialog()
        val subscription = mModel?.userInfo("$userId")?.subscribe(object : Subscriber<AResultInfo<UserInfo>>() {
            override fun onNext(t: AResultInfo<UserInfo>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showUserInfo(t.data)
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


    fun updateUserInfo(nickName: String?, face: String?, password: String, birthday: String, sex: Int, profession: String?, age: String?, signature: String?, interested_id: String?) {

        if (TextUtils.isEmpty(nickName) && (sex == 0) && TextUtils.isEmpty(profession) && TextUtils.isEmpty(age) && TextUtils.isEmpty(signature) && TextUtils.isEmpty(interested_id)) {
            ToastUtils.showCenterToast("请填写用户资料")
            return
        }


        val userId = UserInfoHelper.instance.getUid()

        mView.showLoadingDialog()

        val subscription = mModel?.updateUserInfo("$userId", nickName, face, password, birthday, sex, profession, age, signature, interested_id)
                ?.subscribe(object : Subscriber<ResultInfo<UserInfo>>() {
                    override fun onNext(t: ResultInfo<UserInfo>?) {
                        t?.let {
                            if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                                mView.showUpdateUserInfo(t.data)
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


    fun getUserInterseInfo() {
        val subscription = mModel?.getUserInterseInfo()?.subscribe(object : Subscriber<ResultInfo<List<UserInterInfo>>>() {
            override fun onNext(t: ResultInfo<List<UserInterInfo>>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showUserInterseInfo(t.data)
                    }
                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
            }

        })
        subScriptions?.add(subscription)
    }


    fun addSuggestion(content: String?, qq: String?, wechat: String?) {


        if (!UserInfoHelper.instance.goToLogin(mContext)) {

            val userId = UserInfoHelper.instance.getUid()

            mView.showLoadingDialog()

            val subscription = mModel?.addSuggestion("$userId", content, qq, wechat, "user.suggestion/add")?.subscribe(object : Subscriber<AResultInfo<String>>() {
                override fun onNext(t: AResultInfo<String>?) {
                    t?.let {
                        if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                            val message = t.msg
                            ToastUtils.showCenterToast(message)
                            mView.showSuggestionSuccess()
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


    }

}
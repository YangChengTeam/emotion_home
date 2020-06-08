package com.yc.emotion.home.mine.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.alibaba.fastjson.JSON
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.TimePickerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.yc.emotion.home.R
import com.yc.emotion.home.base.domain.engine.UploadPhotoEngin
import com.yc.emotion.home.base.ui.activity.BasePushPhotoActivity
import com.yc.emotion.home.mine.presenter.MinePresenter
import com.yc.emotion.home.mine.ui.fragment.ExitPublishFragment
import com.yc.emotion.home.mine.view.MineView
import com.yc.emotion.home.model.bean.UploadPhotoBean
import com.yc.emotion.home.model.bean.UserInfo
import com.yc.emotion.home.model.bean.UserInterInfo
import com.yc.emotion.home.model.bean.event.EventLoginState
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.utils.Preference
import com.yc.emotion.home.utils.UserInfoHelper
import kotlinx.android.synthetic.main.activity_user_info.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.io.IOException

class UserInfoActivity : BasePushPhotoActivity(), MineView {


    private var mPvTime: TimePickerView? = null


    private var mPhotoUrl: String? = null

    private var signature: String? = null
    private var sexInt by Preference(this, ConstantKey.SEX, 0)
    private var age: String? = null
    private var job: String? = null
    private var question: String? = null
    private var nickName: String? = null


    override fun getLayoutId(): Int {
        return R.layout.activity_user_info
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()

    }


    override fun initViews() {

        mPresenter = MinePresenter(this, this)

        if (sexInt != 0) {
            mineItemView_sex.setMoreText(getSexStr(sexInt))
        }

        val tvSub = offerActivitySubTitleView()
        tvSub.setTextColor(resources.getColor(R.color.gray_666))

        tvSub.text = "保存"

        val userInfo = UserInfoHelper.instance.getUserInfo()
        userInfo?.let {
            val mobile = it.mobile
            val pwd = it.pwd
            if (!TextUtils.isEmpty(mobile)) {
                mineItemView_phone.setMoreText(mobile?.replace(mobile.substring(3, 7), "****"))
            }
            mineItemView_pwd.setTitle(if (TextUtils.isEmpty(pwd)) "设置密码" else "修改密码")

        }


        tvSub.setOnClickListener(this)
        initListener()
        initData()
    }

    private fun initListener() {

        user_info_iv_icon.setOnClickListener(this)
        mineItemView_signature.setOnClickListener(this)
        mineItemView_sex.setOnClickListener(this)

        mineItemView_age.setOnClickListener(this)
        mineItemView_income.setOnClickListener(this)
        mineItemView_question.setOnClickListener(this)
        mineItemView_nickname.setOnClickListener(this)
        mineItemView_pwd.setOnClickListener(this)
        ivBack.setOnClickListener { exit() }
    }

    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    private fun initData() {

        netData()
    }

    private fun netData() {

        (mPresenter as? MinePresenter)?.userInfo()
    }

    private var count = 0//统计完成程度

    private fun initUserData(userInfo: UserInfo?) {

        nickName = userInfo?.nick_name
        val face = userInfo?.face
        val sex = userInfo?.sex
        signature = userInfo?.signature
        age = userInfo?.age
        job = userInfo?.profession

        if (!TextUtils.isEmpty(nickName)) {
            mineItemView_nickname.setMoreText(nickName)
            count += 1
        }
        mineItemView_id.setMoreText("${userInfo?.id}")
        if (!TextUtils.isEmpty(signature)) {

            mineItemView_signature.setMoreText(signature)
            count += 1
        }
        if (sex != 0) {
            count += 1
            sex?.let {
                sexInt = sex
            }
        }
        if (sex == 1) {
            mineItemView_sex.setMoreText("男神")
        } else {
            mineItemView_sex.setMoreText("女神")
        }
        if (!TextUtils.isEmpty(age)) {
            count += 1
            mineItemView_age.setMoreText(age + "后")
        }

        if (!TextUtils.isEmpty(job)) {
            mineItemView_income.setMoreText(job)
            count += 1
        }

        val inters = userInfo?.inters
        inters?.let {
            if (inters.isNotEmpty()) {
                count += 1
                val info = inters[0]
                info.let {
                    question = info.interested
                    mineItemView_question.setMoreText(question)
                }
            }

        }


        if (!TextUtils.isEmpty(face)) {
            count += 1
            Glide.with(this@UserInfoActivity).load(face).apply(RequestOptions.circleCropTransform()
                    .error(R.mipmap.main_icon_default_head).placeholder(R.mipmap.main_icon_default_head)).into(user_info_iv_icon)
            mPhotoUrl = face
        }


        val ratio = String.format("%.0f", (count / 7f) * 100)
        var text = "资料完善度$ratio%，让我们在相互了解一下吧~"
        if (ratio == "100") {
            text = "您已完善所有资料，太棒了~"
        }
        tv_user_datum_desc.text = text
    }


    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {

            R.id.user_info_iv_icon ->
                //                showToastShort("");
                showSelsctPhotoDialog(user_info_iv_icon)
            R.id.activity_base_same_tv_sub -> {

                netUpdateInfo()
            }
            R.id.mineItemView_nickname -> {
                val intent = Intent(this, EditNicknameActivity::class.java)
                intent.putExtra("nickname", nickName)

                startActivityForResult(intent, 200)
            }
            R.id.mineItemView_signature -> {
                val intent = Intent(this, EditSignatureActivity::class.java)
                intent.putExtra("signature", signature)


                startActivityForResult(intent, 100)
            }
            R.id.mineItemView_sex -> {
                val optionItems = arrayOf("男神", "女神")
                showOptionsPicker("选择性别", optionItems.toList(), OnOptionsSelectListener { options1, options2, options3, v ->
                    val text = optionItems[options1]
                    sexInt = setSex(text)
                    Log.e("TAG", text)
                    mineItemView_sex.setMoreText(text)
                })
            }

            R.id.mineItemView_age -> {
                val optionItems = arrayOf("70后", "80后", "90后", "95后", "00后")
                showOptionsPicker("选择年龄", optionItems.toList(), OnOptionsSelectListener { options1, options2, options3, v ->
                    val text = optionItems[options1]
                    age = text
                    Log.e("TAG", text)
                    mineItemView_age.setMoreText(text)
                })
            }

            R.id.mineItemView_income -> {
                val optionItems = arrayOf("学生", "白领", "私营业主", "家庭主妇", "其他")
                showOptionsPicker("选择收入", optionItems.toList(), OnOptionsSelectListener { options1, options2, options3, v ->
                    val text = optionItems[options1]
                    job = text
                    Log.e("TAG", text)
                    mineItemView_income.setMoreText(text)
                })
            }
            R.id.mineItemView_question -> {
                getUserInterInfo()
            }
            R.id.mineItemView_pwd -> {
                startActivity(Intent(this, PwdSetActivity::class.java))
            }
        }
    }

    private fun netUpdateInfo() {

        (mPresenter as? MinePresenter)?.updateUserInfo(nickName, mPhotoUrl, "", "", sexInt, job, age, signature, "$questionId")

    }

    override fun showUpdateUserInfo(data: UserInfo?) {
        fillData(data)
    }

    private fun fillData(data: UserInfo?) {
        val userInfo = UserInfoHelper.instance.getUserInfo()
        data?.let {
            val face = data.face
            val profession = data.profession
            val nickName = data.nick_name
            val age = data.age
            val inters = data.inters
            val signature = data.signature
            val sex = data.sex
            if (!TextUtils.isEmpty(face)) userInfo?.face = face
            if (!TextUtils.isEmpty(profession)) userInfo?.profession = profession
            if (!TextUtils.isEmpty(nickName)) userInfo?.nick_name = nickName
            if (!TextUtils.isEmpty(age)) userInfo?.age = age
            if (!TextUtils.isEmpty(signature)) userInfo?.signature = signature
            if (sex != 0) userInfo?.sex = sex
            inters?.let {
                if (inters.isNotEmpty()) userInfo?.inters = inters
            }


            UserInfoHelper.instance.saveUserInfo(userInfo)
        }
        EventBus.getDefault().post(EventLoginState(EventLoginState.STATE_UPDATE_INFO, userInfo))
        showToast("完善信息成功")
        finish()

    }

    private fun setSex(sex: String): Int {
        return if (TextUtils.equals("男神", sex)) {
            1
        } else {
            2
        }
    }

    private fun getSexStr(sex: Int): String {
        var sexStr = ""
        if (sex == 1) {
            sexStr = "男神"
        } else if (sex == 2) {
            sexStr = "女神"
        }
        return sexStr
    }


    var questionId: Int = 0

    private fun getUserInterInfo() {

        (mPresenter as? MinePresenter)?.getUserInterseInfo()
    }


    override fun onDestroy() {

        super.onDestroy()
        if (mPvTime != null) {
            mPvTime = null
        }
    }

    override fun offerActivityTitle(): String {
        return "我的资料"
    }

    override fun onLubanFileSuccess(file: File) {

        UploadPhotoEngin(file, object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {

                val string = response.body()?.string()
                Log.d("mylog", "onResponse: response body $string")
                if (!TextUtils.isEmpty(string)) {
                    var uploadPhotoBean: UploadPhotoBean? = null
                    try {
                        uploadPhotoBean = JSON.parseObject(string, UploadPhotoBean::class.java)
                    } catch (e: IllegalStateException) {

                    }

                    if (uploadPhotoBean != null) {
                        val data = uploadPhotoBean.data
                        if (data != null && data.size > 0) {
                            val dataBean = data[0]
                            mPhotoUrl = dataBean.url
                        }
                    }
                }
            }
        })

    }


    private fun <T> showOptionsPicker(title: String, options: List<T>, listener: OnOptionsSelectListener) {
        val optionsPickerBuilder = OptionsPickerBuilder(this, listener).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText(title)//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(ContextCompat.getColor(this, R.color.gray_222222))//标题文字颜色
                .setSubmitColor(ContextCompat.getColor(this, R.color.gray_222222))//确定按钮文字颜色
                .setCancelColor(ContextCompat.getColor(this, R.color.gray_222222))//取消按钮文字颜色
//                .setTitleBgColor(ContextCompat.getColor(this, R.color.gray_222222))//标题背景颜色 Night mode
                .setBgColor(ContextCompat.getColor(this, R.color.white))//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
//                .isDialog(true)//是否显示为对话框样式
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。

                .build<T>()


        optionsPickerBuilder.setPicker(options)
        optionsPickerBuilder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {

            if (requestCode == 100 && resultCode == 3) {
                val result = data.getStringExtra("result")
                signature = result
                mineItemView_signature.setMoreText(result)
            } else if (requestCode == 200 && resultCode == 4) {
                val result = data.getStringExtra("result")
                nickName = result
                mineItemView_nickname.setMoreText(result)
            }
        }
    }

    override fun onBackPressed() {
        exit()
    }

    private fun exit() {
        val exitFragment = ExitPublishFragment.newInstance("保存资料并返回吗?")

        exitFragment.show(supportFragmentManager, "")
        exitFragment.setOnConfirmListener (object :ExitPublishFragment.OnConfirmListener{
            override fun onConfirm() {
                finish()
            }
        })
    }


    private fun isModify(): Boolean {


        return false
    }

    override fun showUserInfo(data: UserInfo?) {
        initUserData(data)
    }

    override fun showUserInterseInfo(data: List<UserInterInfo>?) {
        val optionItems = arrayListOf<String>()
        val optionId = arrayListOf<Int>()


        data?.let {
            data.forEach {
                optionItems.add(it.interested)
                optionId.add(it.id)
            }
        }

        showOptionsPicker("关注问题", optionItems, OnOptionsSelectListener { options1, options2, options3, v ->
            val text = optionItems[options1]
            question = text
            questionId = optionId[options1]
            Log.e("TAG", question)
            mineItemView_question.setMoreText(question)
        })
    }

}

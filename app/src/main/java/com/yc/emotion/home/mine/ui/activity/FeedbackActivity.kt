package com.yc.emotion.home.mine.ui.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.mine.presenter.MinePresenter
import com.yc.emotion.home.mine.view.MineView
import kotlinx.android.synthetic.main.activity_feedback.*

class FeedbackActivity : BaseSameActivity(), MineView {


    private var mTrimEtWorkContent: String? = null
    private var mTrimEtQq: String? = null

    private var mMTrimEtWx: String? = null


    override fun getLayoutId(): Int {
        return R.layout.activity_feedback
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        initViews()

    }

    //

    override fun initViews() {

        mPresenter = MinePresenter(this, this)


        feedback_tv_cope.setOnClickListener(this)
        feedback_tv_next.setOnClickListener(this)


        feedback_et_work_content.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                feedback_tv_num.text = editable.length.toString()
            }
        })
    }

    private fun checkInput(): Boolean {
        mTrimEtWorkContent = feedback_et_work_content.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(mTrimEtWorkContent)) {
            showToast("内容不能为空")
            return false
        }
        mTrimEtQq = feedback_et_qq.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(mTrimEtQq)) {
            showToast("QQ号不能为空")
            return false
        }
        mTrimEtQq?.let {

            if (it.length < 6) {
                showToast("QQ号格式错误")
                return false
            }
        }

        mTrimEtWorkContent?.let {
            if (it.length < 6) {
                showToast("最少输入一句话反馈")
                return false
            }
        }

        mMTrimEtWx = feedback_et_wx.text.toString()
        if (!TextUtils.isEmpty(mMTrimEtWx)) {
            mTrimEtQq?.let {
                if (it.length < 2) {
                    showToast("微信号格式错误")
                    return false
                }
            }

        }
        return true
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.feedback_tv_cope -> {
                val trimQqNum = feedback_tv_qq_num.text.toString().trim { it <= ' ' }
                val myClipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val myClip = ClipData.newPlainText("text", trimQqNum)
                myClipboard.primaryClip = myClip
                showToast("复制客服QQ号成功")
            }
            R.id.feedback_tv_next -> if (checkInput()) {
                Log.d("mylog", "onClick: mTrimEtQq qq号$mTrimEtQq mTrimEtWorkContent 输入内容$mTrimEtWorkContent")
                netNext()
            }
        }
    }

    private fun netNext() {


        (mPresenter as? MinePresenter)?.addSuggestion(mTrimEtWorkContent, mTrimEtQq, mMTrimEtWx)
    }

    override fun showSuggestionSuccess() {
        finish()
    }

    override fun offerActivityTitle(): String {
        return "意见反馈"
    }
}

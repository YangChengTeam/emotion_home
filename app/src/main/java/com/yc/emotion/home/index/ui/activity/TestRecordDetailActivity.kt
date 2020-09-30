package com.yc.emotion.home.index.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSlidingActivity
import com.yc.emotion.home.index.presenter.EmotionTestPresenter
import com.yc.emotion.home.index.view.EmotionTestView
import com.yc.emotion.home.model.bean.EmotionTestInfo
import com.yc.emotion.home.utils.clickWithTrigger
import kotlinx.android.synthetic.main.activity_emotion_test_result.*

/**
 *
 * Created by suns  on 2019/10/14 17:31.
 */
class TestRecordDetailActivity : BaseSlidingActivity(), EmotionTestView {


    companion object {
        fun startActivity(context: Context, record_id: String?) {

            val intent = Intent(context, TestRecordDetailActivity::class.java)
            intent.putExtra("record_id", record_id)

            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        invadeStatusBar() //侵入状态栏
        initViews()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_emotion_test_result
    }

    override fun initViews() {
        mPresenter = EmotionTestPresenter(this, this)

        initView()
    }


    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    private fun initView() {


        val recordId = intent?.getStringExtra("record_id")

        getRecordDetail(recordId)
        tv_test_again.visibility = View.GONE

        showToWxServiceDialog(listener = object : OnWxListener {
            override fun onWx(wx: String) {
                tv_test_wx.text = "如果您需要情感方面的帮助，请添加导师微信$wx,导师将会对您一对一为您贴心服务"
            }
        })
        initListener()
    }

    private fun initListener() {
        tv_test_again.clickWithTrigger { finish() }
        tv_test_praise.clickWithTrigger {
            tv_test_praise.visibility = View.GONE
            tv_test_praise_count.visibility = View.VISIBLE
        }

        activity_base_same_iv_back.clickWithTrigger { finish() }

        tv_copy_wx.clickWithTrigger { showToWxServiceDialog() }

    }

    private fun getRecordDetail(record_id: String?) {

        (mPresenter as? EmotionTestPresenter)?.getTestRecordDetail(record_id)

    }

    override fun showTestRecordDetail(data: EmotionTestInfo?) {
        data?.let {

            tv_test_result.text = data.answer
            if (!TextUtils.isEmpty(data.title))
                tv_test_title.text = data.title
        }
    }


}
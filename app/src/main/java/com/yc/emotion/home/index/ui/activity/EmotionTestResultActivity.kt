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
import com.yc.emotion.home.model.bean.QuestionInfo
import kotlinx.android.synthetic.main.activity_emotion_test_result.*

/**
 *
 * Created by suns  on 2019/10/14 17:31.
 */
class EmotionTestResultActivity : BaseSlidingActivity(), EmotionTestView {


    companion object {
        fun startActivity(context: Context, test_id: String?, answers: ArrayList<QuestionInfo>?, aid: Int?, option_id: String?) {

            val intent = Intent(context, EmotionTestResultActivity::class.java)
            intent.putExtra("test_id", test_id)
            intent.putExtra("answer", answers)
            intent.putExtra("aid", aid)
            intent.putExtra("option_id", option_id)
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_emotion_test_result
    }

    override fun initViews() {
        mPresenter = EmotionTestPresenter(this, this)
        invadeStatusBar() //侵入状态栏
        initView()
    }


    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    private fun initView() {
        val test_id = intent?.getStringExtra("test_id")
        val questionInfos = intent?.getParcelableArrayListExtra<QuestionInfo>("answer")
        val aid = intent?.getIntExtra("aid", 0)
        val option_id = intent?.getStringExtra("option_id")
        submitAnswer(test_id, questionInfos, aid = "$aid", option_id = option_id)
        showToWxServiceDialog(listener = object : OnWxListener {
            override fun onWx(wx: String) {
                tv_test_wx.text = "如果您需要情感方面的帮助，请添加导师微信$wx,导师将会对您一对一为您贴心服务"
            }

        })
        initListener()
    }

    private fun initListener() {
        tv_test_again.setOnClickListener { finish() }
        tv_test_praise.setOnClickListener {
            tv_test_praise.visibility = View.GONE
            tv_test_praise_count.visibility = View.VISIBLE
        }

        activity_base_same_iv_back.setOnClickListener { finish() }

        tv_copy_wx.setOnClickListener { showToWxServiceDialog() }

    }

    private fun submitAnswer(test_id: String?, answrs: List<QuestionInfo>?, aid: String?, option_id: String?) {

        (mPresenter as? EmotionTestPresenter)?.submitAnswer(test_id, answrs, aid, option_id)
    }

    override fun showEmotionTestResult(emotionTestInfo: EmotionTestInfo?) {
        emotionTestInfo?.let {
            tv_test_result.text = emotionTestInfo.answer
            if (!TextUtils.isEmpty(emotionTestInfo.title))
                tv_test_title.text = emotionTestInfo.title
        }

    }

}
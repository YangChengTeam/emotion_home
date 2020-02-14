package com.yc.emotion.home.index.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.Log
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.index.adapter.EmotionTestDetailAdapter
import com.yc.emotion.home.mine.ui.fragment.ExitPublishFragment
import com.yc.emotion.home.model.bean.QuestionInfo
import com.yc.emotion.home.model.bean.event.EventBusEmotionTest
import kotlinx.android.synthetic.main.activity_emotion_test_detail.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 *
 * Created by suns  on 2019/10/11 14:21.
 * 情感测试正式测试类
 */
class EmotionTestDetailActivity : BaseSameActivity() {


    private var pos = 0

    private var total = 0//总页码
    private var finish_total = mutableSetOf<String>()//存储完成题目id //记录进度

    private val answers = mutableMapOf<String, Int>()//存储题目和答案

    private var testId: String? = null

    private var mTimes = 0

    private var type = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_emotion_test_detail
    }

    override fun initViews() {
        val questionInfoList = intent?.getParcelableArrayListExtra<QuestionInfo>("question_list")
        testId = intent?.getStringExtra("test_id")
        type = intent.getIntExtra("type", 2)


        mBaseSameTvSub.setTextColor(ContextCompat.getColor(this, R.color.app_color))

        ivBack.setOnClickListener { exitTest() }

        setData(questionInfoList)
    }

    private fun setData(questionInfoList: List<QuestionInfo>?) {
        questionInfoList?.let {
            total = questionInfoList.size
            setTextProgress()
            val emotionTestDetailAdapter = EmotionTestDetailAdapter(this, questionInfoList)
            viewPager_emotion.adapter = emotionTestDetailAdapter
//        viewPager_emotion.setPageTransformer()
            viewPager_emotion.offscreenPageLimit = questionInfoList.size - 1

            viewPager_emotion.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    pos = position
                }
            })
        }

    }


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getEventMotionTest(e: EventBusEmotionTest) {

        val testTopicInfo = e.emotionTestTopicInfo
        val id = testTopicInfo.question_id

        finish_total.add(id)

        setTextProgress()
        answers[id] = testTopicInfo.option_id
//        if (testTopicInfo.re_qid)


//        Log.e("TAG", "RE: " + testTopicInfo.re_qid)
        if (2 == type)
            viewPager_emotion.currentItem = pos + 1


        if (finish_total.size == total) {
            if (2 == type) {
                val questionAnswers = mutableListOf<QuestionInfo>()
                answers.forEach {
                    val quesionInfo = QuestionInfo()
                    quesionInfo.question_id = it.key
                    quesionInfo.option_id = it.value
                    questionAnswers.add(quesionInfo)
                }
                EmotionTestResultActivity.startActivity(this, testId, questionAnswers as ArrayList<QuestionInfo>, null, null)
            } else if (3 == type) {
                EmotionTestResultActivity.startActivity(this, testId, null, null, "${testTopicInfo.option_id}")
            }

            finish()
        }
    }


    @SuppressLint("SetTextI18n")
    fun setTextProgress() {

        progressBar.progress = finish_total.size * (100 / total)
        tv_emotion_test_progress.text = "${finish_total.size} /$total"


    }

    override fun offerActivityTitle(): String {
        return "情感测试"
    }

    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    override fun onResume() {
        super.onResume()
        startTime()
    }


    override fun onPause() {
        super.onPause()
        stopTime()
    }


    private var subscription: Subscription? = null

    @SuppressLint("SetTextI18n")
    fun startTime() {
        subscription = Observable.interval(1, TimeUnit.SECONDS).timeInterval().observeOn(AndroidSchedulers
                .mainThread())
                .subscribe {
                    mTimes++
                    val minutes = mTimes / 60
                    if (minutes > 60) {
                        if (subscription?.isUnsubscribed != true) {
                            subscription?.unsubscribe()
                        }
                    } else {
                        val seconds = mTimes - minutes * 60
                        mBaseSameTvSub.text = "${timeShortFormat(minutes)}:${timeShortFormat(seconds)}"
                    }
                }
    }

    private fun stopTime() {
        if (subscription != null && subscription?.isUnsubscribed == true) {
            subscription?.unsubscribe()
        }
    }

    private fun timeShortFormat(time: Int) = if (time >= 10) "$time" else "0$time"


    private fun exitTest() {
        val exitFragment = ExitPublishFragment.newInstance("确定退出测试吗?")

        exitFragment.show(supportFragmentManager, "")
        exitFragment.setOnConfirmListener { finish() }
    }

    override fun onBackPressed() {
        exitTest()
    }

}
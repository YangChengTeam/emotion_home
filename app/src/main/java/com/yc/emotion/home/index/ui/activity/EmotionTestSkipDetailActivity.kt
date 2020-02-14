package com.yc.emotion.home.index.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.index.adapter.EmotionTestDetailAnswerAdapter
import com.yc.emotion.home.mine.ui.fragment.ExitPublishFragment
import com.yc.emotion.home.model.bean.QuestionInfo
import com.yc.emotion.home.model.bean.event.EventBusEmotionTest
import kotlinx.android.synthetic.main.fragment_efficient_course.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 *
 * Created by suns  on 2019/10/11 14:21.
 * 情感测试正式测试类
 */
class EmotionTestSkipDetailActivity : BaseSameActivity() {



    private var finish_total = mutableSetOf<String>()//存储完成题目id //记录进度

    private var testId: String? = null

    private var mTimes = 0

    private var emotionTestDetailAdapter: EmotionTestDetailAnswerAdapter? = null
    private var questionInfoList: List<QuestionInfo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_efficient_course
    }

    override fun initViews() {
        questionInfoList = intent?.getParcelableArrayListExtra("question_list")
        testId = intent?.getStringExtra("test_id")


        mBaseSameTvSub.setTextColor(ContextCompat.getColor(this, R.color.app_color))

        rv_efficient_course.layoutManager = LinearLayoutManager(this)
        emotionTestDetailAdapter = EmotionTestDetailAnswerAdapter(null)

        rv_efficient_course.adapter = emotionTestDetailAdapter


        initListener()
        setData(0)
    }

    private fun setData(pos: Int) {

        questionInfoList?.let {
            val questionInfo = questionInfoList?.get(pos)

            val newTestTopicInfos = ArrayList<QuestionInfo?>()


            questionInfo?.type = QuestionInfo.ITEM_TYPE_TOPIC
            newTestTopicInfos.add(questionInfo)

            val options = questionInfo?.options

            if (options != null && options.size > 0) {
                for (option in options) {
                    option.question_id = questionInfo.question_id
                    option.type = QuestionInfo.ITEM_TYPE_ANSWER
                    newTestTopicInfos.add(option)
                }
            }
            emotionTestDetailAdapter?.resetState()
            emotionTestDetailAdapter?.setNewData(newTestTopicInfos)

        }

    }


    fun initListener() {
        emotionTestDetailAdapter?.setOnItemClickListener { adapter, view1, position ->

            val item = emotionTestDetailAdapter?.getItem(position)
            if (item != null) {
                if (item.type == QuestionInfo.ITEM_TYPE_ANSWER) {
                    val itemView = emotionTestDetailAdapter?.getItemView(position)
                    if (itemView != null)
                        itemView.isSelected = true
                    EventBus.getDefault().post(EventBusEmotionTest(item))

                }
            }
        }

        ivBack.setOnClickListener { exitTest() }
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

        if (testTopicInfo.re_qid == 0) {
            EmotionTestResultActivity.startActivity(this, testId, null, testTopicInfo?.aid, null)

            finish()

            return
        }



        setData(testTopicInfo.re_qid - 1)



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
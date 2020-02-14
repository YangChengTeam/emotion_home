package com.yc.emotion.home.index.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.index.presenter.EmotionTestPresenter
import com.yc.emotion.home.index.view.EmotionTestView
import com.yc.emotion.home.model.bean.EmotionTestInfo
import com.yc.emotion.home.model.bean.EmotionTestTopicInfo
import com.yc.emotion.home.model.bean.QuestionInfo
import com.yc.emotion.home.utils.StatusBarUtil
import com.yc.emotion.home.utils.UserInfoHelper
import kotlinx.android.synthetic.main.activity_emotion_test_desc.*
import rx.Subscriber

/**
 *
 * Created by suns  on 2019/10/11 11:29.
 */
class EmotionTestDescActivity : BaseSameActivity(), EmotionTestView {


    companion object {

        fun startActivity(context: Context, test_id: String?) {
            val intent = Intent(context, EmotionTestDescActivity::class.java)
            intent.putExtra("test_id", test_id)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_emotion_test_desc
    }


    override fun initViews() {

        mPresenter = EmotionTestPresenter(this, this)

        val test_id = intent?.getStringExtra("test_id")
        getData(test_id)

        initListener()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val layoutParams = tv_emotion_test_btn.layoutParams as RelativeLayout.LayoutParams

        var bottom = 0

        if (StatusBarUtil.isNavigationBarExist(this)) {
            bottom = StatusBarUtil.getNavigationBarHeight(this)
        }

        layoutParams.bottomMargin = bottom
        tv_emotion_test_btn.layoutParams = layoutParams

    }

    fun initListener() {
        tv_emotion_test_btn.setOnClickListener {

            if (!UserInfoHelper.instance.goToLogin(this)) {

                emotionTestInfo?.let {

                    var dIntent: Intent? = null

                    when {
                        emotionTestInfo?.type == 1 -> dIntent = Intent(this, EmotionTestSkipDetailActivity::class.java)
                        emotionTestInfo?.type == 2 -> {
                            dIntent = Intent(this, EmotionTestDetailActivity::class.java)
                            dIntent.putExtra("type", 2)
                        }
                        emotionTestInfo?.type == 3 -> {
                            dIntent = Intent(this, EmotionTestDetailActivity::class.java)
                            dIntent.putExtra("type", 3)
                        }
                    }

                    dIntent?.putExtra("question_list", questionList)
                    dIntent?.putExtra("test_id", emotionTestInfo?.id)

                    startActivity(dIntent)
                }

            }
        }
    }

    private var questionList: ArrayList<QuestionInfo>? = null

    private var emotionTestInfo: EmotionTestInfo? = null
    private fun getData(test_id: String?) {

        (mPresenter as? EmotionTestPresenter)?.getTestDetailInfo(test_id)
    }

    override fun showEmotionTestInfo(emotionTestTopicInfo: EmotionTestTopicInfo?) {
        emotionTestTopicInfo?.let {

            questionList = emotionTestTopicInfo.question_list
            emotionTestInfo = emotionTestTopicInfo.test_info
            initDescData(emotionTestTopicInfo.test_info, emotionTestTopicInfo.question_list.size)
        }
    }

    private fun initDescData(test_info: EmotionTestInfo?, size: Int) {
        test_info?.let {
            Glide.with(this).load(test_info.img).into(iv_emotion_desc)
            tv_emotion_desc_title.text = test_info.title
            tv_emotion_desc_content.text = test_info.desp
            tv_test_count.text = "${size}题"
        }
    }


    override fun offerActivityTitle(): String {
        return "情感测试"
    }
}
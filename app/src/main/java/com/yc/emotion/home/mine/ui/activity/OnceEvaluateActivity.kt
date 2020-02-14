package com.yc.emotion.home.mine.ui.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import kotlinx.android.synthetic.main.activity_publish_evaluate.*

/**
 *
 * Created by suns  on 2019/10/23 14:22.
 * 再次评价
 */
class OnceEvaluateActivity : BaseSameActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_once_evaluate
    }


    override fun initViews() {
        mBaseSameTvSub.text = "发布"

        initListener()
    }

    private fun initListener() {
        et_evaluate_content.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val result = s.toString().trim()

                Log.e("TAG", "${result.length}")
                tv_evaluate_content_count.text = "${result.length}/200"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

    }


    override fun offerActivityTitle(): String {
        return "追加评价"
    }
}
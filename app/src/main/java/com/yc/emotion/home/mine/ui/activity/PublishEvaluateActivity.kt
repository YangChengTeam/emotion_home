package com.yc.emotion.home.mine.ui.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ImageView
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import kotlinx.android.synthetic.main.activity_publish_evaluate.*

/**
 *
 * Created by suns  on 2019/10/23 14:22.
 * 发布评价
 */
class PublishEvaluateActivity : BaseSameActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_publish_evaluate
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

        setStar(iv_star_1)
        setStar(iv_star_2)
        setStar(iv_star_3)
        setStar(iv_star_4)
        setStar(iv_star_5)

    }


    private fun setStar(imageView: ImageView) {
        imageView.setOnClickListener {
            var tag = imageView.tag

            tag = if (tag == "0") "1"
            else "0"
            imageView.tag = tag
            if (tag == "1") imageView.setImageResource(R.mipmap.comment_star_sel)
            else imageView.setImageResource(R.mipmap.comment_star_default)
        }


    }

    override fun offerActivityTitle(): String {
        return "发表评价"
    }
}
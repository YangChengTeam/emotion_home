package com.yc.emotion.home.index.ui.activity

import android.content.Intent
import android.os.Bundle

import android.view.View
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.base.ui.activity.MainActivity
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.utils.Preference
import com.yc.emotion.home.utils.clickWithTrigger
import kotlinx.android.synthetic.main.activity_select_sex.*

/**
 *
 * Created by suns  on 2019/10/26 09:49.
 */
class SelectSexActivity : BaseSameActivity() {



    private var sex by Preference(ConstantKey.SEX, 0)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        initViews()
    }


    override fun getLayoutId(): Int {
       return R.layout.activity_select_sex
    }

    override fun initViews() {
        ivBack.visibility = View.GONE
        mBaseSameTvSub.text = "跳过"
        initListener()

    }


    private fun initListener() {

        mBaseSameTvSub.clickWithTrigger { switchMain() }
        ll_sex_female.clickWithTrigger {
            ll_sex_female.isSelected = true
            ll_male.isSelected = false
            iv_female.setImageResource(R.mipmap.girl_sel)
            iv_male.setImageResource(R.mipmap.boy_default)
            sex = 2
            mHandler?.postDelayed({ switchMain() }, 500)

        }
        ll_male.clickWithTrigger {
            ll_sex_female.isSelected = false
            ll_male.isSelected = true
            iv_female.setImageResource(R.mipmap.girl_default)
            iv_male.setImageResource(R.mipmap.boy_sel)
            sex = 1
            mHandler?.postDelayed({ switchMain() }, 500)
        }
    }


    private fun switchMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    override fun offerActivityTitle(): String {
        return ""
    }
}
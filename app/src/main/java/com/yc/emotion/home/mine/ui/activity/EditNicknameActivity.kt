package com.yc.emotion.home.mine.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.utils.ToastUtils
import com.yc.emotion.home.utils.clickWithTrigger
import kotlinx.android.synthetic.main.activity_edit_signature.*

/**
 *
 * Created by suns  on 2019/10/18 09:39.
 */
class EditNicknameActivity : BaseSameActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_edit_signature
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }

    override fun initViews() {
        et_signature.hint = "给自己取一个好听的签名吧~"

        intent?.let {
            val signature = intent.getStringExtra("nickname")
            if (!TextUtils.isEmpty(signature)) {
                et_signature.setText(signature)
                et_signature.setSelection(signature.length)
            }
        }


        val tvSub = offerActivitySubTitleView()
        tvSub.setTextColor(resources.getColor(R.color.gray_666))
        tvSub.text = "保存"

        tvSub.clickWithTrigger {
            val result = et_signature.text.toString().trim()
            if (TextUtils.isEmpty(result)) {
                ToastUtils.showCenterToast("请填写昵称再保存")
                return@clickWithTrigger

            }

            val intent = Intent()
            intent.putExtra("result", result)
            setResult(4, intent)

            finish()

        }
    }

    override fun offerActivityTitle(): String {
        return "个人昵称"
    }
}
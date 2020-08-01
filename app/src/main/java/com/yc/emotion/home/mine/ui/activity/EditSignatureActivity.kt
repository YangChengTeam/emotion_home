package com.yc.emotion.home.mine.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_edit_signature.*

/**
 *
 * Created by suns  on 2019/10/18 09:39.
 */
class EditSignatureActivity : BaseSameActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_edit_signature
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }

    override fun initViews() {
        intent?.let {
            val signature = intent.getStringExtra("signature")
            if (!TextUtils.isEmpty(signature)) {
                et_signature.setText(signature)
                et_signature.setSelection(signature.length)
            }
        }

        val tvSub = offerActivitySubTitleView()
        tvSub.setTextColor(resources.getColor(R.color.gray_666))
        tvSub.text = "保存"

        tvSub.setOnClickListener {
            val result = et_signature.text.toString().trim()
            if (TextUtils.isEmpty(result)) {
                ToastUtils.showCenterToast("请填写个性签名再保存")
                return@setOnClickListener

            }

            val intent = Intent()
            intent.putExtra("result", result)
            setResult(3, intent)

            finish()

        }
    }

    override fun offerActivityTitle(): String {
        return "个性签名"
    }
}
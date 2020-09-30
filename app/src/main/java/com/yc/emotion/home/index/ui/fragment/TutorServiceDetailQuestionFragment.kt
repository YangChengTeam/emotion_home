package com.yc.emotion.home.index.ui.fragment

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.yc.emotion.home.R
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.utils.UIUtils
import com.yc.emotion.home.utils.clickWithTrigger
import kotlinx.android.synthetic.main.fragment_tutor_service_detail_question.*

/**
 *
 * Created by suns  on 2019/10/14 10:06.
 */
class TutorServiceDetailQuestionFragment : BaseFragment<BasePresenter<IModel, IView>>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_tutor_service_detail_question
    }

    override fun initViews() {

        initListener()
    }

    fun initListener() {

        clickView(rl_service_1, iv_service_1, tv_service_1)
        clickView(rl_service_2, iv_service_2, tv_service_2)
        clickView(rl_service_3, iv_service_3, tv_service_3)
        clickView(rl_service_4, iv_service_4, tv_service_4)
        clickView(rl_service_5, iv_service_5, tv_service_5)
        clickView(rl_service_6, iv_service_6, tv_service_6)
        clickView(rl_service_7, iv_service_7, tv_service_7)
        clickView(rl_service_8, iv_service_8, tv_service_8)
        clickView(rl_service_9, iv_service_9, tv_service_9)
        clickView(rl_service_10, iv_service_10, tv_service_10)
        clickView(rl_service_11, iv_service_11, tv_service_11)
        clickView(rl_service_12, iv_service_12, tv_service_12)
        clickView(rl_service_13, iv_service_13, tv_service_13)

        tv_tutor_course_intro.text = String.format(getString(R.string.buy_course_intro), UIUtils.getAppName(activity))
    }


    private fun clickView(view: View, imageView: ImageView, textView: TextView) {


        view.clickWithTrigger {

            var isExtend = view.tag
            isExtend = if (isExtend == "0") "1" else "0"
            view.tag = isExtend


            if (isExtend == "1") {
                textView.visibility = View.VISIBLE
                imageView.setImageResource(R.mipmap.community_icon_arrow_down)
            } else {
                textView.visibility = View.GONE
                imageView.setImageResource(R.mipmap.icon_arrow_right)
            }
        }

    }

    override fun lazyLoad() {

    }
}
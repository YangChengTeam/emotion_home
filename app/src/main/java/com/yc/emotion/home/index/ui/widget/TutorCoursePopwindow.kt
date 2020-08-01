package com.yc.emotion.home.index.ui.widget

import android.app.Activity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kk.utils.ScreenUtil
import com.yc.emotion.home.R
import com.yc.emotion.home.base.domain.engine.LoveEngine
import com.yc.emotion.home.base.ui.popwindow.BasePopWindow
import com.yc.emotion.home.index.adapter.TutorCoursePpAdapter
import com.yc.emotion.home.model.bean.LessonInfo
import com.yc.emotion.home.utils.UserInfoHelper.Companion.instance

/**
 * Created by suns  on 2019/10/8 17:12.
 * 导师课时popwindow
 */
class TutorCoursePopwindow(context: Activity?) : BasePopWindow(context) {
    private var tutorCoursePpAdapter: TutorCoursePpAdapter? = null
    private var mLoveEngine: LoveEngine? = null
    private var popupHeight = 0
    private var popupWidth = 0
    private lateinit var ivCourseClose: ImageView
    private lateinit var tvPPtotal: TextView

    override fun getLayoutId(): Int {
        return R.layout.tutor_course_popwindow
    }

    override fun init() {
        mLoveEngine = LoveEngine(mContext)
        val recyclerView: RecyclerView = rootView.findViewById(R.id.rcv_course_pp)
        tvPPtotal = rootView.findViewById(R.id.tv_course_pp_total)
        ivCourseClose = rootView.findViewById(R.id.iv_course_pp_close)
        val linearLayoutManager = LinearLayoutManager(mContext)
        recyclerView.layoutManager = linearLayoutManager
        tutorCoursePpAdapter = TutorCoursePpAdapter(null)
        recyclerView.adapter = tutorCoursePpAdapter

        //获取自身的长宽高
        rootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        popupHeight = rootView.measuredHeight
        popupWidth = rootView.measuredWidth

//        getData();
        initListener()
    }

    fun setCourseData(lessonInfos: List<LessonInfo>) {
        tvPPtotal.text = String.format("共%d课", lessonInfos.size)
        tutorCoursePpAdapter?.setNewData(lessonInfos)
    }

    private fun initListener() {
        tutorCoursePpAdapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int ->
            val lessonInfo = tutorCoursePpAdapter?.getItem(position)
            if (lessonInfo != null) {
                onSelectListener?.let {
//                    if (position == 0 || !instance.goToLogin(mContext))
                    it.onTagSelect(lessonInfo, position)
                }

                dismiss()
            }
        }
        ivCourseClose.setOnClickListener { dismiss() }
    }

    /**
     * 设置显示在v上方(以v的左边距为开始位置)
     *
     * @param v
     */
    fun showUp(v: View) {
        //获取需要在其上方显示的控件的位置信息
        val location = IntArray(2)
        v.getLocationOnScreen(location)
        //在控件上方显示
        Log.e("TAG", popupHeight.toString() + "---" + location[1] + "--" + v.measuredHeight)
        showAtLocation(v, Gravity.NO_GRAVITY, location[0], ScreenUtil.getHeight(mContext) / 2 + 85)
    }

    private var onSelectListener: OnTagSelectListener? = null
    fun setOnTagSelectListener(onTagSelectListener: OnTagSelectListener?) {
        this.onSelectListener = onTagSelectListener
    }

    interface OnTagSelectListener {
        fun onTagSelect(lessonInfo: LessonInfo?, pos: Int)
    }
}
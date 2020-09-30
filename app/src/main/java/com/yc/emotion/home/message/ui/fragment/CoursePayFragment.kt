package com.yc.emotion.home.message.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.yc.emotion.home.R
import com.yc.emotion.home.base.domain.engine.BaseModel
import com.yc.emotion.home.base.ui.fragment.BaseBottomSheetDialogFragment
import com.yc.emotion.home.index.adapter.TutorCoursePpAdapter
import com.yc.emotion.home.model.bean.LessonInfo
import yc.com.rthttplibrary.util.ScreenUtil

/**
 *
 * Created by suns  on 2020/8/25 17:29.
 */
class CoursePayFragment : BaseBottomSheetDialogFragment() {

    private var tutorCoursePpAdapter: TutorCoursePpAdapter? = null
    private var mLoveEngine: BaseModel? = null

    private var ivCourseClose: ImageView? = null
    private var tvPPtotal: TextView? = null
    private var lessonInfos: List<LessonInfo>? = null

    override fun initView() {
        super.initView()

        arguments?.let {
            lessonInfos = it.getParcelableArrayList("lessons")
        }

        mLoveEngine = BaseModel(activity)
        val recyclerView: RecyclerView? = rootView?.findViewById(R.id.rcv_course_pp)
        val layoutParams = recyclerView?.layoutParams
        layoutParams?.height = ScreenUtil.getHeight(activity) * 2 / 3
        recyclerView?.layoutParams = layoutParams

        tvPPtotal = rootView?.findViewById(R.id.tv_course_pp_total)
        ivCourseClose = rootView?.findViewById(R.id.iv_course_pp_close)
        val linearLayoutManager = LinearLayoutManager(activity)
        recyclerView?.layoutManager = linearLayoutManager
        tutorCoursePpAdapter = TutorCoursePpAdapter(lessonInfos)
        recyclerView?.adapter = tutorCoursePpAdapter
        tvPPtotal?.text = String.format("共%d课", lessonInfos?.size)

        initListener()
    }

    fun setCourseData(lessonInfos: List<LessonInfo>?) {
        tvPPtotal?.text = String.format("共%d课", lessonInfos?.size)
        tutorCoursePpAdapter?.setNewData(lessonInfos)
    }

    private fun initListener() {
        tutorCoursePpAdapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int ->
            val lessonInfo = tutorCoursePpAdapter?.getItem(position)
            if (lessonInfo != null) {
                listener?.let {
//                    if (position == 0 || !instance.goToLogin(mContext))
                    it.onLessonClick(lessonInfo, position)
                }

                dismiss()
            }
        }
        ivCourseClose?.setOnClickListener { dismiss() }
    }

    companion object {
        fun newInstance(mLessons: ArrayList<LessonInfo>?): CoursePayFragment {
            val coursePayFragment = CoursePayFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList("lessons", mLessons)
            coursePayFragment.arguments = bundle
            return coursePayFragment
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.tutor_course_popwindow
    }


    private var listener: OnLessonItemClickListener? = null

    fun setOnLessonItemClickListener(listener: OnLessonItemClickListener) {
        this.listener = listener
    }

    interface OnLessonItemClickListener {
        fun onLessonClick(lessonInfo: LessonInfo, position: Int)
    }

}
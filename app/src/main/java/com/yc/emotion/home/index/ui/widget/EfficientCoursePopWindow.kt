package com.yc.emotion.home.index.ui.widget

import android.app.Activity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.popwindow.BasePopWindow
import com.yc.emotion.home.index.adapter.EfficientCourseTagAdapter

/**
 * Created by suns  on 2019/10/8 17:12.
 * 标签popwindow
 */
class EfficientCoursePopWindow(context: Activity) : BasePopWindow(context) {

    private var efficientCourseTagAdapter: EfficientCourseTagAdapter? = null


    private var onTagSelectListener: OnTagSelectListener? = null

    override fun getLayoutId(): Int {
        return R.layout.community_tag_popwindow
    }

    override fun init() {


        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerView_tag)

        val gridLayoutManager = GridLayoutManager(mContext, 3)

        recyclerView.layoutManager = gridLayoutManager

        efficientCourseTagAdapter = EfficientCourseTagAdapter(null)

        recyclerView.adapter = efficientCourseTagAdapter


        initListener()
    }

    fun setData(courseInfos: List<String>?) {
        efficientCourseTagAdapter?.setNewData(courseInfos)
    }

    private fun initListener() {
        efficientCourseTagAdapter?.setOnItemClickListener { adapter, view, position ->
            efficientCourseTagAdapter?.resetView()
            efficientCourseTagAdapter?.setViewState(position)
            val communityTagInfo = efficientCourseTagAdapter?.getItem(position)
            communityTagInfo?.let {
                onTagSelectListener?.onTagSelect(position)
                dismiss()
            }

        }
    }

    fun setOnTagSelectListener(onTagSelectListener: OnTagSelectListener) {
        this.onTagSelectListener = onTagSelectListener
    }

    interface OnTagSelectListener {
        fun onTagSelect(position: Int)
    }


}

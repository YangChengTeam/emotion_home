package com.yc.emotion.home.index.ui.widget

import android.app.Activity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.popwindow.BasePopwindow
import com.yc.emotion.home.index.adapter.AudioFilterAdapter
import com.yc.emotion.home.index.presenter.LoveAudioPresenter
import com.yc.emotion.home.index.view.LoveAudioView
import com.yc.emotion.home.model.bean.AudioDataInfo
import com.yc.emotion.home.model.util.SPUtils

/**
 * Created by wanglin  on 2019/7/22 08:49.
 */
class AudioFilterPopwindow(context: Activity) : BasePopwindow(context), LoveAudioView {


    private var filterRecyclerview: RecyclerView? = null
    private var filterAdapter: AudioFilterAdapter? = null



    private var clickListener: onItemClickListener? = null

    override fun getLayoutId(): Int {
        return R.layout.popwindow_audio_filter
    }

    override fun init() {
        mPresenter = LoveAudioPresenter(mContext, this)

        filterRecyclerview = rootView.findViewById(R.id.filter_recyclerView)
        filterRecyclerview?.layoutManager = GridLayoutManager(mContext, 4)

        filterAdapter = AudioFilterAdapter(null)

        filterRecyclerview?.adapter = filterAdapter

        initData()
        initListener()
    }

    private fun initData() {

        (mPresenter as? LoveAudioPresenter)?.getAudioCategoryCache()

    }

    private fun initListener() {
        filterAdapter?.setOnItemClickListener { adapter, view, position ->
            dismiss()
            if (clickListener != null) {
                clickListener?.onItemClick(filterAdapter?.getItem(position))
            }

            SPUtils.put(mContext, SPUtils.FILTER_POS, position)
        }
    }

    fun setOnItemClickListener(clickListener: onItemClickListener) {
        this.clickListener = clickListener
    }

    interface onItemClickListener {
        fun onItemClick(audioDataInfo: AudioDataInfo?)
    }


    override fun showAudioCategoryList(list: List<AudioDataInfo>) {
        filterAdapter?.setNewData(list)
    }

    override fun initViews() {

    }

    override fun showLoadingDialog() {

    }

    override fun hideLoadingDialog() {

    }

}

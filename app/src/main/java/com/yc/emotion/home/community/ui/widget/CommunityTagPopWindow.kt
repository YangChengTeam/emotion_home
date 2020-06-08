package com.yc.emotion.home.community.ui.widget

import android.app.Activity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.popwindow.BasePopWindow
import com.yc.emotion.home.community.adapter.PublishTagAdapter
import com.yc.emotion.home.community.presenter.CommunityPresenter
import com.yc.emotion.home.community.view.CommunityView
import com.yc.emotion.home.model.bean.CommunityInfo
import com.yc.emotion.home.model.bean.CommunityTagInfo
import com.yc.emotion.home.model.bean.event.EventCommunityTag
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.utils.Preference
import org.greenrobot.eventbus.EventBus

/**
 * Created by suns  on 2019/10/8 17:12.
 * 标签popwindow
 */
class CommunityTagPopWindow(context: Activity?) : BasePopWindow(context), CommunityView {
    override fun shoCommunityNewestCacheInfos(datas: List<CommunityInfo>?) {

    }


    private var publishTagAdapter: PublishTagAdapter? = null


    private var tagSelectListener: onTagSelectListener? = null

    private var pos by Preference(ConstantKey.TAG_POSTION, 0)

    override fun getLayoutId(): Int {
        return R.layout.community_tag_popwindow
    }

    override fun init() {

        mPresenter = CommunityPresenter(mContext, this)



        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerView_tag)

        val gridLayoutManager = GridLayoutManager(mContext, 3)

        recyclerView.layoutManager = gridLayoutManager

        publishTagAdapter = PublishTagAdapter(null)

        recyclerView.adapter = publishTagAdapter

        initListener()
    }

    private fun initListener() {
        publishTagAdapter?.setOnItemClickListener { adapter, view, position ->
            publishTagAdapter?.resetView()
            publishTagAdapter?.setViewState(position)
            val communityTagInfo = publishTagAdapter?.getItem(position)
            if (communityTagInfo != null) {
                pos = position
                tagSelectListener?.onTagSelect(communityTagInfo)
                EventBus.getDefault().post(EventCommunityTag(communityTagInfo))
                dismiss()
            }

        }
    }


    private fun getData() {
        (mPresenter as? CommunityPresenter)?.getCommunityTagInfos()
    }

    fun createNewData(tagInfos: List<CommunityTagInfo>?) {

        if (tagInfos != null) {
            publishTagAdapter?.setNewData(tagInfos)
        } else {
            getData()
        }
    }


    override fun showCommunityTagInfos(list: List<CommunityTagInfo>) {
        publishTagAdapter?.setNewData(list)
    }

    fun setOnTagSelectListener(onTagSelectListener: CommunityTagPopWindow.onTagSelectListener) {
        this.tagSelectListener = onTagSelectListener
    }


    interface onTagSelectListener {
        fun onTagSelect(communityTagInfo: CommunityTagInfo)
    }

    override fun initViews() {

    }

    override fun showLoadingDialog() {

    }

    override fun hideLoadingDialog() {

    }


}

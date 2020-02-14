package com.yc.emotion.home.index.ui.fragment

import android.support.v4.content.ContextCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.BaseLazyFragment
import com.yc.emotion.home.index.adapter.IndexVerbalAdapter
import com.yc.emotion.home.index.presenter.IndexVerbalPresenter
import com.yc.emotion.home.index.ui.activity.LoveHealDetailsActivity
import com.yc.emotion.home.index.view.IndexVerbalView
import com.yc.emotion.home.model.bean.LoveHealDateBean
import kotlinx.android.synthetic.main.fragment_collect_view.*

/**
 *
 * Created by suns  on 2019/9/19 16:17.
 */
class IndexVerbalFragment : BaseLazyFragment<IndexVerbalPresenter>(), IndexVerbalView {


    private var indexVerbalAdapter: IndexVerbalAdapter? = null




    companion object {
        fun newInstance(sence: String): IndexVerbalFragment {
            val fragment = IndexVerbalFragment()
            fragment.setSence(sence)
            return fragment
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_collect_view
    }


    override fun initViews() {
        mPresenter = IndexVerbalPresenter(activity, this)

        fragment_collect_love_healing_rv.layoutManager = GridLayoutManager(activity, 3)
        indexVerbalAdapter = IndexVerbalAdapter(null)
        fragment_collect_love_healing_rv.itemAnimator = DefaultItemAnimator()
        fragment_collect_love_healing_rv.adapter = indexVerbalAdapter

        initListener()
    }

    override fun lazyLoad() {
        netDialogueData(mSence)
    }

    private var mSence: String = ""

    fun setSence(sence: String) {
        this.mSence = sence
    }


    fun initListener() {
        activity?.let {
            swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(activity!!, R.color.app_color))
            swipeRefreshLayout.setOnRefreshListener {
                netDialogueData(mSence)
            }
        }


        indexVerbalAdapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            val item = indexVerbalAdapter?.getItem(position)
            item?.let {
                if (LoveHealDateBean.ITEM_CONTENT == item.type) {
                    activity?.let {
                        LoveHealDetailsActivity.startLoveHealDetailsActivity(activity!!, item.name, "${item.id}")
                    }
                }
            }
        }
    }

    private fun netDialogueData(sence: String) {

        mPresenter?.getCacheData(sence)

    }


    private fun createNewData(loveHealDateBeans: List<LoveHealDateBean>?) {

        indexVerbalAdapter?.setNewData(loveHealDateBeans)

    }

    override fun showVerbalSenceInfo(data: List<LoveHealDateBean>) {
        createNewData(data)
    }

    override fun onComplete() {
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }


    override fun showLoadingDialog() {
        activity?.let {
            (activity as BaseActivity).showLoadingDialog()
        }
    }

    override fun hideLoadingDialog() {
        activity?.let {
            (activity as BaseActivity).hideLoadingDialog()
        }
    }
}
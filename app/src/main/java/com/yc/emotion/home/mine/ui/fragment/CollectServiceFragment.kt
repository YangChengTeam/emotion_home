package com.yc.emotion.home.mine.ui.fragment

import android.content.Intent
import android.os.Handler
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.fragment.BaseCollectFragment
import com.yc.emotion.home.base.ui.widget.LoadDialog
import com.yc.emotion.home.index.adapter.TutorServiceListAdapter
import com.yc.emotion.home.index.ui.activity.TutorServiceDetailActivity
import com.yc.emotion.home.model.bean.TutorServiceInfo
import kotlinx.android.synthetic.main.fragment_collect_view.*


/**
 *
 * Created by suns  on 2019/10/17 15:32.
 */
class CollectServiceFragment : BaseCollectFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_collect_view
    }

    private var loadDialog: LoadDialog? = null
    private var tutorServiceListAdapter: TutorServiceListAdapter? = null
    private var mHandler: Handler? = null


    override fun initViews() {
        loadDialog = LoadDialog(activity)
        mHandler = Handler()
        fragment_collect_love_healing_rv.layoutManager = LinearLayoutManager(activity)

        tutorServiceListAdapter = TutorServiceListAdapter(null)

        fragment_collect_love_healing_rv.adapter = tutorServiceListAdapter

        initListener()
    }

    fun initListener() {


        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mCollectActivity, R.color.app_color))
        swipeRefreshLayout.setOnRefreshListener {
            getData()
        }


        tutorServiceListAdapter?.setOnItemClickListener { adapter, view, position ->
            startActivity(Intent(activity, TutorServiceDetailActivity::class.java))
        }

    }


    private fun getData() {
        loadDialog?.showLoadingDialog()
        mHandler?.postDelayed({
            val datas = arrayListOf<TutorServiceInfo>()
            for (i in 0..5) {
                datas.add(TutorServiceInfo())
            }
            tutorServiceListAdapter?.setNewData(datas)


            if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
            loadDialog?.dismissLoadingDialog()
        }, 500)

    }

    override fun lazyLoad() {
        getData()
    }
}
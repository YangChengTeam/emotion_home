package com.yc.emotion.home.index.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.index.adapter.TutorServiceCommentDetailAdapter
import com.yc.emotion.home.model.bean.CommunityInfo
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.mine.ui.activity.OnceEvaluateActivity
import com.yc.emotion.home.base.ui.widget.LoadDialog
import kotlinx.android.synthetic.main.fragment_efficient_course.*

/**
 *
 * Created by suns  on 2019/10/14 16:03.
 */
class TutorServiceCommentDetailActivity : BaseSameActivity() {


    private var loadDialog: LoadDialog? = null
    private var tutorServiceCommentDetailAdapter: TutorServiceCommentDetailAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_efficient_course
    }

    override fun initViews() {

        rv_efficient_course.setPadding(0, 0, 0, 0)
        loadDialog = LoadDialog(this)

        rv_efficient_course.layoutManager = LinearLayoutManager(this)

        tutorServiceCommentDetailAdapter = TutorServiceCommentDetailAdapter(null)

        rv_efficient_course.adapter = tutorServiceCommentDetailAdapter
        getData()
        initListener()
    }

    fun initListener() {


        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.app_color))
        swipeRefreshLayout.setOnRefreshListener {
            getData()
        }

        tutorServiceCommentDetailAdapter?.setOnItemClickListener { adapter, view, position ->
            //            startActivity(Intent(this, TutorServiceDetailActivity::class.java))
        }

        tutorServiceCommentDetailAdapter?.setOnItemChildClickListener { adapter, view, position ->
            val communityInfo = tutorServiceCommentDetailAdapter?.getItem(position)

            communityInfo?.let {
                if (view.id == R.id.tv_once_again_comment)
                    startActivity(Intent(this, OnceEvaluateActivity::class.java))
            }
        }
    }


    private fun getData() {
        loadDialog?.showLoadingDialog()
        mHandler?.postDelayed({
            val datas = arrayListOf<CommunityInfo>()
            for (i in 0..5) {
                datas.add(CommunityInfo())
            }
            tutorServiceCommentDetailAdapter?.setNewData(datas)


            if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
            loadDialog?.dismissLoadingDialog()
        }, 500)

    }


    override fun offerActivityTitle(): String {
        return "全部评价"
    }

}
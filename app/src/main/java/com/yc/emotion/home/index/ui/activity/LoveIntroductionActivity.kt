package com.yc.emotion.home.index.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.index.adapter.LoveIntroduceAdapter
import com.yc.emotion.home.index.presenter.SkillPresenter
import com.yc.emotion.home.index.view.SkillView
import com.yc.emotion.home.model.bean.ArticleDetailInfo
import kotlinx.android.synthetic.main.activity_love_introduction.*

/**
 * 入门秘籍 提升列表
 */
class LoveIntroductionActivity : BaseSameActivity(), SkillView {


    private var mActivityTitle: String? = null

    private val PAGE_SIZE = 10
    private var PAGE_NUM = 1

    //    private var mLoveEngin: LoveEngine? = null
    private var mId: String? = null
    private var loveIntroduceAdapter: LoveIntroduceAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        initViews()

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_love_introduction
    }

    override fun initViews() {

        mPresenter = SkillPresenter(this, this)

        initRecyclerView()
        initDatas()
        initListener()
    }

    private fun initRecyclerView() {

        val layoutManager = LinearLayoutManager(this)
        love_introduction_rv.layoutManager = layoutManager
        loveIntroduceAdapter = LoveIntroduceAdapter(null)
        love_introduction_rv.adapter = loveIntroduceAdapter


    }


    private fun initDatas() {
        netData()
    }

    private fun initListener() {
        loveIntroduceAdapter?.setOnItemClickListener { adapter, view, position ->
            val exampListsBean = loveIntroduceAdapter?.getItem(position)
            if (exampListsBean != null)
                LoveCaseDetailActivity.startExampleDetailActivity(this@LoveIntroductionActivity, exampListsBean.id, exampListsBean.post_title)
        }
        loveIntroduceAdapter?.setOnLoadMoreListener({ this.netData() }, love_introduction_rv)

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.red_crimson))
        swipeRefreshLayout.setOnRefreshListener {
            PAGE_NUM = 1
            netData()
        }
    }

    private fun netData() {

        (mPresenter as? SkillPresenter)?.exampleTsList(mId, PAGE_NUM, PAGE_SIZE)

    }

    private fun createNewData(exampListsBeans: List<ArticleDetailInfo>) {
        if (PAGE_NUM == 1) {
            loveIntroduceAdapter?.setNewData(exampListsBeans)
        } else {
            loveIntroduceAdapter?.addData(exampListsBeans)
        }
        if (exampListsBeans.size == PAGE_SIZE) {
            loveIntroduceAdapter?.loadMoreComplete()
            PAGE_NUM++
        } else {
            loveIntroduceAdapter?.loadMoreEnd()
        }


    }

    override fun showSkillListInfo(lists: List<ArticleDetailInfo>) {
        createNewData(lists)
    }

    override fun onComplete() {
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }


    override fun initIntentData() {
        val intent = intent
        mActivityTitle = intent.getStringExtra("title")
        mId = intent.getStringExtra("love_id")
    }

    override fun offerActivityTitle(): String? {
        return mActivityTitle
    }

    companion object {

        fun startLoveIntroductionActivity(context: Context, title: String, tagId: String) {
            val intent = Intent(context, LoveIntroductionActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("love_id", tagId)
            context.startActivity(intent)
        }
    }
}

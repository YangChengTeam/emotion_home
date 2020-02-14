package com.yc.emotion.home.index.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager

import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.index.adapter.CreateMainT3Adapter
import com.yc.emotion.home.model.bean.CategoryArticleBean
import com.yc.emotion.home.model.bean.MainT3Bean
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.index.presenter.SkillPresenter
import com.yc.emotion.home.index.view.SkillView
import com.yc.emotion.home.message.ui.fragment.ExpressFragment
import com.yc.emotion.home.skill.ui.fragment.PracticeTeachFragment
import kotlinx.android.synthetic.main.fragment_main_t3_course.*

/**
 * Created by mayn on 2019/6/17.
 * 秘技
 */

class TipsCourseActivity : BaseSameActivity(), SkillView {


    private var mCategoryArticleBeans: List<CategoryArticleBean>? = null

    private var mDatas: List<MainT3Bean>? = null

    private val ID_ITEM_TITLE_CASE = -1
    private val ID_ITEM_TITLE_CURE = -2

    private var mainT3Adapter: CreateMainT3Adapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_main_t3_course
    }

    override fun initViews() {
        mPresenter = SkillPresenter(this, this)

        lazyLoad()
    }


    private fun lazyLoad() {
        MobclickAgent.onEvent(this, ConstantKey.UM_LOVE_SECRET_ID)
        initView()
    }

    private fun initView() {
        (mPresenter as? SkillPresenter)?.getExampleTsCache()

        initRecyclerView()
        initListener()
    }

    private fun initRecyclerView() {

        val gridLayoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        main_t3_course_rl.layoutManager = gridLayoutManager

        mainT3Adapter = CreateMainT3Adapter(mDatas)
        main_t3_course_rl.adapter = mainT3Adapter
    }


    private fun initListener() {
        mainT3Adapter?.setOnItemClickListener { adapter, view, position ->

            val item = mainT3Adapter?.getItem(position)
            if (item != null) {
                if (MainT3Bean.LOVE_HEAL_TYPE_ITEM == item.type || MainT3Bean.LOVE_HEAL_TYPE_ITEM_LOCALITY == item.type) {
                    if (position < 0 || mDatas == null || mDatas!!.isEmpty()) {
                        return@setOnItemClickListener
                    }

                    val id = item.id
                    if (id < 0) {
                        when (id) {
                            ID_ITEM_TITLE_CASE -> startActivity(Intent(this, LoveCaseActivity::class.java))
                            ID_ITEM_TITLE_CURE -> {
                            }
                        }//                                startActivity(new Intent(mMainActivity, LoveHealingActivity.class));
                    } else {
                        LoveIntroductionActivity.startLoveIntroductionActivity(this, item.name, id.toString())
                    }
                }

            }
        }
        mainT3Adapter?.setOnItemChildClickListener { adapter, view, position ->
            val item = mainT3Adapter?.getItem(position)
            if (item != null) {
                if (MainT3Bean.LOVE_HEAL_TYPE_TITLE == item.type) {
                    when (view.id) {
                        R.id.item_t3title_iv_title//表白入口
                        -> startActivity(Intent(this, ExpressFragment::class.java))
                        R.id.item_t3title_tv_icon_01 -> startLoveByStagesActivity(0, "单身期")
                        R.id.item_t3title_tv_icon_02 -> startLoveByStagesActivity(1, "追求期")
                        R.id.item_t3title_tv_icon_03 -> startLoveByStagesActivity(2, "热恋期")
                        R.id.item_t3title_tv_icon_04 -> startLoveByStagesActivity(3, "失恋期")
                        R.id.item_t3title_tv_icon_05 -> startLoveByStagesActivity(4, "婚后期")
                        R.id.iv_practice_teach ->
                            //实战学习
                            startActivity(Intent(this, PracticeTeachFragment::class.java))
                        R.id.iv_practice_love ->
                            //实战情话
                            startActivity(Intent(this, PracticeLoveActivity::class.java))
                    }
                }
            }

        }
    }


    override fun showSkillInfos(mDatas: List<MainT3Bean>) {
        this.mDatas = mDatas
        mainT3Adapter?.setNewData(mDatas)
    }

    override fun showCategoryArticleInfos(data: List<CategoryArticleBean>?) {
        mCategoryArticleBeans = data
    }

    private fun startLoveByStagesActivity(position: Int, title: String) {
        if (mCategoryArticleBeans == null || mCategoryArticleBeans!!.size < position + 1) {
            return
        }
        val categoryArticleBean = mCategoryArticleBeans?.get(position)
        val children = categoryArticleBean?.children
        LoveByStagesActivity.startLoveByStagesActivity(this, title, children)
    }


    override fun offerActivityTitle(): String {
        return "秘技"
    }
}

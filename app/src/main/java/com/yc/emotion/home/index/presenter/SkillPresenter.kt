package com.yc.emotion.home.index.presenter

import android.content.Context
import com.alibaba.fastjson.TypeReference

import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.model.SkillModel
import com.yc.emotion.home.index.view.SkillView
import com.yc.emotion.home.model.bean.*
import com.yc.emotion.home.utils.CommonInfoHelper
import com.yc.emotion.home.utils.ToastUtils
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.observers.DisposableObserver
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.config.HttpConfig

/**
 *
 * Created by suns  on 2019/11/14 11:49.
 * 秘技相关
 */
class SkillPresenter(context: Context?, view: SkillView) : BasePresenter<SkillModel, SkillView>(context, view) {

    init {
        mModel = SkillModel(context)
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {

    }

    override fun getCache() {

    }

    fun getExampleTsCache() {
        CommonInfoHelper.getO(mContext, "main3_example_ts_category", object : TypeReference<List<MainT3Bean>>() {

        }.type, object : CommonInfoHelper.OnParseListener<List<MainT3Bean>> {


            override fun onParse(o: List<MainT3Bean>?) {
                if (o != null && o.isNotEmpty()) {
                    mView.showSkillInfos(o)
                }
            }
        })
        exampleTsCategory()

        getArticleCache()
    }

    private fun exampleTsCategory() {
        mModel?.exampleTsCategory()?.getData(mView, { it, _ ->
            it?.let {
                createNewData(it)
            }
        }, { _, _ -> })


    }


    fun getArticleCache() {
        CommonInfoHelper.getO(mContext, "main1_Article_category", object : TypeReference<List<CategoryArticleBean>>() {

        }.type, object : CommonInfoHelper.OnParseListener<List<CategoryArticleBean>> {
            override fun onParse(o: List<CategoryArticleBean>?) {
                mView.showCategoryArticleInfos(o)
            }
        })

        categoryArticle()
    }

    fun categoryArticle() {
        mModel?.categoryArticle()?.getData(mView, { it, _ ->
            it?.let {

                if (it.isEmpty()) {
                    return@let
                }
                mView.showCategoryArticleInfos(it)
                //                mCacheWorker.setCache("main1_Article_category", mCategoryArticleBeans);
                CommonInfoHelper.setO<List<CategoryArticleBean>>(mContext, it, "main1_Article_category")
            }
        }, { _, _ -> }, false)

    }


    private fun createNewData(exampleTsCategory: ExampleTsCategory?) {
        if (exampleTsCategory == null) {
            return
        }
        val list1 = exampleTsCategory.list1
        val list2 = exampleTsCategory.list2
        if (list1 == null && list2 == null) {
            return
        }
        val mDatas = arrayListOf<MainT3Bean>()
        mDatas.add(MainT3Bean(MainT3Bean.LOVE_HEAL_TYPE_TITLE))
        mDatas.add(MainT3Bean(MainT3Bean.LOVE_HEAL_TYPE_ITEM_TITLE, "入门秘籍"))

        list1?.let {
            for (i in list1.indices) {
                val categoryList = list1[i]
                mDatas.add(MainT3Bean(MainT3Bean.LOVE_HEAL_TYPE_ITEM, categoryList._level, categoryList.desp, categoryList.id, categoryList.image, categoryList.name, categoryList.parent_id))
            }
        }
        mDatas.add(MainT3Bean(MainT3Bean.LOVE_HEAL_TYPE_ITEM_TITLE, "进阶秘籍"))
        list2?.let {
            for (i in list2.indices) {
                val categoryList = list2[i]
                mDatas.add(MainT3Bean(MainT3Bean.LOVE_HEAL_TYPE_ITEM, categoryList._level, categoryList.desp, categoryList.id, categoryList.image, categoryList.name, categoryList.parent_id))
            }
        }
        CommonInfoHelper.setO<List<MainT3Bean>>(mContext, mDatas, "main3_example_ts_category")
        mView.showSkillInfos(mDatas)
    }

    fun exampleTsList(id: String?, page: Int, pageSize: Int) {

        mModel?.exampleTsList(id, page, pageSize)?.getData(mView, { it, _ ->
            it?.let {
                mView.showSkillListInfo(it.lists)
            }
        }, { _, _ -> mView.onComplete() }, page == 1)


    }


    fun listsArticle(categoryId: String?, page: Int, pageSize: Int) {

        mModel?.listsArticle(categoryId, page, pageSize, "article.example/article_lists")?.getData(mView, { it, _ ->
            it?.let {
                mView.showSkillArticleList(it)
            }
        }, { _, _ -> mView.onComplete() }, page == 1)


    }


    fun detailArticle(id: String) {
        val userId = UserInfoHelper.instance.getUid()
        mModel?.detailArticle(id, "$userId", "article.example/detail")?.getData(mView, { it, _ ->
            it?.let {
                mView.showSkillArticleDetailInfo(it)
            }
        }, { _, _ -> })
    }


    fun collectSkillArticle(articleId: String?, isCollectArticle: Boolean) {
        val userId = UserInfoHelper.instance.getUid()
        val mUrl = if (!isCollectArticle) {
            "article.example/collect"
        } else {
            "article.example/uncollect"
        }

        mModel?.collectSkillArticle("$userId", articleId, mUrl)?.getData(mView, { it, msg ->
            it?.let {
                ToastUtils.showCenterToast(msg)
                val isCollectArticle = !isCollectArticle
                mView.showSkillArticleCollectResult(-1, isCollectArticle)
            }
        }, { _, _ -> })

    }


    fun digSkillArticle(articleId: String?, isDigArticle: Boolean) {
        val userId = UserInfoHelper.instance.getUid()
        val mUrl = if (isDigArticle) {
            "article.example/undig"
        } else {
            "article.example/dig"
        }
        mModel?.collectSkillArticle("$userId", articleId, mUrl)?.getData(mView, { it, msg ->
            it?.let {

                ToastUtils.showCenterToast(msg)
                val isDigArticle = !isDigArticle

                mView.showSkillArticleDigResult(isDigArticle)
            }
        }, { _, _ -> })

    }
}
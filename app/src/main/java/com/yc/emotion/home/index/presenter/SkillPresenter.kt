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
        mView.showLoadingDialog()
        mModel?.exampleTsCategory()?.subscribe(object : DisposableObserver<ResultInfo<ExampleTsCategory>>() {
            override fun onNext(t: ResultInfo<ExampleTsCategory>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        createNewData(t.data)
                    }
                }
            }

            override fun onComplete() {
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable) {

            }
        })

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
        mModel?.categoryArticle()?.subscribe(object : DisposableObserver<ResultInfo<List<CategoryArticleBean>>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(t: ResultInfo<List<CategoryArticleBean>>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val data = t.data
                        if (data == null || data.isEmpty()) {
                            return
                        }
                        mView.showCategoryArticleInfos(data)
                        //                mCacheWorker.setCache("main1_Article_category", mCategoryArticleBeans);
                        CommonInfoHelper.setO<List<CategoryArticleBean>>(mContext, data, "main1_Article_category")
                    }
                }
            }

        })
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
        if (page == 1)
            mView.showLoadingDialog()
        mModel?.exampleTsList(id, page, pageSize)?.subscribe(object : DisposableObserver<ResultInfo<ExampDataBean>>() {
            override fun onNext(t: ResultInfo<ExampDataBean>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showSkillListInfo(t.data.lists)
                    }
                }
            }

            override fun onComplete() {
                if (page == 1)
                    mView.hideLoadingDialog()
                mView.onComplete()
            }

            override fun onError(e: Throwable) {

            }

        })

    }


    fun listsArticle(categoryId: String?, page: Int, pageSize: Int) {
        if (page == 1)
            mView.showLoadingDialog()
        mModel?.listsArticle(categoryId, page, pageSize, "article.example/article_lists")?.subscribe(object : DisposableObserver<ResultInfo<List<LoveByStagesBean>>>() {
            override fun onNext(t: ResultInfo<List<LoveByStagesBean>>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showSkillArticleList(t.data)
                    }
                }
            }

            override fun onComplete() {
                if (page == 1) mView.hideLoadingDialog()
                mView.onComplete()
            }

            override fun onError(e: Throwable) {

            }

        })

    }


    fun detailArticle(id: String) {
        val userId = UserInfoHelper.instance.getUid()
        mView.showLoadingDialog()
        mModel?.detailArticle(id, "$userId", "article.example/detail")?.subscribe(object : DisposableObserver<ResultInfo<LoveByStagesDetailsBean>>() {
            override fun onNext(t: ResultInfo<LoveByStagesDetailsBean>) {
                mView.hideLoadingDialog()
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showSkillArticleDetailInfo(t.data)
                    }
                }
            }

            override fun onComplete() {
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable) {

            }

        })
    }


    fun collectSkillArticle(articleId: String?, isCollectArticle: Boolean) {
        val userId = UserInfoHelper.instance.getUid()
        val mUrl = if (!isCollectArticle) {
            "article.example/collect"
        } else {
            "article.example/uncollect"
        }
        mView.showLoadingDialog()
        mModel?.collectSkillArticle("$userId", articleId, mUrl)?.subscribe(object : DisposableObserver<ResultInfo<String>>() {
            override fun onNext(t: ResultInfo<String>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val msg = t.message
                        ToastUtils.showCenterToast(msg)
                        val isCollectArticle = !isCollectArticle

                        mView.showSkillArticleCollectResult(-1, isCollectArticle)

                    }
                }
            }

            override fun onComplete() {
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable) {

            }

        })
    }


    fun digSkillArticle(articleId: String?, isDigArticle: Boolean) {
        val userId = UserInfoHelper.instance.getUid()
        val mUrl = if (isDigArticle) {
            "article.example/undig"
        } else {
            "article.example/dig"
        }
        mView.showLoadingDialog()
        mModel?.collectSkillArticle("$userId", articleId, mUrl)?.subscribe(object : DisposableObserver<ResultInfo<String>>() {
            override fun onNext(t: ResultInfo<String>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val msg = t.message
                        ToastUtils.showCenterToast(msg)
                        val isDigArticle = !isDigArticle

                        mView.showSkillArticleDigResult(isDigArticle)

                    }
                }
            }

            override fun onComplete() {
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable) {

            }

        })
    }
}
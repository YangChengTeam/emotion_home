package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.yc.emotion.home.R
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.base.ui.activity.MainActivity
import com.yc.emotion.home.constant.URLConfig
import com.yc.emotion.home.index.domain.bean.SexInfo
import com.yc.emotion.home.index.ui.activity.*
import com.yc.emotion.home.model.bean.IndexInfo
import com.yc.emotion.home.model.bean.LiveInfoWrapper
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.ArrayList

/**
 *
 * Created by suns  on 2019/11/7 16:38.
 */
class IndexModel(override var context: Context?) : IModel {

    /**
     * 获取首页数据
     *
     * @return
     */
    fun getIndexInfo(): Observable<ResultInfo<IndexInfo>> {
        return HttpCoreEngin.get(context).rxpost(URLConfig.INDEX_INFO_URL, object : TypeReference<ResultInfo<IndexInfo>>() {
        }.type, null,
                true, true, true) as Observable<ResultInfo<IndexInfo>>
    }


    fun getSexInfoBySex(sex: Int): Observable<List<SexInfo>> {
        return Observable.just(sex).subscribeOn(Schedulers.io()).map<List<SexInfo>> { s ->
            val sexInfos = ArrayList<SexInfo>()

            if (sex == 1) {

//                var sexInfo = SexInfo(R.mipmap.home_buttom_love, IndexVerbalMainFragment::class.java, "love_dialogue_id", "恋爱话术")
//                sexInfos.add(sexInfo)
                var sexInfo = SexInfo(R.mipmap.home_buttom_combat, MainActivity::class.java, "chat_practice_click", "聊天实战")
                sexInfos.add(sexInfo)
                sexInfo = SexInfo(R.mipmap.home_buttom_skill, TipsCourseActivity::class.java, "secret_skill_id", "秘技")
                sexInfos.add(sexInfo)
                sexInfo = SexInfo(R.mipmap.home_buttom_article, MoreArticleActivity::class.java, "preferred_article_click", "优选文章")
                sexInfos.add(sexInfo)
                sexInfo = SexInfo(R.mipmap.home_buttom_test, EmotionTestMainActivity::class.java, "affective_test_id", "情感测试")
                sexInfos.add(sexInfo)

            } else if (sex == 2) {
                var sexInfo = SexInfo(R.mipmap.home_buttom_article, MoreArticleActivity::class.java, "preferred_article_click", "优选文章")
                sexInfos.add(sexInfo)
                sexInfo = SexInfo(R.mipmap.home_buttom_test, EmotionTestMainActivity::class.java, "affective_test_id", "情感测试")
                sexInfos.add(sexInfo)
                sexInfo = SexInfo(R.mipmap.home_buttom_combat, MainActivity::class.java, "chat_practice_click", "聊天实战")
                sexInfos.add(sexInfo)
//                sexInfo = SexInfo(R.mipmap.home_buttom_love, IndexVerbalMainFragment::class.java, "love_dialogue_id", "恋爱话术")
//                sexInfos.add(sexInfo)
                sexInfo = SexInfo(R.mipmap.home_buttom_skill, TipsCourseActivity::class.java, "secret_skill_id", "秘技")
                sexInfos.add(sexInfo)

            }

            sexInfos
        }.observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 获取直播列表
     */
    fun getLiveListInfo(): Observable<ResultInfo<LiveInfoWrapper>> {

        return HttpCoreEngin.get(context).rxpost(URLConfig.INDEX_LIVE_LIST_URL, object : TypeReference<ResultInfo<LiveInfoWrapper>>() {}.type, null,
                true, true, true) as Observable<ResultInfo<LiveInfoWrapper>>
    }

}
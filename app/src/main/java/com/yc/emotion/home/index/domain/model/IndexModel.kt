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
import com.yc.emotion.home.mine.domain.bean.LiveInfoWrapper
import com.yc.emotion.home.model.bean.IndexInfo
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

    /**
     * 获取在线直播列表
     */
    fun getOnlineLiveList(): Observable<ResultInfo<LiveInfoWrapper>> {

        return HttpCoreEngin.get(context).rxpost(URLConfig.ONLINE_ROOM_LIST_URL, object : TypeReference<ResultInfo<LiveInfoWrapper>>() {}.type, null, true, true, true)
                as Observable<ResultInfo<LiveInfoWrapper>>
    }

}
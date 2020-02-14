package com.yc.emotion.home.index.domain.model

import android.content.Context
import android.text.TextUtils
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.music.player.lib.bean.MusicInfo
import com.music.player.lib.bean.MusicInfoWrapper
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.constant.URLConfig
import com.yc.emotion.home.model.bean.AudioDataWrapperInfo
import rx.Observable
import java.util.HashMap

/**
 *
 * Created by suns  on 2019/11/20 11:03.
 */
class LoveAudioModel(override var context: Context?) : IModel {


    //列表
    //order  排序   id 按照时间倒序排列   listen_times 按照点击次数排序
    fun getLoveItemList(userId: String, typeId: String?, page: Int, limit: Int, orderInt: Int): Observable<ResultInfo<MusicInfoWrapper>> {


        //page=1&page_size=10&user_id=2&order=listen_times
        val params = HashMap<String, String?>()
        if (!TextUtils.isEmpty(typeId))
            params["cat_id"] = typeId
        params["page"] = page.toString() + ""
        params["page_size"] = limit.toString() + ""
        params["user_id"] = userId
        var order = ""
        if (orderInt == 1) {
            order = "id"
        } else if (orderInt == 2) {
            order = "listen_times"
        }
        params["order"] = order
        return HttpCoreEngin.get(context).rxpost(URLConfig.AUDIO_ITEM_LIST_URL, object : TypeReference<ResultInfo<MusicInfoWrapper>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<MusicInfoWrapper>>

    }


    /**
     * 音频详情页
     *
     * @param userId
     * @param id
     * @return
     */

    fun getMusicDetailInfo(userId: String, id: String?): Observable<ResultInfo<MusicInfoWrapper>> {
        val params = HashMap<String, String?>()

        params["user_id"] = userId
        params["id"] = id

        return HttpCoreEngin.get(context).rxpost(URLConfig.AUDIO_DETAIL_URL, object : TypeReference<ResultInfo<MusicInfoWrapper>>() {

        }.type,
                params, true, true, true) as Observable<ResultInfo<MusicInfoWrapper>>
    }


    //user_id: 用户ID
    //spa_id: SPAID
    //音频收藏
    fun collectAudio(user_id: String, music_id: String?): Observable<ResultInfo<String>> {
        val params = HashMap<String, String?>()
        params["user_id"] = user_id
        params["music_id"] = music_id
        return HttpCoreEngin.get(context).rxpost(URLConfig.AUDIO_COLLECT_URL, object : TypeReference<ResultInfo<String>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<String>>
    }


    fun audioPlay(spa_id: String): Observable<ResultInfo<String>> {
        val params = HashMap<String, String>()

        params["music_id"] = spa_id

        return HttpCoreEngin.get(context).rxpost(URLConfig.AUDIO_PLAY_URL, object : TypeReference<ResultInfo<MusicInfo>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<String>>

    }


    //分类
    fun getAudioDataInfo(): Observable<ResultInfo<AudioDataWrapperInfo>> {
        return HttpCoreEngin.get(context).rxpost(URLConfig.AUDIO_DATA_LIST_URL, object : TypeReference<ResultInfo<AudioDataWrapperInfo>>() {

        }.type, null, true, true, true) as Observable<ResultInfo<AudioDataWrapperInfo>>
    }
}
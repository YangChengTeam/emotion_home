package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.engin.HttpCoreEngin
import com.kk.securityhttp.net.entry.UpFileInfo
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.constant.URLConfig
import com.yc.emotion.home.model.bean.confession.ConfessionBean
import com.yc.emotion.home.model.bean.ImageCreateBean
import rx.Observable
import java.io.File
import java.util.*

/**
 *
 * Created by suns  on 2019/11/20 14:48.
 */
class ExpressModel(override var context: Context?) : IModel {


    fun getExpressData(page: Int): Observable<ConfessionBean> {
        val params = HashMap<String, String>()
        params["id"] = "1"
        params["page"] = page.toString() + ""

        return HttpCoreEngin.get(context).rxpost(URLConfig.CATEGORY_LIST_URL, object : TypeReference<ConfessionBean>() {

        }.type, params,
                false, false, false) as Observable<ConfessionBean>

    }


    fun netNormalData(requestMap: Map<String, String?>, requestUrl: String): Observable<ImageCreateBean> {


        return HttpCoreEngin.get(context).rxpost(requestUrl, object : TypeReference<ImageCreateBean>() {}.type, requestMap, false, false, false)
                as Observable<ImageCreateBean>
    }


    fun netUpFileNet(requestMap: Map<String, String?>, upFile: File, requestUrl: String): Observable<ImageCreateBean> {

        val fileName = URLConfig.BASE_NORMAL_FILE_DIR + File.separator + System.currentTimeMillis() + (Math.random() * 10000).toInt() + ".jpg"
        val uploadFile = UpFileInfo()
        uploadFile.file = upFile
        uploadFile.filename = fileName
        uploadFile.name = "img"

        return HttpCoreEngin.get(context).rxuploadFile(requestUrl, object : TypeReference<ImageCreateBean>() {}.type, uploadFile, requestMap, false) as Observable<ImageCreateBean>

    }


}
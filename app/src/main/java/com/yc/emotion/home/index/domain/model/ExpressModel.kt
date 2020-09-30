package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.yc.emotion.home.base.constant.URLConfig
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.model.bean.ImageCreateBean
import com.yc.emotion.home.model.bean.confession.ConfessionBean
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*

/**
 *
 * Created by suns  on 2019/11/20 14:48.
 */
class ExpressModel(override var context: Context?) : IModel(context) {


    fun getExpressData(page: Int): io.reactivex.Observable<ConfessionBean> {

        return request.getExpressData(URLConfig.CATEGORY_LIST_URL, "1", page, isrsa = false, iszip = false).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    fun netNormalData(requestMap: Map<String, String?>, requestUrl: String): io.reactivex.Observable<ImageCreateBean> {


        return request.netNormalData(requestMap, requestUrl, isrsa = false, iszip = false).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    fun netUpFileNet(params: Map<String, String?>, upFile: File, requestUrl: String): io.reactivex.Observable<ImageCreateBean> {


        val requestBodyMap: MutableMap<String, RequestBody?> = HashMap()
        for ((key, value) in params.entries) {
            value?.let { requestBodyMap[key] = RequestBody.create(MediaType.parse("text/plain"), value) }

        }

        val fileName = URLConfig.BASE_NORMAL_FILE_DIR + File.separator + System.currentTimeMillis() + (Math.random() * 10000).toInt() + ".jpg"

        val photoRequestBody: RequestBody = RequestBody.create(MediaType.parse("image/png"), upFile)
        val photo = MultipartBody.Part.createFormData("img", fileName, photoRequestBody)


        return request.netUpFileNet(requestBodyMap, photo, requestUrl).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


}
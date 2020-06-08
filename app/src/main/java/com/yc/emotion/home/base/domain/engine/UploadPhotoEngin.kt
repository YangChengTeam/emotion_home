package com.yc.emotion.home.base.domain.engine

import com.yc.emotion.home.constant.URLConfig
import okhttp3.*
import java.io.File

/**
 * Created by mayn on 2019/5/17.
 */
class UploadPhotoEngin(file: File?, responseCallback: Callback?) {
    companion object {
        private const val mImageType = "multipart/form-data"
    }

    init {
        val okHttpClient = OkHttpClient()
        val fileBody = RequestBody.create(MediaType.parse("image/png"), file)
        val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM) //可以根据自己的接口需求在这里添加上传的参数
                .addFormDataPart("file", "images", fileBody)
                .addFormDataPart("imagetype", mImageType)
                .build()

        //表单数据参数填入
        val request = Request.Builder()
                .url(URLConfig.uploadPhotoUrl) //                .post(builder1.build())
                .post(requestBody)
                .build()
        val call = okHttpClient.newCall(request)
        call.enqueue(responseCallback)
    }
}
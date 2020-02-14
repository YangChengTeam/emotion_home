package com.yc.emotion.home.base.domain.engine;

import com.yc.emotion.home.constant.URLConfig;

import java.io.File;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by mayn on 2019/5/17.
 */

public class UploadPhotoEngin {

    private static String mImageType = "multipart/form-data";

    public UploadPhotoEngin(File file, Callback responseCallback) {
        OkHttpClient okHttpClient = new OkHttpClient();


        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //可以根据自己的接口需求在这里添加上传的参数
                .addFormDataPart("file", "images", fileBody)
                .addFormDataPart("imagetype", mImageType)
                .build();

        //表单数据参数填入
        final Request request = new Request.Builder()
                .url(URLConfig.uploadPhotoUrl)
//                .post(builder1.build())
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(responseCallback);
    }
}

package yc.com.rthttplibrary.request;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import yc.com.rthttplibrary.config.HttpConfig;
import yc.com.rthttplibrary.converter.LogInterceptor;

/**
 * Created by suns  on 2020/7/24 11:57.
 */
public class OkHttpClientUtil {


    private static OkHttpClient.Builder defaultClientBuilder() {
        return new OkHttpClient.Builder()
                .connectTimeout(HttpConfig.TIMEOUT, TimeUnit.MILLISECONDS)//设置读取超时时间
                .readTimeout(HttpConfig.TIMEOUT, TimeUnit.MILLISECONDS)//设置请求超时时间
                .writeTimeout(HttpConfig.TIMEOUT, TimeUnit.MILLISECONDS)//设置写入超时时间

                .retryOnConnectionFailure(true);
    }

    public static OkHttpClient.Builder setClientBuilder(OkHttpClient.Builder builder) {
        if (builder == null) {
            builder = defaultClientBuilder();
        }
        return builder;
    }

    public static OkHttpClient.Builder addInterceptorBuilder(OkHttpClient.Builder builder, Interceptor interceptor) {
        builder = setClientBuilder(builder);
        if (interceptor == null) {
            interceptor = new LogInterceptor();
        }
        builder.addInterceptor(interceptor);  //添加打印拦截器
        return builder;
    }


}

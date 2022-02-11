package com.yc.emotion.home.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Process
import android.text.TextUtils
import android.util.Log
import android.webkit.WebView
import androidx.multidex.MultiDexApplication
import com.bytedance.embedapplog.AppLog
import com.bytedance.embedapplog.InitConfig
import com.bytedance.embedapplog.util.UriConfig
import com.danikula.videocache.HttpProxyCacheServer
import com.kk.share.UMShareImpl
import com.paradigm.botkit.BotKitClient
import com.paradigm.botlib.VisitorInfo
import com.tencent.bugly.Bugly
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.UMShareAPI
import com.yc.emotion.home.R
import com.yc.emotion.home.base.constant.Constant
import com.yc.emotion.home.base.constant.URLConfig.getBaseUrl
import com.yc.emotion.home.model.ModelApp
import com.yc.emotion.home.model.util.SPUtils
import com.yc.emotion.home.utils.Preference
import com.yc.emotion.home.utils.ShareInfoHelper.getNetShareInfo
import com.yc.emotion.home.utils.UIUtils.getAppName
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject
import yc.com.rthttplibrary.config.GoagalInfo
import yc.com.rthttplibrary.config.HttpConfig
import yc.com.rthttplibrary.converter.FastJsonConverterFactory
import yc.com.rthttplibrary.request.RetrofitHttpRequest
import yc.com.rthttplibrary.util.FileUtil
import yc.com.rthttplibrary.util.LogUtil
import yc.com.toutiao_adv.TTAdManagerHolder
import java.io.File
import java.util.*
import java.util.zip.ZipFile

/**
 * Created by mayn on 2019/4/24.
 */
class EmApplication : MultiDexApplication() {
    var activityIdCorList: List<Activity>? = null

    /**
     * 当前Activity个数
     */
    private var activityCount = 0
    private var isPaused = false
    private var isFirstOpen = true

    @SuppressLint("CheckResult")
    override fun onCreate() {
        super.onCreate()
        instance = this
        webViewSetPath(this)
        RetrofitHttpRequest.Builder(getBaseUrl())
                .convert(FastJsonConverterFactory.create())
        Observable.just("").subscribeOn(Schedulers.io()).subscribe { s: String? -> init() }
        ModelApp.init(this)
        TTAdManagerHolder.init(this, Constant.TOUTIAO_AD_ID)
        initRAData()
        initBot()
    }

    private fun initBot() {
        BotKitClient.getInstance().enableDebugLog()
        BotKitClient.getInstance().init(this, getString(R.string.plo_key))
        setVistorInfo()
    }

    private fun init() {
        //        Bugly.init(getApplicationContext(), "注册时申请的APPID", false);  //腾迅自动更新
        Bugly.init(applicationContext, "dc88d75f55", false) //腾迅自动更新
//        UMConfigure.setLogEnabled(true)
        //预初始化友盟SDK
        UMConfigure.preInit(applicationContext, "5da983e44ca357602b00046d", "Umeng")
        isFirstOpen = SPUtils.get(this, Constant.first_open, true) as Boolean
        if (!isFirstOpen) {
            //初始化友盟SDK
            UMConfigure.init(applicationContext, "5da983e44ca357602b00046d", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null)
        }

        UMShareAPI.get(this) //初始化sdk

        //开启debug模式，方便定位错误，具体错误检查方式可以查看
        //http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        UMConfigure.setLogEnabled(true)

//        UMGameAgent.setPlayerLevel(1);
//        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        val builder = UMShareImpl.Builder()
        builder.setWeixin("wxe224386e89afc8c1", "a6ce8283ca3524ff2d75dad0791a0101")
                .setQQ("101811246", "8310b6974f5f712f827fc8eff8228822")
                .build(this)
        //


        //全局信息初始化
        GoagalInfo.get().init(applicationContext)
        val appinfo = applicationInfo
        val sourceDir = appinfo.sourceDir
        val zf: ZipFile
        try {
            zf = ZipFile(sourceDir)
            val ze1 = zf.getEntry("META-INF/channelconfig.json")
            val in1 = zf.getInputStream(ze1)
            val result1 = FileUtil.readString(in1)
            val jsonObject = JSONObject(result1)
            setHttpDefaultParams(jsonObject)
            LogUtil.msg("渠道->$result1")
        } catch (e: Exception) {
            setHttpDefaultParams(null)
        }

        HttpConfig.setPublickey("""
    -----BEGIN PUBLIC KEY-----
    MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA5KaI8l7xplShIEB0Pwgm
    MRX/3uGG9BDLPN6wbMmkkO7H1mIOXWB/Jdcl4/IMEuUDvUQyv3P+erJwZ1rvNsto
    hXdhp2G7IqOzH6d3bj3Z6vBvsXP1ee1SgqUNrjX2dn02hMJ2Swt4ry3n3wEWusaW
    mev4CSteSKGHhBn5j2Z5B+CBOqPzKPp2Hh23jnIH8LSbXmW0q85a851BPwmgGEan
    5HBPq04QUjo6SQsW/7dLaaAXfUTYETe0HnpLaimcHl741ftGyrQvpkmqF93WiZZX
    wlcDHSprf8yW0L0KA5jIwq7qBeu/H/H5vm6yVD5zvUIsD7htX0tIcXeMVAmMXFLX
    35duvYDpTYgO+DsMgk2Q666j6OcEDVWNBDqGHc+uPvYzVF6wb3w3qbsqTnD0qb/p
    WxpEdgK2BMVz+IPwdP6hDsDRc67LVftYqHJLKAfQt5T6uRImDizGzhhfIfJwGQxI
    7TeJq0xWIwB+KDUbFPfTcq0RkaJ2C5cKIx08c7lYhrsPXbW+J/W4M5ZErbwcdj12
    hrfV8TPx/RgpJcq82otrNthI3f4QdG4POUhdgSx4TvoGMTk6CnrJwALqkGl8OTfP
    KojOucENSxcA4ERtBw4It8/X39Mk0aqa8/YBDSDDjb+gCu/Em4yYvrattNebBC1z
    ulK9uJIXxVPi5tNd7KlwLRMCAwEAAQ==
    -----END PUBLIC KEY-----
    """.trimIndent())
        activityIdCorList = ArrayList()
        getNetShareInfo(this)
//        SensitiveWord.initWords(this)
        // 初始化 JPush
//        registerActivityLifecycleCallbacks(callbacks)
    }

    private fun setHttpDefaultParams(jsonObject: JSONObject?) {
        //设置http默认参数
        var agent_id = "2"
        val params: MutableMap<String, String> = HashMap()
        if (GoagalInfo.get().channelInfo != null && GoagalInfo.get().channelInfo.agent_id != null) {
            params["from_id"] = GoagalInfo.get().channelInfo.from_id + ""
            params["author"] = GoagalInfo.get().channelInfo.author + ""
            agent_id = GoagalInfo.get().channelInfo.agent_id
        }
        params["agent_id"] = agent_id
        params["ts"] = System.currentTimeMillis().toString() + ""
        params["device_type"] = "2"
        params["app_id"] = "5" //8
        var uid = GoagalInfo.get().uuid
        if (TextUtils.isEmpty(uid)) uid = pesudoUniqueID
        params["imeil"] = uid
        try {
            if (jsonObject != null) {
                params["site_id"] = jsonObject.getString("site_id")
                params["soft_id"] = jsonObject.getString("soft_id")
                params["app_name"] = getAppName(this)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val sv = if (Build.MODEL.contains(Build.BRAND)) Build.MODEL + " " + Build.VERSION.RELEASE else Build.BRAND + " " + Build.MODEL + " " + Build.VERSION.RELEASE
        params["sys_version"] = sv
        if (GoagalInfo.get().packageInfo != null) {
            params["app_version"] = GoagalInfo.get().packageInfo.versionCode.toString() + ""
        }
        HttpConfig.setDefaultParams(params)

    }

    fun setVistorInfo() {
        val info = VisitorInfo()
        val instance: UserInfoHelper = UserInfoHelper.instance
        info.userId = "${instance.getUid()}" // 用户的唯一标识
        if (instance.getUserInfo() != null) {
            info.userName = instance.getUserInfo()?.nick_name
            info.phone = instance.getUserInfo()?.mobile
        }
        BotKitClient.getInstance().setVisitor(info)
    }

    //we make this look like a valid IMEI
    private val pesudoUniqueID: String
        get() = "6c" + //we make this look like a valid IMEI
                Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 + Build.HOST.length % 10 + Build.ID.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10 + Build.TAGS.length % 10 + Build.TYPE.length % 10 + Build.USER.length % 10

    private val callbacks: ActivityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle) {}
        override fun onActivityStarted(activity: Activity) {
            activityCount++
            if (activityCount == 1 && isPaused) {
                isPaused = false
                isBackToForeground = true
                //                EventBus.getDefault().post(new IndexRefreshEvent("回到前台"));
            }
            Log.e(TAG, "onActivityStarted: $activityCount")
        }

        override fun onActivityResumed(activity: Activity) {}
        override fun onActivityPaused(activity: Activity) {}
        override fun onActivityStopped(activity: Activity) {
            activityCount--
            if (activityCount == 0) {
                isPaused = true
                Log.e(TAG, "isForeground : ")
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        override fun onActivityDestroyed(activity: Activity) {}
    }

    //Android P行为变更，不可多进程使用同一个目录webView，需要为不同进程webView设置不同目录
    private fun webViewSetPath(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val processName = getProcessName(context)
            if (applicationInfo.packageName != processName) { //判断不等于默认进程名称
                WebView.setDataDirectorySuffix(processName)
            }
        }
    }

    private fun getProcessName(context: Context?): String? {
        if (context == null) return null
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == Process.myPid()) {
                return processInfo.processName
            }
        }
        return null
    }

    private fun initRAData() {


        /* 初始化开始 */
        // appid和渠道，appid须保证与广告后台申请记录一致，渠道可自定义，如有多个马甲包建议设置渠道号唯一标识一个马甲包。
        val config = InitConfig("your_appid", "your_channel")

        /*
         域名默认国内: DEFAULT, 新加坡:SINGAPORE, 美东:AMERICA
         注意：国内外不同vendor服务注册的did不一样。由DEFAULT切换到SINGAPORE或者AMERICA，会发生变化，
         切回来也会发生变化。因此vendor的切换一定要慎重，随意切换导致用户新增和统计的问题，需要自行评估。
         */config.setUriConfig(UriConfig.DEFAULT)

//        // 游戏模式，YES会开始 playSession 上报，每隔一分钟上报心跳日志
//        config.setEnablePlay(true)

        // 是否在控制台输出日志，可用于观察用户行为日志上报情况，建议仅在调试时使用，release版本请设置为false ！
        AppLog.setEnableLog(false)

        AppLog.init(this, config)

        /* 初始化结束 */
        // 自定义 “用户公共属性”（可选，初始化后调用, key相同会覆盖）

        val headerMap = HashMap<String, Any>()
        headerMap["level"] = "8"
        headerMap["gender"] = "female"
        AppLog.setHeaderInfo(headerMap)
    }


    private var proxy: HttpProxyCacheServer? = null

    fun getProxy(): HttpProxyCacheServer? {
        val app = this.applicationContext as EmApplication
        if (app.proxy == null) {
            app.proxy = app.newProxy()
            return app.proxy
        }
        return app.proxy
    }

    private fun newProxy(): HttpProxyCacheServer? {

        val videoCacheDir: String = getExternalFilesDir(Environment.DIRECTORY_MOVIES)?.absolutePath + "/emotion_home/Cache/Video/"

        //如果SD卡已挂载并且可读写

        return HttpProxyCacheServer.Builder(this)
                .maxCacheSize(1024 * 1024 * 1024.toLong())
                .cacheDirectory(File(videoCacheDir))
                .maxCacheFilesCount(30)
                // 1 Gb for cache
                .build()
    }


    companion object {
        private const val TAG = "YcApplication"
        lateinit var instance: EmApplication
            private set

        //是否是从后台到前台
        var isBackToForeground = false
    }
}
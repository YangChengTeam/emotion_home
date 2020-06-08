package com.yc.emotion.home.constant

import java.io.File

/**
 * Created by mayn on 2019/5/8.
 */
object URLConfig {
    const val DEBUG = Config.DEBUG
    private const val baseUrl = "http://qg.bshu.com/api/"
    private const val baseUrlV1 = baseUrl + "v1/"
    var debugBaseUrl = "http://love.bshu.com/v1/"
    private const val URL_SERVER_IP = "http://nz.qqtn.com/zbsq/index.php?"

    //表白
    const val CATEGORY_LIST_URL = URL_SERVER_IP + "m=Home&c=zbsq&a=getCateList"

    // 5、图片合成
    const val URL_IMAGE_CREATE = URL_SERVER_IP + "m=Home&c=Zbsq&a=start_zb"

    //音频分类
    const val AUDIO_DATA_LIST_URL = "http://love.bshu.com/v1/music/cats"

    //音频列表
    const val AUDIO_ITEM_LIST_URL = "http://love.bshu.com/v1/music/lists"
    const val LOVE_INDEX_URL = baseUrlV1 + "Hotsearch/index"

    //音频收藏接口
    const val AUDIO_COLLECT_URL = baseUrlV1 + "music/collect"

    //音频收藏列表
    const val AUDIO_COLLECT_LIST_URL = baseUrlV1 + "music/collect_list"

    //首页搜索框下拉热词
    val INDEX_DROP_URL = getBaseUrl() + "dialogue.dialogue/dropdown"

    //音频详情
    const val AUDIO_DETAIL_URL = "http://love.bshu.com/v1/music/info"

    //分享得会员
    val SHARE_REWARD_URL = getBaseUrl() + "share.share/reward"

    /**
     * spa随便听听
     */
    const val SPA_RANDOM_URL = baseUrlV1 + "spa/random"

    //音频播放次数
    const val AUDIO_PLAY_URL = baseUrlV1 + "music/play"

    //默认值
    private const val sdPath = "/storage/emulated/0/Android/data/com.ant.flying/cache"
    private const val SD_DIR = sdPath
    private val BASE_SD_DIR = SD_DIR + File.separator + "TNZBSQ"
    val BASE_NORMAL_FILE_DIR = BASE_SD_DIR + File.separator + "files"

    //上传接口
    var uploadPhotoUrl = getBaseUrl() + "user.upload/upimg"
    fun getBaseUrl(): String {
        return if (DEBUG) debugBaseUrl else baseUrl
    }

    fun getUrl(url: String): String {
        return if (DEBUG) debugBaseUrl + url else baseUrl + url
    }

    fun getUrlV1(url: String?): String {
        return if (DEBUG) debugBaseUrl + url else baseUrlV1 + url
    }

    //用户资料
    val USER_INFO_URL = getBaseUrl() + "user.user/info"

    // apk 下载地址
    var download_apk_url = "http://toppic-mszs.oss-cn-hangzhou.aliyuncs.com/xfzs.apk"
    var SHARE_INFO_URL = getBaseUrl() + "share.share/info"
    val WECHAT_INFO_URL = getBaseUrl() + "tutor.tutor/weixin"

    //最新帖子
    val COMMUNITY_NEWEST_LIST_URL = getBaseUrl() + "topic.topic/newlist"

    //帖子点赞
    val TOPIC_LIKE_URL = getBaseUrl() + "topic.topic/dig"

    //帖子详情
    val TOPIC_DETAIL_URL = getBaseUrl() + "topic.topic/detail"

    //创建评论
    val CREATE_COMMENT_URL = getBaseUrl() + "topic.topic_comment/create"

    //评论点赞
    val COMMENT_LIKE_URL = getBaseUrl() + "topic.topic_comment/dig"

    //评论标签
    val COMMUNITY_TAG_URL = getBaseUrl() + "topic.topic_cat/all"

    //热门帖子
    val COMMUNITY_HOT_LIST_URL = getBaseUrl() + "topic.topic/hotlist"

    //发帖
    val PUBLISH_COMMUNITY_URL = getBaseUrl() + "topic.topic/post"

    //我的发帖
    val COMMUNITY_MY_URL = getBaseUrl() + "topic.topic/mylist"

    //顶部公告
    val TOP_TOPIC_URL = getBaseUrl() + "topic.topic/top"

    //热门标签列表
    val COMMUNITY_TAG_LIST_URL = getBaseUrl() + "topic.topic/catlist"

    //删除发帖
    val DELETE_TOPIC_URL = getBaseUrl() + "topic.topic/delete"

    //注册
    val REGISTER_URL = getBaseUrl() + "user.user/reg"

    //发送验证码
    val SEND_CODE_URL = getBaseUrl() + "user.user/send_code"

    //登录
    val LOGIN_URL = getBaseUrl() + "user.user/login"

    //第三方登录
    val THRID_LOGIN_URL = getBaseUrl() + "user.user/snsLogin"

    //更新用户资料
    val UPDATE_USER_INFO_URL = getBaseUrl() + "user.user/update"

    //商品列表
    val GOOD_LIST_URL = getBaseUrl() + "orders.goods/index"

    //创建订单
    val ORDER_URL = getBaseUrl() + "orders.orders/init"

    //话术场景数据
    val DIALOGUE_CATEGORY_URL = getBaseUrl() + "dialogue.dialogue/category"

    //话术详情列表数据
    val DIALOGUE_DETAIL_LISTS_URL = getBaseUrl() + "dialogue.dialogue/lists"

    //话术搜索
    val VERBAL_SEARCH_URL = getBaseUrl() + "dialogue.dialogue/search"

    //搜索次数统计
    val SEARCH_COUNT_URL = getBaseUrl() + "dialogue.dialogue/searchlog"

    //导师列表
    val TUTOR_LIST_URL = getBaseUrl() + "tutor.tutor/index"

    //导师详情
    val TUTOR_DETAIL_URL = getBaseUrl() + "tutor.tutor/desp"

    //聊天实战列表
    val PRACTICE_LIS_URL = getBaseUrl() + "article.example/lists"

    //话术实战详情
    val VERBAL_PRATICE_DETAIL_URL = getBaseUrl() + "article.example/detail"

    //首页信息
    val INDEX_INFO_URL = getBaseUrl() + "index/index"

    //高效课程详情
    val COURSE_DETAIL_URL = getBaseUrl() + "lesson.lesson_chapter/lessons"

    //文章分类
    val ARTICLE_CATEGORY_URL = getBaseUrl() + "article.article/category"

    //文章分类列表
    val ARTICLE_LIST_URL = getBaseUrl() + "article.article/cate_lists"

    //文章详情页
    val ARTICLE_DETAIL_URL = getBaseUrl() + "article.article/detail"

    //文章收藏列表
    val ARTICLE_COLLECT_LIST_URL = getBaseUrl() + "article.article/collect_list"

    //订单列表
    val ORDER_LIST_URL = getBaseUrl() + "orders.orders/lists"

    //咨询服务
    val CONSULT_TUTOR_URL = getBaseUrl() + "tutor.tutor/reserve"

    //课程分类
    val COURSE_CATEGORY_URL = getBaseUrl() + "lesson.lesson_category/cat_list"

    //课程列表
    val COURSE_LIST_URL = getBaseUrl() + "lesson.lesson_chapter/cat_list"

    //情感测试列表
    val TEST_LIST_URL = getBaseUrl() + "psych.psych_test/index"

    //情感测试详情
    val TEST_DETAIL_URL = getBaseUrl() + "psych.psych_test/detail"

    //测试提交答案
    val TEST_SUBMIT_ANSWER_URL = getBaseUrl() + "psych.psych_test/answer"

    //测试记录
    val TEST_RECORDS_URL = getBaseUrl() + "psych.psych_test/records"

    //测试分类
    val TEST_CATEGORY_URL = getBaseUrl() + "psych.psych_test/cats"

    //导师评论列表
    val TUTOR_COMMENT_LIST_URL = getBaseUrl() + "tutor.tutor/comment_list"

    //导师详情页课程
    val TUTOR_COURSE_DETAIL_URL = getBaseUrl() + "tutor.tutor/lessons"

    //导师分类
    val TUTOR_CATGORY_URL = getBaseUrl() + "tutor.tutor/cats"

    //首页搜索相关
    val INDEX_SEARCH_URL = getBaseUrl() + "index/search"

    //导师证书
    val TUTOR_CERTS_URL = getBaseUrl() + "tutor.tutor/certs"

    //消息列表
    val MESSAGE_LIST_URL = getBaseUrl() + "notice.notice/announce"

    //用户感兴趣内容
    val INTER_ALL_URL = getBaseUrl() + "user.interested/all"

    //课程收藏或者取消
    val COLLECT_COURSE_URL = getBaseUrl() + "lesson.lesson_chapter/collect"

    //课程收藏列表
    val COURSE_COLLECT_LIST_URL = getBaseUrl() + "lesson.lesson_chapter/collect_list"

    //测试详情
    val TEST_RECORD_DETAIL_URL = getBaseUrl() + "psych.psych_test/records_detail"

    //导师服务详情
    val TUTOR_SERVICE_DEATAIL_URL = getBaseUrl() + "tutor.tutor/service_detail"

    //导师服务列表
    val TUTOR_SERVICE_LIST_URL = getBaseUrl() + "tutor.tutor/service"

    //重置密码
    val RESET_PWD_URL = getBaseUrl() + "user.user/resetPassword"

    //通知详情
    val NOTIFICATION_DETAIL_URL = getBaseUrl() + "notice.notice/detail"

    //秘技主页
    val SKILL_MAIN_URL = getBaseUrl() + "article.example/ts_category"

    //直播列表
    val INDEX_LIVE_LIST_URL = getBaseUrl() + "video.video/video_list"
    val SET_PWD_URL = getBaseUrl() + "user.user/set_pwd"
    val MODIFY_PWD_URL = getBaseUrl() + "user.user/edit_pwd"

    //专题文章
    val MONOGRAPH_ARTICLE_URL = getBaseUrl() + "article.article/lists"
    //    public static String download_apk_url = "http://toppic-mszs.oss-cn-hangzhou.aliyuncs.com/";

    //主播登录接口
    //http://qg.bshu.com/api/
    val ANCHOR_LOGIN_URL = baseUrl + "live.user/login"

    //主播创建房间号
    val CREATE_ROOM_URL = baseUrl + "live.room/create"

    //首页直播列表
    val ONLINE_ROOM_LIST_URL = baseUrl + "live.room/lists"

}
package com.yc.emotion.home.constant;

import java.io.File;

/**
 * Created by mayn on 2019/5/8.
 */

public class URLConfig {


    public static final boolean DEBUG = Config.DEBUG;


    private static final String baseUrl = "http://qg.bshu.com/api/";
    private static final String baseUrlV1 = baseUrl.concat("v1/");

    public static String debugBaseUrl = "http://love.bshu.com/v1/";


    private final static String URL_SERVER_IP = "http://nz.qqtn.com/zbsq/index.php?";

    //表白
    public final static String CATEGORY_LIST_URL = URL_SERVER_IP + "m=Home&c=zbsq&a=getCateList";
    // 5、图片合成
    public final static String URL_IMAGE_CREATE = URL_SERVER_IP + "m=Home&c=Zbsq&a=start_zb";
    //音频分类
    public static final String AUDIO_DATA_LIST_URL = "http://love.bshu.com/v1/music/cats";
    //音频列表
    public static final String AUDIO_ITEM_LIST_URL = "http://love.bshu.com/v1/music/lists";

    public static final String LOVE_INDEX_URL = baseUrlV1.concat("Hotsearch/index");

    //音频收藏接口
    public static final String AUDIO_COLLECT_URL = baseUrlV1.concat("music/collect");
    //音频收藏列表
    public static final String AUDIO_COLLECT_LIST_URL = baseUrlV1.concat("music/collect_list");

    //首页搜索框下拉热词
    public static final String INDEX_DROP_URL = getBaseUrl().concat("dialogue.dialogue/dropdown");

    //音频详情
    public static final String AUDIO_DETAIL_URL = "http://love.bshu.com/v1/music/info";

    //分享得会员
    public static final String SHARE_REWARD_URL = getBaseUrl().concat("share.share/reward");

    /**
     * spa随便听听
     */
    public static final String SPA_RANDOM_URL = baseUrlV1.concat("spa/random");

    //音频播放次数
    public static final String AUDIO_PLAY_URL = baseUrlV1.concat("music/play");


    //默认值
    private static String sdPath = "/storage/emulated/0/Android/data/com.ant.flying/cache";
    private static String SD_DIR = sdPath;
    private static final String BASE_SD_DIR = SD_DIR + File.separator + "TNZBSQ";
    public static final String BASE_NORMAL_FILE_DIR = BASE_SD_DIR + File.separator + "files";


    //上传接口
    public static String uploadPhotoUrl = getBaseUrl().concat("user.upload/upimg");


    public static String getBaseUrl() {
        return (DEBUG ? debugBaseUrl : baseUrl);
    }

    public static String getUrl(String url) {
        return DEBUG ? debugBaseUrl.concat(url) : baseUrl.concat(url);
    }

    public static String getUrlV1(String url) {
        return DEBUG ? debugBaseUrl.concat(url) : baseUrlV1.concat(url);
    }


    //用户资料
    public static final String USER_INFO_URL = getBaseUrl() + "user.user/info";

    // apk 下载地址
    public static String download_apk_url = "http://toppic-mszs.oss-cn-hangzhou.aliyuncs.com/xfzs.apk";


    public static String SHARE_INFO_URL = getBaseUrl().concat("share.share/info");

    public static final String WECHAT_INFO_URL = getBaseUrl().concat("tutor.tutor/weixin");

    //最新帖子
    public static final String COMMUNITY_NEWEST_LIST_URL = getBaseUrl().concat("topic.topic/newlist");

    //帖子点赞
    public static final String TOPIC_LIKE_URL = getBaseUrl().concat("topic.topic/dig");

    //帖子详情
    public static final String TOPIC_DETAIL_URL = getBaseUrl().concat("topic.topic/detail");

    //创建评论
    public static final String CREATE_COMMENT_URL = getBaseUrl().concat("topic.topic_comment/create");

    //评论点赞
    public static final String COMMENT_LIKE_URL = getBaseUrl().concat("topic.topic_comment/dig");

    //评论标签
    public static final String COMMUNITY_TAG_URL = getBaseUrl().concat("topic.topic_cat/all");

    //热门帖子
    public static final String COMMUNITY_HOT_LIST_URL = getBaseUrl().concat("topic.topic/hotlist");
    //发帖
    public static final String PUBLISH_COMMUNITY_URL = getBaseUrl().concat("topic.topic/post");

    //我的发帖
    public static final String COMMUNITY_MY_URL = getBaseUrl().concat("topic.topic/mylist");
    //顶部公告
    public static final String TOP_TOPIC_URL = getBaseUrl().concat("topic.topic/top");
    //热门标签列表
    public static final String COMMUNITY_TAG_LIST_URL = getBaseUrl().concat("topic.topic/catlist");

    //删除发帖
    public static final String DELETE_TOPIC_URL = getBaseUrl().concat("topic.topic/delete");


    //注册
    public static final String REGISTER_URL = getBaseUrl().concat("user.user/reg");

    //发送验证码
    public static final String SEND_CODE_URL = getBaseUrl().concat("user.user/send_code");

    //登录
    public static final String LOGIN_URL = getBaseUrl().concat("user.user/login");
    //第三方登录
    public static final String THRID_LOGIN_URL = getBaseUrl().concat("user.user/snsLogin");
    //更新用户资料
    public static final String UPDATE_USER_INFO_URL = getBaseUrl().concat("user.user/update");


    //商品列表
    public static final String GOOD_LIST_URL = getBaseUrl().concat("orders.goods/index");
    //创建订单
    public static final String ORDER_URL = getBaseUrl().concat("orders.orders/init");

    //话术场景数据
    public static final String DIALOGUE_CATEGORY_URL = getBaseUrl().concat("dialogue.dialogue/category");
    //话术详情列表数据
    public static final String DIALOGUE_DETAIL_LISTS_URL = getBaseUrl().concat("dialogue.dialogue/lists");

    //话术搜索
    public static final String VERBAL_SEARCH_URL = getBaseUrl().concat("dialogue.dialogue/search");

    //搜索次数统计
    public static final String SEARCH_COUNT_URL = getBaseUrl().concat("dialogue.dialogue/searchlog");

    //导师列表
    public static final String TUTOR_LIST_URL = getBaseUrl().concat("tutor.tutor/index");

    //导师详情
    public static final String TUTOR_DETAIL_URL = getBaseUrl().concat("tutor.tutor/desp");

    //聊天实战列表
    public static final String PRACTICE_LIS_URL = getBaseUrl().concat("article.example/lists");

    //话术实战详情
    public static final String VERBAL_PRATICE_DETAIL_URL = getBaseUrl().concat("article.example/detail");


    //首页信息
    public static final String INDEX_INFO_URL = getBaseUrl().concat("index/index");

    //高效课程详情
    public static final String COURSE_DETAIL_URL = getBaseUrl().concat("lesson.lesson_chapter/lessons");


    //文章分类
    public static final String ARTICLE_CATEGORY_URL = getBaseUrl().concat("article.article/category");

    //文章分类列表
    public static final String ARTICLE_LIST_URL = getBaseUrl().concat("article.article/cate_lists");

    //文章详情页
    public static final String ARTICLE_DETAIL_URL = getBaseUrl().concat("article.article/detail");

    //文章收藏列表
    public static final String ARTICLE_COLLECT_LIST_URL = getBaseUrl().concat("article.article/collect_list");

    //订单列表
    public static final String ORDER_LIST_URL = getBaseUrl().concat("orders.orders/lists");

    //咨询服务
    public static final String CONSULT_TUTOR_URL = getBaseUrl().concat("tutor.tutor/reserve");

    //课程分类
    public static final String COURSE_CATEGORY_URL = getBaseUrl().concat("lesson.lesson_category/cat_list");

    //课程列表
    public static final String COURSE_LIST_URL = getBaseUrl().concat("lesson.lesson_chapter/cat_list");

    //情感测试列表
    public static final String TEST_LIST_URL = getBaseUrl().concat("psych.psych_test/index");

    //情感测试详情
    public static final String TEST_DETAIL_URL = getBaseUrl().concat("psych.psych_test/detail");

    //测试提交答案
    public static final String TEST_SUBMIT_ANSWER_URL = getBaseUrl().concat("psych.psych_test/answer");

    //测试记录
    public static final String TEST_RECORDS_URL = getBaseUrl().concat("psych.psych_test/records");

    //测试分类
    public static final String TEST_CATEGORY_URL = getBaseUrl().concat("psych.psych_test/cats");

    //导师评论列表
    public static final String TUTOR_COMMENT_LIST_URL = getBaseUrl().concat("tutor.tutor/comment_list");

    //导师详情页课程
    public static final String TUTOR_COURSE_DETAIL_URL = getBaseUrl().concat("tutor.tutor/lessons");

    //导师分类
    public static final String TUTOR_CATGORY_URL = getBaseUrl().concat("tutor.tutor/cats");

    //首页搜索相关
    public static final String INDEX_SEARCH_URL = getBaseUrl().concat("index/search");

    //导师证书
    public static final String TUTOR_CERTS_URL = getBaseUrl().concat("tutor.tutor/certs");

    //消息列表
    public static final String MESSAGE_LIST_URL = getBaseUrl().concat("notice.notice/announce");

    //用户感兴趣内容
    public static final String INTER_ALL_URL = getBaseUrl().concat("user.interested/all");

    //课程收藏或者取消
    public static final String COLLECT_COURSE_URL = getBaseUrl().concat("lesson.lesson_chapter/collect");

    //课程收藏列表
    public static final String COURSE_COLLECT_LIST_URL = getBaseUrl().concat("lesson.lesson_chapter/collect_list");


    //测试详情
    public static final String TEST_RECORD_DETAIL_URL = getBaseUrl().concat("psych.psych_test/records_detail");

    //导师服务详情
    public static final String TUTOR_SERVICE_DEATAIL_URL = getBaseUrl().concat("tutor.tutor/service_detail");

    //导师服务列表
    public static final String TUTOR_SERVICE_LIST_URL = getBaseUrl().concat("tutor.tutor/service");

    //重置密码
    public static final String RESET_PWD_URL = getBaseUrl().concat("user.user/resetPassword");

    //通知详情
    public static final String NOTIFICATION_DETAIL_URL = getBaseUrl().concat("notice.notice/detail");
    //秘技主页
    public static final String SKILL_MAIN_URL = getBaseUrl().concat("article.example/ts_category");


//    public static String download_apk_url = "http://toppic-mszs.oss-cn-hangzhou.aliyuncs.com/";

}

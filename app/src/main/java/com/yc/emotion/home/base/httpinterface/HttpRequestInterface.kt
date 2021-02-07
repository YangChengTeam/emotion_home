package com.yc.emotion.home.base.httpinterface

import com.yc.emotion.home.index.domain.bean.SmartChatItem
import com.yc.emotion.home.index.domain.bean.UserSeg
import com.yc.emotion.home.message.domain.bean.VideoItemInfoWrapper
import com.yc.emotion.home.mine.domain.bean.*
import com.yc.emotion.home.model.bean.*
import com.yc.emotion.home.model.bean.confession.ConfessionBean
import io.reactivex.Flowable
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.intellij.lang.annotations.Flow
import retrofit2.http.*
import yc.com.rthttplibrary.bean.ResultInfo
import java.util.*

/**
 *
 * Created by suns  on 2020/8/20 15:17.
 */
interface HttpRequestInterface {

    //首页
    @POST("index/index")
    fun getIndexInfo(): Flowable<ResultInfo<IndexInfo>>

    /**
     * 获取在线直播列表
     */
    @POST("live.room/lists")
    fun getOnlineLiveList(): Flowable<ResultInfo<LiveInfoWrapper>>

    /**
     * 获取视频直播列表
     */
    @POST("live.room/banners")
    fun getLiveVideoInfoList(): Flowable<ResultInfo<LiveVideoInfoWrapper>>

    @FormUrlEncoded
    @POST("lesson.lesson/click")
    fun statisticsLive(@Field("id") id: String?): Flowable<ResultInfo<String>>


    @FormUrlEncoded
    @POST("search.search/search")
    fun getAIChatContent(@FieldMap params: Map<String, String>): Flowable<ResultInfo<String>>


    @FormUrlEncoded
    @POST("article.article/detail")
    fun getArticleDetail(@Field("article_id") id: String, @Field("user_id") userId: String): Flowable<ResultInfo<LoveByStagesDetailsBean>>

    @FormUrlEncoded
    @POST
    fun articleCollect(@Field("user_id") userId: String, @Field("article_id") exampleId: String, @Url url: String): Flowable<ResultInfo<String>>


    @POST("article.article/category")
    fun getArticleTagInfos(): Flowable<ResultInfo<List<AticleTagInfo>>>

    @FormUrlEncoded
    @POST("article.article/cate_lists")
    fun getArticleInfoList(@Field("cat_id") cat_id: Int?, @Field("sex") sex: Int, @Field("page") page: Int, @Field("page_size") page_size: Int): Flowable<ResultInfo<List<ArticleDetailInfo>>>


    @FormUrlEncoded
    @POST("index/search")
    fun searchIndexInfo(@Field("keyword") keyword: String?, @Field("type") type: Int): Flowable<ResultInfo<IndexSearchInfo>>

    @FormUrlEncoded
    @POST("psych.psych_test/detail")
    fun getTestDetailInfo(@Field("user_id") user_id: String, @Field("test_id") test_id: String?): Flowable<ResultInfo<EmotionTestTopicInfo>>

    @FormUrlEncoded
    @POST("psych.psych_test/answer")
    fun submitAnswer(@FieldMap params: Map<String, String?>): Flowable<ResultInfo<EmotionTestInfo>>

    @POST("psych.psych_test/cats")
    fun getTestCategoryInfos(): Flowable<ResultInfo<CourseInfoWrapper>>

    @FormUrlEncoded
    @POST("psych.psych_test/index")
    fun getEmotionTestInfos(@Field("cat_id") catId: String?, @Field("page") page: Int, @Field("page_size") pageSize: Int): Flowable<ResultInfo<List<EmotionTestInfo>>>

    @FormUrlEncoded
    @POST("psych.psych_test/records")
    fun getTestRecords(@Field("user_id") userId: String, @Field("page") page: Int, @Field("page_size") page_size: Int): Flowable<ResultInfo<List<EmotionTestInfo>>>

    @FormUrlEncoded
    @POST("psych.psych_test/records_detail")
    fun getTestRecordDetail(@Field("record_id") record_id: String?): Flowable<ResultInfo<EmotionTestInfo>>

    @FormUrlEncoded
    @POST
    fun getExpressData(@Url url: String, @Field("id") id: String, @Field("page") page: Int, @Field("isrsa") isrsa: Boolean, @Field("iszip") iszip: Boolean): Observable<ConfessionBean>

    @FormUrlEncoded
    @POST
    fun netNormalData(@FieldMap requestMap: Map<String, String?>, @Url requestUrl: String, @Field("isrsa") isrsa: Boolean, @Field("iszip") iszip: Boolean): Observable<ImageCreateBean>

    @Multipart
    @POST
    fun netUpFileNet(@PartMap requestMap: MutableMap<String, RequestBody?>, @Part upFile: MultipartBody.Part, @Url requestUrl: String): Observable<ImageCreateBean>


    @FormUrlEncoded
    @POST("dialogue.dialogue/dropdown")
    fun getIndexDropInfos(@Field("keyword") keyword: String?): Flowable<ResultInfo<IndexHotInfoWrapper>>

    @FormUrlEncoded
    @POST("dialogue.dialogue/search")
    fun searchVerbalTalk(@Field("user_id") userId: String, @Field("keyword") keyword: String?, @Field("page") page: Int, @Field("page_size") pageSize: Int): Flowable<ResultInfo<SearchDialogueBean>>


    @FormUrlEncoded
    @POST("dialogue.dialogue/searchlog")
    fun searchCount(@Field("user_id") userId: String, @Field("keyword") keyword: String?): Flowable<ResultInfo<String>>

    @FormUrlEncoded
    @POST("dialogue.dialogue/category")
    fun loveCategory(@Field("sence") sence: String): Flowable<ResultInfo<List<LoveHealDateBean>>>

    @FormUrlEncoded
    @POST("dialogue.dialogue/lists")
    fun loveListCategory(@Field("user_id") userId: String, @Field("category_id") category_id: String?, @Field("page") page: Int, @Field("page_size") page_size: Int): Flowable<ResultInfo<List<LoveHealDetBean>>>

    @FormUrlEncoded
    @POST("article.example/detail")
    fun detailLoveCase(@Field("id") id: String, @Field("user_id") userId: String): Flowable<ResultInfo<LoveByStagesDetailsBean>>

    @FormUrlEncoded
    @POST
    fun collectLoveCase(@Field("user_id") userId: String, @Field("example_id") exampleId: String, @Url url: String): Flowable<ResultInfo<String>>

    @FormUrlEncoded
    @POST("article.example/lists")
    fun exampLists(@Field("user_id") userId: String, @Field("page") page: Int, @Field("page_size") pageSize: Int): Flowable<ResultInfo<ExampDataBean>>

    @FormUrlEncoded
    @POST
    fun recommendLovewords(@Field("user_id") userId: String?, @Field("page") page: String?, @Field("page_size") page_size: String?, @Url url: String?): Flowable<ResultInfo<List<LoveHealingBean>>>

    @FormUrlEncoded
    @POST
    fun collectLovewords(@Field("user_id") userId: String?, @Field("lovewords_id") lovewordsId: String?, @Url url: String?): Flowable<ResultInfo<String>>


    @POST("article.example/ts_category")
    fun exampleTsCategory(): Flowable<ResultInfo<ExampleTsCategory>>


    @POST("article.example/article_category")
    fun categoryArticle(): Flowable<ResultInfo<List<CategoryArticleBean>>>

    @FormUrlEncoded
    @POST("article.example/ts_lists")
    fun exampleTsList(@Field("category_id") id: String?, @Field("page") page: Int, @Field("page_size") pageSize: Int): Flowable<ResultInfo<ExampDataBean>>

    @FormUrlEncoded
    @POST
    fun listsArticle(@Field("category_id") categoryId: String?, @Field("page") page: Int, @Field("page_size") pageSize: Int, @Url url: String): Flowable<ResultInfo<List<LoveByStagesBean>>>

    @FormUrlEncoded
    @POST
    fun detailArticle(@Field("id") id: String, @Field("user_id") userId: String, @Url url: String): Flowable<ResultInfo<LoveByStagesDetailsBean>>

    @FormUrlEncoded
    @POST
    fun collectSkillArticle(@Field("user_id") userId: String, @Field("example_id") articleId: String?, @Url url: String): Flowable<ResultInfo<String>>


    @FormUrlEncoded
    @POST("lesson.lesson_chapter/lessons")
    fun getCourseInfo(@Field("chapter_id") chapter_id: String?, @Field("user_id") user_id: String): Flowable<ResultInfo<TutorCourseDetailInfo>>


    @FormUrlEncoded
    @POST("tutor.tutor/comment_list")
    fun getTutorCommentInfos(@Field("tutor_id") tutor_id: String?, @Field("page") page: Int, @Field("page_size") pageSize: Int): Flowable<ResultInfo<TutorCommentInfoWrapper>>


    @FormUrlEncoded
    @POST("lesson.lesson_chapter/collect")
    fun collectCourse(@Field("chapter_id") chapter_id: String?, @Field("user_id") userId: String): Flowable<ResultInfo<List<CourseInfo>>>


    @POST("lesson.lesson_category/cat_list")
    fun getCourseCategory(): Flowable<ResultInfo<ArrayList<CourseInfo>>>

    @FormUrlEncoded
    @POST("lesson.lesson_chapter/cat_list")
    fun getCourseList(@Field("cat_id") cat_id: String?): Flowable<ResultInfo<List<CourseInfo>>>

    @FormUrlEncoded
    @POST("tutor.tutor/lessons")
    fun getTutorCourseInfos(@Field("tutor_id") tutor_id: String?, @Field("page") page: Int, @Field("page_size") pageSize: Int): Flowable<ResultInfo<CourseInfoWrapper>>

    @FormUrlEncoded
    @POST("orders.orders/init")
    fun initOrders(@FieldMap params: Map<String, String>): Flowable<ResultInfo<OrdersInitBean>>

    @FormUrlEncoded
    @POST("tutor.tutor/desp")
    fun getTutorDetailInfo(@Field("tutor_id") tutor_id: String?): Flowable<ResultInfo<TutorDetailInfo>>

    @FormUrlEncoded
    @POST("tutor.tutor/service")
    fun getTutorServices(@Field("tutor_id") tutor_id: String?, @Field("page") page: Int, @Field("page_sie") page_sie: Int): Flowable<ResultInfo<List<TutorServiceInfo>>>

    @FormUrlEncoded
    @POST("tutor.tutor/service_detail")
    fun getTutorServiceDetailInfo(@Field("service_id") service_id: String?): Flowable<ResultInfo<TutorServiceDetailInfo>>


    @FormUrlEncoded
    @POST("tutor.tutor/certs")
    fun getApitudeInfo(@Field("tutor_id") tutor_id: String?): Flowable<ResultInfo<TutorInfoWrapper>>

    @POST("tutor.tutor/cats")
    fun getTutorCategory(): Flowable<ResultInfo<List<CourseInfo>>>


    @FormUrlEncoded
    @POST("tutor.tutor/index")
    fun getTutorListInfo(@Field("cat_id") catid: String?, @Field("page") page: Int, @Field("page_size") pageSize: Int): Flowable<ResultInfo<List<TutorInfo>>>

    @POST("notice.notice/announce")
    fun getMessageInfoList(): Flowable<ResultInfo<List<MessageInfo>>>

    @FormUrlEncoded
    @POST("notice.notice/detail")
    fun getNotificationDetail(@Field("id") id: String): Flowable<ResultInfo<MessageInfo>>

    @FormUrlEncoded
    @POST("lesson.lesson/index")
    fun getVideoItemInfos(@Field("page") page: Int, @Field("page_size") page_size: Int): Flowable<ResultInfo<VideoItemInfoWrapper>>

    @FormUrlEncoded
    @POST("video.video/click")
    fun statisticsVideo(@Field("id") id: String): Flowable<ResultInfo<String>>


    @FormUrlEncoded
    @POST("lesson.lesson_chapter/collect_list")
    fun getCourseCollectList(@Field("user_id") userId: String, @Field("page") page: Int, @Field("page_size") page_size: Int): Flowable<ResultInfo<List<CourseInfo>>>


    @FormUrlEncoded
    @POST("article.article/collect_list")
    fun getArticleCollectList(@Field("user_id") userId: String, @Field("page") page: Int, @Field("page_size") pageSize: Int): Flowable<ResultInfo<List<ArticleDetailInfo>>>


    @FormUrlEncoded
    @POST("live.user/login")
    fun liveLogin(@Field("username") username: String?, @Field("password") password: String?): Flowable<ResultInfo<LiveInfo>>


    @FormUrlEncoded
    @POST("live.room/create")
    fun createRoom(@Field("user_id") userId: String?): Flowable<ResultInfo<LiveInfo>>

    @FormUrlEncoded
    @POST("live.room/close")
    fun liveEnd(@Field("room_id") roomId: String?): Flowable<ResultInfo<String>>

    @FormUrlEncoded
    @POST("live.room/close_team")
    fun dismissGroup(@Field("group_id") group_id: String): Flowable<ResultInfo<String>>

    @FormUrlEncoded
    @POST("user.user/info")
    fun userInfo(@Field("user_id") userId: String): Flowable<ResultInfo<UserInfo>>

    @FormUrlEncoded
    @POST("user.user/update")
    fun updateUserInfo(@FieldMap params: Map<String, String?>): Flowable<ResultInfo<UserInfo>>


    @POST("user.interested/all")
    fun getUserInterseInfo(): Flowable<ResultInfo<List<UserInterInfo>>>

    @FormUrlEncoded
    @POST
    fun addSuggestion(@FieldMap params: Map<String, String?>, @Url url: String): Flowable<ResultInfo<String>>


    @FormUrlEncoded
    @POST("orders.orders/lists")
    fun getOrderList(@Field("user_id") userId: String, @Field("page") page: Int, @Field("page_size") pageSize: Int): Flowable<ResultInfo<List<OrderInfo>>>

    @FormUrlEncoded
    @POST("user.user/login")
    fun phoneLogin(@FieldMap params: Map<String, String?>): Flowable<ResultInfo<UserInfo>>


    @FormUrlEncoded
    @POST("user.user/send_code")
    fun sendCode(@Field("mobile") mobile: String?): Flowable<ResultInfo<String>>


    @FormUrlEncoded
    @POST("user.user/reg")
    fun phoneRegister(@FieldMap params: Map<String, String?>): Flowable<ResultInfo<UserInfo>>

    @FormUrlEncoded
    @POST("user.user/resetPassword")
    fun resetPwd(@Field("mobile") mobile: String?, @Field("code") code: String?, @Field("new_password") pwd: String?): Flowable<ResultInfo<String>>

    @FormUrlEncoded
    @POST("tutor.tutor/reserve")
    fun consultAppoint(@FieldMap params: Map<String, String?>): Flowable<ResultInfo<String>>

    @FormUrlEncoded
    @POST("user.user/snsLogin")
    fun thridLogin(@Field("token") access_token: String?, @Field("sns") account_type: Int, @Field("face") face: String?, @Field("sex") sex: String?, @Field("nick_name") nick_name: String?): Flowable<ResultInfo<UserInfo>>


    @FormUrlEncoded
    @POST("user.user/set_pwd")
    fun setPwd(@Field("user_id") user_id: String, @Field("password") pwd: String?): Flowable<ResultInfo<UserInfo>>

    @FormUrlEncoded
    @POST("user.user/edit_pwd")
    fun modifyPwd(@Field("user_id") user_id: String, @Field("password") pwd: String?, @Field("new_password") new_pwd: String?): Flowable<ResultInfo<UserInfo>>

    @FormUrlEncoded
    @POST("orders.goods/index")
    fun getGoodListInfo(@Field("user_id") userId: String): Flowable<ResultInfo<List<GoodsInfo>>>


    @FormUrlEncoded
    @POST("share.share/record")
    fun shareReward(@Field("user_id") userId: String?): Flowable<ResultInfo<String>>

    @FormUrlEncoded
    @POST("tutor.tutor/weixin")
    fun getWechatInfo(@FieldMap params: Map<String, String?>): Observable<ResultInfo<WetChatInfo>>

    @POST("topic.topic_cat/all")
    fun getCommunityTagInfos(): Flowable<ResultInfo<CommunityTagInfoWrapper>>


    @FormUrlEncoded
    @POST("topic.topic/newlist")
    fun getCommunityNewstInfos(@Field("user_id") userId: String, @Field("page") page: Int, @Field("page_size") pageSize: Int): Flowable<ResultInfo<CommunityInfoWrapper>>

    @FormUrlEncoded
    @POST("topic.topic/dig")
    fun likeTopic(@Field("user_id") user_id: String, @Field("topic_id") topic_id: String): Flowable<ResultInfo<String>>


    @FormUrlEncoded
    @POST("topic.topic/catlist")
    fun getCommunityTagListInfo(@Field("user_id") userId: String, @Field("cat_id") catId: String, @Field("page") page: Int, @Field("page_size") pageSize: Int): Flowable<ResultInfo<CommunityInfoWrapper>>


    @FormUrlEncoded
    @POST("topic.topic/hotlist")
    fun getCommunityHotList(@Field("user_id") userId: String, @Field("page") page: Int, @Field("page_size") pageSize: Int): Flowable<ResultInfo<CommunityInfoWrapper>>

    @FormUrlEncoded
    @POST("topic.topic/mylist")
    fun getMyCommunityInfos(@Field("user_id") userId: String, @Field("page") page: Int, @Field("page_size") pageSize: Int): Flowable<ResultInfo<CommunityInfoWrapper>>

    @FormUrlEncoded
    @POST("topic.topic/delete")
    fun deleteTopic(@Field("topic_id") comment_id: String?, @Field("user_id") userId: String): Flowable<ResultInfo<String>>

    @FormUrlEncoded
    @POST("topic.topic/detail")
    fun getCommunityDetailInfo(@Field("user_id") userId: String, @Field("topic_id") topicId: String?): Flowable<ResultInfo<CommunityDetailInfo>>


    @FormUrlEncoded
    @POST("topic.topic_comment/dig")
    fun commentLike(@Field("user_id") userId: String, @Field("comment_id") commentId: String): Flowable<ResultInfo<String>>


    @FormUrlEncoded
    @POST("topic.topic_comment/create")
    fun createComment(@Field("user_id") user_id: String, @Field("topic_id") topicId: String?, @Field("content") content: String?): Flowable<ResultInfo<String>>


    @POST("topic.topic/top")
    fun getTopTopicInfos(): Flowable<ResultInfo<TopTopicInfo>>

    @FormUrlEncoded
    @POST("topic.topic/post")
    fun publishCommunityInfo(@Field("user_id") userId: String, @Field("cat_id") cat_id: String?, @Field("content") content: String): Flowable<ResultInfo<String>>

    @FormUrlEncoded
    @POST("relation.relation/show")
    fun getRewardInfo(@Field("user_id") userId: String): Flowable<ResultInfo<RewardInfo>>

    @FormUrlEncoded
    @POST("relation.code/bind")
    fun bindInvitation(@Field("user_id") userId: String, @Field("code") code: String?): Flowable<ResultInfo<String>>

    @FormUrlEncoded
    @POST("relation.award_record/index")
    fun getRewardDetailInfo(@Field("user_id") userId: String, @Field("page") page: Int, @Field("page_size") pageSize: Int): Flowable<ResultInfo<List<RewardDetailInfo>>>

    @FormUrlEncoded
    @POST("relation.withdraw/apply")
    fun applyDispose(@Field("user_id") userId: String, @Field("amount") amount: String, @Field("alipay_number") alipay_number: String, @Field("truename") truename: String): Flowable<ResultInfo<String>>

    @FormUrlEncoded
    @POST("relation.withdraw/detail")
    fun getDisposeDetailInfoList(@Field("user_id") userId: String, @Field("page") page: Int, @Field("page_size") pageSize: Int): Flowable<ResultInfo<List<DisposeDetailInfo>>>


    @FormUrlEncoded
    @POST("share.share/detail")
    fun getShareLink(@Field("user_id") userId: String): Observable<ResultInfo<ShareInfo>>


    @FormUrlEncoded
    @POST("relation.withdraw/list")
    fun getRewardMoneyList(@Field("user_id") userId: String, @Field("page") page: Int, @Field("page_size") pageSize: Int): Flowable<ResultInfo<List<RewardDetailInfo>>>

    @FormUrlEncoded
    @POST("relation.code/rank")
    fun getRankingList(@Field("page") page: Int, @Field("page_size") pageSize: Int): Flowable<ResultInfo<List<RewardDetailInfo>>>

    @FormUrlEncoded
    @POST("search.search/new")
    fun smartSearchVerbal(@Field("s_key") keyword: String?, @Field("user_id") userId: String, @Field("section") section: Int): Flowable<ResultInfo<SmartChatItem>>

    @FormUrlEncoded
    @POST("search.search/favour")
    fun aiPraise(@Field("id") id: String?, @Field("user_id") userId: String): Flowable<ResultInfo<String>>

    @FormUrlEncoded
    @POST("search.search/collect")
    fun aiCollect(@Field("id") id: String?, @Field("user_id") userId: String): Flowable<ResultInfo<String>>

    @FormUrlEncoded
    @POST("search.search/collectList")
    fun getVerbalList(@Field("user_id") userId: String, @Field("page") page: Int, @Field("page_size") page_size: Int): Flowable<ResultInfo<List<LoveHealDetDetailsBean>>>

    @FormUrlEncoded
    @POST("relation.relation/cat")
    fun isBindInvitation(@Field("user_id") user_id: String): Flowable<ResultInfo<RewardInfo>>
}
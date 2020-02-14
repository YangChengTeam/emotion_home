package com.yc.emotion.home.model.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by suns  on 2019/10/10 08:42.
 */
public class TutorCourseDetailInfo implements MultiItemEntity {


    public static final int ITEM_TYPE_ONE = 1;//顶部 导师视频课程
    public static final int ITEM_TYPE_SECOND = 2;//导师课程目录
    public static final int ITEM_TYPE_THIRD = 3;//导师介绍
    public static final int ITEM_TYPE_FOUR = 4;//用户评价



    public TutorCommentInfo communityInfo;//学员评论


    public int itemType;
    /**
     * chapter : {"chapter_id":28,"tutor_id":5,"chapter_price":"0.00","chapter_image":"http://qg.bshu.com/attach/20191023/5b6c089d44d8817046b54cb30983b56a.png","study_people":135,"lesson_total":10,"cat_id":"31","chapter_title":"吸引力塑造","chapter_content":"<html><head><meta charset=\"utf-8\" /><meta content=\"yes\" name=\"apple-mobile-web-app-capable\" /><meta content=\"yes\" name=\"apple-touch-fullscreen\" /><meta content=\"telephone=no,email=no\" name=\"format-detection\" /><meta name=\"App-Config\"  content=\"fullscreen=yes,useHistoryState=yes,transition=yes\" /><meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no\" /><style> html,body{overflow:hidden; font-size:16px; line-height: 1.6;} img { width:100%; height:auto; overflow:hidden;}<\/style><\/head><body><p>第01节&nbsp; 基础知识：第一印象<br/>第02节&nbsp; 基础知识：认识女孩的渠道<br/>第03节&nbsp; 基础知识：目标障碍僚机<br/>第04节&nbsp; 基础知识：购买度<\/p><p>第05节&nbsp; 基础知识：上钩点<br/>第06节&nbsp; 基础知识：社交认证<br/>第07节&nbsp; 基础知识：兴趣指标<br/>第08节&nbsp; 基础知识：生存与繁衍价值<br/>第09节&nbsp; 基础知识：男女社交直觉的不同<br/>第10节&nbsp; 基础知识：关系升级<br/>第11节&nbsp; 基础知识：进入私密空间<br/>第12节&nbsp; 基础知识：私密空间的建设<br/>第13节&nbsp; 基础知识：反荡妇机制<br/>第14节&nbsp; 基础知识：性后预期管理<br/>第15节&nbsp; 男女关系5种定位：怪叔叔<br/>第16节&nbsp; <span style=\"white-space: normal;\">男女关系5种定位<\/span>：好人<br/>第17节&nbsp; <span style=\"white-space: normal;\">男女关系5种定位<\/span>：潜在婚姻对象<br/>第18节&nbsp;&nbsp;<span style=\"white-space: normal;\">男女关系5种定位<\/span>：情人<br/>第19节&nbsp; <span style=\"white-space: normal;\">男女关系5种定位<\/span>：男女朋友<br/>第20节&nbsp; 总结什么样的关系有利于你<br/>第21节&nbsp; 解读女人的5种情绪：入门<br/>第22节&nbsp;&nbsp;<span style=\"white-space: normal;\">解读女人的5种情绪：兴趣初阶<br/><\/span>第23节&nbsp;&nbsp;<span style=\"white-space: normal;\">解读女人的5种情绪：兴趣进阶<br/>第24节&nbsp;&nbsp;<span style=\"white-space: normal;\">解读女人的5种情绪：吸引<br/>第25节&nbsp;&nbsp;<span style=\"white-space: normal;\">解读女人的5种情绪：联系感<br/>第26节&nbsp;&nbsp;<span style=\"white-space: normal;\">解读女人的5种情绪：性拉力<\/span><br/><\/span><\/span><\/span><\/p><\/body><\/html>","tutor_name":"胜羽","tutor_face":"http://qg.bshu.com/ueditor/php/up/file/20191015/1571133280227644.jpg","goods_id":235,"m_price":0,"price":0}
     * lessons : [{"lesson_id":9,"chapter_id":28,"lesson_title":"第01节  基础知识：第一印象","lesson_desp":"第一印象是男女交往中最重要的指标之一，在某种程度上来说，男生无法主动地去改变女生对自己的第一印象。第一印象主要体现在两方面：外在形象和初步印象","lesson_image":"http://qg.bshu.com/attach/20191023/2b41b1bef3b7eabee86ec27874b570cc.png","lesson_url":"http://voice.wk2.com/qgzj/T01/%E7%AC%AC01%E8%8A%82%20%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86%EF%BC%9A%E7%AC%AC%E4%B8%80%E5%8D%B0%E8%B1%A1l.mp4","need_pay":0},{"lesson_id":10,"chapter_id":28,"lesson_title":"第02节  基础知识：认识女孩的渠道","lesson_desp":"选择你想要的女孩类型，掌握主动权。在不同的环境下选择不同的搭讪方式来主动认识你想要认识的女生，是一种阳光的、积极的、健康的生活方式","lesson_image":"http://qg.bshu.com/attach/20191023/0e074f29798ae3be981561eebdcfdc59.png","lesson_url":"http://voice.wk2.com/qgzj/T01/%E7%AC%AC02%E8%8A%82%20%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86%EF%BC%9A%E8%AE%A4%E8%AF%86%E5%A5%B3%E5%AD%A9%E7%9A%84%E6%B8%A0%E9%81%93.mp4","need_pay":0},{"lesson_id":11,"chapter_id":28,"lesson_title":"第03节  基础知识：目标障碍僚机","lesson_desp":"目标、障碍以及僚机。目标指你所中意的女生，障碍则是指目标周围一切会干扰你们交流的人或因素，而僚机通常指与你同行的朋友、同事以及一切可以为你与女生的交流带来积极影响的人","lesson_image":"http://qg.bshu.com/attach/20191023/4a1ea396c086bb1827225a1d0cb093dc.png","lesson_url":"http://voice.wk2.com/qgzj/T01/%E7%AC%AC03%E8%8A%82%20%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86%EF%BC%9A%E7%9B%AE%E6%A0%87%E7%9A%84%E5%83%9A%E6%9C%BA.mp4","need_pay":0},{"lesson_id":12,"chapter_id":28,"lesson_title":"第04节  基础知识：购买度","lesson_desp":"购买度是指女生此时此刻对你表现出的兴趣，是在交往初期衡量是否有交往潜质的重要指标。我们在初次交往的过程中应该尽量尝试提升女生对我们的购买度","lesson_image":"http://qg.bshu.com/attach/20191023/8f9f034da6e9059720b29f555f4cb01a.png","lesson_url":"http://voice.wk2.com/qgzj/T01/%E7%AC%AC04%E8%8A%82%20%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86%EF%BC%9A%E8%B4%AD%E4%B9%B0%E5%BA%A6.mp4","need_pay":0},{"lesson_id":13,"chapter_id":28,"lesson_title":"第05节  基础知识：上钩点","lesson_desp":"交往中，女生对你的购买度以及兴趣度达到一个峰值的时候，接下来你和她会有更多的时间和空间展开互动，这个时间点就称之为上钩点，这是从朋友关系到两性关系转变的一个十分重要的点","lesson_image":"http://qg.bshu.com/attach/20191023/54966d23ff2fd6bf8b587a87510b8787.png","lesson_url":"http://voice.wk2.com/qgzj/T01/%E7%AC%AC05%E8%8A%82%20%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86%EF%BC%9A%E4%B8%8A%E9%92%A9%E7%82%B9.mp4","need_pay":0},{"lesson_id":14,"chapter_id":28,"lesson_title":"第06节  基础知识：社交认证","lesson_desp":"社交认证是一个男生在社交中的硬实力，是女生判断男生是否存在社会价值以及繁衍价值的嘴重要指标。具体指，人在社交场所中，别人对他的认可度、知名度以及价值的集中体现","lesson_image":"http://qg.bshu.com/attach/20191023/cdb7769cfaec4f2ca0fa76634cc23c66.jpg","lesson_url":"http://voice.wk2.com/qgzj/T01/%E7%AC%AC06%E8%8A%82%20%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86%EF%BC%9A%E7%A4%BE%E4%BA%A4%E8%AE%A4%E8%AF%81.mp4","need_pay":0},{"lesson_id":15,"chapter_id":28,"lesson_title":"第07节  基础知识：兴趣指标","lesson_desp":"判断一个女生是否对你感兴趣，可以从女生身上的一些细微的行为判断。正确判定女生对你的兴趣指标以及无兴趣指标，可以有利于在交往中对女生建立更多的吸引","lesson_image":"http://qg.bshu.com/attach/20191023/5e04886ec9785ae11415fe369550b08f.jpg","lesson_url":"http://voice.wk2.com/qgzj/T01/%E7%AC%AC07%E8%8A%82%20%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86%EF%BC%9A%E5%85%B4%E8%B6%A3%E6%8C%87%E6%A0%87%E4%B8%8E%E6%97%A0%E5%85%B4%E8%B6%A3%E6%8C%87%E6%A0%87.mp4","need_pay":0},{"lesson_id":16,"chapter_id":28,"lesson_title":"第08节  基础知识：生存与繁衍价值","lesson_desp":"生存价值以及繁衍价值是吸引力的根源，二者结合就能构成吸引力这一属性。换言之，这种原始的吸引力即是激发人类选择本能的基础属性，现代社会生存价值很繁衍价值的体现也在随着时代而改变","lesson_image":"http://qg.bshu.com/attach/20191023/e52f5608213f2d607e5cb1367d2e6b69.png","lesson_url":"http://voice.wk2.com/qgzj/T01/%E7%AC%AC08%E8%8A%82%20%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86%EF%BC%9A%E7%94%9F%E5%AD%98%E4%BB%B7%E5%80%BC%20%E7%B9%81%E8%A1%8D%E4%BB%B7%E5%80%BC.mp4","need_pay":0},{"lesson_id":17,"chapter_id":28,"lesson_title":"第09节  基础知识：男女社交直觉的不同","lesson_desp":"社交直觉，泛指人与人之间在社交行为上的解读，而解读方法因人而异。男女社交直觉的差异受成长经历及人生阅历影响，而女生会被哪种类型的男生吸引也受到了社交直觉的影响","lesson_image":"http://qg.bshu.com/attach/20191023/39710350e748b3e8c43d958c9931679d.png","lesson_url":"http://voice.wk2.com/qgzj/T01/%E7%AC%AC9%E8%8A%82%20%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86%EF%BC%9A%E7%94%B7%E5%A5%B3%E7%A4%BE%E4%BA%A4%E7%9B%B4%E8%A7%89%E7%9A%84%E4%B8%8D%E5%90%8C.mp4","need_pay":0},{"lesson_id":18,"chapter_id":28,"lesson_title":"第10节  基础知识：关系升级","lesson_desp":"交往过程中，在建立了良好兴趣之后，关系升级直接影响你和女生未来关系的发展方向。升级分为关系升级以及身体接触上的升级，良性的关系升级可以帮助你在与女生交往中获得优势","lesson_image":"http://qg.bshu.com/attach/20191023/8cdf02ac44bcd4613eea8e7f1ed470aa.png","lesson_url":"http://voice.wk2.com/qgzj/T01/%E7%AC%AC10%E8%8A%82%20%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86%EF%BC%9A%E5%8D%87%E7%BA%A7.mp4","need_pay":0}]
     */




    public TutorCourseDetailInfo(int itemType) {
        this.itemType = itemType;
    }

    public TutorCourseDetailInfo() {
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    /**
     * chapter : {"chapter_id":13,"tutor_id":5,"chapter_price":"0.00","chapter_image":"http://qg.bshu.com/attach/20191023/cdb7769cfaec4f2ca0fa76634cc23c66.jpg","study_people":100,"lesson_total":0,"cat_id":"13","chapter_title":"01.基础知识：第一印象","chapter_content":"<html><head><meta charset=\"utf-8\" /><meta content=\"yes\" name=\"apple-mobile-web-app-capable\" /><meta content=\"yes\" name=\"apple-touch-fullscreen\" /><meta content=\"telephone=no,email=no\" name=\"format-detection\" /><meta name=\"App-Config\"  content=\"fullscreen=yes,useHistoryState=yes,transition=yes\" /><meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no\" /><style> html,body{overflow:hidden; font-size:16px; line-height: 1.6;} img { width:100%; height:auto; overflow:hidden;}<\/style><\/head><body><p>第一印象是男女交往中最重要的指标之一，在某种程度上来说，男生无法主动地去改变女生对自己的第一印象。第一印象主要体现在两方面：外在形象和初步印象<\/p><\/body><\/html>"}
     * lessons : {"lesson_id":7,"chapter_id":13,"lesson_title":"01.基础知识：第一印象","lesson_desp":"第一印象是男女交往中最重要的指标之一，在某种程度上来说，男生无法主动地去改变女生对自己的第一印象。第一印象主要体现在两方面：外在形象和初步印象","lesson_image":"http://qg.bshu.com/attach/20191023/cdb7769cfaec4f2ca0fa76634cc23c66.jpg","lesson_url":"http://wk2-voice.oss-cn-shenzhen.aliyuncs.com/qgzj/T01/%E7%AC%AC01%E8%8A%82%20%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86%EF%BC%9A%E7%AC%AC%E4%B8%80%E5%8D%B0%E8%B1%A1l.mp4","need_pay":0}
     * goods : {"name":"01.基础知识：第一印象","type_id":2,"type_relate_val":13,"app_id":0,"img":"http://qg.bshu.com/attach/20191023/cdb7769cfaec4f2ca0fa76634cc23c66.jpg","desp":"","price":"0.00","m_price":"0.00","vip_price":"0.00","unit":"","use_time_limit":0,"goods_id":216}
     */

    private CourseInfo chapter;
    private List<LessonInfo> lessons;
    private GoodsInfo goods;

    private TutorInfo tutors;

    public CourseInfo getChapter() {
        return chapter;
    }

    public void setChapter(CourseInfo chapter) {
        this.chapter = chapter;
    }

    public List<LessonInfo> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonInfo> lessons) {
        this.lessons = lessons;
    }

    public GoodsInfo getGoods() {
        return goods;
    }

    public void setGoods(GoodsInfo goods) {
        this.goods = goods;
    }


    public TutorInfo getTutors() {
        return tutors;
    }

    public void setTutors(TutorInfo tutors) {
        this.tutors = tutors;
    }
}

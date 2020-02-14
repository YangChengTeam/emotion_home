package com.yc.emotion.home.model.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suns  on 2019/10/11 16:42.
 */
public class EmotionTestTopicInfo {

    //题目id
    private String id;


    public EmotionTestTopicInfo() {
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }


    /**
     * test_info : {"test_id":44,"subject":"找不到恋人？测你的恋爱标准正确吗","desp":"对于爱情，有些人是有自己的一套标准的，只有达到了自己想要的标准线，才有可能获得自己的爱情。但是，你用这样的标准去要求恋情，却找不到对象，是标准有问题了吗？下面，跟小编做一道爱情测试看看吧。","image":"http://qg.bshu.com/attach/20191023/8f9f034da6e9059720b29f555f4cb01a.png","need_pay":0,"people":125}
     * question_list : [{"question_id":239,"question":"1：你喜欢在自己家里和朋友聚会吗？","test_id":44,"sort":500,"image":"","qid":1,"options":[{"option_id":1321,"question_id":239,"option_text":"　　A：喜欢 ","image":"","score":0,"re_qid":2,"test_id":44,"aid":0},{"option_id":1322,"question_id":239,"option_text":"　　B：一般 ","image":"","score":0,"re_qid":3,"test_id":44,"aid":0},{"option_id":1323,"question_id":239,"option_text":"　　C：不喜欢 ","image":"","score":0,"re_qid":4,"test_id":44,"aid":0}]},{"question_id":240,"question":"2：如果经济条件允许，你会去国外看偶像的演唱会吗？","test_id":44,"sort":500,"image":"","qid":2,"options":[{"option_id":1324,"question_id":240,"option_text":"　　A：会 ","image":"","score":0,"re_qid":3,"test_id":44,"aid":0},{"option_id":1325,"question_id":240,"option_text":"　　B：不确定 ","image":"","score":0,"re_qid":4,"test_id":44,"aid":0},{"option_id":1326,"question_id":240,"option_text":"　　C：不会 ","image":"","score":0,"re_qid":5,"test_id":44,"aid":0}]},{"question_id":241,"question":"3：你开车或坐车的时候喜欢听歌吗？","test_id":44,"sort":500,"image":"","qid":3,"options":[{"option_id":1327,"question_id":241,"option_text":"　　A：喜欢 ","image":"","score":0,"re_qid":4,"test_id":44,"aid":0},{"option_id":1328,"question_id":241,"option_text":"　　B：一般 ","image":"","score":0,"re_qid":5,"test_id":44,"aid":0},{"option_id":1329,"question_id":241,"option_text":"　　C：不喜欢 ","image":"","score":0,"re_qid":6,"test_id":44,"aid":0}]},{"question_id":242,"question":"4：你会因为别人说你胖就吃减肥药吗？","test_id":44,"sort":500,"image":"","qid":4,"options":[{"option_id":1330,"question_id":242,"option_text":"　　A：会 ","image":"","score":0,"re_qid":5,"test_id":44,"aid":0},{"option_id":1331,"question_id":242,"option_text":"　　B：不确定 ","image":"","score":0,"re_qid":6,"test_id":44,"aid":0},{"option_id":1332,"question_id":242,"option_text":"　　C：不会 ","image":"","score":0,"re_qid":7,"test_id":44,"aid":0}]},{"question_id":243,"question":"5：你觉得自己适合上夜班吗？","test_id":44,"sort":500,"image":"","qid":5,"options":[{"option_id":1333,"question_id":243,"option_text":"　　A：适合 ","image":"","score":0,"re_qid":6,"test_id":44,"aid":0},{"option_id":1334,"question_id":243,"option_text":"　　B：一般 ","image":"","score":0,"re_qid":7,"test_id":44,"aid":0},{"option_id":1335,"question_id":243,"option_text":"　　C：不适合 ","image":"","score":0,"re_qid":8,"test_id":44,"aid":0}]},{"question_id":244,"question":"6：朋友带你一起去见相亲对象，你会跟朋友说你的真实意见吗？","test_id":44,"sort":500,"image":"","qid":6,"options":[{"option_id":1336,"question_id":244,"option_text":"　　A：会 ","image":"","score":0,"re_qid":7,"test_id":44,"aid":0},{"option_id":1337,"question_id":244,"option_text":"　　B：不确定 ","image":"","score":0,"re_qid":8,"test_id":44,"aid":0},{"option_id":1338,"question_id":244,"option_text":"　　C：不会 ","image":"","score":0,"re_qid":9,"test_id":44,"aid":0}]},{"question_id":245,"question":"7：你会为了丰厚的收益做自己不喜欢的工作吗？","test_id":44,"sort":500,"image":"","qid":7,"options":[{"option_id":1339,"question_id":245,"option_text":"　　A：会 ","image":"","score":0,"re_qid":8,"test_id":44,"aid":0},{"option_id":1340,"question_id":245,"option_text":"　　B：不确定 ","image":"","score":0,"re_qid":9,"test_id":44,"aid":0},{"option_id":1341,"question_id":245,"option_text":"　　C：不会 ","image":"","score":0,"re_qid":1,"test_id":44,"aid":0}]},{"question_id":246,"question":"8：家里的日用品你会经常买不同的牌子吗？","test_id":44,"sort":500,"image":"","qid":8,"options":[{"option_id":1342,"question_id":246,"option_text":"　　A：会 ","image":"","score":0,"re_qid":9,"test_id":44,"aid":0},{"option_id":1343,"question_id":246,"option_text":"　　B：要看心情 ","image":"","score":0,"re_qid":10,"test_id":44,"aid":0},{"option_id":1344,"question_id":246,"option_text":"　　C：不会 ","image":"","score":0,"re_qid":11,"test_id":44,"aid":0}]},{"question_id":247,"question":"9：你买自己不熟悉的商品，会征询父母的意见吗？","test_id":44,"sort":500,"image":"","qid":9,"options":[{"option_id":1345,"question_id":247,"option_text":"　　A：会 ","image":"","score":0,"re_qid":10,"test_id":44,"aid":0},{"option_id":1346,"question_id":247,"option_text":"　　B：不确定 ","image":"","score":0,"re_qid":11,"test_id":44,"aid":0},{"option_id":1347,"question_id":247,"option_text":"　　C：不会 ","image":"","score":0,"re_qid":12,"test_id":44,"aid":0}]},{"question_id":248,"question":"10：朋友问你借钱，你会要求写借条吗？","test_id":44,"sort":500,"image":"","qid":10,"options":[{"option_id":1348,"question_id":248,"option_text":"　　A：会 ","image":"","score":0,"re_qid":11,"test_id":44,"aid":0},{"option_id":1349,"question_id":248,"option_text":"　　B：不确定 ","image":"","score":0,"re_qid":12,"test_id":44,"aid":0},{"option_id":1350,"question_id":248,"option_text":"　　C：不会 ","image":"","score":0,"re_qid":0,"test_id":44,"aid":3}]},{"question_id":249,"question":"11：如果到一个新公司，你会请新同事吃饭拉近关系吗？","test_id":44,"sort":500,"image":"","qid":11,"options":[{"option_id":1351,"question_id":249,"option_text":"　　A：会 ","image":"","score":0,"re_qid":12,"test_id":44,"aid":0},{"option_id":1352,"question_id":249,"option_text":"　　B：看心情 ","image":"","score":0,"re_qid":0,"test_id":44,"aid":1},{"option_id":1353,"question_id":249,"option_text":"　　C：不会 ","image":"","score":0,"re_qid":0,"test_id":44,"aid":2}]},{"question_id":250,"question":"12：你喜欢直接按照明星的发型来做头发吗？","test_id":44,"sort":500,"image":"","qid":12,"options":[{"option_id":1354,"question_id":250,"option_text":"　　A：喜欢 ","image":"","score":0,"re_qid":0,"test_id":44,"aid":4},{"option_id":1355,"question_id":250,"option_text":"　　B：一般 ","image":"","score":0,"re_qid":0,"test_id":44,"aid":2},{"option_id":1356,"question_id":250,"option_text":"　　C：不喜欢 ","image":"","score":0,"re_qid":0,"test_id":44,"aid":3}]}]
     */

    private EmotionTestInfo test_info;
    private ArrayList<QuestionInfo> question_list;

    public EmotionTestInfo getTest_info() {
        return test_info;
    }

    public void setTest_info(EmotionTestInfo test_info) {
        this.test_info = test_info;
    }

    public ArrayList<QuestionInfo> getQuestion_list() {
        return question_list;
    }

    public void setQuestion_list(ArrayList<QuestionInfo> question_list) {
        this.question_list = question_list;
    }


}

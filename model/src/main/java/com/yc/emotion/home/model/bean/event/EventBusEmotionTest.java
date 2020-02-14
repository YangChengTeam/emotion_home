package com.yc.emotion.home.model.bean.event;

import com.yc.emotion.home.model.bean.QuestionInfo;

/**
 * Created by suns  on 2019/10/11 17:50.
 */
public class EventBusEmotionTest {

    public QuestionInfo emotionTestTopicInfo;

    public EventBusEmotionTest(QuestionInfo emotionTestTopicInfo) {
        this.emotionTestTopicInfo = emotionTestTopicInfo;
    }
}

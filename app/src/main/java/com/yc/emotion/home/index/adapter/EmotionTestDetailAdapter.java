package com.yc.emotion.home.index.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yc.emotion.home.R;
import com.yc.emotion.home.model.bean.EmotionTestTopicInfo;
import com.yc.emotion.home.model.bean.QuestionInfo;
import com.yc.emotion.home.model.bean.event.EventBusEmotionTest;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suns  on 2019/10/11 15:11.
 */
public class EmotionTestDetailAdapter extends PagerAdapter {

    private List<QuestionInfo> mEmotionTestTopicInfos;
    private Context mContext;

    public EmotionTestDetailAdapter(Context context, List<QuestionInfo> emotionTestTopicInfos) {
        this.mContext = context;
        this.mEmotionTestTopicInfos = emotionTestTopicInfos;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        QuestionInfo questionInfo = mEmotionTestTopicInfos.get(position);

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_emotion_test_detail_item, null);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_emotion_test_detail);


        List<QuestionInfo> newTestTopicInfos = new ArrayList<>();


        questionInfo.setType(QuestionInfo.ITEM_TYPE_TOPIC);
        newTestTopicInfos.add(questionInfo);

        List<QuestionInfo> options = questionInfo.getOptions();

        if (options != null && options.size() > 0) {
            for (QuestionInfo option : options) {
                option.setQuestion_id(questionInfo.getQuestion_id());
                option.setType(QuestionInfo.ITEM_TYPE_ANSWER);
                newTestTopicInfos.add(option);
            }
        }


        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        EmotionTestDetailAnswerAdapter emotionTestDetailAdapter = new EmotionTestDetailAnswerAdapter(newTestTopicInfos);

        recyclerView.setAdapter(emotionTestDetailAdapter);

        emotionTestDetailAdapter.setOnItemClickListener((adapter, view1, position1) -> {

            QuestionInfo item = emotionTestDetailAdapter.getItem(position1);
            if (item != null) {
                if (item.getType() == QuestionInfo.ITEM_TYPE_ANSWER) {
                    View itemView = emotionTestDetailAdapter.getItemView(position1);
                    if (itemView != null)
                        itemView.setSelected(true);
                    EventBus.getDefault().post(new EventBusEmotionTest(item));

                }
            }
        });


        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return mEmotionTestTopicInfos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

}

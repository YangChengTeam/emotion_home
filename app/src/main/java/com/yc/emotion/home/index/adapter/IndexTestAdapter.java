package com.yc.emotion.home.index.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.yc.emotion.home.R;
import com.yc.emotion.home.index.ui.activity.EmotionTestDescActivity;
import com.yc.emotion.home.model.bean.EmotionTestInfo;

import java.util.List;

/**
 * Created by suns  on 2019/9/28 10:57.
 */
public class IndexTestAdapter extends PagerAdapter {

    private List<EmotionTestInfo> emotionTestInfos;
    private Context mContext;

    public IndexTestAdapter(Context context, List<EmotionTestInfo> emotionTestInfoList) {
        this.mContext = context;
        this.emotionTestInfos = emotionTestInfoList;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        EmotionTestInfo emotionTestInfo = emotionTestInfos.get(position % emotionTestInfos.size());
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_index_test, null);

        ImageView imageView = view.findViewById(R.id.image);
        TextView tvTestTitle = view.findViewById(R.id.tv_test_title);
        TextView tvTestCount = view.findViewById(R.id.tv_test_count);
        CardView cardView = view.findViewById(R.id.cardView_emotion_test);
        tvTestTitle.setText(emotionTestInfo.getTitle());
        tvTestCount.setText(String.format(mContext.getString(R.string.test_count), emotionTestInfo.getPeople()));

        cardView.setOnClickListener(v -> EmotionTestDescActivity.Companion.startActivity(mContext, emotionTestInfo.getId()));

//        Log.e("TAG", "instantiateItem: "+imageUrl );
        Glide.with(container.getContext()).load(emotionTestInfo.getImg()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA).circleCrop()).into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return emotionTestInfos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

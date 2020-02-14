package com.yc.emotion.home.index.ui.widget;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk.utils.ScreenUtil;
import com.yc.emotion.home.R;
import com.yc.emotion.home.index.adapter.TutorCoursePpAdapter;
import com.yc.emotion.home.model.bean.LessonInfo;
import com.yc.emotion.home.base.domain.engine.LoveEngine;
import com.yc.emotion.home.base.ui.popwindow.BasePopwindow;
import com.yc.emotion.home.utils.UserInfoHelper;

import java.util.List;

/**
 * Created by suns  on 2019/10/8 17:12.
 * 导师课时popwindow
 */
public class TutorCoursePopwindow extends BasePopwindow {

    private TutorCoursePpAdapter tutorCoursePpAdapter;
    private LoveEngine mLoveEngine;
    private int popupHeight;
    private int popupWidth;
    private ImageView ivCourseClose;
    private TextView tvPPtotal;

    public TutorCoursePopwindow(Activity context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.tutor_course_popwindow;
    }

    @Override
    public void init() {
        mLoveEngine = new LoveEngine(mContext);


        RecyclerView recyclerView = rootView.findViewById(R.id.rcv_course_pp);

        tvPPtotal = rootView.findViewById(R.id.tv_course_pp_total);

        ivCourseClose = rootView.findViewById(R.id.iv_course_pp_close);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);

        recyclerView.setLayoutManager(linearLayoutManager);

        tutorCoursePpAdapter = new TutorCoursePpAdapter(null);

        recyclerView.setAdapter(tutorCoursePpAdapter);

        //获取自身的长宽高
        rootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupHeight = rootView.getMeasuredHeight();
        popupWidth = rootView.getMeasuredWidth();

//        getData();

        initListener();
    }


    public void setCourseData(List<LessonInfo> lessonInfos) {
        tvPPtotal.setText("共" + lessonInfos.size() + "课");
        tutorCoursePpAdapter.setNewData(lessonInfos);
    }


    private void initListener() {
        tutorCoursePpAdapter.setOnItemClickListener((adapter, view, position) -> {

            LessonInfo lessonInfo = tutorCoursePpAdapter.getItem(position);
            if (lessonInfo != null) {
                if (onTagSelectListener != null) {
                    if (position == 0 || !UserInfoHelper.Companion.getInstance().goToLogin(mContext))
                        onTagSelectListener.onTagSelect(lessonInfo);
                }
                dismiss();
            }

        });
        ivCourseClose.setOnClickListener(v -> dismiss());

    }

    /**
     * 设置显示在v上方(以v的左边距为开始位置)
     *
     * @param v
     */
    public void showUp(View v) {
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //在控件上方显示
        Log.e("TAG", popupHeight + "---" + location[1] + "--" + v.getMeasuredHeight());

        showAtLocation(v, Gravity.NO_GRAVITY, location[0], ScreenUtil.getHeight(mContext) / 2 + 85);

    }


    private onTagSelectListener onTagSelectListener;

    public void setOnTagSelectListener(TutorCoursePopwindow.onTagSelectListener onTagSelectListener) {
        this.onTagSelectListener = onTagSelectListener;
    }

    public interface onTagSelectListener {
        void onTagSelect(LessonInfo lessonInfo);
    }


}

package com.yc.emotion.home.message.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.video.player.lib.base.BaseVideoController;
import com.video.player.lib.base.IMediaPlayer;
import com.video.player.lib.constants.VideoConstants;
import com.video.player.lib.controller.VideoWindowController;
import com.video.player.lib.manager.VideoPlayerManager;
import com.video.player.lib.utils.Logger;
import com.yc.emotion.home.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by suns  on 2020/8/6 10:54.
 */
public class MyVideoController extends BaseVideoController implements SeekBar.OnSeekBarChangeListener {
    private View mBottomBarLayout, mErrorLayout, mMobileLayout, mVideoStart;

    private ProgressBar mBottomProgressBar, mProgressBar;
    private SeekBar mSeekBar;

    //用户是否手指正在持续滚动
    private boolean isTouchSeekBar = false;
    //实时播放位置
    private long mOldPlayProgress;

    public MyVideoController(@NonNull Context context) {
        this(context, null);
    }

    public MyVideoController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyVideoController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.video_my_controller_layout, this);


        mBottomBarLayout = findViewById(R.id.video_bottom_tab);
        mErrorLayout = findViewById(R.id.error_layout);
        mMobileLayout = findViewById(R.id.mobile_layout);

        View btnResetPlay = findViewById(R.id.video_btn_reset_play);


        mBottomProgressBar = findViewById(R.id.bottom_progress);
        mProgressBar = (ProgressBar) findViewById(R.id.video_loading);
        mSeekBar = (SeekBar) findViewById(R.id.video_seek_progress);
        mVideoStart = findViewById(R.id.video_start);
        OnClickListener onClickListener = v -> {
            int id = v.getId();
            if (id == R.id.error_layout) {
                IMediaPlayer.getInstance().reStartVideoPlayer(mOldPlayProgress);
//                PIMediaPlayer.getInstance().reStartVideoPlayer(mOldPlayProgress);
            } else if (id == R.id.video_btn_reset_play) {
                VideoPlayerManager.getInstance().setMobileWorkEnable(true);
                IMediaPlayer.getInstance().reStartVideoPlayer(0);
//                PIMediaPlayer.getInstance().reStartVideoPlayer(0);
            } else if (id == R.id.video_full_window) {
                if (null != mOnFuctionListener) {
                    mOnFuctionListener.onStartGlobalWindown(
                            new VideoWindowController(getContext()), true);
                }
            } else if (id == R.id.video_start) {
//                IMediaPlayer.getInstance().play();
                VideoPlayerManager.getInstance().playOrPause();
            }
        };
        mErrorLayout.setOnClickListener(onClickListener);
        btnResetPlay.setOnClickListener(onClickListener);
        mVideoStart.setOnClickListener(onClickListener);

        mSeekBar.setOnSeekBarChangeListener(this);
    }

    /**
     * 负责控制器显示、隐藏
     */
    private Runnable controllerRunnable = new Runnable() {
        @Override
        public void run() {
            if (null != mBottomBarLayout) {
                mBottomBarLayout.setVisibility(INVISIBLE);
            }
            if (null != mBottomProgressBar) {
                mBottomProgressBar.setVisibility(VISIBLE);
            }
        }
    };

    //=========================================SEEK BAR=============================================

    /**
     * 用户手指持续拨动中
     *
     * @param seekBar
     * @param progress 实时进度
     * @param fromUser 是否来自用户手动拖动
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            long durtion = VideoPlayerManager.getInstance().getDurtion();
        }
        if (stateListener != null) {
            stateListener.onProgressChanged();
        }
    }

    /**
     * 手指按下
     *
     * @param seekBar
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isTouchSeekBar = true;
        removeCallbacks(View.VISIBLE);
    }

    /**
     * 手指松开
     *
     * @param seekBar
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        isTouchSeekBar = false;
        //只有非暂停下才恢复控制器显示隐藏规则
        if (VideoPlayerManager.getInstance().getVideoPlayerState() != VideoConstants.MUSIC_PLAYER_PAUSE) {
            changeControllerState(mScrrenOrientation, false);
        }
        //跳转至某处
        long duration = VideoPlayerManager.getInstance().getDurtion();
        if (duration > 0) {
            long currentTime = seekBar.getProgress() * duration / 100;
            VideoPlayerManager.getInstance().seekTo(currentTime);
        }
    }

    //=========================================播放状态=============================================

    /**
     * 开始播放器准备中
     */
    @Override
    public void readyPlaying() {

        removeCallbacks(View.INVISIBLE);
        if (stateListener != null) {
            stateListener.onPrepare();
        }

        Log.e(TAG, "readyPlaying: " );
        //常规、全屏
        updateVideoControllerUI(View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE,
                View.INVISIBLE);

    }


    /**
     * 开始缓冲中
     */
    @Override
    public void startBuffer() {
        Log.e(TAG, "startBuffer: " );
        //removeCallbacks(View.INVISIBLE)
        updateVideoControllerUI(View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE,
                View.INVISIBLE);

    }

    /**
     * 缓冲结束
     */
    @Override
    public void endBuffer() {


        if (mScrrenOrientation == VideoConstants.SCREEN_ORIENTATION_PORTRAIT) {
            updateVideoControllerUI(View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE,
                    View.INVISIBLE);
        } else if (mScrrenOrientation == VideoConstants.SCREEN_ORIENTATION_WINDOW) {
            updateVideoControllerUI(View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE,
                    View.INVISIBLE);
        } else {
            updateVideoControllerUI(View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE,
                    View.INVISIBLE);
        }

    }

    /**
     * 已开始
     */
    @Override
    public synchronized void play() {

        Log.e(TAG, "play: " );
//        mVideoStart.setVisibility(GONE);
        if (mScrrenOrientation == VideoConstants.SCREEN_ORIENTATION_WINDOW) {
            updateVideoControllerUI(View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE,
                    View.INVISIBLE);
        } else if (mScrrenOrientation == VideoConstants.SCREEN_ORIENTATION_PORTRAIT) {
            updateVideoControllerUI(View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE,
                    View.INVISIBLE);
        }

        if (stateListener != null) {
            stateListener.onPlay();
        }
    }

    /**
     * 已暂停
     * tips:播放器组件处理了暂停时若处在小窗口播放，则停止播放,故此处无需处理小窗口事件
     */
    @Override
    public void pause() {
//        mVideoStart.setVisibility(View.VISIBLE);
        removeCallbacks(View.VISIBLE);
        if (mScrrenOrientation == VideoConstants.SCREEN_ORIENTATION_WINDOW) {
            updateVideoControllerUI(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                    View.INVISIBLE);
        } else if (mScrrenOrientation == VideoConstants.SCREEN_ORIENTATION_PORTRAIT) {
            updateVideoControllerUI(View.VISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                    View.INVISIBLE);
        }
        if (stateListener != null) {
            stateListener.onPause();
        }
    }

    /**
     * 恢复播放
     * tips:播放器组件处理了暂停时若处在小窗口播放，则停止播放,故此处无需处理小窗口事件
     */
    @Override
    public void repeatPlay() {

        mVideoStart.setVisibility(View.VISIBLE);
        if (mScrrenOrientation == VideoConstants.SCREEN_ORIENTATION_WINDOW) {
            updateVideoControllerUI(View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE,
                    View.INVISIBLE);
        } else if (mScrrenOrientation == VideoConstants.SCREEN_ORIENTATION_PORTRAIT) {
            updateVideoControllerUI(View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE,
                    View.INVISIBLE);
        }
        changeControllerState(mScrrenOrientation, false);
    }

    /**
     * 移动网络环境下工作
     */
    @Override
    public void mobileWorkTips() {

        removeCallbacks(View.INVISIBLE);
        updateVideoControllerUI(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                View.VISIBLE);
    }

    /**
     * 播放失败
     * tips:播放器组件处理了播放失败时若处在小窗口播放，则停止播放,故此处无需处理小窗口事件
     *
     * @param errorCode
     * @param errorMessage
     */
    @Override
    public void error(int errorCode, String errorMessage) {

        mVideoStart.setVisibility(View.VISIBLE);
        removeCallbacks(View.INVISIBLE);
        updateVideoControllerUI(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE,
                View.INVISIBLE);
    }

    /**
     * 还原所有状态
     */
    @Override
    public void reset() {

        removeCallbacks(View.INVISIBLE);
        updateVideoControllerUI(View.VISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                View.INVISIBLE);

        if (null != mSeekBar) {
            mSeekBar.setSecondaryProgress(0);
            mSeekBar.setProgress(0);
        }
        if (null != mBottomProgressBar) {
            mBottomProgressBar.setSecondaryProgress(0);
            mBottomProgressBar.setProgress(0);
        }


    }


    @Override
    public void complete() {
        removeCallbacks(View.INVISIBLE);
        updateVideoControllerUI(View.VISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                View.INVISIBLE);
        if (null != mSeekBar) {
            mSeekBar.setSecondaryProgress(0);
            mSeekBar.setProgress(0);
        }
        if (null != mBottomProgressBar) {
            mBottomProgressBar.setSecondaryProgress(0);
            mBottomProgressBar.setProgress(0);
        }


        if (stateListener != null) {
            stateListener.onPlayComplete();
        }
    }

    /**
     * 横屏模式开启,默认展开控制器
     */
    @Override
    public void startHorizontal() {

        changeControllerState(VideoConstants.SCREEN_ORIENTATION_FULL, false);
    }

    /**
     * 开启小窗口播放
     */
    @Override
    public void startTiny() {

        removeCallbacks(View.INVISIBLE);
        updateVideoControllerUI(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                View.INVISIBLE);
    }

    /**
     * 开启全局悬浮窗播放
     */
    @Override
    public void startGlobalWindow() {
        removeCallbacks(View.INVISIBLE);

        updateVideoControllerUI(View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE,
                View.INVISIBLE);
    }


    /**
     * 跳转至某处尝试开始播放中
     */
    @Override
    public void startSeek() {

        //小窗口
        updateVideoControllerUI(View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE,
                View.INVISIBLE);
    }


    /**
     * 播放进度
     *
     * @param totalDurtion   视频总长度 单位：毫秒，暂停下为-1
     * @param currentDurtion 播放进度 单位：毫秒，暂停下为-1
     * @param bufferPercent  缓冲进度，单位：百分比
     */
    @Override
    public void onTaskRuntime(long totalDurtion, long currentDurtion, int bufferPercent) {
        Logger.d("播放实时进度", "onTaskRuntime-->totalDurtion:" + totalDurtion
                + ",currentDurtion:" + currentDurtion);
        if (totalDurtion > -1) {
            mOldPlayProgress = currentDurtion;

            //得到当前进度
            int progress = (int) (((float) currentDurtion / totalDurtion) * 100);
            if (null != mSeekBar) {
                if (bufferPercent >= 100 && mSeekBar.getSecondaryProgress() < bufferPercent) {
                    mSeekBar.setSecondaryProgress(bufferPercent);
                }
                if (!isTouchSeekBar) {
                    mSeekBar.setProgress(progress);
                }
            }
        }
    }

    /**
     * 实时播放和缓冲进度，子线程更新
     *
     * @param totalPosition   总视频时长，单位：毫秒
     * @param currentPosition 实施播放进度，单位：毫秒
     * @param bufferPercent   缓冲进度，单位：百分比
     */
    @Override
    protected void currentPosition(long totalPosition, long currentPosition, int bufferPercent) {
        if (null != mBottomProgressBar && currentPosition > -1) {
            //播放进度，这里比例是1/1000
            int progress = (int) (((float) currentPosition / totalPosition) * 1000);
            mBottomProgressBar.setProgress(progress);
            //缓冲进度
            if (null != mBottomProgressBar && mBottomProgressBar.getSecondaryProgress() < (bufferPercent * 10)) {
                mBottomProgressBar.setSecondaryProgress(bufferPercent * 10);
            }
        }
    }

    /**
     * 缓冲进度
     *
     * @param percent 实时缓冲进度，单位：百分比
     */
    @Override
    public void onBufferingUpdate(int percent) {
        Logger.d("onBufferingUpdate", "percent-->" + percent);
        if (null != mSeekBar && mSeekBar.getSecondaryProgress() < 100) {
            mSeekBar.setSecondaryProgress(percent);
        }
    }

    /**
     * 更新控制器
     *
     * @param start          开始
     * @param loading        加载中
     * @param bottomProgress 底部进度条
     * @param errorLayout    失败图层
     * @param mobileLayout   移动网络提示图层
     */
    private void updateVideoControllerUI(int start, int loading, int bottomProgress, int errorLayout,
                                         int mobileLayout) {

        Log.e(TAG, "updateVideoControllerUI: start->" + start + " :loading->" + loading);
        if (null != mVideoStart) {
            mVideoStart.setVisibility(start);
        }
        if (null != mProgressBar) {
            mProgressBar.setVisibility(loading);
        }

        //如果是显示底部进度条，当底部控制器显示时不应该显示底部进度条
        if (null != mBottomProgressBar) {
            if (bottomProgress == View.VISIBLE) {
                //仅当底部控制器处于非显示状态下，才显示底部进度条
                if (null != mBottomBarLayout && mBottomBarLayout.getVisibility() != VISIBLE) {
                    mBottomProgressBar.setVisibility(bottomProgress);
                }
            } else {
                mBottomProgressBar.setVisibility(bottomProgress);
            }
        }
        if (null != mErrorLayout) {
            mErrorLayout.setVisibility(errorLayout);
        }

        if (null != mMobileLayout) {
            mMobileLayout.setVisibility(mobileLayout);
        }
    }

    /**
     * 移除定时器，保留在最后的状态
     *
     * @param visible 最后的状态
     */
    private void removeCallbacks(int visible) {
        removeCallbacks(controllerRunnable);
        if (null != mBottomBarLayout) {
            mBottomBarLayout.setVisibility(visible);
        }

        if (View.INVISIBLE == visible) {
            mBottomProgressBar.setVisibility(VISIBLE);
        }
    }

    /**
     * 显示、隐藏 控制器 上下交互功能区
     * 手动点击，根据播放状态自动处理，手势交互处理等状态
     *
     * @param screenOrientation 当前的窗口方向
     * @param isInterceptIntent 为true：用户主动点击
     */

    @Override
    public void changeControllerState(int screenOrientation, boolean isInterceptIntent) {
        if (null == mBottomBarLayout) return;
        Logger.d(TAG, "changeControllerState-->" + screenOrientation
                + ",isInterceptIntent:" + isInterceptIntent);
        //小窗口样式不处理任何事件
        if (screenOrientation == VideoConstants.SCREEN_ORIENTATION_TINY) {
            return;
        }
        //重复显示
        if (isInterceptIntent && mBottomBarLayout.getVisibility() == View.VISIBLE) {
            removeCallbacks(View.INVISIBLE);
            return;
        }
        //移除已post的事件
        removeCallbacks(controllerRunnable);
        if (mBottomBarLayout.getVisibility() != View.VISIBLE) {
            mBottomBarLayout.setVisibility(View.VISIBLE);
        }

        if (null != mBottomProgressBar) {
            mBottomProgressBar.setVisibility(View.INVISIBLE);
        }

        int videoState = VideoPlayerManager.getInstance().getVideoPlayerState();


        if (videoState == VideoConstants.MUSIC_PLAYER_START) {
            IMediaPlayer.getInstance().pause();
//            PIMediaPlayer.getInstance().pause();

        } else if (videoState == VideoConstants.MUSIC_PLAYER_PAUSE) {
            IMediaPlayer.getInstance().play();
//            PIMediaPlayer.getInstance().play();
        }

        postDelayed(controllerRunnable, 5000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mBottomBarLayout = null;
        mErrorLayout = null;
        mMobileLayout = null;
        mVideoStart = null;

        mBottomProgressBar = null;
        mProgressBar = null;
        mSeekBar = null;
        isTouchSeekBar = false;
        mOldPlayProgress = 0;
    }


    private OnStateListener stateListener;

    public void setOnStateListener(OnStateListener stateListener) {
        this.stateListener = stateListener;
    }

    public interface OnStateListener {
        void onPrepare();

        void onPlay();

        void onPause();

        void onPlayComplete();

        void onProgressChanged();
    }
}

package com.yc.emotion.home.index.ui.activity


import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.TextView

import com.kk.utils.ScreenUtil
import com.music.player.lib.bean.MusicInfo
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.index.adapter.AudioMainAdapter
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.index.ui.widget.AudioFilterPopwindow
import com.yc.emotion.home.index.presenter.LoveAudioPresenter
import com.yc.emotion.home.index.view.LoveAudioView
import com.yc.emotion.home.model.bean.AudioDataInfo
import kotlinx.android.synthetic.main.activity_main_audio.*


/**
 * Created by wanglin  on 2018/1/10 17:18.
 */

open class AudioActivity : BaseSameActivity(), View.OnClickListener, LoveAudioView {


    private var itemPage = 1

    private val pageSize = 10


    private var audioMainAdapter: AudioMainAdapter? = null


    private var cateId: String? = null
    private var order = 1//按时间排序


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main_audio
    }


    override fun initViews() {

        mPresenter = LoveAudioPresenter(this, this)
        tv_select_time.isSelected = true
        init()

    }

    fun init() {

        audioMainAdapter = AudioMainAdapter(null)
        recyclerView_sleep.layoutManager = LinearLayoutManager(this)
        recyclerView_sleep.adapter = audioMainAdapter


        recyclerView_sleep.addItemDecoration(MyDecoration())
        initData()
        initListener()

    }

    private fun initData() {


//        CommonInfoHelper.getO(this, "audio_main_data", object : TypeReference<List<MusicInfo>>() {
//
//        }.type, CommonInfoHelper.onParseListener<List<MusicInfo>> { datas ->
//            if (datas != null && itemPage == 1) {
//                createNewData(datas, true)
//            }
//        })

        (mPresenter as? LoveAudioPresenter)?.getAudioListCache()


        getData()

    }

    private fun getData() {

        (mPresenter as? LoveAudioPresenter)?.getLoveItemList(cateId, itemPage, pageSize, order)

    }


    private fun createNewData(datas: List<MusicInfo>?, isCache: Boolean) {
        if (itemPage == 1) {
            audioMainAdapter?.setNewData(datas)
            //            mCacheWorker.setCache("audio_main_data", datas);

        } else {
            datas?.let {
                audioMainAdapter?.addData(datas)

            }
        }

        if (datas != null && datas.size == pageSize && !isCache) {
            audioMainAdapter?.loadMoreComplete()
            itemPage++
        } else {
            audioMainAdapter?.loadMoreEnd()
        }


    }


    private fun initListener() {
        tv_select_time.setOnClickListener(this)
        tv_select_hot.setOnClickListener(this)
        tv_select_filter.setOnClickListener(this)
        audioMainAdapter?.setOnLoadMoreListener({ this.getData() }, recyclerView_sleep)

        audioMainAdapter?.setOnItemClickListener { adapter, view, position ->
            val item = audioMainAdapter?.getItem(position)

            if (item != null) {
                MobclickAgent.onEvent(this, "audio_play_id", "音频播放")
                val intent = Intent(this, LoveAudioDetailActivity::class.java)

                intent.putExtra("type_id", item.id)
                intent.putExtra("spa_id", item.id)
                startActivity(intent)

            }
        }

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.red_crimson))
        swipeRefreshLayout.setOnRefreshListener {
            itemPage = 1
            getData()
        }

    }

    override fun offerActivityTitle(): String {
        return "撩爱音频"
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_select_time -> {
                resetState()
                tv_select_time.isSelected = true
                startAnimation(tv_select_time)
                itemPage = 1
                order = 1
                getData()
            }
            R.id.tv_select_hot -> {
                resetState()
                tv_select_hot.isSelected = true
                startAnimation(tv_select_hot)
                itemPage = 1
                order = 2
                getData()
            }
            R.id.tv_select_filter -> {
                val filterPopwindow = AudioFilterPopwindow(this)
                filterPopwindow.showAsDropDown(rl_select_container)
                filterPopwindow.setOnItemClickListener(object :AudioFilterPopwindow.onItemClickListener{
                    override fun onItemClick(audioDataInfo: AudioDataInfo?) {
                        if (audioDataInfo != null) {
                            cateId = audioDataInfo.id
                            itemPage = 1
                            getData()
                        }
                    }
                })
            }
        }
    }

    private fun resetState() {
        tv_select_hot.isSelected = false
        tv_select_time.isSelected = false
    }


    private inner class MyDecoration : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View,
                                    parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.set(0, 0, 0, ScreenUtil.dip2px(this@AudioActivity, 10f))
        }

    }

    public override fun onDestroy() {
        super.onDestroy()
        itemPage = 1
    }

    private fun startAnimation(textView: TextView) {
        val scaleAnimation = ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        val alphaAnimation = AlphaAnimation(0f, 1f)
        val animationSet = AnimationSet(false)
        animationSet.addAnimation(scaleAnimation)
        animationSet.addAnimation(alphaAnimation)
        animationSet.duration = 100
        textView.startAnimation(animationSet)

        //
        //        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1f);
        //        valueAnimator.setTarget(textView);
        //        valueAnimator.setDuration(300).start();
        //        valueAnimator.addUpdateListener(animation -> {
        //
        //            textView.getBackground().setAlpha((Integer) animation.getAnimatedValue());
        //        });


    }

    override fun onComplete() {
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }

    override fun onError() {
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }

    override fun showAudioListInfo(list: List<MusicInfo>, b: Boolean) {
        createNewData(list, b)
    }
}

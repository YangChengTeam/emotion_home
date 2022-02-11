package com.yc.emotion.home.mine.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.adapter.CommonMainPageAdapter
import com.yc.emotion.home.base.ui.widget.ColorFlipPagerTitleView
import com.yc.emotion.home.mine.domain.bean.DisposeDetailInfo
import com.yc.emotion.home.mine.domain.bean.RewardDetailInfo
import com.yc.emotion.home.mine.domain.bean.RewardInfo
import com.yc.emotion.home.mine.presenter.RewardPresenter
import com.yc.emotion.home.mine.ui.fragment.BindInvitationFragment
import com.yc.emotion.home.mine.ui.fragment.RankingListFragment
import com.yc.emotion.home.mine.ui.fragment.RewardMoneyFragment
import com.yc.emotion.home.mine.ui.fragment.StrategyFragment
import com.yc.emotion.home.mine.view.RewardView
import com.yc.emotion.home.model.bean.event.EventPayVipSuccess
import com.yc.emotion.home.pay.ui.activity.BecomeVipActivityNew
import com.yc.emotion.home.utils.UserInfoHelper
import com.yc.emotion.home.utils.clickWithTrigger
import kotlinx.android.synthetic.main.activity_reward.*
import kotlinx.android.synthetic.main.fragment_bind_invitation.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.math.abs

/**
 *
 * Created by suns  on 2020/8/22 11:08.
 */
class RewardActivity : BaseActivity(), RewardView {

    private var isCanReward = false

    private var scaledTouchSlop: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_reward
    }


    override fun initViews() {
        mPresenter = RewardPresenter(this, this)
        invadeStatusBar() //侵入状态栏
        setAndroidNativeLightStatusBar() //状态栏字体颜色改变
        scaledTouchSlop = ViewConfiguration.get(this).scaledTouchSlop

        val userInfo = UserInfoHelper.instance.getUserInfo()

        Glide.with(this).load(userInfo?.face).error(R.mipmap.main_icon_default_head).circleCrop().into(iv_reward_face)
        if (!TextUtils.isEmpty(userInfo?.nick_name)) {
            tv_reward_name.text = userInfo?.nick_name
        } else {
            tv_reward_name.text = "普通用户"
        }
        (mPresenter as RewardPresenter).isBindInvitation()
        netSwitchPagerData()
        getData()
        initListener()
        initViewAnim()
    }


    private fun initViewAnim() {
        val viewList = arrayListOf<ImageView>()
        viewList.add(iv_reward300_1)
        viewList.add(iv_reward300_2)
        viewList.add(iv_reward_98)
        viewList.add(iv_reward78)
        viewList.add(iv_reward58)
        viewList.add(iv_reward38)


        startAnimation1(viewList)
    }

    private fun netSwitchPagerData() {

        val arrays = resources.getStringArray(R.array.reward_array)

        val titleList = listOf(*arrays)

        initNavigator(titleList)
        val fragmentList = arrayListOf<Fragment>()
        fragmentList.add(StrategyFragment())
        fragmentList.add(RewardMoneyFragment())
        fragmentList.add(RankingListFragment())


        val commonMainPageAdapter = CommonMainPageAdapter(supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, titleList, fragmentList)

        reward_viewpager.adapter = commonMainPageAdapter
    }

    private fun initNavigator(titleList: List<String>?) {
        val commonNavigator = CommonNavigator(this)
        //        commonNavigator.setScrollPivotX(0.65f);
        commonNavigator.isAdjustMode = true
        val navigatorAdapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return titleList?.size ?: 0
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = ColorFlipPagerTitleView(context)
                simplePagerTitleView.text = titleList?.get(index)
                simplePagerTitleView.textSize = 12f
                simplePagerTitleView.normalColor = resources.getColor(R.color.orange_ffe5bf)
                simplePagerTitleView.selectedColor = resources.getColor(R.color.orange_ffe5bf)

                simplePagerTitleView.setOnClickListener { v -> reward_viewpager.currentItem = index }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                //                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.lineHeight = UIUtil.dip2px(context, 2.0).toFloat()
                indicator.lineWidth = UIUtil.dip2px(context, 30.0).toFloat()
                indicator.roundRadius = UIUtil.dip2px(context, 3.0).toFloat()
                indicator.startInterpolator = AccelerateInterpolator()
                indicator.endInterpolator = DecelerateInterpolator(2.0f)
                indicator.setColors(resources.getColor(R.color.white))
                return indicator
            }

        }
        reward_viewpager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {

            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                resetNavigator(commonNavigator)
                //
                val pagerTitleView = commonNavigator.getPagerTitleView(position) as SimplePagerTitleView
                pagerTitleView.textSize = 14f
                pagerTitleView.typeface = Typeface.DEFAULT_BOLD
            }
        })
        commonNavigator.adapter = navigatorAdapter
        reward_pager_tabs.navigator = commonNavigator
        ViewPagerHelper.bind(reward_pager_tabs, reward_viewpager)
    }

    private fun resetNavigator(commonNavigator: CommonNavigator) {
        val titleContainer = commonNavigator.titleContainer
        val childCount = titleContainer.childCount
        for (i in 0 until childCount) {
            val pagerTitleView = titleContainer.getChildAt(i) as SimplePagerTitleView
            pagerTitleView.typeface = Typeface.DEFAULT

            //            pagerTitleView.setTextSize(20);
        }

    }


    private fun getData() {
        (mPresenter as RewardPresenter).getRewardInfo()

    }

    private fun initListener() {
        tv_reward_sub_title.clickWithTrigger {
            startActivity(Intent(this, EarningsDetailActivity::class.java))
        }
        tv_dispose.clickWithTrigger {
            if (isCanReward) {
                MobclickAgent.onEvent(this, "dispose_click", "提现点击")
                WithdrawalApplyActivity.startActivity(this, money)
            } else {
                BecomeVipActivityNew.startActivity(this, true)
                MobclickAgent.onEvent(this, "reward_vip_click", "赚现金VIP点击")
            }
        }
        iv_bind_invitation.clickWithTrigger {
            val bindInvitationFragment = BindInvitationFragment()
            bindInvitationFragment.show(supportFragmentManager, "")
        }

        iv_reward_back.clickWithTrigger {
            finish()
        }


    }

    private var startY = 0f
    private var isMoved = false
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {


        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                startY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                val endY = ev.y
                if (abs(endY - startY) > scaledTouchSlop && !isMoved && !hasBind) {
                    isMoved = true
                    startOutTranslation(iv_bind_invitation)
                }
            }
            MotionEvent.ACTION_UP -> {
                if (isMoved && !hasBind) {
                    startInTranslation(iv_bind_invitation)
                    isMoved = false
                }
            }
        }

        return super.dispatchTouchEvent(ev)
    }


    private fun startOutTranslation(iv: ImageView) {

        val translation = TranslateAnimation(0f, iv.width - 130f, 0f, 0f)
        translation.duration = 500
        translation.interpolator = LinearInterpolator()
        translation.repeatCount = 0
        translation.fillAfter = true
        iv.startAnimation(translation)
    }

    private fun startInTranslation(iv: ImageView) {
        val translation = TranslateAnimation(iv.width - 130f, 0f, 0f, 0f)
        translation.duration = 500
        translation.interpolator = LinearInterpolator()
        translation.repeatCount = 0
        translation.fillAfter = true
        iv.startAnimation(translation)
    }

    private fun startAnimation1(ivList: List<ImageView>) {

        for (i in ivList.indices) {
            val iv = ivList[i]

            val rotate = RotateAnimation(0f, 20f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            val lin = AccelerateInterpolator()
            rotate.interpolator = lin
            rotate.duration = 200 //设置动画持续周期

            rotate.repeatCount = -1 //设置重复次数

            rotate.fillAfter = false //动画执行完后是否停留在执行完的状态
            rotate.startOffset = (i * 400).toLong()
            rotate.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    startAnimation2(iv)
                }

                override fun onAnimationStart(animation: Animation?) {

                }

            })

            iv.animation = rotate
            rotate.start()
        }


    }

    private fun startAnimation2(iv: ImageView) {
        val rotate = RotateAnimation(0f, -40f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        val lin = AccelerateInterpolator()
        rotate.interpolator = lin
        rotate.duration = 200 //设置动画持续周期

        rotate.repeatCount = -1 //设置重复次数


        rotate.fillAfter = false //动画执行完后是否停留在执行完的状态
        rotate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                if (iv == iv_reward38) {
                    initViewAnim()
                }
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })

        iv.animation = rotate

        rotate.start()
    }


    private var money: Float = 0f
    override fun showRewardInfo(data: RewardInfo?) {
        data?.let {
            if (it.has_permission == 1) {//拥有权限

                money = it.money
                if (money > 0) {
                    isCanReward = true

                    iv_plan_icon.visibility = View.VISIBLE
                    tv_balance.text = "可提现金额：${it.money.toInt()}元"
                    tv_reward_money.text = "可提现金额${it.money.toInt()}元"
                    tv_dispose.text = "提现"
                } else {
                    iv_plan_icon.visibility = View.GONE
                    tv_dispose.visibility = View.GONE
                    tv_reward_money.text = "暂无可提现金额，快去邀请好友吧~"
                }

                tv_invitation.text = "邀请码：${data.code}"

                tv_reward_sub_title.visibility = View.VISIBLE
            } else {
                isCanReward = false
                iv_plan_icon.visibility = View.GONE
                tv_reward_money.text = "成为VIP合伙人，获得分销资格~"
                tv_dispose.text = "立即开通"

                tv_reward_sub_title.visibility = View.GONE
                tv_invitation.visibility = View.GONE
                tv_balance.visibility = View.GONE
            }


        }
    }

    override fun showBindSuccess() {

    }

    override fun showRewardDetailList(data: List<RewardDetailInfo>?) {

    }

    override fun showDisposeApplySuccess() {

    }

    override fun showDisposeDetailInfoList(data: List<DisposeDetailInfo>?) {

    }

    override fun showRewardMoneyList(data: List<RewardDetailInfo>?) {

    }

    override fun showRankingList(data: List<RewardDetailInfo>?) {

    }

    private var hasBind = false
    override fun showBindInvitation(data: RewardInfo?) {
        data?.let {
            hasBind = it.has_bind == 1
            iv_bind_invitation.visibility = if (it.has_bind == 1) View.GONE else View.VISIBLE
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPaySuccess(eventPayVipSuccess: EventPayVipSuccess) {
        getData()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onBindSuccess(str: String) {
        if (TextUtils.equals("bind_success", str)) {
            tv_bind_code.visibility = View.GONE
        } else if (TextUtils.equals("dispose_success", str)) {
            getData()
        }
    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


}
package com.yc.emotion.home.index.ui.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kk.utils.ScreenUtil
import com.yc.emotion.home.R
import com.yc.emotion.home.base.domain.engine.MySubscriber
import com.yc.emotion.home.base.domain.engine.OrderEngine
import com.yc.emotion.home.base.ui.activity.PayActivity
import com.yc.emotion.home.base.ui.adapter.CommonMainPageAdapter
import com.yc.emotion.home.base.ui.widget.ColorFlipPagerTitleView
import com.yc.emotion.home.base.ui.widget.LoadDialog
import com.yc.emotion.home.index.presenter.TutorPresenter
import com.yc.emotion.home.index.ui.fragment.TutorServiceDetailDescFragment
import com.yc.emotion.home.index.ui.fragment.TutorServiceDetailQuestionFragment
import com.yc.emotion.home.index.ui.fragment.VipPayWayFragment
import com.yc.emotion.home.index.view.TutorView
import com.yc.emotion.home.mine.ui.activity.ShareActivity
import com.yc.emotion.home.model.bean.AResultInfo
import com.yc.emotion.home.model.bean.GoodsInfo
import com.yc.emotion.home.model.bean.OrdersInitBean
import com.yc.emotion.home.model.bean.TutorServiceDetailInfo
import com.yc.emotion.home.model.bean.event.EventBusWxPayResult
import com.yc.emotion.home.model.bean.event.EventPayVipSuccess
import com.yc.emotion.home.utils.UserInfoHelper
import kotlinx.android.synthetic.main.activity_tutor_service_detail.*
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
import java.util.*

/**
 *
 * Created by suns  on 2019/10/12 17:59.
 */
class TutorServiceDetailActivity : PayActivity(), TutorView {


    private var serviceId: String? = null
    private var tutorId: String? = null

    private var mOrderEngine: OrderEngine? = null

    companion object {
        fun startActivity(context: Context, service_id: String?, tutorId: String?) {
            val intent = Intent(context, TutorServiceDetailActivity::class.java)
            intent.putExtra("service_id", service_id)
            intent.putExtra("tutor_id", tutorId)
            context.startActivity(intent)
        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        initViews()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_tutor_service_detail
    }


    override fun initViews() {
        invadeStatusBar() //侵入状态栏
        setAndroidNativeLightStatusBar() //状态栏字体颜色改变
        mOrderEngine = OrderEngine(this)

        mPresenter = TutorPresenter(this, this)

        serviceId = intent?.getStringExtra("service_id")

        tutorId = intent?.getStringExtra("tutor_id")

        initHScrollView()//初始化评价横向scrollview

        getServiceDetail()//初始化viewpager
        initToolbar()
        initListener()
    }


    private fun initToolbar() {
        supportActionBar?.hide()
        setSupportActionBar(toolbar)

        collapsingToolbarLayout.isTitleEnabled = false
        collapsingToolbarLayout.expandedTitleGravity = Gravity.CENTER//设置展开后标题的位置
        collapsingToolbarLayout.collapsedTitleGravity = Gravity.CENTER//设置收缩后标题的位置
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE)//设置展开后标题的颜色
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE)//设置收缩后标题的颜色
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { p0, verticalOffset ->
            //verticalOffset  当前偏移量 appBarLayout.getTotalScrollRange() 最大高度 便宜值
            val offset = Math.abs(verticalOffset) //目的是将负数转换为绝对正数；
            //标题栏的渐变
            toolbar.setBackgroundColor(changeAlpha(resources.getColor(R.color.white)
                    , Math.abs(verticalOffset * 1.0f) / appBarLayout.totalScrollRange))


            /**
             * 当前最大高度便宜值除以2 在减去已偏移值 获取浮动 先显示在隐藏
             */
            if (offset < appBarLayout.totalScrollRange / 2) {
                toolbar.title = ""
                toolbar.alpha = (appBarLayout.totalScrollRange / 2 - offset * 1.0f) / (appBarLayout.totalScrollRange / 2)
                activity_base_same_tv_title.visibility = View.GONE
                toolbar.setNavigationIcon(R.mipmap.icon_arr_lift_translate)


                /**
                 * 从最低浮动开始渐显 当前 Offset就是  appBarLayout.getTotalScrollRange() / 2
                 * 所以 Offset - appBarLayout.getTotalScrollRange() / 2
                 */
            } else if (offset > appBarLayout.totalScrollRange / 2) {
                val floate = (offset - appBarLayout.totalScrollRange / 2) * 1.0f / (appBarLayout.totalScrollRange / 2)
                toolbar.alpha = floate
                toolbar.setNavigationIcon(R.mipmap.icon_arr_lift_black)
                activity_base_same_tv_title.visibility = View.VISIBLE
//                toolbar.title = "导师详情"
            }
        })
    }


    private fun initListener() {
        rl_free_consult.setOnClickListener { showToWxServiceDialog(tutorId = tutorId) }
        tv_tutor_service_detail_comment_all.setOnClickListener { startActivity(Intent(this, TutorServiceCommentDetailActivity::class.java)) }
        tv_tutor_service_detail_homepage.setOnClickListener {
            startActivity(Intent(this, TutorDetailActivity::class.java))
            finish()
        }
        iv_tutor_back.setOnClickListener { finish() }
        toolbar.setNavigationOnClickListener { finish() }
        iv_service_share.setOnClickListener { startActivity(Intent(this, ShareActivity::class.java)) }
        rl_buy_service.setOnClickListener {
            val vipPayWayFragment = VipPayWayFragment()
            vipPayWayFragment.show(supportFragmentManager, "")
            vipPayWayFragment.setOnPayWaySelectListener(object : VipPayWayFragment.OnPayWaySelectListener {
                override fun onPayWaySelect(payway: String) {

                    mServiceDetailInfo?.let {
                        val indexDoodsBean = GoodsInfo()
                        val goodsInfo = mServiceDetailInfo?.goods_info
                        indexDoodsBean.m_price = goodsInfo?.m_price
                        indexDoodsBean.name = goodsInfo?.name
                        goodsInfo?.let {
                            indexDoodsBean.id = goodsInfo.id

                        }
                        nextOrders(payway, indexDoodsBean)
                    }


                }
            })
        }
    }

    private fun nextOrders(payWayName: String, indexDoodsBean: GoodsInfo?) { // PAY_TYPE_ZFB=0   PAY_TYPE_WX=1;
        if (indexDoodsBean == null) return

        if (!UserInfoHelper.instance.goToLogin(this)) {

            (mPresenter as? TutorPresenter)?.initOrders(payWayName, indexDoodsBean.m_price, indexDoodsBean.name, "${indexDoodsBean.id}")
        }
    }


    private fun initViewPager(content: String?) {
        val arrays = resources.getStringArray(R.array.tutor_service_detail_array)


        val titleList = Arrays.asList(*arrays)

        initNavigator(titleList)

        val fragments = arrayListOf<Fragment>()
        repeat(titleList.size) {
            //            Log.e("TAG", it)
            if (it == 0) {
                val serviceDescFragment = TutorServiceDetailDescFragment()
                val bundle = Bundle()

                bundle.putString("content", content)

                serviceDescFragment.arguments = bundle
                fragments.add(serviceDescFragment)
            } else if (it == 1) {
                fragments.add(TutorServiceDetailQuestionFragment())
            }
        }

        val efficientCourseMainAdapter = CommonMainPageAdapter(supportFragmentManager, titleList, fragments)
        viewPager_tutor_service_detail.adapter = efficientCourseMainAdapter
//        mViewPager.setOffscreenPageLimit(2)
        viewPager_tutor_service_detail.currentItem = 0
    }

    private fun initNavigator(titleList: List<String>) {
        val commonNavigator = CommonNavigator(this)
        //        commonNavigator.setScrollPivotX(0.65f);
        commonNavigator.isAdjustMode = true
        val navigatorAdapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return titleList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                //                Log.e(TAG, "getTitleView: " + index);

                val simplePagerTitleView = ColorFlipPagerTitleView(context)
                simplePagerTitleView.text = titleList[index]
                if (index == 0) simplePagerTitleView.typeface = Typeface.DEFAULT_BOLD
                simplePagerTitleView.textSize = 14f
                simplePagerTitleView.normalColor = resources.getColor(R.color.gray_999)
                simplePagerTitleView.selectedColor = resources.getColor(R.color.gray_222222)
                simplePagerTitleView.setOnClickListener { v -> viewPager_tutor_service_detail.currentItem = index }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                //                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.lineHeight = UIUtil.dip2px(context, 3.0).toFloat()
                indicator.lineWidth = UIUtil.dip2px(context, 40.0).toFloat()
                indicator.roundRadius = UIUtil.dip2px(context, 3.0).toFloat()
                indicator.startInterpolator = AccelerateInterpolator()
                indicator.endInterpolator = DecelerateInterpolator(2.0f)
                //                indicator.setYOffset(-5f);
                indicator.setColors(resources.getColor(R.color.red_crimson))
                return indicator
            }


        }
        commonNavigator.adapter = navigatorAdapter
        magicIndicator_tutor_service_detail.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator_tutor_service_detail, viewPager_tutor_service_detail)

        viewPager_tutor_service_detail.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(i: Int) {
                resetNavigator(commonNavigator)

                val pagerTitleView = commonNavigator.getPagerTitleView(i) as SimplePagerTitleView
                //                pagerTitleView.setTextSize(12);
                pagerTitleView.typeface = Typeface.DEFAULT_BOLD
            }


        })

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


    private fun initHScrollView() {
        for (i in 0..3) {
            val childView = LayoutInflater.from(this).inflate(R.layout.layout_tutor_service_comment_item, null)

            val rootView = childView.findViewById<ConstraintLayout>(R.id.rootView)

            val layoutParams = rootView.layoutParams

            layoutParams.width = ScreenUtil.getWidth(this) / 2
            rootView.layoutParams = layoutParams

            childView.setOnClickListener {
                startActivity(Intent(this, TutorServiceCommentDetailActivity::class.java))
            }

            ll_scroll_container.addView(childView)

        }
    }

    private fun getServiceDetail() {

        (mPresenter as? TutorPresenter)?.getTutorServiceDetailInfo(serviceId)

    }

    private var mServiceDetailInfo: TutorServiceDetailInfo? = null

    private fun initData(data: TutorServiceDetailInfo?) {
        data?.let {
            mServiceDetailInfo = data
            val serviceInfo = data.service_info
            val goodsInfo = data.goods_info
            Glide.with(this).load(serviceInfo?.img).apply(RequestOptions().error(R.mipmap.tutor_banner_example)).into(iv_service_detail)
            tv_service_title.text = serviceInfo?.name
            tv_service_price.text = goodsInfo?.m_price
            tv_service_buy_count.text = "${serviceInfo?.buy_count}人已购"
            tv_service_days.text = "${serviceInfo?.serviceday}天"
            initViewPager(serviceInfo?.content)
        }
    }

    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    override fun onZfbPauResult(result: Boolean, des: String?) {
        showPaySuccessDialog(result, des)
    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: EventBusWxPayResult) {
        when (event.code) {
            0//支付成功
            ->
                //  微信支付成功
                showPaySuccessDialog(true, event.mess)
            -1//错误
            -> showPaySuccessDialog(false, event.mess)
            -2//用户取消
            -> showPaySuccessDialog(false, event.mess)
            else -> {
            }
        }
    }

    private fun showPaySuccessDialog(result: Boolean, des: String?) {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setCancelable(false)
        alertDialog.setTitle("提示")
        if (result) {

            EventBus.getDefault().post(EventPayVipSuccess())
        }
        alertDialog.setMessage(des)

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "确定") { dialog, which ->
            if (result) {
                finish()
            }
        }
        alertDialog.show()


    }

    override fun showTutorServiceDetailInfo(data: TutorServiceDetailInfo?) {
        initData(data)
    }


    override fun showTutorServiceOrder(payWayName: String, data: OrdersInitBean) {
        val paramsBean = data.params
        Log.d("mylog", "onNetNext: payType == 0  Zfb   payType $payWayName")
        if (payWayName == "alipay") {
            //                    String info="alipay_sdk=alipay-sdk-php-20180705&app_id=2019051564672294&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22seller_id%22%3A%22%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A0.01%2C%22subject%22%3A%221%22%2C%22body%22%3A%22%5Cu5145%5Cu503c%22%2C%22out_trade_no%22%3A%22201905161657594587%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Flove.bshu.com%2Fnotify%2Falipay%2Fdefault&sign_type=RSA2&timestamp=2019-05-16+16%3A57%3A59&version=1.0&sign=BRj%2FY6Bk319dZwNoHwWbYIKYZFJahg1TRgvhFf7ubJzFKZEIESnattbFnaGJ6wq6%2BmauaKZcGv83ianrZfw0R%2BMQ9OmbTPXjKYGZUMzdPNDV3NygmVMgM68vs6oeHyQOxsbx16L4ltGi%2BdEjPDsLWqlw8E1INukZMxV4EDbFl8ZlyzKYerY9YZR1dRtxscFXgG7npmyPp3mO%2BA%2BywZABb5sANxqBShG%2FgeGbE%2BG1hpkZUE4KYGV7rCC80dcBjODWPgj%2FKQtFUXnx5NzCfWIeUMcyc8UaeK%2FsxqyrMJmsFPQgCBYGR5HH1llIfQ8NJuitwhDnJTKMhqCgh03UG9j%2B%2BQ%3D%3D";
            //                    toZfbPay(info);
            toZfbPay(paramsBean.info)
        } else {
            toWxPay(paramsBean)
        }
    }

}
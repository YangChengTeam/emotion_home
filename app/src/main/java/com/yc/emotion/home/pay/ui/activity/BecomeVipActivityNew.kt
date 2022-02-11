package com.yc.emotion.home.pay.ui.activity


import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.PayActivity
import com.yc.emotion.home.index.adapter.VipEquitiesAdapter
import com.yc.emotion.home.index.domain.bean.VipEquitiesInfo
import com.yc.emotion.home.index.ui.fragment.PaySuccWxFragment
import com.yc.emotion.home.model.bean.GoodsInfo
import com.yc.emotion.home.model.bean.OrdersInitBean
import com.yc.emotion.home.model.bean.event.EventBusWxPayResult
import com.yc.emotion.home.model.bean.event.EventPayVipSuccess
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.model.util.SizeUtils
import com.yc.emotion.home.pay.adapter.VipItemAdapter
import com.yc.emotion.home.pay.presenter.VipPresenter
import com.yc.emotion.home.pay.view.VipView
import com.yc.emotion.home.utils.Preference
import com.yc.emotion.home.utils.StatusBarUtil
import com.yc.emotion.home.utils.UserInfoHelper
import com.yc.emotion.home.utils.clickWithTrigger
import kotlinx.android.synthetic.main.activity_become_vip_new.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.random.Random

class BecomeVipActivityNew : PayActivity(), View.OnClickListener, VipView {

    private lateinit var names: Array<String>
    private lateinit var subNames: Array<String>


    private lateinit var becomeVipAdapter: VipItemAdapter
    private lateinit var vipEquitiesAdapter: VipEquitiesAdapter
    private var mGoodsInfo: GoodsInfo? = null
    private var vipnum by Preference(ConstantKey.VIP_NUM, 0)
    private var isReward: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        invadeStatusBar() //侵入状态栏
        setAndroidNativeLightStatusBar() //状态栏字体颜色改变
        names = resources.getStringArray(R.array.vip_item_name)
        subNames = resources.getStringArray(R.array.vip_item_sub_name)
        initViews()
        mPresenter = VipPresenter(this, this)
        MobclickAgent.onEvent(this, "vip_ui_statistics", "跳转vip付费界面统计")

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_become_vip_new
    }

    override fun initViews() {
        intent?.let {
            isReward = it.getBooleanExtra("isReward", false)
        }

        initTitleView()
        initRecyclerView()
    }

    private fun initTitleView() {

        val ivBack = findViewById<ImageView>(R.id.activity_base_same_iv_back)
        val tvTitle = findViewById<TextView>(R.id.activity_base_same_tv_title)

        ivBack.setOnClickListener(this)
        ivBack.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.icon_arr_lift_black))

        tvTitle.text = "开通VIP"

    }

    fun initRecyclerView() {

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        become_vip_rv.layoutManager = layoutManager
        becomeVipAdapter = VipItemAdapter(null)

//        if (vipnum == 0) {
//            vipnum = Random.nextInt(656592, 1003612)
//        }
        become_vip_rv.adapter = becomeVipAdapter


        val manager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView_equ.layoutManager = manager
        vipEquitiesAdapter = VipEquitiesAdapter(null)
        recyclerView_equ.adapter = vipEquitiesAdapter

        initListener()
    }

    private fun initListener() {

        becomeVipAdapter.setOnItemClickListener { adapter: BaseQuickAdapter<*, *>?, view: View, position: Int ->
            mGoodsInfo = becomeVipAdapter.getItem(position)

            if (mGoodsInfo?.isSelect == true) {
                return@setOnItemClickListener
            }
            becomeVipAdapter.data.forEach {
                it.isSelect = false
            }
            mGoodsInfo?.isSelect = mGoodsInfo?.isSelect != true
            becomeVipAdapter.notifyDataSetChanged()
            setVipEquities(position)
        }

        item_become_vip_rl_btn_wx.setOnClickListener {
            nextOrders(1, mGoodsInfo)
            MobclickAgent.onEvent(this, "wx_pay", "微信支付点击")
        }

        item_become_vip_rl_btn_zfb.clickWithTrigger {
            nextOrders(0, mGoodsInfo)
            MobclickAgent.onEvent(this, "zfb_pay", "支付宝支付点击")
        }
    }

    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    private fun nextOrders(payType: Int, indexDoodsBean: GoodsInfo?) { // PAY_TYPE_ZFB=0   PAY_TYPE_WX=1;
        if (indexDoodsBean == null) return
        val userInfo = UserInfoHelper.instance.getUserInfo()
        val vipTips = userInfo?.vip_tips
        if (vipTips == 1) {
            //已开通
            showPaySuccessDialog(false, "您已经开通了vip，不需要再次开通！")
            return
        }

        if (!UserInfoHelper.instance.goToLogin(this)) {
            val payWayName: String = if (payType == 0) {
                "alipay"
            } else {

                "wxpay"
            }

            val goodId: Int = indexDoodsBean.id

            (mPresenter as VipPresenter).initOrders(payWayName, indexDoodsBean.m_price, indexDoodsBean.name, "$goodId")

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: EventBusWxPayResult) {
        when (event.code) {
            0 ->                 //  微信支付成功
                showPaySuccessDialog(true, event.mess)
            -1, -2 -> showPaySuccessDialog(false, event.mess)
            else -> {
            }
        }
    }

    //  支付宝支付成功
    override fun onZfbPauResult(result: Boolean, des: String?) {
        showPaySuccessDialog(result, des)
    }

    private fun showPaySuccessDialog(result: Boolean, des: String?) {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setCancelable(false)
        alertDialog.setTitle("提示")
        if (result) {

            EventBus.getDefault().post(EventPayVipSuccess())
            val paySuccessFragment = PaySuccWxFragment()
            paySuccessFragment.show(supportFragmentManager, "")
        }
        alertDialog.setMessage(des)
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "确定") { dialog: DialogInterface?, which: Int ->
            if (result) {
                finish()
            }
        }
        alertDialog.show()
    }


    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.activity_base_same_iv_back -> finish()
            R.id.become_vip_iv_to_wx -> showToWxServiceDialog(null)
        }
    }

    override fun showGoodInfoList(data: List<GoodsInfo>?) {

        createNewData(data)

    }

    private fun createNewData(doodsBeanList: List<GoodsInfo?>?) {
        var goodsList = doodsBeanList

        if (goodsList == null) {
            goodsList = ArrayList()
        }
        if (goodsList.isNotEmpty()) {
            mGoodsInfo = goodsList[0]
        }
        if (isReward) {
            goodsList = goodsList.subList(0, 1)
        }
        for ((index, item) in goodsList.withIndex()) {
            if (index == 0) {
                item?.isSelect = true
            }
        }

        becomeVipAdapter.setNewData(goodsList)
        setVipEquities(0)
    }

    private fun setVipEquities(pos: Int) {
        val data = becomeVipAdapter.data
        if (data.isEmpty()) {
            return
        }

        val goodsInfo = data[pos]
        val dataList = mutableListOf<VipEquitiesInfo>()
        when {
            goodsInfo?.name?.contains("一年") == true -> {
                dataList.add(VipEquitiesInfo(R.mipmap.vip_word, "无限话术"))
                dataList.add(VipEquitiesInfo(R.mipmap.vip_search, "一键搜索"))
            }
            goodsInfo?.name?.contains("永久") == true -> {
                dataList.add(VipEquitiesInfo(R.mipmap.vip_word, "无限话术"))
                dataList.add(VipEquitiesInfo(R.mipmap.vip_search, "一键搜索"))
                dataList.add(VipEquitiesInfo(R.mipmap.vip_interest, "永久权益"))

            }
            else -> {
                dataList.add(VipEquitiesInfo(R.mipmap.vip_tutor, "专业导师"))
                dataList.add(VipEquitiesInfo(R.mipmap.vip_advisory, "一对一咨询"))
                dataList.add(VipEquitiesInfo(R.mipmap.vip_suggest, "情感建议"))
            }
        }

        vipEquitiesAdapter.setNewData(dataList)
    }

    override fun showOrderInfo(data: OrdersInitBean?, pay_way_name: String) {
        data?.let {
            val paramsBean = data.params
            Log.d("mylog", "onNetNext: payType == 0  Zfb   payType $pay_way_name")
            if (pay_way_name == "alipay") {

                toZfbPay(paramsBean.info)
            } else {
                toWxPay(paramsBean)
            }
        }
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val layoutParams = ll_pay_way.layoutParams as LinearLayout.LayoutParams

        val bottom = if (StatusBarUtil.isNavigationBarExist(this)) {
            StatusBarUtil.getNavigationBarHeight(this) + SizeUtils.dp2px(this, 15f)

        } else {
            SizeUtils.dp2px(this, 15f)
        }
        layoutParams.bottomMargin = bottom
        ll_pay_way.layoutParams = layoutParams


    }

    companion object {
        fun startActivity(context: Context, isReward: Boolean) {
            val intent = Intent(context, BecomeVipActivityNew::class.java)
            intent.putExtra("isReward", isReward)
            context.startActivity(intent)
        }
    }
}
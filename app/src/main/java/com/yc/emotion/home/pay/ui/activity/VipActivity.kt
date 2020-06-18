package com.yc.emotion.home.pay.ui.activity

import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.util.EventLog
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.PayActivity
import com.yc.emotion.home.index.ui.fragment.VipPayWayFragment
import com.yc.emotion.home.model.bean.GoodsInfo
import com.yc.emotion.home.model.bean.OrdersInitBean
import com.yc.emotion.home.model.bean.event.EventBusWxPayResult
import com.yc.emotion.home.model.bean.event.EventPayVipSuccess
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.pay.adapter.VipItemAdapter
import com.yc.emotion.home.pay.presenter.VipPresenter
import com.yc.emotion.home.pay.view.VipView
import com.yc.emotion.home.utils.Preference
import com.yc.emotion.home.utils.UserInfoHelper
import kotlinx.android.synthetic.main.activity_vip.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 *
 * Created by suns  on 2019/10/15 19:03.
 */
class VipActivity : PayActivity(), VipView {


    private var vipItemAdapter: VipItemAdapter? = null

    private var timeTotal by Preference(ConstantKey.TIME_TOTAL, 86400000L)
    private var mTime = timeTotal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_vip
    }

    override fun initViews() {
        invadeStatusBar()

        initView()
    }

    private fun initView() {
        mPresenter = VipPresenter(this, this)

        rcv_vip.layoutManager = GridLayoutManager(this, 2)
        vipItemAdapter = VipItemAdapter(null)
        rcv_vip.adapter = vipItemAdapter
        initListener()
        countDownTime()
    }


    private fun countDownTime() {

        /**
         * CountDownTimer 实现倒计时
         */
        val countDownTimer = object : CountDownTimer(timeTotal, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                mTime = millisUntilFinished

                calTime(millisUntilFinished)

            }

            override fun onFinish() {

            }
        }
        countDownTimer.start()

    }

    private fun calTime(millisUntilFinished: Long) {
        val day = millisUntilFinished / (1000 * 60 * 60 * 24)//天

        val hour = (millisUntilFinished - day * (100 * 60 * 60 * 24)) / (1000 * 60 * 60)//小时

        val minute = (millisUntilFinished - day * (100 * 60 * 60 * 24) - hour * (1000 * 60 * 60)) / (1000 * 60)//分

        val second = (millisUntilFinished - day * (100 * 60 * 60 * 24) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / (1000)//分//时

        var newHour = "$hour"
        var newMinute = "$minute"
        var newSecond = "$second"
        if (hour < 10) {
            newHour = "0$newHour"
        }
        if (minute < 10) {
            newMinute = "0$newMinute"
        }
        if (second < 10) {
            newSecond = "0$newSecond"
        }

        tv_time_count_down.text = "VIP特权卡限时特价倒计时$newHour:$newMinute:$newSecond"

    }


    private fun createNewData(doodsBeanList: List<GoodsInfo>?) {

        vipItemAdapter?.setNewData(doodsBeanList)

    }


    private fun initListener() {
        iv_vip_close.setOnClickListener { finish() }

        vipItemAdapter?.setOnItemClickListener { adapter, view, position ->


            val userInfo = UserInfoHelper.instance.getUserInfo()
            val vipTips = userInfo?.vip_tips
            if (vipTips == 1) {
                //已开通
                showPaySuccessDialog(false, "您已经开通了vip，不需要再次开通！")
                return@setOnItemClickListener
            }

            val indexDoodsBean = vipItemAdapter?.getItem(position)
            indexDoodsBean?.let {
                val vipPayWayFragment = VipPayWayFragment()
                vipPayWayFragment.show(supportFragmentManager, "")
                vipPayWayFragment.setOnPayWaySelectListener(object : VipPayWayFragment.OnPayWaySelectListener {
                    override fun onPayWaySelect(payway: String) {
                        nextOrders(payway, indexDoodsBean)
                    }
                })
            }
        }

    }

    private fun nextOrders(payWayName: String, indexDoodsBean: GoodsInfo?) { // PAY_TYPE_ZFB=0   PAY_TYPE_WX=1;
        if (indexDoodsBean == null) return

        if (!UserInfoHelper.instance.goToLogin(this)) {
            (mPresenter as? VipPresenter)?.initOrders(payWayName, indexDoodsBean.m_price, indexDoodsBean.name, "${indexDoodsBean.id}")
        }
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

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


    override fun onZfbPauResult(result: Boolean, des: String?) {
        showPaySuccessDialog(result, des)
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

    override fun onDestroy() {
        super.onDestroy()
        timeTotal = mTime
    }

    override fun showGoodInfoList(data: List<GoodsInfo>?) {
        createNewData(data)
    }

    override fun showOrderInfo(data: OrdersInitBean?, pay_way_name: String) {
        data?.let {
            val paramsBean = data.params
            Log.d("mylog", "onNetNext: payType == 0  Zfb   payType $pay_way_name")
            if (pay_way_name == "alipay") {
                //                    String info="alipay_sdk=alipay-sdk-php-20180705&app_id=2019051564672294&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22seller_id%22%3A%22%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A0.01%2C%22subject%22%3A%221%22%2C%22body%22%3A%22%5Cu5145%5Cu503c%22%2C%22out_trade_no%22%3A%22201905161657594587%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Flove.bshu.com%2Fnotify%2Falipay%2Fdefault&sign_type=RSA2&timestamp=2019-05-16+16%3A57%3A59&version=1.0&sign=BRj%2FY6Bk319dZwNoHwWbYIKYZFJahg1TRgvhFf7ubJzFKZEIESnattbFnaGJ6wq6%2BmauaKZcGv83ianrZfw0R%2BMQ9OmbTPXjKYGZUMzdPNDV3NygmVMgM68vs6oeHyQOxsbx16L4ltGi%2BdEjPDsLWqlw8E1INukZMxV4EDbFl8ZlyzKYerY9YZR1dRtxscFXgG7npmyPp3mO%2BA%2BywZABb5sANxqBShG%2FgeGbE%2BG1hpkZUE4KYGV7rCC80dcBjODWPgj%2FKQtFUXnx5NzCfWIeUMcyc8UaeK%2FsxqyrMJmsFPQgCBYGR5HH1llIfQ8NJuitwhDnJTKMhqCgh03UG9j%2B%2BQ%3D%3D";
                //                    toZfbPay(info);
                toZfbPay(paramsBean.info)
            } else {
                toWxPay(paramsBean)
            }
        }
    }
}
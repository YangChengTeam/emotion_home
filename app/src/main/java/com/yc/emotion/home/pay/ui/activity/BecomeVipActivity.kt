package com.yc.emotion.home.pay.ui.activity

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.TypeReference
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.domain.engine.OrderEngine
import com.yc.emotion.home.base.ui.activity.PayActivity
import com.yc.emotion.home.base.ui.widget.LoadDialog
import com.yc.emotion.home.model.bean.BecomeVipBean
import com.yc.emotion.home.model.bean.GoodsInfo
import com.yc.emotion.home.model.bean.OrdersInitBean
import com.yc.emotion.home.pay.adapter.BecomeVipAdapter
import com.yc.emotion.home.pay.presenter.VipPresenter
import com.yc.emotion.home.pay.view.VipView
import com.yc.emotion.home.utils.CommonInfoHelper.OnParseListener
import com.yc.emotion.home.utils.CommonInfoHelper.getO
import com.yc.emotion.home.utils.CommonInfoHelper.setO
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class BecomeVipActivity : PayActivity(), View.OnClickListener, VipView {
    private val imgResIds = intArrayOf(R.mipmap.become_vip_icon_06, R.mipmap.become_vip_icon_01, R.mipmap.become_vip_icon_02, R.mipmap.become_vip_icon_03,
            R.mipmap.become_vip_icon_04, R.mipmap.become_vip_icon_05)
    private var names: Array<String>
    private var subNames: Array<String>
    private var mRecyclerView: RecyclerView? = null
    private var mOrderEngine: OrderEngine? = null
    private var mDatas: MutableList<BecomeVipBean>? = null
    private val mNumber = 0
    private val mIsCacheNumberExist = false
    private var becomeVipAdapter: BecomeVipAdapter? = null
    private var mGoodsInfo: GoodsInfo? = null
    private var loadDialog: LoadDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_become_vip)
        mOrderEngine = OrderEngine(this)
        invadeStatusBar() //侵入状态栏
        setAndroidNativeLightStatusBar() //状态栏字体颜色改变
        names = resources.getStringArray(R.array.vip_item_name)
        subNames = resources.getStringArray(R.array.vip_item_sub_name)
        initViews()

//        netIsVipData();
        netData()
    }

    private fun netData() {
        loadDialog = LoadDialog(this)
        loadDialog!!.showLoadingDialog()
        mPresenter = VipPresenter(this, this)
        getO(this, "pay_vip_goods_index", object : TypeReference<List<BecomeVipBean?>?>() {}.type, OnParseListener<List<BecomeVipBean>> { o: MutableList<BecomeVipBean>? ->
            mDatas = o
            if (mDatas != null && mDatas!!.size > 0) {
                becomeVipAdapter!!.setNewData(mDatas)
                loadDialog!!.dismissLoadingDialog()
            }
        } as OnParseListener<List<BecomeVipBean>>?)
        mOrderEngine.indexGoods().subscribe(object : DisposableObserver<ResultInfo<List<IndexDoodsBean?>?>?>() {
            fun onComplete() {
                loadDialog!!.dismissLoadingDialog()
            }

            fun onError(e: Throwable?) {
                loadDialog!!.dismissLoadingDialog()
            }

            fun onNext(listAResultInfo: ResultInfo<List<IndexDoodsBean?>?>?) {
                loadDialog!!.dismissLoadingDialog()
                if (listAResultInfo != null && listAResultInfo.code === HttpConfig.STATUS_OK && listAResultInfo.data != null) createNewData(listAResultInfo.data)
            }
        })
    }

    private fun createNewData(doodsBeanList: List<IndexDoodsBean?>) {
        var doodsBeanList: List<IndexDoodsBean?>? = doodsBeanList
        mDatas = ArrayList()
        mDatas.add(BecomeVipBean(BecomeVipBean.VIEW_TITLE, "升级VIP解锁全部聊天话术及20W+条话术免费搜索"))
        if (doodsBeanList == null) {
            doodsBeanList = ArrayList<IndexDoodsBean?>()
        }
        if (doodsBeanList.size > 0) {
            mGoodsInfo = doodsBeanList[0]
        }
        mDatas.add(BecomeVipBean(BecomeVipBean.VIEW_TAIL, doodsBeanList))
        mDatas.add(BecomeVipBean(BecomeVipBean.VIEW_VIP_TAG, "vip标识"))
        mDatas.add(BecomeVipBean(BecomeVipBean.VIEW_ITEM, names[0], subNames[0], imgResIds[0]))
        becomeVipAdapter!!.setNewData(mDatas)
        setO<List<BecomeVipBean>>(this, mDatas, "pay_vip_goods_index")
    }

    protected override fun initViews() {
        val ivToWx = findViewById<ImageView>(R.id.become_vip_iv_to_wx)
        ivToWx.setOnClickListener(this)
        initTitleView()
        initRecyclerView()
    }

    private fun initTitleView() {
        val viewBar = findViewById<View>(R.id.activity_base_same_view_bar)
        val rlTitleCon = findViewById<RelativeLayout>(R.id.activity_base_same_rl_title_con)
        val ivBack = findViewById<ImageView>(R.id.activity_base_same_iv_back)
        val tvTitle = findViewById<TextView>(R.id.activity_base_same_tv_title)
        viewBar.setBackgroundColor(Color.TRANSPARENT)
        rlTitleCon.setBackgroundColor(Color.TRANSPARENT)
        ivBack.setOnClickListener(this)
        ivBack.setImageDrawable(resources.getDrawable(R.mipmap.icon_arr_lift_white))
        tvTitle.setTextColor(Color.WHITE)
        //        tvTitle.setText("开通VIP");
        tvTitle.text = ""
        setStateBarHeight(viewBar, 25)
    }

    fun initRecyclerView() {
        mRecyclerView = findViewById(R.id.become_vip_rv)
        val layoutManager = LinearLayoutManager(this)
        mRecyclerView.setLayoutManager(layoutManager)
        becomeVipAdapter = BecomeVipAdapter(null)
        mRecyclerView.setAdapter(becomeVipAdapter)
        initListener()
    }

    private fun initListener() {
        becomeVipAdapter!!.setOnItemChildClickListener { adapter: BaseQuickAdapter<*, *>?, view: View, position: Int ->
            val becomeVipBean = becomeVipAdapter!!.getItem(position)
            if (becomeVipBean != null && BecomeVipBean.VIEW_TAIL == becomeVipBean.type) {
                when (view.id) {
                    R.id.item_become_vip_rl_btn_wx -> nextOrders(1, mGoodsInfo)
                    R.id.item_become_vip_rl_btn_zfb -> nextOrders(0, mGoodsInfo)
                    R.id.item_become_vip_rl_btn_share -> {
                        val shareAppFragment = ShareAppFragment()
                        shareAppFragment.setIsShareMoney(true)
                        shareAppFragment.show(supportFragmentManager, "")
                    }
                }
            }
        }
        becomeVipAdapter!!.setOnPayClickListener { doodsBean: GoodsInfo? -> mGoodsInfo = doodsBean }
    }

    private fun nextOrders(payType: Int, indexDoodsBean: IndexDoodsBean?) { // PAY_TYPE_ZFB=0   PAY_TYPE_WX=1;
        if (indexDoodsBean == null) return
        if (UserInfoHelper.isLogin(this)) {
            val payWayName: String
            payWayName = if (payType == 0) {
                MobclickAgent.onEvent(this, ConstantKey.UM_PAY_ORDERS_ALIPAY)
                "alipay"
            } else {
                MobclickAgent.onEvent(this, ConstantKey.UM_PAY_ORDERS_WXPAY)
                "wxpay"
            }
            val params: MutableMap<String, String> = HashMap()
            params["user_id"] = UserInfoHelper.getUid()
            val userInfo: UserInfo = UserInfoHelper.getUserInfo()
            if (null != userInfo && !TextUtils.isEmpty(userInfo.name)) {
                params["user_name"] = userInfo.name
            }
            params["title"] = indexDoodsBean.name //订单标题，会员购买，商品购买等
            if (!TextUtils.isEmpty(indexDoodsBean.m_price)) params["money"] = java.lang.String.valueOf(indexDoodsBean.m_price)
            params["pay_way_name"] = payWayName
            val jsonObject = JsonObject()
            val goodId: Int = indexDoodsBean.id
            jsonObject.addProperty("goods_id", goodId)
            jsonObject.addProperty("num", 1)
            val jsonArray = JsonArray()
            jsonArray.add(jsonObject)
            params["goods_list"] = jsonArray.toString()
            if (mLoadingDialog == null) {
                mLoadingDialog = LoadDialog(this)
            }
            mLoadingDialog!!.showLoadingDialog()
            mOrderEngine!!.initOrders(params).subscribe(object : DisposableObserver<ResultInfo<OrdersInitBean?>?>() {
                fun onComplete() {
                    mLoadingDialog!!.dismissLoadingDialog()
                }

                fun onError(e: Throwable?) {
                    mLoadingDialog!!.dismissLoadingDialog()
                }

                fun onNext(ordersInitBeanAResultInfo: ResultInfo<OrdersInitBean?>?) {
                    mLoadingDialog!!.dismissLoadingDialog()
                    if (ordersInitBeanAResultInfo != null && ordersInitBeanAResultInfo.code === HttpConfig.STATUS_OK && ordersInitBeanAResultInfo.data != null) {
                        val ordersInitBean: OrdersInitBean = ordersInitBeanAResultInfo.data
                        val paramsBean = ordersInitBean.params
                        Log.d("mylog", "onNetNext: payType == 0  Zfb   payType $payType")
                        if (payType == 0) {
//                    String info="alipay_sdk=alipay-sdk-php-20180705&app_id=2019051564672294&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22seller_id%22%3A%22%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A0.01%2C%22subject%22%3A%221%22%2C%22body%22%3A%22%5Cu5145%5Cu503c%22%2C%22out_trade_no%22%3A%22201905161657594587%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Flove.bshu.com%2Fnotify%2Falipay%2Fdefault&sign_type=RSA2&timestamp=2019-05-16+16%3A57%3A59&version=1.0&sign=BRj%2FY6Bk319dZwNoHwWbYIKYZFJahg1TRgvhFf7ubJzFKZEIESnattbFnaGJ6wq6%2BmauaKZcGv83ianrZfw0R%2BMQ9OmbTPXjKYGZUMzdPNDV3NygmVMgM68vs6oeHyQOxsbx16L4ltGi%2BdEjPDsLWqlw8E1INukZMxV4EDbFl8ZlyzKYerY9YZR1dRtxscFXgG7npmyPp3mO%2BA%2BywZABb5sANxqBShG%2FgeGbE%2BG1hpkZUE4KYGV7rCC80dcBjODWPgj%2FKQtFUXnx5NzCfWIeUMcyc8UaeK%2FsxqyrMJmsFPQgCBYGR5HH1llIfQ8NJuitwhDnJTKMhqCgh03UG9j%2B%2BQ%3D%3D";
//                    toZfbPay(info);
                            toZfbPay(paramsBean.info)
                        } else {
                            toWxPay(paramsBean)
                        }
                    }
                }
            })
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
            MobclickAgent.onEvent(this@BecomeVipActivity, ConstantKey.UM_PAY_SUCCESS_ID)
            EventBus.getDefault().post(EventPayVipSuccess())
            val paySuccessFragment = PaySuccessFragment()
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

    override fun showGoodInfoList(data: List<GoodsInfo>?) {}
    override fun showOrderInfo(data: OrdersInitBean?, pay_way_name: String) {}
    override fun getLayoutId(): Int {
        return 0
    }
}
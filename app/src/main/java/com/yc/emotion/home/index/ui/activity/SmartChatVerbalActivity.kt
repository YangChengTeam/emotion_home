package com.yc.emotion.home.index.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.index.adapter.SmartChatVerbalAdapter
import com.yc.emotion.home.index.domain.bean.AIItem
import com.yc.emotion.home.index.domain.bean.SmartChatItem
import com.yc.emotion.home.index.presenter.AIChatPresenter
import com.yc.emotion.home.index.view.AIChatView
import com.yc.emotion.home.pay.ui.activity.BecomeVipActivityNew
import com.yc.emotion.home.utils.ToastUtils
import com.yc.emotion.home.utils.UserInfoHelper
import com.yc.emotion.home.utils.clickWithTrigger
import kotlinx.android.synthetic.main.activity_smart_chat.*

/**
 *
 * Created by suns  on 2020/9/1 10:03.
 */
class SmartChatVerbalActivity : BaseActivity(), AIChatView {

    private lateinit var smartChatVerbalAdapter: SmartChatVerbalAdapter
    private var kewword: String? = null
    private var section = 0
    private var preKeyWord: String? = ""

    private val contentTotalMap = mutableMapOf<String?, SmartChatItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置fitsSystemWindows属性后添加

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(getLayoutId())
        invadeStatusBar()
        setAndroidNativeLightStatusBar() //状态栏字体颜色改变
        initViews()
        initListener()
        MobclickAgent.onEvent(this, "smart_ai_verbal", "智能AI话术")
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_smart_chat
    }

    override fun initViews() {
        kewword = intent?.getStringExtra("keyword")

        mPresenter = AIChatPresenter(this, this)
        recyclerview_smart_chat.layoutManager = LinearLayoutManager(this)

        smartChatVerbalAdapter = SmartChatVerbalAdapter(null)
        recyclerview_smart_chat.adapter = smartChatVerbalAdapter
        if (!TextUtils.isEmpty(kewword)) {
            val aiItem = SmartChatItem(SmartChatItem.CHAT_ITEM_SELF, kewword)
            createNewData(aiItem, SmartChatItem.CHAT_ITEM_SELF, false)
            getData(false)
        }

    }


    private fun initListener() {
        iv_ai_back.clickWithTrigger {
            finish()
        }
        smartChatVerbalAdapter.setOnItemChildClickListener { adapter, view, position ->
            val item = smartChatVerbalAdapter.getItem(position)
            when (item?.itemType) {
                SmartChatItem.CHAT_ITEM_VERBAL -> {
                    if (view.id == R.id.ll_ai_change) {
                        refreshAni(view.findViewById(R.id.iv_ai_change))
                        section = 1
                        getData(true)

                    } else if (view.id == R.id.ll_verbal_change) {
                        refreshAni(view.findViewById(R.id.iv_verbal_refresh))
                        section = 2
                        getData(true)


                    }
                }
                SmartChatItem.CHAT_ITEM_VIP -> {
                    if (view.id == R.id.tv_open_vip) {
                        BecomeVipActivityNew.startActivity(this, false)
                    }
                }
            }
        }

        smartChatVerbalAdapter.setOnAiItemClickListener(object : SmartChatVerbalAdapter.OnAiItemClickListener {
            override fun onPraise(aiItem: AIItem?) {
                (mPresenter as AIChatPresenter).aiPraise(aiItem?.id)
            }

            override fun onCollect(aiItem: AIItem?) {
                (mPresenter as AIChatPresenter).aiCollect(aiItem?.id)
            }
        })

        tv_send.clickWithTrigger {
            val content = et_content.text.toString().trim()
            if (TextUtils.isEmpty(content)) {
                ToastUtils.showCenterToast("发送内容不能为空")
                return@clickWithTrigger
            }
            MobclickAgent.onEvent(this, "smart_ai_search", "智能AI话术搜索")
            kewword = content
            et_content.setText("")
            createNewData(SmartChatItem(SmartChatItem.CHAT_ITEM_SELF, content), SmartChatItem.CHAT_ITEM_SELF, false)

            section = 0
            getData(false)
        }

    }

    private fun refreshAni(iv: ImageView) {
        val rotate = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.ani_rotate)
        iv.startAnimation(rotate)

    }


    override fun showAIContent(data: String?) {

    }

    override fun showUseCountUp(message: String) {

        if (!UserInfoHelper.instance.goToLogin(this@SmartChatVerbalActivity)) {
//            val intent = Intent(this@SmartChatVerbalActivity, BecomeVipActivityNew::class.java)
//            startActivity(intent)

            createNewData(SmartChatItem(SmartChatItem.CHAT_ITEM_VIP, ""), SmartChatItem.CHAT_ITEM_VIP, false)
        }
//        val exitPublishFragment = ExitPublishFragment.newInstance(message, isCancel = true)
//
//        exitPublishFragment.setOnConfirmListener(object : ExitPublishFragment.OnConfirmListener {
//            override fun onConfirm() {
//                if (!UserInfoHelper.instance.goToLogin(this@SmartChatVerbalActivity)) {
//                    val intent = Intent(this@SmartChatVerbalActivity, BecomeVipActivity::class.java)
//                    startActivity(intent)
////                    val shareAppFragment = ShareAppFragment()
////                    shareAppFragment.setIsShareMoney(true)
////                    shareAppFragment.show(supportFragmentManager, "")
//                }
//
//
//            }
//        })
//        exitPublishFragment.show(supportFragmentManager, "")
    }

    override fun showSmartAiInfos(data: SmartChatItem?, refresh: Boolean) {
        createNewData(data, SmartChatItem.CHAT_ITEM_VERBAL, refresh)
    }

    private fun createData(data: SmartChatItem?, type: Int, refresh: Boolean) {


        val contentList = arrayListOf<SmartChatItem>()

        data?.let {

            if (refresh) {
//                if (!TextUtils.equals(preKeyWord, kewword)) {
                contentList.addAll(smartChatVerbalAdapter.data)
                preKeyWord = kewword
//                } else {
//                    contentList.add(SmartChatItem(SmartChatItem.CHAT_ITEM_SELF, kewword))
//                }
                data.setType(type)
                contentList.add(data)
                smartChatVerbalAdapter.setNewData(contentList)
            } else {
                data.setType(type)
                contentList.add(data)
                smartChatVerbalAdapter.addData(contentList)
            }

            recyclerview_smart_chat.scrollToPosition(smartChatVerbalAdapter.itemCount - 1)
        }

    }


    private fun createNewData(data: SmartChatItem?, type: Int, refresh: Boolean) {
        val contentList = arrayListOf<SmartChatItem>()


        data?.let {

            if (section == 0) {
                contentTotalMap[kewword] = it
            } else {
                val aiItems = it.aiItems
                val loveHealDetBeans = it.loveHealDetBeans
                val item = contentTotalMap[kewword]
                if (aiItems == null || aiItems.size == 0) {
                    it.aiItems = item?.aiItems
                }
                if (loveHealDetBeans == null || loveHealDetBeans.size == 0) {
                    it.loveHealDetBeans = item?.loveHealDetBeans
                } else {
                }
            }
            if (refresh) {
                if (section == 0) {
                    contentList.addAll(smartChatVerbalAdapter.data)
                } else {
                    contentList.add(SmartChatItem(SmartChatItem.CHAT_ITEM_SELF, kewword))
                }
                data.setType(type)
                contentList.add(data)
                smartChatVerbalAdapter.setNewData(contentList)
            } else {

                data.setType(type)
                contentList.add(data)
                smartChatVerbalAdapter.addData(contentList)
            }

            recyclerview_smart_chat.scrollToPosition(smartChatVerbalAdapter.itemCount - 1)

        }


    }


    private fun getData(isRefresh: Boolean) {
        (mPresenter as AIChatPresenter).smartSearchVerbal(kewword, section, isRefresh)
    }


    companion object {
        fun startActivity(context: Context, kewword: String?) {
            val intent = Intent(context, SmartChatVerbalActivity::class.java)
            intent.putExtra("keyword", kewword)
            context.startActivity(intent)
        }
    }

}
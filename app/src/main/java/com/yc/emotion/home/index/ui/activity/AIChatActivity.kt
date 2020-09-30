package com.yc.emotion.home.index.ui.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.common.ShareAppFragment
import com.yc.emotion.home.index.adapter.AIChatAdapter
import com.yc.emotion.home.index.domain.bean.AIItem
import com.yc.emotion.home.index.domain.bean.SmartChatItem
import com.yc.emotion.home.index.presenter.AIChatPresenter
import com.yc.emotion.home.index.view.AIChatView
import com.yc.emotion.home.mine.ui.fragment.ExitPublishFragment
import com.yc.emotion.home.utils.ToastUtils
import com.yc.emotion.home.utils.UserInfoHelper
import com.yc.emotion.home.utils.clickWithTrigger
import kotlinx.android.synthetic.main.activity_ai_chat.*

/**
 *
 * Created by suns  on 2020/8/11 14:27.
 */
class AIChatActivity : BaseActivity(), AIChatView {
    private lateinit var aiChatAdapter: AIChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //设置fitsSystemWindows属性后添加
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(getLayoutId())
//        AndroidBug5497Workaround.assistActivity(this)

        invadeStatusBar()
        setAndroidNativeLightStatusBar() //状态栏字体颜色改变

        initViews()
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_ai_chat
    }

    override fun initViews() {

        mPresenter = AIChatPresenter(this, this)
        recyclerview_ai_chat.layoutManager = LinearLayoutManager(this)
        aiChatAdapter = AIChatAdapter(null)
        recyclerview_ai_chat.adapter = aiChatAdapter
//        tv_ai_title.text = "超级聊天AI对话"

        initListener()
    }

    private fun initListener() {
        iv_fly.clickWithTrigger {
            val content = et_content.text.toString().trim()
            if (TextUtils.isEmpty(content)) {
                ToastUtils.showCenterToast("发送内容不能为空")
                return@clickWithTrigger
            }
            et_content.setText("")
            createData(content, AIItem.TYPE_FROM_ME)
            (mPresenter as AIChatPresenter).getAIChatContent(content)

        }
        iv_ai_back.clickWithTrigger {
            finish()
        }

        aiChatAdapter.setOnItemChildClickListener { adapter, view, position ->
            val aiItem = aiChatAdapter.getItem(position)
            aiItem?.let {
                if (view.id == R.id.tv_chat_message) {
                    copyText(it.content)
                }
            }
        }
    }

    private fun createData(content: String?, type: Int) {
        val contentList = arrayListOf<AIItem>()
        val aiItem = AIItem(content, type)

        contentList.add(aiItem)
        aiChatAdapter.addData(contentList)
        recyclerview_ai_chat.scrollToPosition(aiChatAdapter.itemCount - 1)
    }

    override fun showAIContent(data: String?) {
        createData(data, AIItem.TYPE_FROM_OTHER)
    }

    override fun showUseCountUp(message: String) {
        val exitPublishFragment = ExitPublishFragment.newInstance(message, isCancel = true)

        exitPublishFragment.setOnConfirmListener(object : ExitPublishFragment.OnConfirmListener {
            override fun onConfirm() {
                if (!UserInfoHelper.instance.goToLogin(this@AIChatActivity)) {
//                    val intent = Intent(this@AIChatActivity, BecomeVipActivity::class.java)
//                    startActivity(intent)
                    val shareAppFragment = ShareAppFragment()
                    shareAppFragment.setIsShareMoney(true)
                    shareAppFragment.show(supportFragmentManager, "")
                }


            }
        })
        exitPublishFragment.show(supportFragmentManager, "")
    }

    override fun showSmartAiInfos(data: SmartChatItem?, refresh: Boolean) {

    }

    private fun copyText(text: String?) {
        val myClipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val myClip = ClipData.newPlainText("text", text)
        myClipboard.primaryClip = myClip
        ToastUtils.showCenterToast("内容已复制", true)
    }

}
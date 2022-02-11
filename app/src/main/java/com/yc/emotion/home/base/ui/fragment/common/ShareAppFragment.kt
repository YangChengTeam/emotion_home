package com.yc.emotion.home.base.ui.fragment.common

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.kk.share.UMShareImpl

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.umeng.analytics.MobclickAgent
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.yc.emotion.home.R
import com.yc.emotion.home.base.domain.engine.BaseModel
import com.yc.emotion.home.base.ui.widget.LoadDialog
import com.yc.emotion.home.model.bean.ShareInfo
import com.yc.emotion.home.model.bean.event.EventBusWxPayResult
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.utils.ShareInfoHelper
import com.yc.emotion.home.utils.UIUtils
import com.yc.emotion.home.utils.UserInfoHelper.Companion.instance
import com.yc.emotion.home.utils.clickWithTrigger
import io.reactivex.FlowableSubscriber
import io.reactivex.FlowableTransformer
import io.reactivex.observers.DisposableObserver
import io.reactivex.subscribers.ResourceSubscriber
import org.greenrobot.eventbus.EventBus
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.config.HttpConfig
import yc.com.rthttplibrary.util.ToastUtil

import java.io.ByteArrayOutputStream
import java.util.*

/**
 * Created by wanglin  on 2019/7/9 15:54.
 */
class ShareAppFragment : BottomSheetDialogFragment() {
    private lateinit var mContext: Activity
    private lateinit var dialog: BottomSheetDialog
    private var rootView: View? = null
    private var mBehavior: BottomSheetBehavior<View>? = null
    private var loadingView: LoadDialog? = null
    private var llWx: LinearLayout? = null
    private var llCircle: LinearLayout? = null
    private var tvCancelShare: TextView? = null
    private var mShareInfo: ShareInfo? = null
    private lateinit var baseModel: BaseModel
    private var llMiniProgram: LinearLayout? = null
    private var api: IWXAPI? = null

    fun setShareInfo(mShareInfo: ShareInfo?) {
        this.mShareInfo = mShareInfo
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as Activity
    }

    override fun onStart() {
        super.onStart()
        val window = getDialog()?.window
        val windowParams = window?.attributes
        //这里设置透明度
        windowParams?.dimAmount = 0.5f
        //        windowParams.width = (int) (ScreenUtil.getWidth(mContext) * 0.98);
        window?.attributes = windowParams
        window?.setWindowAnimations(R.style.share_anim)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = BottomSheetDialog(mContext, theme)
        if (rootView == null) {
            //缓存下来的 View 当为空时才需要初始化 并缓存
            rootView = LayoutInflater.from(mContext).inflate(layoutId, null)
            loadingView = LoadDialog(mContext)
            llWx = rootView?.findViewById(R.id.ll_wx)
            llCircle = rootView?.findViewById(R.id.ll_circle)
            llMiniProgram = rootView?.findViewById(R.id.ll_mini_program)
            tvCancelShare = rootView?.findViewById(R.id.tv_cancel_share)
        }
        rootView?.let {
            dialog.setContentView(it)
        }

        mBehavior = BottomSheetBehavior.from(rootView?.parent as View)
        (rootView?.parent as View).setBackgroundColor(Color.TRANSPARENT)
        rootView?.post {
            /**
             * PeekHeight 默认高度 256dp 会在该高度上悬浮
             * 设置等于 view 的高 就不会卡住
             */
            /**
             * PeekHeight 默认高度 256dp 会在该高度上悬浮
             * 设置等于 view 的高 就不会卡住
             */
            rootView?.let {

                mBehavior?.setPeekHeight(it.height)
            }
        }
        init()
        return dialog
    }

    private fun init() {
        api = WXAPIFactory.createWXAPI(mContext, ConstantKey.WX_APP_ID, false)
        baseModel = BaseModel(mContext)

        tvCancelShare?.clickWithTrigger { v: View? -> dismiss() }
        val shareItemViews: MutableList<View?> = ArrayList()
        shareItemViews.add(llWx)
        shareItemViews.add(llCircle)
        shareItemViews.add(llMiniProgram)
        for (i in shareItemViews.indices) {
            val view = shareItemViews[i]
            view?.tag = i
            view?.clickWithTrigger { v: View? -> shareInfo(i) }
        }
        getShareLink()
    }

    private fun shareInfo(tag: Int) {
        var title = "表白神器——" + UIUtils.getAppName(mContext) + "！会聊才会撩，撩到心动的TA！"
        var url: String? = "http://tic.upkao.com/apk/love.apk"
        var desc = """
            谈恋爱时，你是否因为不会聊天而苦恼过？
            和女生聊天没有话题？遇见喜欢的人不敢搭讪？无法吸引TA的注意？猜不透女生的心意？恋爱话术宝，一款专门为您解决以上所有情感烦恼的APP。从搭讪开场到线下邀约互动对话，妹子的回复再也不用担心没话聊。只需搜一搜，N+精彩聊天回复任你选择，让您和女生聊天不再烦恼，全方面提高您的聊天能力。
            """.trimIndent()
        val bitmap = mShareInfo?.bitmap
        if (mShareInfo == null)
            mShareInfo = ShareInfoHelper.shareInfo


        mShareInfo?.let {
            if (!TextUtils.isEmpty(it.url)) {
                url = it.url
            }
            if (!TextUtils.isEmpty(it.title)) {
                title = it.title
            }
            if (!TextUtils.isEmpty(it.desp)) {
                desc = it.desp
            }
        }


        dismiss()

//        else {
        if (tag == 2) {
            shareMiniProgram(title, desc, R.mipmap.ic_launcher)
            //            UMShareImpl.get().setCallback(mContext, umShareListener).shareMiniParam(title, desc, R.mipmap.ic_launcher, "gh_d063497205cc");
        } else {
            if (bitmap != null) {
                UMShareImpl.get().setCallback(mContext, umShareListener).shareImage("app", bitmap, getShareMedia(tag.toString() + ""))
            } else {
                UMShareImpl.get().setCallback(mContext, umShareListener).shareUrl(title, url, desc, R.mipmap.ic_launcher, getShareMedia(tag.toString() + ""))
            }
        }
        //        }
    }

    private fun shareMiniProgram(title: String, desc: String, drawableId: Int) {

//        api.registerApp(ConstantKey.WX_APP_ID);
        val miniProgramObj = WXMiniProgramObject()
        miniProgramObj.webpageUrl = "https://sj.qq.com/myapp/detail.htm?apkName=com.yc.emotion.home" // 兼容低版本的网页链接
        miniProgramObj.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE // 正式版:0，测试版:1，体验版:2
        miniProgramObj.userName = ConstantKey.WX_MINI_ORIGIN_ID // 小程序原始id
        miniProgramObj.path = "pages/index/index" //小程序页面路径；对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"
        val msg = WXMediaMessage(miniProgramObj)
        msg.title = title // 小程序消息title
        msg.description = desc // 小程序消息desc
        msg.thumbData = getThumb(drawableId) // 小程序消息封面图片，小于128k
        val req = SendMessageToWX.Req()
        //        req.transaction = buildTransaction("miniProgram");
        req.message = msg
        req.scene = SendMessageToWX.Req.WXSceneSession // 目前只支持会话
        api?.sendReq(req)
    }

    private fun getThumb(drawableId: Int): ByteArray? {
        val drawable = ContextCompat.getDrawable(mContext, drawableId)
        drawable?.let {
            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, drawable.intrinsicWidth,
                    drawable.intrinsicHeight)
            drawable.draw(canvas)
            val size = bitmap.width * bitmap.height
            // 创建一个字节数组输出流,流的大小为size
            val baos = ByteArrayOutputStream(size)
            // 设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            return baos.toByteArray()
        }


        // 将字节数组输出流转化为字节数组byte[]
        return null
    }

    private fun getShareMedia(tag: String): SHARE_MEDIA {
        if (tag == "0") {
            MobclickAgent.onEvent(mContext, "share_with_wechat", "分享到微信")
            return SHARE_MEDIA.WEIXIN
        }
        if (tag == "1") {
            MobclickAgent.onEvent(mContext, "sharing_circle_id", "分享到朋友圈")
            return SHARE_MEDIA.WEIXIN_CIRCLE
        }
        return if (tag == "2") {
            SHARE_MEDIA.WEIXIN
        } else SHARE_MEDIA.WEIXIN
    }

    private var mIsShareMoney = false
    fun setIsShareMoney(isShareMoney: Boolean) {
        mIsShareMoney = isShareMoney
    }

    private val umShareListener: UMShareListener = object : UMShareListener {
        override fun onStart(share_media: SHARE_MEDIA) {
            loadingView?.setText("正在分享...")
            loadingView?.showLoadingDialog()
            UIUtils.postDelay(Runnable {
                loadingView?.dismissLoadingDialog()
            }, 3000)
        }

        override fun onResult(share_media: SHARE_MEDIA) {

            loadingView?.dismissLoadingDialog()
            ToastUtil.toast(mContext, "分享成功")
            //            RxSPTool.putBoolean(mContext, SpConstant.SHARE_SUCCESS, true);

//            if (mShareInfo == null)
//                mShareInfo = ShareInfoHelper.shareInfo
//
//            mShareInfo ?.let {
            if (mIsShareMoney) {
                shareReward()
            }
            //                else {
////                    if (!TextUtils.isEmpty(mShareInfo?.task_id)) {
//                    mPresenter.share(mShareInfo ?.task_id)
////                    }
//                }
//            }
        }

        override fun onError(share_media: SHARE_MEDIA, throwable: Throwable) {
            loadingView?.dismissLoadingDialog()
            ToastUtil.toast(mContext, "分享有误")
        }

        override fun onCancel(share_media: SHARE_MEDIA) {
//            if (loadingView != null) {
            loadingView?.dismissLoadingDialog()
            ToastUtil.toast(mContext, "取消发送")
        }
    }

    @SuppressLint("CheckResult")
    private fun shareReward() {
        val id = instance.getUid()
        if (id < 0) {
            return
        }
        baseModel.shareReward("$id").subscribeWith(object : ResourceSubscriber<ResultInfo<String>?>() {
            override fun onNext(t: ResultInfo<String>?) {

            }

            override fun onError(t: Throwable?) {

            }

            override fun onComplete() {

            }
        })
    }

    private fun getShareLink() {
        baseModel.getShareLink()?.subscribe(object : DisposableObserver<ResultInfo<ShareInfo>>() {
            override fun onComplete() {

            }

            override fun onNext(t: ResultInfo<ShareInfo>) {
                if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                    val shareInfo = t.data

                    mShareInfo = shareInfo
                    ShareInfoHelper.shareInfo = shareInfo
                }
            }

            override fun onError(e: Throwable) {

            }

        })
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart(this.javaClass.simpleName)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPageEnd(this.javaClass.simpleName)
    }

    override fun onDestroy() {
        super.onDestroy()

        //解除缓存 View 和当前 ViewGroup 的关联
        (rootView?.parent as ViewGroup).removeView(rootView)
        //        Runtime.getRuntime().gc();
    }

    val layoutId: Int
        get() = R.layout.fragment_share
}
package com.yc.emotion.home.base.ui.widget

import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.mine.presenter.CollectPresenter
import com.yc.emotion.home.mine.view.CollectView
import com.yc.emotion.home.model.bean.LoveHealDetDetailsBean
import com.yc.emotion.home.model.bean.OpenApkPkgInfo
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.utils.UserInfoHelper
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by mayn on 2019/5/21.
 */

class OpenAkpDialog(private val mContext: Context?, private val mOpenApkPkgInfos: List<OpenApkPkgInfo>?, private val mLoveHealDetDetailsBean: LoveHealDetDetailsBean?, private val mIsCollect: Boolean) : AlertDialog(mContext, 0), CollectView {

    private var mPresenter: BasePresenter<*, *>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instanceDialog()

        mPresenter = CollectPresenter(mContext, this)
    }


    private fun instanceDialog() {
        setContentView(R.layout.dialog_open_akp)
        val window = window

        window?.let {

            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val tvQQ = window.findViewById<TextView>(R.id.dialog_open_akp_tv_qq)
            val tvWx = window.findViewById<TextView>(R.id.dialog_open_akp_tv_wx)
            val tvMm = window.findViewById<TextView>(R.id.dialog_open_akp_tv_mm)
            val tvTt = window.findViewById<TextView>(R.id.dialog_open_akp_tv_tt)
            val tvCollect = window.findViewById<TextView>(R.id.dialog_open_collect)




            if (mIsCollect) {
                tvCollect?.text = "取消收藏"
            } else {
                tvCollect.text = mContext?.getString(R.string.collect)
            }

            tvQQ.setOnClickListener(clickToOpenApk(mOpenApkPkgInfos?.get(0)?.pkg))
            tvWx.setOnClickListener(clickToOpenApk(mOpenApkPkgInfos?.get(1)?.pkg))
            tvMm.setOnClickListener(clickToOpenApk(mOpenApkPkgInfos?.get(2)?.pkg))
            tvCollect.setOnClickListener { v ->
                if (!UserInfoHelper.instance.goToLogin(mContext)) {
                    handleCollect()
                    MobclickAgent.onEvent(mContext, "collection_id", "收藏话术")
                    dismiss()
                }
            }
            //        tvTt.setOnClickListener(clickToOpenApk(openApkPkgInfos.get(3).pkg));

        }
    }

    private fun handleCollect() {


        if (mIsCollect) {

            (mPresenter as? CollectPresenter)?.deleteCollectLoveHeals(mLoveHealDetDetailsBean)

        } else {

            (mPresenter as? CollectPresenter)?.collectLoveHeal(mLoveHealDetDetailsBean)

        }

    }


    private fun clickToOpenApk(pkg: String?): View.OnClickListener {
        return View.OnClickListener {
            if (TextUtils.isEmpty(pkg)) {
                Toast.makeText(mContext, "未安装该应用", Toast.LENGTH_SHORT).show()
            } else {
                openApk(pkg)

                when (pkg) {
                    "com.tencent.mobileqq" -> MobclickAgent.onEvent(mContext, ConstantKey.UM_OPEN_DIALOGUE_QQ)
                    "com.tencent.mm" -> MobclickAgent.onEvent(mContext, ConstantKey.UM_OPEN_DIALOGUE_WX, "粘贴话术打开微信")
                    "com.immomo.momo" -> MobclickAgent.onEvent(mContext, ConstantKey.UM_OPEN_DIALOGUE_MOMO)
                }
            }
            if (isShowing) {
                dismiss()
            }
        }
    }


    private fun openApk(name: String?) {

        //        list.add()
        //同AndroidManifest中主入口Activity一样
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        //得到一个PackageManager的对象
        val packageManager = mContext?.applicationContext?.packageManager
        //获取到主入口的Activity集合
        val mlist = packageManager?.queryIntentActivities(intent, 0)

        Collections.sort(mlist, ResolveInfo.DisplayNameComparator(packageManager))

        mlist?.let {

            for (res in mlist) {
                val pkg = res.activityInfo.packageName
                val cls = res.activityInfo.name
                name?.let {
                    if (pkg.contains(name)) {
                        val componentName = ComponentName(pkg, cls)
                        val intent1 = Intent()
                        intent1.component = componentName
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        mContext?.startActivity(intent1)
                    }

                }
            }
        }
    }

    private fun openWeiXin() {
        val intent = Intent()
        val cmp = ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.component = cmp
        context.startActivity(intent)
    }


    override fun showLoadingDialog() {

    }

    override fun hideLoadingDialog() {

    }

    override fun getLayoutId(): Int {
        return 0
    }

    override fun initViews() {

    }


    override fun showDeleteSuccess() {
        EventBus.getDefault().post("collect_cancel")
        dismiss()
    }

    override fun showCollectSuccess() {
        dismiss()
    }

}

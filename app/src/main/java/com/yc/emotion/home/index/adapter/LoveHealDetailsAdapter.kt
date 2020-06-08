package com.yc.emotion.home.index.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.widget.CustomLoadMoreView
import com.yc.emotion.home.base.ui.widget.OpenAkpDialog
import com.yc.emotion.home.model.bean.LoveHealDetBean
import com.yc.emotion.home.model.bean.LoveHealDetDetailsBean
import com.yc.emotion.home.model.bean.OpenApkPkgInfo
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.model.util.PackageUtils
import java.util.*


/**
 * Created by Administrator on 2017/9/12.
 */


/**
 * Same as QuickAdapter#QuickAdapter(Context,int) but with
 * some initialization data.
 *
 * @param data A new list is created out of this one to avoid mutable list
 */

class LoveHealDetailsAdapter(data: List<LoveHealDetBean>?, private val mTitle: String?) : BaseMultiItemQuickAdapter<LoveHealDetBean, BaseViewHolder>(data) {


    init {
        addItemType(LoveHealDetBean.VIEW_ITEM, R.layout.recycler_view_item_love_heal_det)
        addItemType(LoveHealDetBean.VIEW_VIP, R.layout.recycler_view_item_love_heal_det_vip)
        setLoadMoreView(CustomLoadMoreView())
    }


    override fun convert(helper: BaseViewHolder, item: LoveHealDetBean?) {
        item?.let {
            var details: List<LoveHealDetDetailsBean>? = item.details
            if (item.type == LoveHealDetBean.VIEW_ITEM) {

                val recyclerView = helper.getView<RecyclerView>(R.id.item_love_heal_rv)
                val layoutManager = LinearLayoutManager(mContext)
                recyclerView.layoutManager = layoutManager
                layoutManager.orientation = LinearLayoutManager.VERTICAL


                if (details == null || details.isEmpty()) {
                    details = item.detail
                }

                val loveHealDetAdapterNew = LoveHealDetAdapter(details)

                recyclerView.adapter = loveHealDetAdapterNew
                loveHealDetAdapterNew.setOnItemChildClickListener { adapter, view, position ->
                    val item1 = loveHealDetAdapterNew.getItem(position)
                    if (item1 != null) {
                        MobclickAgent.onEvent(mContext, "copy_dialogue_id", "复制恋爱话术")
                        item1.setTitle(mTitle)
                        toCopy(mContext, item1)
                    }
                }

                loveHealDetAdapterNew.setOnItemClickListener { adapter, view, position ->
                    val item1 = loveHealDetAdapterNew.getItem(position)
                    if (item1 != null) {
                        item1.setTitle(mTitle)
                        MobclickAgent.onEvent(mContext, "copy_dialogue_id", "复制恋爱话术")
                        toCopy(mContext, item1)
                    }
                }
            } else if (item.type == LoveHealDetBean.VIEW_VIP) {
                if (details == null || details.isEmpty()) {
                    details = item.detail
                }

                if (details != null && details.isNotEmpty()) {
                    if (details.size > 1) {
                        val detailsBean = details[0]
                        helper.setText(R.id.item_love_heal_det_vip_tv_name, detailsBean.content)
                    } else {
                        helper.setText(R.id.item_love_heal_det_vip_tv_name, "*************")
                    }
                    //                        String ansSex = detailsBean.ans_sex;
                    //                        if (!TextUtils.isEmpty(ansSex)) {
                    helper.setImageDrawable(R.id.item_love_heal_det_vip_iv_sex, mContext.resources.getDrawable(R.mipmap.icon_dialogue_women))
                    //                        }
                }
                helper.addOnClickListener(R.id.tv_look)
            }
        }
    }
}

private fun toCopy(context: Context, content: LoveHealDetDetailsBean) {
    MobclickAgent.onEvent(context, ConstantKey.UM_COPY_DIALOGUE_HEAL)
    val myClipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val myClip = ClipData.newPlainText("text", content.content)
    myClipboard.primaryClip = myClip
    showOpenAkpDialog(context, content)
}

private fun showOpenAkpDialog(context: Context, content: LoveHealDetDetailsBean) {
    val openApkPkgInfos = ArrayList<OpenApkPkgInfo>()
    val qq = OpenApkPkgInfo(1, "", "QQ", ContextCompat.getDrawable(context, R.mipmap.icon_d_qq))
    val wx = OpenApkPkgInfo(2, "", "微信", ContextCompat.getDrawable(context, R.mipmap.icon_d_wx))
    val mm = OpenApkPkgInfo(3, "", "陌陌", ContextCompat.getDrawable(context, R.mipmap.icon_d_momo))
    //        OpenApkPkgInfo tt = new OpenApkPkgInfo(4, "", "探探", getResources().getDrawable(R.mipmap.icon_d_tt));

    val apkList = PackageUtils.getApkList(context)
    for (i in apkList.indices) {
        when (val apkPkgName = apkList[i]) {
            "com.tencent.mobileqq" -> qq.pkg = apkPkgName
            "com.tencent.mm" -> wx.pkg = apkPkgName
            "com.immomo.momo" -> mm.pkg = apkPkgName
        }/* else if ("com.p1.mobile.putong".equals(apkPkgName)) {
                tt.pkg = apkPkgName;
            }*/
        /* else if ("com.p1.mobile.putong".equals(apkPkgName)) {
                       tt.pkg = apkPkgName;
                   }*/
    }

    openApkPkgInfos.add(qq)
    openApkPkgInfos.add(wx)
    openApkPkgInfos.add(mm)
    //        openApkPkgInfos.add(tt);
    val openAkpDialog = OpenAkpDialog(context, openApkPkgInfos, content, false)

    openAkpDialog.show()

}
package com.yc.emotion.home.mine.ui.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.base.ui.widget.OpenAkpDialog
import com.yc.emotion.home.index.adapter.CollectLoveHealDetailAdapter
import com.yc.emotion.home.mine.presenter.CollectPresenter
import com.yc.emotion.home.mine.ui.activity.CollectActivity
import com.yc.emotion.home.mine.view.CollectView
import com.yc.emotion.home.model.bean.LoveHealDetDetailsBean
import com.yc.emotion.home.model.bean.OpenApkPkgInfo
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.model.util.PackageUtils
import com.yc.emotion.home.utils.ToastUtils
import kotlinx.android.synthetic.main.fragment_collect_view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * 收藏 话术
 */

class CollectVerbalTalkFragment : BaseFragment<CollectPresenter>(), CollectView {


    private val PAGE_SIZE = 10
    private var PAGE_NUM = 1


    private var loveHealDetailsAdapter: CollectLoveHealDetailAdapter? = null
    //    private var emptyView: View? = null


    private var mCollectActivity: CollectActivity? = null

    override fun initBundle() {
        val arguments = arguments
        if (arguments != null) {
            val position = arguments.getInt("position")
            //            mCategoryId = arguments.getInt("category_id", -1);
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CollectActivity) {
            mCollectActivity = context
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_collect_view
    }


    override fun initViews() {

        mPresenter = CollectPresenter(mCollectActivity, this)


        initRecyclerView()
    }


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(tint: String) {  //无网状态

        if (TextUtils.equals("collect_cancel", tint)) {
            PAGE_NUM = 0
            netData()
        }
    }


    private fun initRecyclerView() {


        val layoutManager = LinearLayoutManager(mCollectActivity)
        fragment_collect_love_healing_rv.layoutManager = layoutManager
        loveHealDetailsAdapter = CollectLoveHealDetailAdapter(null)

        fragment_collect_love_healing_rv.adapter = loveHealDetailsAdapter
        fragment_collect_love_healing_rv.setHasFixedSize(true)
        initListener()

    }

    private fun initListener() {
        mCollectActivity?.let {

            swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(it, R.color.app_color))
        }
        swipeRefreshLayout.setOnRefreshListener {
            PAGE_NUM = 0
            netData()
        }
        loveHealDetailsAdapter?.setOnLoadMoreListener({ netData() }, fragment_collect_love_healing_rv)
        loveHealDetailsAdapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            val item = loveHealDetailsAdapter?.getItem(position)
            item?.let {
                toCopy(item)
            }
        }

    }

    override fun lazyLoad() {
        netData()
    }


    private fun netData() {

        mPresenter?.getCollectLoveHeals(PAGE_NUM, PAGE_SIZE)

    }

    private fun createData(loveHealDetDetailsBeans: List<LoveHealDetDetailsBean>?) {
        if (PAGE_NUM == 0)
            loveHealDetailsAdapter?.setNewData(loveHealDetDetailsBeans)
        else
            loveHealDetDetailsBeans?.let {

                loveHealDetailsAdapter?.addData(loveHealDetDetailsBeans)
            }
        if (loveHealDetDetailsBeans != null && loveHealDetDetailsBeans.size == PAGE_SIZE) {
            PAGE_NUM++
            loveHealDetailsAdapter?.loadMoreComplete()
        } else {
            loveHealDetailsAdapter?.loadMoreEnd()
        }
        swipeRefreshLayout?.let {
            if (it.isRefreshing) it.isRefreshing = false
        }
    }

    private fun toCopy(content: LoveHealDetDetailsBean) {
        MobclickAgent.onEvent(activity, ConstantKey.UM_COPY_DIALOGUE_HEAL)
        val myClipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val myClip = ClipData.newPlainText("text", content.content)
        myClipboard.primaryClip = myClip
        ToastUtils.showCenterToast("内容已复制", true)
    }


    private fun showOpenAkpDialog(content: LoveHealDetDetailsBean) {
        val openApkPkgInfos = ArrayList<OpenApkPkgInfo>()
        val qq = OpenApkPkgInfo(1, "", "QQ", activity?.resources?.getDrawable(R.mipmap.icon_d_qq))
        val wx = OpenApkPkgInfo(2, "", "微信", activity?.resources?.getDrawable(R.mipmap.icon_d_wx))
        val mm = OpenApkPkgInfo(3, "", "陌陌", activity?.resources?.getDrawable(R.mipmap.icon_d_momo))
        //        OpenApkPkgInfo tt = new OpenApkPkgInfo(4, "", "探探", getResources().getDrawable(R.mipmap.icon_d_tt));

        val apkList = PackageUtils.getApkList(activity)
        for (i in apkList.indices) {
            val apkPkgName = apkList[i]
            when (apkPkgName) {
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

        mCollectActivity?.let {
            val openAkpDialog = OpenAkpDialog(it, openApkPkgInfos, content, true)

            openAkpDialog.show()
        }


    }

    override fun showCollectVerbalList(t: List<LoveHealDetDetailsBean>) {
        if (PAGE_NUM == 0) {
            if (t.isEmpty()) {
                top_empty_view?.let { it.visibility = View.VISIBLE }
            } else {
                top_empty_view?.let { it.visibility = View.GONE }
            }
        }

        createData(t)
    }

    override fun onComplete() {
        swipeRefreshLayout?.let {
            if (it.isRefreshing) it.isRefreshing = false
        }
    }

    override fun onError() {
        if (PAGE_NUM == 0) top_empty_view?.visibility = View.VISIBLE
        swipeRefreshLayout?.let {
            if (it.isRefreshing) it.isRefreshing = false
        }

    }

    override fun showLoading() {
        mCollectActivity?.showLoading()
    }

    override fun hideLoading() {
        mCollectActivity?.hideLoading()
    }

}

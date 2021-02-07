package com.yc.emotion.home.index.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.domain.engine.BaseModel
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.base.ui.widget.LoadDialog
import com.yc.emotion.home.index.adapter.LoveUpDownPhotoAdapter
import com.yc.emotion.home.index.presenter.LoveUpDownPresenter
import com.yc.emotion.home.index.ui.activity.LoveUpDownPhotoActivity
import com.yc.emotion.home.index.view.LoveUpDownView
import com.yc.emotion.home.model.bean.LoveHealingBean
import com.yc.emotion.home.model.bean.LoveHealingDetailBean
import com.yc.emotion.home.utils.UserInfoHelper.Companion.instance
import kotlinx.android.synthetic.main.common_topbar_view.*
import kotlinx.android.synthetic.main.fragment_love_up_down_photo.*
import kotlinx.android.synthetic.main.layout_activity_title.*
import java.util.*

/**
 * Created by mayn on 2019/5/5.
 */
class LoveUpDownPhotoFragment : BaseFragment<LoveUpDownPresenter>(), View.OnClickListener, LoveUpDownView {
    private var mDataString: String? = null
    private lateinit var mLoveUpDownPhotoActivity: LoveUpDownPhotoActivity
    private var mLoadingDialog: LoadDialog? = null
    private var mLoveEngin: BaseModel? = null
    private var mPosition = 0
    private var mIsVisibleFragment = false


    private val names = arrayOf("正在小鹿乱撞", "正在输入", "正在不知所措", "笑容逐渐浮现", "正在害羞", "心中一愣",
            "正在小鹿乱撞", "正在输入", "正在不知所措", "笑容逐渐浮现", "正在害羞", "心中一愣")


    private var mLovewordsId = 0
    private var mIsCollectLovewords = false
    private var mChildUrl: String? = null


    override fun getLayoutId(): Int {
        return R.layout.fragment_love_up_down_photo
    }

    override fun initViews() {
        mPresenter = LoveUpDownPresenter(activity, this)

        mLoveUpDownPhotoActivity = activity as LoveUpDownPhotoActivity
        mLoadingDialog = mLoveUpDownPhotoActivity.mLoadingDialog
        mLoveEngin = BaseModel(mLoveUpDownPhotoActivity)
        //        TextView tv = rootView.findViewById(R.love_id.fragment_love_up_down_photo_tv);
        initTitle()
//        mRecyclerView = rootView.findViewById(R.id.fragment_love_up_down_photo_rv)
        val layoutManager = LinearLayoutManager(mLoveUpDownPhotoActivity)
        fragment_love_up_down_photo_rv.layoutManager = layoutManager
        //        tv.setText(mDataString);
        netIdData()
    }

    override fun showLoading() {
        mLoveUpDownPhotoActivity.showLoading()
    }

    override fun hideLoading() {
        mLoveUpDownPhotoActivity.hideLoading()
    }


    private fun initTitle() {


        mLoveUpDownPhotoActivity.setStateBarHeight(activity_base_same_view_bar)
        val i = Random().nextInt(names.size)
        activity_base_same_tv_title.text = "对方" + names[i] + "..."

//        mTvSub.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_star_black, 0);
        activity_base_same_iv_back.setOnClickListener(this)
        activity_base_same_tv_sub.setOnClickListener(this)
    }

    override fun initBundle() {
        val arguments = arguments
        if (arguments != null) {
            mPosition = arguments.getInt("position")
            mDataString = arguments.getString("dataString", "-1")
            mChildUrl = arguments.getString("childUrl", "") //lovewords/recommend
        }
    }

    override fun lazyLoad() {
//        netIdData();
    }

    override fun showRecommendWords(data: List<LoveHealingBean>?) {
        data?.let {
            val loveHealingBean = it[0]
            val detail = loveHealingBean.detail
            if (detail != null && detail.size >= 1) {
                val loveHealingDetailBean = detail[0]
                mLovewordsId = loveHealingDetailBean.lovewords_id
            }
            val isCollect = loveHealingBean.is_collect
            if (isCollect > 0) { //是否收藏
                mIsCollectLovewords = true
            }
            changSubImg()
            setNewData(detail as ArrayList<LoveHealingDetailBean>)
        }

    }


    private fun netIdData() {

        mPresenter?.recommendLovewords("${instance.getUid()}", "${mPosition + 1}", "1", mChildUrl, false)

    }

    private fun setNewData(detail: ArrayList<LoveHealingDetailBean>?) {
        if (detail != null && detail.isNotEmpty()) {
            for (loveHealingDetailBean in detail) {
                val ansSex = loveHealingDetailBean.ans_sex
                when {
                    TextUtils.isEmpty(ansSex) || "null" == ansSex -> loveHealingDetailBean.type = LoveHealingDetailBean.VIEW_TITLE
                    TextUtils.equals("1", ansSex) -> loveHealingDetailBean.type = LoveHealingDetailBean.VIEW_ITEM_MEN
                    else -> loveHealingDetailBean.type = LoveHealingDetailBean.VIEW_ITEM_WOMEN
                }
            }
        }
        initRecyclerData(detail)
    }

    private fun initRecyclerData(detail: ArrayList<LoveHealingDetailBean>?) {
        val loveHealingDetailBeanTitle = LoveHealingDetailBean("")
        detail?.add(0, loveHealingDetailBeanTitle)
        val adapter = LoveUpDownPhotoAdapter(detail)
        fragment_love_up_down_photo_rv.adapter = adapter

//        LoveUpDownPhotoAdapter adapter = new LoveUpDownPhotoAdapter(detail, mRecyclerView) {
//            @Override
//            protected RecyclerView.ViewHolder getUpDownTitleHolder(ViewGroup parent) {
//                return new UpDownTitleHolder(mLoveUpDownPhotoActivity, null, parent);
//            }
//
//            @Override
//            public BaseViewHolder getMenHolder(ViewGroup parent) {
//                return new UpDownMenHolder(mLoveUpDownPhotoActivity, null, parent);
//            }
//
//            @Override
//            public BaseViewHolder getWomenHolder(ViewGroup parent) {
//                return new UpDownWomenHolder(mLoveUpDownPhotoActivity, null, parent);
//            }
//
//        };
//        mRecyclerView.setAdapter(adapter);
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.activity_base_same_iv_back -> mLoveUpDownPhotoActivity.finish()
            R.id.activity_base_same_tv_sub -> {
                val id = instance.getUid()
                if (!instance.goToLogin(mLoveUpDownPhotoActivity)) netCollect(mIsCollectLovewords, id)
            }
        }
    }

    private fun netCollect(isCollect: Boolean, userId: Int) {

        val url = if (isCollect) {
            "Lovewords/uncollect"
        } else {
            "Lovewords/collect"
        }

        mPresenter?.collectLovewords("$userId", "$mLovewordsId", url)

    }


    override fun showCollectSuccess(msg: String?) {
        msg?.let {

            mLoveUpDownPhotoActivity.showToast(msg, Toast.LENGTH_SHORT)
        }
        mIsCollectLovewords = !mIsCollectLovewords
        changSubImg()
    }

    private fun changSubImg() {
        if (mIsCollectLovewords) {
            activity_base_same_tv_sub.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_star_s, 0)
        } else {
            activity_base_same_tv_sub.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_star_black, 0)
        }

//        EventBus.getDefault().post(new EventLoveUpDownCollectChangBean(mIsCollectLovewords));
    }

    companion object {
        fun newInstance(childUrl: String?, dataString: String?, position: Int): LoveUpDownPhotoFragment {
            val fragment = LoveUpDownPhotoFragment()
            val args = Bundle()
            args.putString("dataString", dataString)
            args.putString("childUrl", childUrl)
            args.putInt("position", position)
            fragment.arguments = args
            return fragment
        }
    }
}
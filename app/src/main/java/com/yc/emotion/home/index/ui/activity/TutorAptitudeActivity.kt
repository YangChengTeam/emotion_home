package com.yc.emotion.home.index.ui.activity

import android.os.Bundle
import android.text.Html
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.index.presenter.TutorPresenter
import com.yc.emotion.home.index.view.TutorView
import com.yc.emotion.home.model.bean.TutorInfoWrapper
import com.yc.emotion.home.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_tutor_aptitude.*
import rx.Subscriber

/**
 *
 * Created by suns  on 2019/10/12 15:44.
 */
class TutorAptitudeActivity : BaseSameActivity(), TutorView {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(getLayoutId())
        initViews()
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_tutor_aptitude
    }

    override fun initViews() {

        mPresenter = TutorPresenter(this, this)
        val tutorId = intent?.getStringExtra("tutor_id")
        getData(tutorId)

    }

    override fun offerActivityTitle(): String {
        return "团队资质保障"
    }

    private fun getData(tutorId: String?) {
        (mPresenter as? TutorPresenter)?.getApitudeInfo(tutorId)

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val layoutParams = rl_aptitude_container.layoutParams as LinearLayout.LayoutParams
        var bottom = 0
        if (StatusBarUtil.isNavigationBarExist(this)) {
            bottom = StatusBarUtil.getNavigationBarHeight(this)
        }

        layoutParams.bottomMargin = bottom
        rl_aptitude_container.layoutParams = layoutParams

    }

    private fun initData(tutorInfoWrapper: TutorInfoWrapper) {

        val tutorList = tutorInfoWrapper.certs_list
        val companyInfo = tutorInfoWrapper.company_info
        val cerInfo = tutorList?.get(0)
        cerInfo?.let {

            Glide.with(this).load(cerInfo.img).apply(RequestOptions().error(R.mipmap.qualification_head).circleCrop()).into(iv_cert_face)
            tv_tutor_name.text = cerInfo.name
            tv_cert_job.text = cerInfo.cert_name
            tv_cert_organ.text = cerInfo.organ
            tv_cert_number.text = cerInfo.cert_no
            Glide.with(this).load(cerInfo.cert_img).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)).into(iv_cert_pic)
            val html = Html.fromHtml("<strong><big><font color='#000000'>" + cerInfo.name + "</font></big></strong>" + getString(R.string.guarantee_content))
            tv_deal_guarantee_content.text = html

        }

        companyInfo?.let {
            Glide.with(this).load(cerInfo?.img).apply(RequestOptions().error(R.mipmap.qualification_head).circleCrop()).into(iv_commpany_face)
            tv_commpany_name.text = cerInfo?.name
            tv_commpany_cert_name.text = "营业执照"
            tv_commpany_cert_number.text = companyInfo.business_license
            tv_commpany_cert_orian.text = companyInfo.company_name
            Glide.with(this).load(companyInfo.image).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)).into(iv_commpany_cert_pic)
        }

    }

    override fun showTutorApitude(data: TutorInfoWrapper?) {
        data?.let {
            initData(data)
        }

    }
}
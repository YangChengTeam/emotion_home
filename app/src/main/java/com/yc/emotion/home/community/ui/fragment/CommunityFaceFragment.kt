package com.yc.emotion.home.community.ui.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle

import android.view.*
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kk.utils.ScreenUtil
import com.yc.emotion.home.R
import kotlinx.android.synthetic.main.frament_community_face.*

/**
 *
 * Created by suns  on 2019/10/8 11:27.
 */
class CommunityFaceFragment : DialogFragment() {
    private var window: Window? = null
    private var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        window = dialog?.window


        if (rootView == null) {
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            rootView = inflater.inflate(R.layout.frament_community_face, container, false)

            //            window.setLayout((int) (RxDeviceTool.getScreenWidth(getActivity()) * getWidth()), getHeight());//这2行,和上面的一样,注意顺序就行;
            window?.setWindowAnimations(getAnimationId())
        }
        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)


        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }


    fun getView(resId: Int): View? {
        return rootView?.findViewById(resId)
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        if (window != null) {
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))//注意此处

            val layoutParams = window.attributes

            layoutParams.width = (ScreenUtil.getWidth(activity) * getWidth()).toInt()
            layoutParams.height = getHeight()
            window.attributes = layoutParams
        }

    }


    private fun initView() {

        val name = arguments?.getString("name")
        val face = arguments?.getString("face")

        tv_face_name.text = name

        Glide.with(this).load(face).apply(RequestOptions().error(R.mipmap.head_bomb).circleCrop()).into(iv_face)


//        val tvCancel = getView(R.id.tv_cancel) as TextView
//        val tvConfirm = getView(R.id.tv_confirm) as TextView
//
//        tvCancel.setOnClickListener { v -> dismiss() }
//
//        tvConfirm.setOnClickListener { v ->
//            if (confirmListener != null) {
//                confirmListener!!.onConfirm()
//            }
//            dismiss()
//        }
    }

    private fun getWidth(): Float {
        return 0.7f
    }


    private fun getAnimationId(): Int {
        return R.style.share_anim
    }

    private fun getHeight(): Int {
        return ViewGroup.LayoutParams.WRAP_CONTENT
    }

    protected fun getGravity(): Int {
        return Gravity.CENTER
    }


}
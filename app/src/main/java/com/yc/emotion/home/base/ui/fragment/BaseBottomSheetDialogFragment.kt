package com.yc.emotion.home.base.ui.fragment

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity

/**
 *
 * Created by suns  on 2019/10/16 11:35.
 */
abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {


    private var mContext: BaseActivity? = null
    private var dialog: BottomSheetDialog? = null
    protected var rootView: View? = null
    private var mBehavior: BottomSheetBehavior<View>? = null


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mContext = context as BaseActivity
    }

    override fun onStart() {
        super.onStart()
        val window = getDialog().window

        window?.let {
            val windowParams = window.attributes
            //这里设置透明度
            windowParams.dimAmount = 0.5f
            //        windowParams.width = (int) (ScreenUtil.getWidth(mContext) * 0.98);
            window.attributes = windowParams
            window.setWindowAnimations(R.style.share_anim)
        }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        mContext?.let {
            dialog = BottomSheetDialog(mContext!!, theme)
            if (rootView == null) {
                //缓存下来的 View 当为空时才需要初始化 并缓存
                rootView = LayoutInflater.from(mContext).inflate(getLayoutId(), null)

            }
            dialog?.setContentView(rootView)

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
                mBehavior?.peekHeight = rootView!!.height
            }
        }



        return dialog!!
    }

    abstract fun getLayoutId(): Int

    protected open fun initView() {

    }


    override fun onDestroy() {
        super.onDestroy()
        //解除缓存 View 和当前 ViewGroup 的关联
        (rootView?.parent as ViewGroup).removeView(rootView)

    }
}
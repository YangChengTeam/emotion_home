package com.yc.emotion.home.base.ui.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.DialogFragment
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.widget.LoadDialog
import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.model.util.ScreenUtils

/**
 * Created by wanglin  on 2018/3/6 11:14.
 */
abstract class BaseDialogFragment : DialogFragment(),IView {
    protected var rootView: View? = null
    private var loadingView: LoadDialog? = null
    protected var mContext: BaseActivity? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val window = dialog?.window
        if (rootView == null) {
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            rootView = inflater.inflate(getLayoutId(), container, false)
            loadingView = LoadDialog(activity)
            //            window.setLayout((int) (RxDeviceTool.getScreenWidth(getActivity()) * getWidth()), getHeight());//这2行,和上面的一样,注意顺序就行;
            window?.setWindowAnimations(animationId)
        }
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        initViews()
        return rootView
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as BaseActivity


    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        if (window != null) {
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //注意此处
            val layoutParams = window.attributes
            layoutParams.width = (ScreenUtils.getScreenWidth(activity) * width).toInt()
            layoutParams.height = height
            window.attributes = layoutParams
            window.setGravity(gravity)
        }
    }

    protected abstract val width: Float
    abstract val animationId: Int
    abstract val height: Int

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
        Runtime.getRuntime().gc()
    }

    open val gravity: Int
        get() = Gravity.CENTER
}
package com.yc.emotion.home.base.ui.popwindow

import android.app.Activity
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.PopupWindow
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.utils.StatusBarUtil
import com.yc.emotion.home.utils.UIUtils

/**
 * Created by wanglin  on 2018/3/8 16:43.
 */
abstract class BasePopWindow(activity: Activity?) : PopupWindow() {
    private var mBackgroundDrawable: ColorDrawable? = null

    protected var rootView: View
    protected var mPresenter: BasePresenter<*, *>? = null
    protected var mContext: Activity? = null


    init {
        mContext = activity
        val inflater = mContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        rootView = inflater.inflate(getLayoutId(), null)
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = getPopHeight()
        contentView = rootView
        setWindowAlpha(1f)
        setBackgroundDrawable(BitmapDrawable())
        val aid = animationID
        if (aid != 0) {
            animationStyle = aid
        }
        init()
        mPresenter?.subscribe()
    }

    protected fun setWindowAlpha(alpha: Float) {
        val window = mContext?.window
        val lp = window?.attributes
        lp?.alpha = alpha
        window?.attributes = lp
    }

    override fun dismiss() {
        super.dismiss()
        UIUtils.postDelay(Runnable { setWindowAlpha(1f) }, 300)
        mPresenter?.unSubscribe()
    }

    val animationID: Int
        get() = 0

    override fun setContentView(contentView: View?) {
        contentView?.let {
            super.setContentView(contentView)
            isFocusable = true
            isTouchable = true
            contentView.isFocusable = true
            contentView.isFocusableInTouchMode = true
            contentView.setOnKeyListener { _: View?, keyCode: Int, event: KeyEvent? ->
                when (keyCode) {
                    KeyEvent.KEYCODE_BACK -> {
                        dismiss()
                        return@setOnKeyListener true
                    }
                    else -> {
                    }
                }
                false
            }
        }

    }


    fun show(view: View? = mContext?.window?.decorView?.rootView, gravity: Int = Gravity.BOTTOM) {
        mContext?.let {

            showAtLocation(view, gravity, 0, StatusBarUtil.getStatusBarHeight(it))
        }
    }

    override fun setOutsideTouchable(touchable: Boolean) {
        super.setOutsideTouchable(touchable)
        if (touchable) {
            if (mBackgroundDrawable == null) {
                mBackgroundDrawable = ColorDrawable(0x00000000)
            }
            super.setBackgroundDrawable(mBackgroundDrawable)
        } else {
            super.setBackgroundDrawable(null)
        }
    }


    abstract fun getLayoutId(): Int
    abstract fun init()

    open fun getPopHeight(): Int {
        return ViewGroup.LayoutParams.WRAP_CONTENT
    }
}
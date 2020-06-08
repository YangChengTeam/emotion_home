package com.yc.emotion.home.base.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.base.view.IView

/**
 * Created by mayn on 2019/4/24.
 */
abstract class BaseFragment<P : BasePresenter<out IModel, out IView>?> : Fragment(), IView {

    protected lateinit var rootView: View
    private var isInitView = false
    private var visible: Boolean = false

    protected var mPresenter: P? = null

    internal var fragmentTag = ""


    //    protected BasePresenter mPresenter;
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(getLayoutId(), container, false)

        return rootView
    }

    protected open fun initBundle() {}
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBundle()
        initViews()
        isInitView = true
        isCanLoadData()
        mPresenter?.subscribe()
    }


    override fun onResume() {
        super.onResume()
        if (!isHidden && isResumed) {
            visible = true
            isCanLoadData()
        }
    }

    override fun onPause() {
        super.onPause()
        visible = false
    }

//    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
//        super.setUserVisibleHint(isVisibleToUser)
//        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见，获取该标志记录下来
//        if (isVisibleToUser) {
//            visible = true
//            isCanLoadData()
//        } else {
//            visible = false
//        }
//    }//防止重复加载数据

    //所以条件是view初始化完成并且对用户可见
    private fun isCanLoadData() {
        //所以条件是view初始化完成并且对用户可见
        if (isInitView && isVisible) {
            lazyLoad()

            //防止重复加载数据
            isInitView = false
//            visible = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        mPresenter?.unSubscribe()

    }

    /**
     * 加载要显示的数据
     */
    protected abstract fun lazyLoad()

}
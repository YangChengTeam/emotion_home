package com.yc.emotion.home.base.ui.adapter


import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.yc.emotion.home.factory.CommonPagerFactory

/**
 *
 * Created by suns  on 2019/10/9 14:15.
 * 通用的fragmentPagerAdapter
 */

class CommonMainPageAdapter(ft: FragmentManager, behavior: Int, titles: List<String>?, fragments: List<Fragment>) : FragmentPagerAdapter(ft, behavior) {


    private var mTitles: List<String>? = titles

    private var mFragments: List<Fragment> = fragments

    private var mFt = ft


    override fun getItem(position: Int): Fragment {

        return CommonPagerFactory.createFragment(position, mFragments)

    }

    override fun getCount(): Int {
        return mFragments.size
    }


    override fun getPageTitle(position: Int): CharSequence? {
//        Log.e("TAG", mTitles[position])
        return mTitles?.get(position)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Fragment {
        val fragment = super.instantiateItem(container,
                position) as Fragment
        val fragmentTransaction = mFt.beginTransaction()
        fragmentTransaction.hide(fragment)

        fragmentTransaction.show(fragment)
        /**
         * 使用的 commit方法是在Activity的onSaveInstanceState()之后调用的，这样会出错，因为
         * onSaveInstanceState方法是在该Activity即将被销毁前调用，来保存Activity数据的，如果在保存玩状态后
         * 再给它添加Fragment就会出错。解决办法就是把commit（）方法替换成 commitAllowingStateLoss()就行
         * 了，其效果是一样的。
         */
        //        fragmentTransaction.commit();
        fragmentTransaction.commitAllowingStateLoss()

        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        //        container.removeView(object);
        val fragment = CommonPagerFactory.createFragment(position, mFragments)
        val fragmentTransaction = mFt.beginTransaction()
        fragment.let {
            fragmentTransaction.hide(fragment)
        }
        fragmentTransaction.commitAllowingStateLoss()
    }


}
package com.yc.emotion.home.factory

import android.util.SparseArray
import androidx.fragment.app.Fragment

/**
 *
 * Created by suns  on 2019/11/2 10:33.
 */
object CommonPagerFactory {

    private var fragmentArray: SparseArray<Fragment>? = null

    fun createFragment(position: Int, fragments: List<Fragment>): Fragment {
        fragmentArray = SparseArray()
        var fragment = fragmentArray?.get(position)
        if (null != fragment) {
            return fragment
        }
        fragment = fragments[position]
        fragmentArray?.put(position, fragment)
        return fragment
    }
}
package com.yc.emotion.home.index.domain.bean

import com.yc.emotion.home.base.ui.activity.BaseActivity

/**
 *
 * Created by suns  on 2019/10/26 10:33.
 */
class SexInfo {
    var imgId: Int = 0
    var aClass: Class<out BaseActivity>? = null
    var umId: String? = null
    var umDesc: String? = null


    constructor()
    constructor(imgId: Int, aClass: Class<out BaseActivity>?, umId: String?, umDesc: String?) {
        this.imgId = imgId
        this.aClass = aClass
        this.umId = umId
        this.umDesc = umDesc
    }
}
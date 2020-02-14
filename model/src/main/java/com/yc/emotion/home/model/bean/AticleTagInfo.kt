package com.yc.emotion.home.model.bean

/**
 * Created by suns  on 2019/10/23 17:58.
 */
class AticleTagInfo {


    /**
     * cat_id : 29
     * cat_name : 婚姻家庭
     * parent_id : 0
     * _level : 1
     * sort : 1
     * children : [{"cat_id":33,"cat_name":"挽回老公","parent_id":29,"_level":"2","sort":500},{"cat_id":34,"cat_name":"老公出轨","parent_id":29,"_level":"2","sort":500},{"cat_id":35,"cat_name":"挽回老婆","parent_id":29,"_level":"2","sort":500},{"cat_id":36,"cat_name":"老婆外遇","parent_id":29,"_level":"2","sort":500},{"cat_id":37,"cat_name":"离婚维权","parent_id":29,"_level":"2","sort":500}]
     */

    var cat_id: Int = 0
    var cat_name: String = ""
    var parent_id: Int = 0
    var _level: String? = null
    var sort: Int = 0
    var children: List<AticleTagInfo>? = null
    override fun toString(): String {
        return "AticleTagInfo(cat_id=$cat_id, cat_name=$cat_name, parent_id=$parent_id, _level=$_level, sort=$sort, children=$children)"
    }


}

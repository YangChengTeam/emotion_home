package com.yc.emotion.home.base.listener

import java.io.File
import java.io.Serializable

/**
 * Created by mayn on 2019/5/7.
 */
interface Callback : Serializable {
    fun onSingleImageSelected(path: String?)
    fun onImageSelected(path: String?)
    fun onImageUnselected(path: String?)
    fun onCameraShot(imageFile: File?)
    fun onPreviewChanged(select: Int, sum: Int, visible: Boolean)
}
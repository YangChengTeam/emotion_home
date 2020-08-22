package com.yc.emotion.home.base.ui.widget

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.yc.emotion.home.R

/**
 * Created by Administrator on 2017/9/22.
 */
abstract class SelectPhotoDialog {
    //    private TextView tv_look;
    var alertDialog: AlertDialog? = null
    fun instanceDialog(context: Context?) {
        alertDialog = AlertDialog.Builder(context!!, R.style.Dialog_FS).create()
        alertDialog.show()
        alertDialog.setCancelable(true)
        val window = alertDialog.getWindow()
        window.setContentView(R.layout.dialog_select_photo)
        window.setWindowAnimations(R.style.DialogBottom) // 添加动画
        window.setGravity(Gravity.BOTTOM)
        val tv_album = window.findViewById<TextView>(R.id.dialog_select_photo_tv_album)
        val tv_camera = window.findViewById<TextView>(R.id.dialog_select_photo_tv_camera)
        val tv_cancel = window.findViewById<TextView>(R.id.dialog_select_photo_tv_cancel)
        tv_album.setOnClickListener(clickAlbum)
        tv_camera.setOnClickListener(clickCamera)
        tv_cancel.setOnClickListener(clickCancel)
    }

    private val clickAlbum = View.OnClickListener { view: View? -> clickAlbum() }
    private val clickCamera = View.OnClickListener { view: View? -> clickCamera() }
    var clickCancel = View.OnClickListener {
        clickCancelOther()
        alertDialog!!.dismiss()
    }

    protected fun clickCancelOther() {}
    protected abstract fun clickAlbum()
    protected abstract fun clickCamera()
    fun dialogDismiss() {
        if (alertDialog != null) {
            alertDialog!!.dismiss()
        }
    }
}
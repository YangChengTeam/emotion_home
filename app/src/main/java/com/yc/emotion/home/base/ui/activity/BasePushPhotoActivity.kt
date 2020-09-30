package com.yc.emotion.home.base.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BasePushPhotoActivity
import com.yc.emotion.home.base.ui.widget.SelectPhotoDialog
import com.yc.emotion.home.index.ui.view.imgs.ISListConfig
import com.yc.emotion.home.index.ui.view.imgs.ISNav
import com.yc.emotion.home.index.ui.view.imgs.ImageLoader
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File

/**
 * Created by mayn on 2019/5/7.
 */
abstract class BasePushPhotoActivity : BaseSameActivity() {
    private var putPhotoImageViewPhoto: ImageView? = null
    private var selectPhotoDialog: SelectPhotoDialog? = null

    //    private static final int REQUEST_CROP = 6569;
    private var mInstance: ISNav? = null
    fun showSelsctPhotoDialog(putPhotoImageViewPhoto: ImageView?) {
        this.putPhotoImageViewPhoto = putPhotoImageViewPhoto
        initPhotoDialog()
        // 自定义图片加载器
        if (mInstance == null) {
            mInstance = ISNav.getInstance()
            mInstance?.init { context: Context, path: String, imageView: ImageView? ->
                Glide.with(context).load("file://$path").into(imageView!!)
                Log.d("mylog", "displayImage: path $path")
            }
        }
    }

    private fun initPhotoDialog() {
        selectPhotoDialog = object : SelectPhotoDialog() {
            override fun clickAlbum() {
                openAlbum()
                selectPhotoDialog!!.dialogDismiss()
            }

            override fun clickCamera() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), CAMERA_REQUEST_CODE)
                } else {
                    openCamera()
                }
            }
        }
        selectPhotoDialog?.instanceDialog(this)
    }

    fun openAlbum() {
//        ImageSelectorUtils.openPhotoAndClip(this, TAKE_PICTURE_ALBUM);
        // 自由配置选项
        val config = ISListConfig.Builder() // 是否多选, 默认true
                .multiSelect(false) // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                .rememberSelected(false) // “确定”按钮背景色
                .btnBgColor(Color.GRAY) // “确定”按钮文字颜色
                .btnTextColor(Color.BLUE) // 使用沉浸式状态栏
                .statusBarColor(Color.WHITE) // 返回图标ResId
                .backResId(R.mipmap.icon_arr_lift_black) // 标题
                .title("图片") // 标题文字颜色
                .titleColor(Color.BLACK) // TitleBar背景色
                .titleBgColor(Color.WHITE) // 裁剪大小。needCrop为true的时候配置
                .cropSize(1, 1, 200, 200)
                .needCrop(true) // 第一个是否显示相机，默认true
                .needCamera(false) // 最大选择图片数量，默认9
                .maxNum(9)
                .build()
        // 跳转到图片选择器
        ISNav.getInstance().toListActivity(this, config, TAKE_PICTURE_ALBUM)
    }

    private fun openCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val builder = VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build()) // 开启
            //            builder.detectFileUriExposure();
        }
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val imageUri = Uri.fromFile(File(Environment.getExternalStorageDirectory(), PICTURE_FILE))
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, TAKE_PICTURE_CAMERA)

        /*ISCameraConfig config = new ISCameraConfig.Builder().needCrop(true)
                // 裁剪
                .cropSize(1, 1, 200, 200).build();
        ISNav.getInstance().toCameraActivity(this, config, TAKE_PICTURE_CAMERA);*/selectPhotoDialog!!.dialogDismiss()
    }

    override fun onRequestPermissionsResult(permsRequestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        Log.d("mylog", "onRequestPermissionsResult: permsRequestCode $permsRequestCode")
        if (permsRequestCode == CAMERA_REQUEST_CODE) {
            val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
            if (storageAccepted) {
                openCamera()
            } else {
                showToast("没有获取到拍照的权限", Toast.LENGTH_SHORT)
                Log.d("ssss", "没有权限操作这个请求")
                selectPhotoDialog!!.dialogDismiss()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        //        Bitmap photo = null;
        var file: File? = null
        when (requestCode) {
            TAKE_PICTURE_ALBUM -> {
                val images = data!!.getStringArrayListExtra("result")
                for (path in images) {
//                    tvResult.append(path + "\n");
                    Log.d("mylog", "onActivityResult: path $path")
                }

                /*//获取选择器返回的数据
                ArrayList<String> images = data.getStringArrayListExtra(
                        ImageSelectorUtils.SELECT_RESULT);*/if (images.size <= 0) {
                    return
                }
                val image = images[0]
                if (TextUtils.isEmpty(image)) {
                    return
                }
                file = File(image)
            }
            TAKE_PICTURE_CAMERA ->              /*   String path = data.getStringExtra("result"); // 图片地址
                if (TextUtils.isEmpty(path)) {
                    return;
                }
                file = new File(path);*/file = File(Environment.getExternalStorageDirectory()
                    .toString() + "/" + PICTURE_FILE)
            else -> {
            }
        }
        if (file == null) {
            showToast("获取图片失败--file", Toast.LENGTH_SHORT)
            return
        }
        Luban.with(this)
                .load(file) // 传人要压缩的图片列表
                .ignoreBy(100) // 忽略不压缩图片的大小
                //                .setTargetDir(this.getFileStreamPath("certificatess").getAbsolutePath())                        // 设置压缩后文件存储位置
                .setCompressListener(object : OnCompressListener {
                    //设置回调
                    override fun onStart() {
                        //  压缩开始前调用，可以在方法内启动 loading UI
                        Log.d("mylog", "onStart: dddddddddddd")
                    }

                    override fun onSuccess(file: File) {
                        //        View 设置图片
                        Glide.with(this@BasePushPhotoActivity).load(file).apply(RequestOptions.circleCropTransform()).into(putPhotoImageViewPhoto!!)
                        Log.d("mylog", "onSuccess: file.getPath() " + file.path)
                        onLubanFileSuccess(file)
                    }

                    override fun onError(e: Throwable) {
                        Log.d("mylog", "onError: $e")
                        //  当压缩过程出现问题时调用
                    }
                }).launch() //启动压缩
    }

    protected abstract fun onLubanFileSuccess(file: File?)

    companion object {
        //    private static final String PICTURE_FILE = "temp.jpg";
        //    private File mImageFile;
        private const val TAKE_PICTURE_ALBUM = 666
        private const val TAKE_PICTURE_CAMERA = 667
        private const val CAMERA_REQUEST_CODE = 5241
        const val PICTURE_FILE = "temp.jpg"
    }
}
package com.yc.emotion.home.pay.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.snackbar.Snackbar
import com.qw.soul.permission.SoulPermission
import com.qw.soul.permission.bean.Permission
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener
import com.tencent.connect.share.QQShare
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXImageObject
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.base.ui.widget.ShareShowImgDialog
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.pay.ui.activity.ResultActivity
import kotlinx.android.synthetic.main.activity_result.*
import java.io.*

class ResultActivity : BaseSameActivity() {
    private var bitmapShow: Bitmap? = null
    private var mCreateTitle: String? = null
    private var mResImagePath: String? = null
    private lateinit var tencent: Tencent
    private lateinit var mMsgApi: IWXAPI
    private var mFilePath: String? = null
    private var fileBitmap: Bitmap? = null
//    private var mImageView: ImageView? = null
    private val mIsRepeat = false
    override fun initIntentData() {
        val intent = intent
        mResImagePath = intent.getStringExtra("resImagePath")
        mCreateTitle = intent.getStringExtra("createTitle")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_result
    }

    override fun initViews() {
        if (TextUtils.isEmpty(mResImagePath)) {
            showToast("获取图片失败", Toast.LENGTH_SHORT)
            finish()
            return
        }
        tencent = Tencent.createInstance(ConstantKey.TENCENT_APP_ID, applicationContext)
        mMsgApi = WXAPIFactory.createWXAPI(this, null)
        // 将该app注册到微信
        mMsgApi.registerApp("wxe224386e89afc8c1")
        mBaseSameTvSub.text = "分享"
        mBaseSameTvSub.setOnClickListener(this)
        mBaseSameTvSub.setTextColor(resources.getColor(R.color.red_crimson))
        mBaseSameTvSub.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_to_share, 0, 0, 0)
        mBaseSameTvSub.setOnClickListener(this)
//        mImageView = findViewById(R.id.result_iv_img)
//        val shareLayoutImg = findViewById<ImageView>(R.id.result_iv_share_img)
        creatBitmapLoadImg()
        result_iv_share_img.setOnClickListener(this)
    }

    private fun creatBitmapLoadImg() {  //网络图片转Bitmap 对象
        Log.d("mylog", "initViews: mResImagePath  $mResImagePath")
        if (TextUtils.isEmpty(mResImagePath)) {
            return
        }
        mLoadingDialog?.showLoadingDialog()
        Glide.with(this@ResultActivity).asBitmap().load(mResImagePath).into(object : SimpleTarget<Bitmap>() {
            override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
                Log.d("mylog", "onLoadFailed: ")
                mLoadingDialog?.dismissLoadingDialog()
            }

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                fileBitmap = resource
                result_iv_img.setImageBitmap(fileBitmap)
                //                mFilePath = saveToSystemGallery(bitmap);
                Log.d("mylog", "onBitmapLoaded: fileBitmap $fileBitmap")
                mLoadingDialog?.dismissLoadingDialog()
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                super.onLoadCleared(placeholder)
                Log.d("mylog", "onLoadCleared: ")
                mLoadingDialog?.dismissLoadingDialog()
            }


        })
        /*Picasso.with(ResultActivity.this).load(mResImagePath).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                fileBitmap = bitmap;
                mImageView.setImageBitmap(fileBitmap);
//                mFilePath = saveToSystemGallery(bitmap);

                Log.d("mylog", "onBitmapLoaded: fileBitmap " + fileBitmap);

                if (fileBitmap != null) {
                    mLoadingDialog.dismissLoadingDialog();
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.d("mylog", "onBitmapFailed:  fileBitmap " + fileBitmap);
                if (fileBitmap == null) {
                    creatBitmapLoadImg();
                }
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.d("mylog", "onPrepareLoad: fileBitmap " + fileBitmap);
                Log.d("mylog", "onPrepareLoad: placeHolderDrawable " + placeHolderDrawable);

                */
        /*if (fileBitmap == null) {
                    creatBitmapLoadImg();
                }*/
        /*

         */
        /* if (fileBitmap != null || mIsRepeat) {
                    mLoadingDialog.dismissLoadingDialog();
                } else {
                    mImageView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mIsRepeat) {
                                return;
                            }
                            creatBitmapLoadImg();
                            mIsRepeat = true;
                        }
                    }, 200);
                }*/
        /*
            }
        });*/
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.activity_base_same_tv_sub, R.id.result_iv_share_img -> showShareDialog()
        }
    }

    private fun showShareDialog() {
        if (isValidContext(this@ResultActivity)) {
            if (bitmapShow == null) {
                bitmapShow = BitmapFactory.decodeResource(resources, R.mipmap.share_fight_default)
            }
            val shareShowImgDialog = ShareShowImgDialog(this@ResultActivity, mResImagePath)
            shareShowImgDialog.show()
            shareShowImgDialog.setOnClickShareItemListent { postion: Int ->
                when (postion) {
                    0 -> {
                        mFilePath = saveToSystemGallery(fileBitmap)
                        if (TextUtils.isEmpty(mFilePath)) {
                            showToast("获取图片资源失败，请稍后再试", Toast.LENGTH_SHORT)
                            return@setOnClickShareItemListent
                        }
                        sharePhotoToQQ(this@ResultActivity, tencent, QQShareIUiListener())
                    }
                    1 -> {
                        mFilePath = saveToSystemGallery(fileBitmap)
                        if (TextUtils.isEmpty(mFilePath)) {
                            showToast("获取图片资源失败，请稍后再试", Toast.LENGTH_SHORT)
                            return@setOnClickShareItemListent
                        }
                        sharePhotoToWeChat()
                    }
                    2 -> {
                        mFilePath = saveToSystemGallery(fileBitmap)
                        if (TextUtils.isEmpty(mFilePath)) {
                            showToast("获取图片资源失败，请稍后再试", Toast.LENGTH_SHORT)
                            return@setOnClickShareItemListent
                        }
                        saveToSystemGallery(fileBitmap, true)
                    }
                }
            }
        }
    }

    private fun sharePhotoToWeChat() {

//        第一步：判读图像文件是否存在
//        String path ="/storage/emulated/0/image/123.jpg";
        val path = mFilePath
        val file = File(path)
        if (!file.exists()) {
            showToast("文件不存在", Toast.LENGTH_SHORT)
        }

//        第二步：创建WXImageObject，
        val imgObj = WXImageObject()
        //        设置文件的路径
        imgObj.setImagePath(path)
        //        第三步：创建WXMediaMessage对象，并包装WXimageObjext对象
        val msg = WXMediaMessage()
        msg.mediaObject = imgObj
        //        第四步：压缩图片
        val bitmap = BitmapFactory.decodeFile(path)
        val thumBitmap = Bitmap.createScaledBitmap(bitmap, 120, 150, true)
        //        释放图片占用的内存资源
        bitmap.recycle()
        msg.thumbData = bitmapToByteArray(thumBitmap, true) //压缩图
        //        第五步：创建SendMessageTo.Req对象，发送数据
        val req = SendMessageToWX.Req()
        //        唯一标识
        req.transaction = buildTransaction("img")
        //        发送的内容或者对象
        req.message = msg
        //        req.scene = send_friend.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        req.scene = SendMessageToWX.Req.WXSceneSession
        mMsgApi.sendReq(req)
    }

    private fun bitmapToByteArray(bitmap: Bitmap, recycle: Boolean): ByteArray {
        val output = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
        if (recycle) {
            bitmap.recycle()
        }
        val result = output.toByteArray()
        try {
            output.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    private fun buildTransaction(type: String?): String {
        return if (type == null) System.currentTimeMillis().toString() else type + System.currentTimeMillis()
    }

    private fun sharePhotoToQQ(activity: Activity, tencent: Tencent, iUiListener: IUiListener) {
        val params = Bundle()
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, mFilePath)
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.getString(R.string.app_name))
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE)
        //        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        activity.runOnUiThread { tencent.shareToQQ(activity, params, iUiListener) }
    }

    private inner class QQShareIUiListener : IUiListener {
        override fun onComplete(o: Any) {
            // 操作成功
//            showToastShort(PostManageActivity.this, "QQShare--操作成功");
            Log.d("mylog", "onComplete: QQShare--操作成功 ")
            //            onQQShareSuccessListent.onQQShareSuccess();
        }

        override fun onError(uiError: UiError) {
            // 分享异常
            Log.d("mylog", "onComplete: QQShare--分享异常 ")
            showToast("QQShare--分享异常" + uiError.errorCode + " " + uiError.errorDetail + " " + uiError.errorMessage, Toast.LENGTH_SHORT)
        }

        override fun onCancel() {
            // 取消分享
            Log.d("mylog", "onComplete: QQShare--取消分享 ")
            //            showToastShort(PostManageActivity.this, "QQShare--取消分享");
        }
    }

    private fun isValidContext(ctx: Context): Boolean {
        val activity = ctx as Activity
        return !(activity.isDestroyed || activity.isFinishing)
    }

    /**
     * Bitmap 转成 本地图片
     *
     * @param bmp Bitmap对象
     * @return
     */
    private fun saveToSystemGallery(bmp: Bitmap?): String? {
        if (bmp == null) {
            creatBitmapLoadImg()
            return ""
        }
        return if (!TextUtils.isEmpty(mFilePath)) {
            mFilePath
        } else saveToSystemGallery(bmp, false)
    }

    private fun saveToSystemGallery(bmp: Bitmap?, isShowToast: Boolean): String {
        Log.d("mylog", "saveToSystemGallery: bmp $bmp")

        // 首先保存图片
//        File fileDir = new File(Environment.getExternalStorageDirectory(), SdPathConfig.SAVE_IMG_PATH);
        val fileDir = File(Environment.getExternalStorageDirectory().path)
        if (!fileDir.exists()) {
            fileDir.mkdir()
        }
        val fileName = System.currentTimeMillis().toString() + ".jpg"
        val file = File(fileDir, fileName)
        try {
            val fos = FileOutputStream(file)
            bmp?.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(contentResolver,
                    file.absolutePath, fileName, null)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        // 最后通知图库更新
        //sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getAbsolutePath())));
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val uri = Uri.fromFile(file)
        intent.data = uri
        sendBroadcast(intent)
        //图片保存成功，图片路径：
        if (isShowToast) {
            val filePath = file.absolutePath
            Log.d("mylog", "onClick: filePath  filePath---------- $filePath")
            if (Build.VERSION.SDK_INT < 24) {
                Snackbar.make(result_iv_img, "图片已保存至相册", Snackbar.LENGTH_LONG)
                        .setAction("查看") { v: View? ->
                            SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,  //if you want do noting or no need all the callbacks you may use SimplePermissionAdapter instead
                                    object : CheckRequestPermissionListener {
                                        override fun onPermissionOk(permission: Permission) {
                                            val photoInten = Intent()
                                            val path = file.absolutePath //图片路径
                                            val file1 = File(path)
                                            Log.d("mylog", "onPermissionOk: path $path")
                                            Log.d("mylog", "onPermissionOk: file $file1")
                                            Log.d("mylog", "onPermissionOk: file.exists() " + file1.exists())
                                            val uri1: Uri
                                            photoInten.action = Intent.ACTION_VIEW
                                            if (Build.VERSION.SDK_INT >= 24) {
                                                uri1 = FileProvider.getUriForFile(applicationContext, "$packageName.provider", file1)
                                                photoInten.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                            } else {
                                                uri1 = Uri.fromFile(file1)
                                            }
                                            photoInten.setDataAndType(uri1, "image/*")
                                            startActivity(photoInten)
                                        }

                                        override fun onPermissionDenied(permission: Permission) {}
                                    })
                        }
                        .show()
            } else {
//                showToastShort("图片已保存路径：" + file.getAbsolutePath());
                showToast("图片已保存至设备图库", Toast.LENGTH_SHORT)
            }
        }
        //        Toast.makeText(this,
//                "图片保存路径：" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        return file.absolutePath
    }

    override fun onDestroy() {
        super.onDestroy()
        if (fileBitmap != null) {
            fileBitmap = null
        }
    }

    override fun offerActivityTitle(): String? {
        if (TextUtils.isEmpty(mCreateTitle)) {
            mCreateTitle = "合成成功"
        }
        return mCreateTitle
    }

    companion object {
        fun startResultActivity(context: Context, resImagePath: String?, createTitle: String?) {
            val intent = Intent(context, ResultActivity::class.java)
            intent.putExtra("resImagePath", resImagePath)
            intent.putExtra("createTitle", createTitle)
            context.startActivity(intent)
        }
    }
}
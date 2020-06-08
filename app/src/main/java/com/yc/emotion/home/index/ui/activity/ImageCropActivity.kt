package com.yc.emotion.home.index.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import com.isseiaoki.simplecropview.CropImageView
import com.isseiaoki.simplecropview.callback.CropCallback
import com.isseiaoki.simplecropview.callback.SaveCallback
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.utils.HeadImageUtils
import kotlinx.android.synthetic.main.activity_image_crop.*
import java.io.File

class ImageCropActivity : BaseSameActivity() {
    private var options: DisplayImageOptions? = null
    private var fileName = "temp_crop.jpg"
    private lateinit var imageLoader: ImageLoader

    //    private var mCropImageView: CropImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
        initData()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_image_crop
    }

    override fun initViews() {
        imageLoader = ImageLoader.getInstance()
        imageLoader.init(ImageLoaderConfiguration.createDefault(this))
//        mCropImageView = findViewById(R.id.cropImageView)
        crop_confirm_btn.setOnClickListener(this)
        crop_confirm_btn.setOnClickListener(this)
        mBaseSameTvSub.text = "确定"
        mBaseSameTvSub.setOnClickListener(this)
    }

    private fun initData() {
        //清除历史路径数据
        HeadImageUtils.imgResultPath = null
        options = DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .displayer(FadeInBitmapDisplayer(50))
                .build()
        if (HeadImageUtils.imgPath != null && HeadImageUtils.imgPath.isNotEmpty()) {
            fileName = HeadImageUtils.imgPath.substring(HeadImageUtils.imgPath.lastIndexOf("/") + 1, HeadImageUtils.imgPath.length)

            //加载本地图片，需要在图片前面加上前缀 "file:///"
            imageLoader.displayImage("file:///" + HeadImageUtils.imgPath, cropImageView, options)
            val intent = intent
            val bundle = intent.extras
            if (bundle != null && bundle.getInt("xcrop") > 0 && bundle.getInt("ycrop") > 0) {
                cropImageView.setCustomRatio(bundle.getInt("xcrop"), bundle.getInt("ycrop"))
            } else {
                //设置自由裁剪
                cropImageView.setCropMode(CropImageView.CropMode.FREE)
            }
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.crop_cancel_btn -> finish()
            R.id.crop_confirm_btn, R.id.activity_base_same_tv_sub -> {
                //                loadDialog = new ProgressDialog(ImageCropActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
//                loadDialog.setMessage("图片裁剪中...");
                if (isValidContext(this@ImageCropActivity)) {
                    mLoadingDialog?.showLoadingDialog()
                }
                cropImageView.startCrop(createSaveUri(), mCropCallback, mSaveCallback)
            }
        }
    }

    private fun createSaveUri(): Uri {
        return Uri.fromFile(File(externalCacheDir, "cropped"))
    }

    private val mCropCallback: CropCallback = object : CropCallback {
        override fun onSuccess(cropped: Bitmap) {
            HeadImageUtils.cropBitmap = cropped
        }

        override fun onError() {
            if (isValidContext(this@ImageCropActivity)) {
                mLoadingDialog?.let {
                    if (it.isShowing) {
                        it.dismissLoadingDialog()
                    }
                }
            }
            finish()
        }
    }
    private val mSaveCallback: SaveCallback = object : SaveCallback {
        override fun onSuccess(outputUri: Uri) {

            /*if (outputUri != null) {
                //Log.e("outputUri is not null","outputUri is not null---");

                try {
                    HeadImageUtils.cropBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), outputUri);
                    Toast.makeText(context, "not null"+ HeadImageUtils.cropBitmap.getWidth(), Toast.LENGTH_SHORT).show();
                    cancelBtn.setImageBitmap(HeadImageUtils.cropBitmap);
                    HeadImageUtils.cutPhoto = outputUri;
                    Toast.makeText(context, HeadImageUtils.cropBitmap +"", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //Log.e("outputUri null-----"," outputUri null-----");
                Toast.makeText(context, "uri null", Toast.LENGTH_SHORT).show();
            }*/

            /*if (!TextUtils.isEmpty(outputUri.getAuthority())) {
                Cursor cursor = getContentResolver().query(outputUri,
                        new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                if (null == cursor) {
                    Toast.makeText(context, "图片没找到", Toast.LENGTH_SHORT).show();
                    return;
                }
                cursor.moveToFirst();
                HeadImageUtils.imgResultPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
                Toast.makeText(context, "4444444", Toast.LENGTH_SHORT).show();
            } else {
                //HeadImageUtils.imgResultPath = outputUri.getPath();
                Toast.makeText(context, "555555", Toast.LENGTH_SHORT).show();
            }*/
            if (isValidContext(this@ImageCropActivity)) {
                mLoadingDialog?.let {
                    if (it.isShowing) {
                        it.dismissLoadingDialog()
                    }
                }

            }
            val intent = Intent()
            setResult(HeadImageUtils.FREE_CUT, intent)
            finish()
        }

        override fun onError() {
            if (isValidContext(this@ImageCropActivity) ) {
                mLoadingDialog?.let {
                    if (it.isShowing) {
                        it.dismissLoadingDialog()
                    }
                }
            }
            finish()
        }
    }

    private fun isValidContext(ctx: Context): Boolean {
        val activity = ctx as Activity
        return !(activity.isDestroyed || activity.isFinishing)
    }

    override fun offerActivityTitle(): String? {
        return "裁剪图片"
    }
}
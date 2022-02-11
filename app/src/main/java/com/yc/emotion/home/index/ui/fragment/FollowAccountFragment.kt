package com.yc.emotion.home.index.ui.fragment

import android.content.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.BaseDialogFragment
import com.yc.emotion.home.mine.ui.fragment.ExitPublishFragment
import com.yc.emotion.home.utils.ThreadPoolUtils
import com.yc.emotion.home.utils.ToastUtils
import com.yc.emotion.home.utils.clickWithTrigger
import java.io.*


/**
 * Created by suns  on 2021/12/01 15:06 .
 * 关注公众号
 */
class FollowAccountFragment : BaseDialogFragment() {
    override val width: Float
        get() = 0.8f
    override val animationId: Int
        get() = R.style.share_anim
    override val height: Int
        get() = ViewGroup.LayoutParams.WRAP_CONTENT

    override fun getLayoutId(): Int {
        return R.layout.fragment_follow_account
    }

    override fun initViews() {
        rootView?.findViewById<ImageView>(R.id.iv_close)?.clickWithTrigger { dismiss() }
        rootView?.findViewById<TextView>(R.id.tv_save_code)?.clickWithTrigger {//保存二维码
            saveImg()
        }
        rootView?.findViewById<TextView>(R.id.tv_copy_wx)?.clickWithTrigger { //复制公众号
            //lj18508609

            val myClipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val myClip = ClipData.newPlainText("text", "lj18508609")
            myClipboard.setPrimaryClip(myClip)
            showWxDialog()
            dismiss()
        }

    }

    private fun showWxDialog() {
        val toWxFragment = ToWxFragment()
        toWxFragment.show(requireActivity().supportFragmentManager, "")
    }

    private fun saveImg() {
        ThreadPoolUtils(ThreadPoolUtils.FixedThread, 1).execute {
            try {
                val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.code)
                val file = File(context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "code.jpg")
                if (!file.exists()) {
                    file.createNewFile()
                }
                //添加水印文字位置。

                //保存到系统相册
                savePhotoAlbum(bitmap, file)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 保存到相册
     *
     * @param src  源图片
     * @param file 要保存到的文件
     */
    private fun savePhotoAlbum(src: Bitmap, file: File) {

        //先保存到文件
        var outputStream: OutputStream?
        try {
            outputStream = BufferedOutputStream(FileOutputStream(file))
            src.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            if (!src.isRecycled) {
                src.recycle()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        //再更新图库
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues()
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
            values.put(MediaStore.MediaColumns.MIME_TYPE, "image/vnd.google.panorama360+jpg")
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
            val contentResolver: ContentResolver = requireActivity().contentResolver
            val uri: Uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                    ?: return
            try {
                outputStream = contentResolver.openOutputStream(uri)
                val fileInputStream = FileInputStream(file)
                copy(fileInputStream, outputStream)

                fileInputStream.close()
                outputStream?.close()

            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            MediaScannerConnection.scanFile(
                    context?.applicationContext, arrayOf(file.absolutePath), arrayOf("image/jpeg")
            ) { path, uri -> }
        }
        requireActivity().runOnUiThread {
            ToastUtils.showCenterToast("二维码已保存在相册中")
            showWxDialog()
            dismiss()

        }
    }

    @Throws(IOException::class)
    private fun copy(fis: FileInputStream, os: OutputStream?) {
        val bytes = ByteArray(1024)
        var len: Int
        while (fis.read(bytes).also { len = it } != -1) {
            try {
                os?.write(bytes, 0, len)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}
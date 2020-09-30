package com.yc.emotion.home.skill.ui.activity

import android.graphics.*
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.utils.clickWithTrigger
import kotlinx.android.synthetic.main.activity_promotion_plan.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

/**
 *
 * Created by suns  on 2020/8/28 17:08.
 */
class PromotionPlanActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        invadeStatusBar()
        setAndroidNativeLightStatusBar()
        initViews()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_promotion_plan
    }

    override fun initViews() {
        showLoadingDialog()
        Glide.with(this).asBitmap().load(R.mipmap.promotion_plan_bg).diskCacheStrategy(DiskCacheStrategy.DATA).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                setBitmapToImg(resource)
            }
        })

        initListener()

    }

    private fun initListener() {

        iv_back.clickWithTrigger { finish() }
        comp_main_iv_to_wx.clickWithTrigger {
            showToWxServiceDialog()
        }
    }

    private fun setBitmapToImg(resource: Bitmap) {
        try {
            val mRect = Rect()
            val baos = ByteArrayOutputStream()
            resource.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val isBm: InputStream = ByteArrayInputStream(baos.toByteArray())

            //BitmapRegionDecoder newInstance(InputStream is, boolean isShareable)
            //用于创建BitmapRegionDecoder，isBm表示输入流，只有jpeg和png图片才支持这种方式，
            // isShareable如果为true，那BitmapRegionDecoder会对输入流保持一个表面的引用，
            // 如果为false，那么它将会创建一个输入流的复制，并且一直使用它。即使为true，程序也有可能会创建一个输入流的深度复制。
            // 如果图片是逐步解码的，那么为true会降低图片的解码速度。如果路径下的图片不是支持的格式，那就会抛出异常
            val decoder: BitmapRegionDecoder = BitmapRegionDecoder.newInstance(isBm, true)
            val imgWidth: Int = decoder.width
            val imgHeight: Int = decoder.height
            val opts: BitmapFactory.Options = BitmapFactory.Options()

            //计算图片要被切分成几个整块，
            // 如果sum=0 说明图片的长度不足3000px，不进行切分 直接添加
            // 如果sum>0 先添加整图，再添加多余的部分，否则多余的部分不足3000时底部会有空白
            val sum = imgHeight / 3000
            val redundant = imgHeight % 3000
            val bitmapList: MutableList<Bitmap> = ArrayList()

            //说明图片的长度 < 3000
            if (sum == 0) {
                //直接加载
                bitmapList.add(resource)
            } else {
                //说明需要切分图片
                for (i in 0 until sum) {
                    //需要注意：mRect.set(left, top, right, bottom)的第四个参数，
                    //也就是图片的高不能大于这里的4096
                    mRect.set(0, i * 3000, imgWidth, (i + 1) * 3000)
                    val bm: Bitmap = decoder.decodeRegion(mRect, opts)
                    bitmapList.add(bm)
                }

                //将多余的不足3000的部分作为尾部拼接
                if (redundant > 0) {
                    mRect.set(0, sum * 3000, imgWidth, imgHeight)
                    val bm: Bitmap = decoder.decodeRegion(mRect, opts)
                    bitmapList.add(bm)
                }
            }
            val bigbitmap: Bitmap = Bitmap.createBitmap(imgWidth, imgHeight, Bitmap.Config.ARGB_8888)
            val bigcanvas = Canvas(bigbitmap)
            val paint = Paint()
            var iHeight = 0f

            //将之前的bitmap取出来拼接成一个bitmap
            for (i in bitmapList.indices) {
                val bmp: Bitmap = bitmapList[i]
                bigcanvas.drawBitmap(bmp, 0f, iHeight, paint)
                iHeight += bmp.height
                bmp.recycle()

            }
            iv_promotion_plan.setImageBitmap(bigbitmap)
            hideLoadingDialog()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
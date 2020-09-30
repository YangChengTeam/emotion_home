package com.yc.emotion.home.index.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.text.InputFilter
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.qw.soul.permission.SoulPermission
import com.qw.soul.permission.bean.Permission
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.statistics.common.DeviceConfig
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.base.constant.URLConfig
import com.yc.emotion.home.index.presenter.ExpressPresenter
import com.yc.emotion.home.index.view.ExpressView
import com.yc.emotion.home.model.bean.confession.ConfessionDataBean
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.model.util.InputLenLimit
import com.yc.emotion.home.model.util.SPUtils
import com.yc.emotion.home.model.util.ScreenUtils
import com.yc.emotion.home.model.util.SizeUtils
import com.yc.emotion.home.pay.ui.activity.ResultActivity
import com.yc.emotion.home.utils.HeadImageUtils
import com.yc.emotion.home.utils.PhoneIMEIUtil
import kotlinx.android.synthetic.main.activity_create_before.*
import me.iwf.photopicker.PhotoPicker
import java.io.File
import java.io.FileOutputStream
import java.util.*

class CreateBeforeActivity : BaseSameActivity(), ExpressView {


    private var mConfessionDataBean: ConfessionDataBean? = null
    private var cWidth: Int = 0
    private var cHeight: Int = 0

    private var createSelectImageView: ImageView? = null

    //    private CustomProgress dialog;
    private val timeNum = 0
    private var isChooseImage: Boolean = false
    private var mSelectedImages: List<String>? = null

    //    private var mNormalPresenter: NormalPresenter? = null
    private var netCompoundRequestData: Map<String, String>? = null

    override fun initIntentData() {
        val bundle = intent.extras
        mConfessionDataBean = bundle?.getSerializable("zb_data_info") as ConfessionDataBean
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())


        //        mRequestMap = new HashMap<>();
        //        mDataUrl = URLConfig.CATEGORY_LIST_URL;
        initViews()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_create_before
    }

    override fun initViews() {

        mPresenter = ExpressPresenter(this, this)

        initData()
    }

    private fun initData() {

        Log.d("mylog", "initData: mConfessionDataBean $mConfessionDataBean")

        val maxWidth = ScreenUtils.getScreenWidth(this)
        val maxHeight = ScreenUtils.getScreenHeight(this) / 2

        if (mConfessionDataBean != null) {
            Glide.with(this).asBitmap().load(mConfessionDataBean?.front_img).into(object : BitmapImageViewTarget(iv_create_bg_iv) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                    val rWidth = resource.width.toDouble()
                    val rHeight = resource.height.toDouble()

                    //实际的宽高
                    var inputHeight = (maxHeight - 100).toDouble()
                    val scale = rWidth / rHeight
                    var inputWidth = inputHeight * scale

                    if (inputWidth > maxWidth) {
                        inputWidth = (maxWidth - 100).toDouble()
                        inputHeight = inputWidth / scale
                    }

                    iv_create_bg_iv.layoutParams.width = inputWidth.toInt()
                    iv_create_bg_iv.layoutParams.height = inputHeight.toInt()
                    iv_create_bg_iv.setImageBitmap(resource)

                    //                    mLoadingLayout.setVisibility(View.GONE);
                    Log.d("mylog", "onResourceReady: " + "w-->" + resource.width + "---h-->" + resource.height)
                    createInputView()
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    Log.d("mylog", "onLoadFailed: ")
                }

            })

        }
    }

    private fun createInputView() {

        mConfessionDataBean?.let {
            it.field?.let { item ->


                val paddingLeft = SizeUtils.dp2px(this, 10f)
                val textSize = SizeUtils.sp2px(this, 6f)
                val tHeight = SizeUtils.dp2px(this, 38f)
                val tMargin = SizeUtils.dp2px(this, 42f)


                Log.d("mylog", "createInputView: " + "create field --->" + mConfessionDataBean?.field?.size)
                for (i in item.indices) {
                    //                ZBDataFieldInfo zField = mConfessionDataBean.field.get(i);
                    val confessionFieldBean = item[i]

                    if ("0" == confessionFieldBean.is_hide) {

                        if ("0" == confessionFieldBean.input_type) {
                            val wordTv = EditText(this)
                            wordTv.hint = confessionFieldBean.def_val
                            wordTv.setBackgroundResource(R.drawable.input_bg)
                            wordTv.setPadding(paddingLeft, 0, 0, 0)
                            wordTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)

                            //限制是能输入中文
                            if ("2" == confessionFieldBean.restrain) {
                                InputLenLimit.lengthFilter(parseInt(confessionFieldBean.text_len_limit), this, wordTv)
                            } else {
                                wordTv.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(parseInt(confessionFieldBean.text_len_limit)))
                            }

                            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, tHeight)
                            params.gravity = Gravity.CENTER
                            params.setMargins(tMargin, 0, tMargin, SizeUtils.dp2px(this, 10f))
                            layout_create_type.addView(wordTv, params)
                        }
                        if ("1" == confessionFieldBean.input_type) {

                            val customLayout = LinearLayout(this)
                            val cParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                            customLayout.orientation = LinearLayout.VERTICAL

                            val sParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                            sParams.setMargins(tMargin, 0, tMargin, SizeUtils.dp2px(this, 10f))
                            sParams.gravity = Gravity.CENTER
                            val niceSpinner = Spinner(this)

                            val adapter = ArrayAdapter<String>(this, R.layout.spinner_item)
                            adapter.setDropDownViewResource(R.layout.spinner_item_text)
                            val dataSet = LinkedList<String>()

                            if (confessionFieldBean.select != null && confessionFieldBean.select.size > 0) {
                                for (j in confessionFieldBean.select.indices) {
                                    //if (!zField.select.get(j).opt_text.equals("自定义文字")) {
                                    dataSet.add(confessionFieldBean.select[j].opt_text)
                                    //}
                                }
                            }
                            adapter.addAll(dataSet)
                            niceSpinner.adapter = adapter
                            customLayout.addView(niceSpinner, sParams)

                            val customTv = EditText(this@CreateBeforeActivity)
                            customTv.hint = "请输入文字"
                            customTv.setBackgroundResource(R.drawable.input_bg)
                            customTv.setPadding(paddingLeft, 0, 0, 0)
                            customTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                            customTv.visibility = View.GONE
                            customTv.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(parseInt(confessionFieldBean.text_len_limit)))

                            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, tHeight)
                            params.gravity = Gravity.CENTER
                            params.setMargins(tMargin, 0, tMargin, SizeUtils.dp2px(this@CreateBeforeActivity, 10f))
                            customLayout.addView(customTv, params)

                            layout_create_type.addView(customLayout, cParams)

                            niceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                                    niceSpinner.contentDescription = dataSet[position]

                                    if (dataSet[position].contains("自定义")) {
                                        for (j in 0 until customLayout.childCount) {
                                            if (customLayout.getChildAt(j) is EditText) {
                                                customLayout.getChildAt(j).visibility = View.VISIBLE
                                            }
                                        }
                                    } else {
                                        for (k in 0 until customLayout.childCount) {
                                            if (customLayout.getChildAt(k) is EditText) {
                                                customLayout.getChildAt(k).visibility = View.GONE
                                            }
                                        }
                                    }
                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {}
                            }
                        }

                        if ("2" == confessionFieldBean.input_type || "3" == confessionFieldBean.input_type || "4" == confessionFieldBean.input_type) {


                            cWidth = parseInt(confessionFieldBean.x2) - parseInt(confessionFieldBean.x1)
                            cHeight = parseInt(confessionFieldBean.y2) - parseInt(confessionFieldBean.y1)

                            val imageLayout = RelativeLayout(this)
                            val ivParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
                            ivParams.addRule(RelativeLayout.CENTER_HORIZONTAL)

                            val imageText = TextView(this)
                            imageText.text = "选择图片："
                            imageText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                            val leftParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
                            leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
                            leftParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
                            leftParams.setMargins(tMargin, SizeUtils.dp2px(this, 12f), 0, 0)
                            imageLayout.addView(imageText, leftParams)

                            createSelectImageView = ImageView(this)
                            createSelectImageView?.setBackgroundResource(R.mipmap.create_select_img_icon)
                            val rightParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
                            rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                            rightParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
                            rightParams.setMargins(0, SizeUtils.dp2px(this, 12f), tMargin, 0)
                            imageLayout.addView(createSelectImageView, rightParams)

                            layout_create_type.addView(imageLayout, ivParams)

                            createSelectImageView?.setOnClickListener {
                                //                                checkCameraPermissions();
                                checkSdPermissions()
                            }
                        }
                    } else {
                        val hideEv = EditText(this)
                        hideEv.visibility = View.GONE
                        val hParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                        hParams.gravity = Gravity.CENTER
                        layout_create_type.addView(hideEv, hParams)
                    }
                }

                //生成按钮
                val createBtn = Button(this)
                createBtn.setBackgroundResource(R.drawable.selectot_btn_brim_red_crimson)
                createBtn.text = "一键生成"
                createBtn.setTextColor(ContextCompat.getColor(this, R.color.white))

                val btnParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, tHeight + 6)
                btnParams.gravity = Gravity.CENTER
                btnParams.setMargins(tMargin, SizeUtils.dp2px(this, 10f), tMargin, SizeUtils.dp2px(this, 30f))
                layout_create_type.addView(createBtn, btnParams)

                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

                createBtn.setOnClickListener {
                    MobclickAgent.onEvent(this@CreateBeforeActivity, ConstantKey.UM_CREAT_ID)
                    //                    checkSdPermissions();
                    checkCameraPermissions()
                }

            }
        }

    }

    private fun checkCameraPermissions() {
        SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.CAMERA,
                //if you want do noting or no need all the callbacks you may use SimplePermissionAdapter instead
                object : CheckRequestPermissionListener {
                    override fun onPermissionOk(permission: Permission) {
                        createImage()
                    }

                    override fun onPermissionDenied(permission: Permission) {
                        //                                Activity activity = SoulPermission.getInstance().getTopActivity();
                        /*if (null == activity) {
                                    return;
                                }*/
                        //绿色框中的流程
                        //用户第一次拒绝了权限、并且没有勾选"不再提示"这个值为true，此时告诉用户为什么需要这个权限。
                        if (permission.shouldRationale()) {
                            showToast("未获取到相机权限")
                        } else {
                            //此时请求权限会直接报未授予，需要用户手动去权限设置页，所以弹框引导用户跳转去设置页
                            val permissionDesc = permission.permissionNameDesc
                            AlertDialog.Builder(this@CreateBeforeActivity)
                                    .setTitle("提示")
                                    .setMessage(permissionDesc + "异常，请前往设置－>权限管理，打开" + permissionDesc + "。")
                                    .setPositiveButton("去设置") { dialogInterface, i ->
                                        //去设置页
                                        SoulPermission.getInstance().goPermissionSettings()
                                    }.create().show()
                        }
                    }
                })
    }

    private fun checkSdPermissions() {
        SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                //if you want do noting or no need all the callbacks you may use SimplePermissionAdapter instead
                object : CheckRequestPermissionListener {
                    override fun onPermissionOk(permission: Permission) {
                        PhotoPicker.builder()
                                .setPhotoCount(1)
                                .setShowCamera(true)
                                .setShowGif(true)
                                .setPreviewEnabled(false)
                                .start(this@CreateBeforeActivity, PhotoPicker.REQUEST_CODE)
                    }

                    override fun onPermissionDenied(permission: Permission) {
                        //                                Activity activity = SoulPermission.getInstance().getTopActivity();
                        /*if (null == activity) {
                                    return;
                                }*/
                        //绿色框中的流程
                        //用户第一次拒绝了权限、并且没有勾选"不再提示"这个值为true，此时告诉用户为什么需要这个权限。
                        if (permission.shouldRationale()) {
                            showToast("未获取到相机权限")
                        } else {
                            //此时请求权限会直接报未授予，需要用户手动去权限设置页，所以弹框引导用户跳转去设置页
                            val permissionDesc = permission.permissionNameDesc
                            AlertDialog.Builder(this@CreateBeforeActivity)
                                    .setTitle("提示")
                                    .setMessage(permissionDesc + "异常，请前往设置－>权限管理，打开" + permissionDesc + "。")
                                    .setPositiveButton("去设置") { dialogInterface, i ->
                                        //去设置页
                                        SoulPermission.getInstance().goPermissionSettings()
                                    }.create().show()
                        }
                    }
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        computeTime()

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                mSelectedImages = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS)
                mSelectedImages?.let {
                    if (it.isNotEmpty()) {
                        //Glide.with(this).load(mSelectedImages.get(0)).into(createSelectImageView);
                        HeadImageUtils.imgPath = it[0]
                        val intent = Intent(this@CreateBeforeActivity, ImageCropActivity::class.java)
                        val bundle = Bundle()
                        bundle.putInt("xcrop", cWidth)
                        bundle.putInt("ycrop", cHeight)
                        intent.putExtras(bundle)
                        startActivityForResult(intent, HeadImageUtils.FREE_CUT)
                    }
                }

            }
        }

        if (requestCode == HeadImageUtils.FREE_CUT && HeadImageUtils.cropBitmap != null) {

            val fileName = URLConfig.BASE_NORMAL_FILE_DIR + File.separator + System.currentTimeMillis() + (Math.random() * 10000).toInt() + ".jpg"
            val fileDir = File(URLConfig.BASE_NORMAL_FILE_DIR)
            if (!fileDir.exists()) {
                fileDir.mkdirs()
            }

            val tempFile = File(fileName)
            try {
                if (!tempFile.exists()) {
                    tempFile.createNewFile()
                    val fos = FileOutputStream(tempFile)
                    HeadImageUtils.cropBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                    fos.flush()
                    fos.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            isChooseImage = true
            HeadImageUtils.imgResultPath = tempFile.absolutePath
            Log.d("mylog", "onActivityResult: " + "tempfile path--->" + HeadImageUtils.imgResultPath)
            createSelectImageView?.let {

                Glide.with(this).load(tempFile).apply(RequestOptions.circleCropTransform()).into(it)
            }
        }
    }

    fun computeTime() {
        if (timeNum > 12) {
            //TODO
            //            String sourceIdsKey = App.loginUser != null ? App.loginUser.love_id + "_ids" : App.ANDROID_ID + "_ids";
            val sourceIdsKey = "123456"
            val ssss = SPUtils.get(this@CreateBeforeActivity, sourceIdsKey, "") as String
            val sourceIds = StringBuffer(ssss)
            if (!TextUtils.isEmpty(sourceIds.toString())) {
                sourceIds.append(",")
            }

            sourceIds.append(mConfessionDataBean?.id)
            SPUtils.put(this@CreateBeforeActivity, sourceIdsKey, sourceIds.toString())

            SPUtils.put(this@CreateBeforeActivity, "is_comment", true)
        }
    }

    private fun createImage() {  //一键生成

        val requestData = HashMap<String, String>()
        if (layout_create_type != null) {
            var isValidate = true
            for (i in 0 until layout_create_type.childCount) {
                if (layout_create_type.getChildAt(i) is EditText) {
                    val iEditText = layout_create_type.getChildAt(i) as EditText
                    if (TextUtils.isEmpty(iEditText.text) && iEditText.visibility == View.VISIBLE) {
                        showToast("请输入值")
                        isValidate = false
                        break
                    } else {
                        requestData[i.toString() + ""] = iEditText.text.toString()
                    }
                    continue
                }

                if (layout_create_type.getChildAt(i) is LinearLayout) {
                    val tempLinearLayout = layout_create_type.getChildAt(i) as LinearLayout
                    for (m in 0 until tempLinearLayout.childCount) {
                        if (tempLinearLayout.getChildAt(m) is EditText) {
                            val iEditText = tempLinearLayout.getChildAt(m) as EditText
                            if (TextUtils.isEmpty(iEditText.text) && iEditText.visibility == View.VISIBLE) {
                                showToast("请输入值")
                                isValidate = false
                                break
                            } else {
                                if (iEditText.visibility == View.VISIBLE) {
                                    requestData[i.toString() + ""] = iEditText.text.toString()
                                }
                            }
                            continue
                        }

                        if (tempLinearLayout.getChildAt(m) is Spinner) {
                            val iSpinner = tempLinearLayout.getChildAt(m) as Spinner
                            if (TextUtils.isEmpty(iSpinner.contentDescription)) {
                                showToast("请选择值")
                                isValidate = false
                                break
                            } else {
                                requestData[i.toString() + ""] = iSpinner.contentDescription.toString()
                            }
                            continue
                        }
                    }
                }

                if (layout_create_type.getChildAt(i) is RelativeLayout) {
                    val tempLayout = layout_create_type.getChildAt(i) as RelativeLayout
                    for (j in 0 until tempLayout.childCount) {
                        if (tempLayout.getChildAt(j) is ImageView) {
                            requestData[i.toString() + ""] = ""
                            break
                        }
                    }
                    continue
                }
            }

            // 自定义事件,统计次数
            //            MobclickAgent.onEvent(CreateBeforeActivity.this, "create_click", SystemTool.getAppVersionName(this));

            if (isValidate) {
                //                String mime="";

                this.netCompoundRequestData = requestData
                checkIMEIPermissions()
            }
        }
    }

    private fun netCompoundData() {  //一键生成 请求网络

        val params = HashMap<String, String?>()
        params["id"] = if (mConfessionDataBean != null) mConfessionDataBean?.id else ""
        //                params.put("mime", ANDROID_ID);
        var phoneIMEI = PhoneIMEIUtil.getPhoneIMEI(this@CreateBeforeActivity)
        if (TextUtils.isEmpty(phoneIMEI)) {
            phoneIMEI = "99000854223779"
        }

        val deviceIdForGeneral = DeviceConfig.getDeviceIdForGeneral(this@CreateBeforeActivity)

        //        phoneIMEI  ="99000854223779";
        params["mime"] = phoneIMEI  //99000854223779   99001140644954  356615505655247

        Log.d("mylog", "netCompoundData: mime $phoneIMEI")
        Log.d("mylog", "netCompoundData: deviceIdForGeneral $deviceIdForGeneral")

        netCompoundRequestData?.let {
            if (it.isNotEmpty()) {
                params["requestData"] = JSON.toJSONString(netCompoundRequestData)
            }
        }

        if (isChooseImage) {
//            mNormalPresenter?.netUpFileNet(params, File(HeadImageUtils.imgResultPath), URLConfig.URL_IMAGE_CREATE)
            (mPresenter as? ExpressPresenter)?.netUpFileNet(params, File(HeadImageUtils.imgResultPath), URLConfig.URL_IMAGE_CREATE)


        } else {
//            mNormalPresenter?.netNormalData(params, URLConfig.URL_IMAGE_CREATE)
            (mPresenter as? ExpressPresenter)?.netNormalData(params, URLConfig.URL_IMAGE_CREATE)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getPhoneIMEI(cxt: Context): String {
        val tm = cxt
                .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            "123456"

        } else tm.meid
    }


    private fun checkIMEIPermissions() {
        SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.READ_PHONE_STATE,
                //if you want do noting or no need all the callbacks you may use SimplePermissionAdapter instead
                object : CheckRequestPermissionListener {
                    override fun onPermissionOk(permission: Permission) {
                        netCompoundData()
                    }

                    override fun onPermissionDenied(permission: Permission) {
                        netCompoundData()
                    }
                })
    }

    override fun offerActivityTitle(): String? {

        return if (TextUtils.isEmpty(mConfessionDataBean?.title)) {
            "合成图片"
        } else {
            mConfessionDataBean?.title
        }
    }

    private fun parseInt(s: String): Int {
        if (TextUtils.isEmpty(s)) {
            return 0
        }
        return try {
            Integer.parseInt(s)
        } catch (e: Exception) {
            0
        }

    }


    override fun showNormalDataSuccess(data: String?) {
        data?.let {

            doResult(data)
        }

    }


    fun doResult(data: String?) {

        Log.d("mylog", "doResult: response $data")
        //        Log.d("securityhttp", "doResult: response " + response);


        if (data != null) {
            //            Logger.e("create result --- >" + response);
            try {
//                val res = JSON.parseObject(response, ImageCreateBean::class.java)

//                val res = Gson().fromJson<ImageCreateBean>(response, ImageCreateBean::class.java)
//                val data = res.data
                /*ImageCreateBean res = Contants.gson.fromJson(response, new TypeToken<ImageCreateBean>() {
                }.getType());
                 Bundle bundle = new Bundle();
                bundle.putString("imagePath", data);
                bundle.putString("createTitle", mConfessionDataBean != null ? mConfessionDataBean.title : "");
                Intent intent = new Intent(CreateBeforeActivity.this, ResultActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);*/

                ResultActivity.startResultActivity(this@CreateBeforeActivity, data, if (mConfessionDataBean != null) mConfessionDataBean?.title else "")

            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {
            Toast.makeText(this@CreateBeforeActivity, "生成失败,请稍后重试!", Toast.LENGTH_SHORT).show()
        }
    }


    companion object {
        //    private Map<String, String> mRequestMap;
        //    private String mDataUrl;


        fun startCreateBeforeActivity(context: Context?, confessionDataBean: ConfessionDataBean) {
            val intent = Intent(context, CreateBeforeActivity::class.java)
            intent.putExtra("zb_data_info", confessionDataBean)
            context?.startActivity(intent)
        }
    }

}

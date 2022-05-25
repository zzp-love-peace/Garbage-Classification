package com.zzp.rubbish.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import com.kongzue.dialog.v2.BottomMenu
import com.kongzue.dialog.v2.WaitDialog
import com.tencent.cos.xml.exception.CosXmlClientException
import com.tencent.cos.xml.exception.CosXmlServiceException
import com.tencent.cos.xml.listener.CosXmlResultListener
import com.tencent.cos.xml.model.CosXmlRequest
import com.tencent.cos.xml.model.CosXmlResult
import com.zzp.rubbish.R
import com.zzp.rubbish.bitmapToByteArray
import com.zzp.rubbish.bottomdrawer.RotateExampleDialog
import com.zzp.rubbish.compressImage
import com.zzp.rubbish.network.NetWork
import com.zzp.rubbish.network.TencentFileManager
import com.zzp.rubbish.showToast
import com.zzp.rubbish.data.UserInfo
import kotlinx.coroutines.*
import java.io.File
import java.util.*


class DiscernFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = DiscernFragment()
        private const val TAG = "DiscernFragment"
    }

    private lateinit var discernLayout: MaterialCardView
    private var waitDialog: WaitDialog? = null

    private var imageUri: Uri? = null
    private lateinit var headImage: File
    private val takePhoto = 1
    private val fromAlbum = 2
    private var isPermissionRequested = false

    private var rubbishImage: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_discern, container, false)
        discernLayout = view.findViewById(R.id.discern_layout)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        discernLayout.setOnClickListener {
            val list: MutableList<String> = ArrayList()
            list.add("拍照")
            list.add("从相册中选取照片")
            BottomMenu.show(requireActivity() as AppCompatActivity, list, { text, index ->
                when (index) {
                    0 -> {
                        if (ActivityCompat.checkSelfPermission(
                                requireContext(),
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            startCameraActivity()
                        } else {
                            checkPermission()
                        }
                    }
                    1 -> {
                        startAlbumActivity()
                    }
                }

            }, true)
        }

    }


    @OptIn(DelicateCoroutinesApi::class)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            takePhoto -> {
                if (resultCode == Activity.RESULT_OK) {
                    waitDialog = showWaitDialog()
                    imageUri?.let {
                        val bitmap = BitmapFactory.decodeStream(
                            requireActivity().contentResolver.openInputStream(it)
                        )
                        rubbishImage = bitmap
                        TencentFileManager.uploadImage(
                            UserInfo.phoneNumber,
                            bitmapToByteArray(compressImage(bitmap, 450.0f)),
                            object : CosXmlResultListener {
                                override fun onSuccess(
                                    request: CosXmlRequest?,
                                    result: CosXmlResult?
                                ) {
                                    Log.d(TAG, "onSuccess: ${result?.accessUrl}")
                                    postImageUrl(result?.accessUrl ?: "")
                                }

                                override fun onFail(
                                    request: CosXmlRequest?,
                                    clientException: CosXmlClientException?,
                                    serviceException: CosXmlServiceException?
                                ) {
                                    if (clientException != null) {
                                        clientException.printStackTrace();
                                    } else {
                                        serviceException?.printStackTrace();
                                    }
                                }
                            })
                    }
                }
            }
            fromAlbum -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    waitDialog = showWaitDialog()
                    data.data?.let {
                        val bitmap = getBitmapFromUri(it)
                        rubbishImage = bitmap
                        TencentFileManager.uploadImage(
                            UserInfo.phoneNumber,
                            bitmapToByteArray(compressImage(bitmap!!, 450.0f)),
                            object : CosXmlResultListener {
                                override fun onSuccess(
                                    request: CosXmlRequest?,
                                    result: CosXmlResult?
                                ) {
                                    Log.d(TAG, "onSuccess: ${result?.accessUrl}")
                                    postImageUrl(result?.accessUrl ?: "")
                                }

                                override fun onFail(
                                    request: CosXmlRequest?,
                                    clientException: CosXmlClientException?,
                                    serviceException: CosXmlServiceException?
                                ) {
                                    if (clientException != null) {
                                        clientException.printStackTrace();
                                    } else {
                                        serviceException?.printStackTrace();
                                    }
                                }
                            })
                    }
                }
            }
        }

    }

    private fun startCameraActivity() {
        headImage = File(requireActivity().externalCacheDir, "head_image.jpg")
        if (headImage.exists()) {
            headImage.delete()
        }
        headImage.createNewFile()
        imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                requireContext(), "com.zzp.rubbish." +
                        "fileprovider", headImage
            )
        } else {
            Uri.fromFile(headImage)
        }
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, takePhoto)
    }

    private fun startAlbumActivity() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, fromAlbum)
    }

    private fun getBitmapFromUri(uri: Uri) = requireContext().contentResolver
        .openFileDescriptor(uri, "r")?.use {
            BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
        }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionRequested) {
            isPermissionRequested = true
            val permissionsList = ArrayList<String>()
            permissionsList.add(Manifest.permission.CAMERA)
            if (permissionsList.isNotEmpty()) {
                requestPermissions(permissionsList.toTypedArray(), 0)
            }
        }
    }

    @DelicateCoroutinesApi
    private fun postImageUrl(url: String) {
        if (url.isNotEmpty()) {
            val token = System.currentTimeMillis().toString() + UserInfo.username
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val result = NetWork.postImageUrl(url, token)
                    Log.d(TAG, "postImageUrl: ${result.data}")
                    requireActivity().supportFragmentManager.beginTransaction()
                        .add(RotateExampleDialog(result.data.toInt(), rubbishImage!!), "test1").commit()
                } catch (e: Exception) {
                    e.printStackTrace()
                    "识别失败".showToast()
                } finally {
                    waitDialog?.doDismiss()
                }
            }
        }
    }

    private fun showWaitDialog() = WaitDialog.show(requireActivity(), "识别中...")
}
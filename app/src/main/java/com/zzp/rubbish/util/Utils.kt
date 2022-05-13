package com.zzp.rubbish

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.os.Build
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.zzp.rubbish.activities.MainActivity
import com.zzp.rubbish.util.MyApplication
import com.zzp.rubbish.util.UserInfo
import java.io.ByteArrayOutputStream

fun String.showToast() { Toast.makeText(MyApplication.context, this, Toast.LENGTH_SHORT).show() }

fun TextInputLayout.setFocus() {
    editText?.setOnFocusChangeListener { v, hasFocus ->  error = ""}
}

//  使状态栏透明
fun transparencyBar(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = activity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    } else {
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
    }
}

fun initData(){
    MyApplication.context.getSharedPreferences("data", Context.MODE_PRIVATE).apply {
        UserInfo.score = getInt("score", 0)
        UserInfo.lastSignInDay = getInt("day", 0)
        UserInfo.username = getString("username", "---") ?: "---"
    }
}

fun saveData() {
    MyApplication.context.getSharedPreferences("data", Context.MODE_PRIVATE).edit().apply {
        putInt("score", UserInfo.score)
        putInt("day", UserInfo.lastSignInDay)
        putString("username", UserInfo.username)
        apply()
    }
}

//图片压缩
fun compressImage(image: Bitmap, trueWidth: Float): Bitmap {
    val matrix = Matrix()
    val w = image.width
    val h = image.height
    val trueHeight = trueWidth * h / w
    if (trueWidth > w) return image
    val wsx = trueHeight / w
    matrix.setScale(wsx, wsx)
    return Bitmap.createBitmap(image, 0, 0, w, h, matrix, true)
}

fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val byteArrayOutPutStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutPutStream)
    byteArrayOutPutStream.close()
    return byteArrayOutPutStream.toByteArray()
}

fun checkWindow(window: Window) {
    window.let {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // for drawing behind status bar
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //make system bar to be translucent
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE

            //make status bar color transparent
            window.statusBarColor = Color.TRANSPARENT

            var flags = it.decorView.systemUiVisibility
            // make dark status bar icons
            flags =
                flags xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // make dark navigation bar icons
                flags =
                    flags xor View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
            window.decorView.systemUiVisibility = flags
        }
    }
}
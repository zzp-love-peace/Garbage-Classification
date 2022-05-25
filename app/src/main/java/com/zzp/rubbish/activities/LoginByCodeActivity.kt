package com.zzp.rubbish.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.zzp.rubbish.R
import com.zzp.rubbish.network.NetWork
import com.zzp.rubbish.setFocus
import com.zzp.rubbish.showToast
import com.zzp.rubbish.data.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class LoginByCodeActivity : AppCompatActivity() {

    private lateinit var phoneNumLayout: TextInputLayout
    private lateinit var codeLayout: TextInputLayout
    private lateinit var loginButton: MaterialButton
    private lateinit var registerText: TextView
    private lateinit var loginByPwdText: TextView
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_by_code)
        findViewById()
        setFocus()
        setPhoneNumText()
        codeLayout.setEndIconOnClickListener {
            val phoneNum = phoneNumLayout.editText?.text.toString()
            if (phoneNum.isEmpty()) {
                phoneNumLayout.error = "手机号为空"
            } else {
                if (isPhoneNum(phoneNum)) {
                    CoroutineScope(Dispatchers.IO).launch {
                        getCode(phoneNum)
                    }
                }
            }
        }

        loginButton.setOnClickListener {
            val phoneNum = phoneNumLayout.editText?.text.toString()
            val code = codeLayout.editText?.text.toString()
            if (isNotEmpty(phoneNum, code) && isPhoneNum(phoneNum)) {
                CoroutineScope(Dispatchers.IO).launch { login(phoneNum, code) }
            }
        }
        loginByPwdText.setOnClickListener {
            startActivity(Intent(this, LoginByPwdActivity::class.java))
        }
        registerText.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun findViewById() {
        phoneNumLayout = findViewById(R.id.phone_number_layout)
        codeLayout = findViewById(R.id.code_layout)
        loginButton = findViewById(R.id.login_button)
        registerText = findViewById(R.id.register_text)
        loginByPwdText = findViewById(R.id.login_by_pwd_text)
    }

    private fun setFocus() {
        phoneNumLayout.setFocus()
        codeLayout.setFocus()
    }

    private fun isNotEmpty(phoneNum: String, code: String): Boolean {
        var flag = true
        if (phoneNum.trim().isEmpty()) {
            phoneNumLayout.error = "手机号为空"
            flag = false
        } else {
            phoneNumLayout.error = ""
        }
        if (code.trim().isEmpty()) {
            codeLayout.error = "验证码为空"
            flag = false
        } else {
            codeLayout.error = ""
        }
        return flag
    }

    private fun isPhoneNum(phoneNum: String): Boolean {
        return if (phoneNum.matches(Regex("[1][35789]\\d{9}")) && phoneNum.length == 11) {
            true
        } else {
            phoneNumLayout.error = "手机号格式不正确"
            false
        }
    }

    private fun savePhoneNum(num: String) {
        val edit = getSharedPreferences("data", Context.MODE_PRIVATE).edit()
        edit.putString("phone_num", num)
        edit.apply()
    }

    private fun getPhoneNum() = getSharedPreferences("data", Context.MODE_PRIVATE).getString("phone_num", "")

    private fun setPhoneNumText() {
        val num = getPhoneNum()
        phoneNumLayout.editText?.setText(num)
    }

    private suspend fun login(phoneNum: String, code: String) {
        try {
            val response = NetWork.loginByCode(phoneNum, code)
            CoroutineScope(Dispatchers.Main).launch {
                if (response.code == 0) {
                    startActivity(Intent(this@LoginByCodeActivity, MainActivity::class.java))
                    finish()
                    savePhoneNum(phoneNum)
                    UserInfo.phoneNumber = phoneNum
                    UserInfo.token = response.data.token
                }
                response.msg.showToast()
            }
            Log.d("TAG", "login: $response")
        } catch (e: Exception) {
            Log.e("TAG", "login: ", e)
        }
    }

    private suspend fun getCode(phoneNum: String) {
        try {
            val response = NetWork.getCodeOfLogin(phoneNum)
            CoroutineScope(Dispatchers.Main).launch {
                if (response.code == 0) {
                    "发送成功".showToast()
                    refreshCodeLayout()
                } else response.msg.showToast()
            }
            Log.d("TAG", "getCode: $response")
        } catch (e: Exception) {
            Log.e("TAG", "getCode: ", e)
        }
    }

    private fun refreshCodeLayout() {
        codeLayout.isEndIconVisible = false
        codeLayout.requestFocus()
        timer = object : CountDownTimer(80000, 1000) {
            override fun onTick(p0: Long) {
                codeLayout.suffixText = (p0 / 1000).toString()
            }
            override fun onFinish() {
                codeLayout.suffixText = ""
                codeLayout.isEndIconVisible = true
                cancel()
            }
        }
        timer?.start()
    }
}
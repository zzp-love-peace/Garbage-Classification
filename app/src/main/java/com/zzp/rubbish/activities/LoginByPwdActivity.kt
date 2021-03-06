package com.zzp.rubbish.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
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
import kotlin.math.log

class LoginByPwdActivity : AppCompatActivity() {

    private lateinit var phoneNumLayout: TextInputLayout
    private lateinit var pwdLayout: TextInputLayout
    private lateinit var loginButton: MaterialButton
    private lateinit var registerText: TextView
    private lateinit var loginByCodeText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_by_pwd)
        findViewById()
        setFocus()
        setPhoneNumText()

        loginButton.setOnClickListener {
            val phoneNum = phoneNumLayout.editText?.text.toString()
            val code = pwdLayout.editText?.text.toString()
            if (isNotEmpty(phoneNum, code) && isPhoneNum(phoneNum)) {
                CoroutineScope(Dispatchers.IO).launch { login(phoneNum, code) }
            }
        }
        loginByCodeText.setOnClickListener {
            startActivity(Intent(this, LoginByCodeActivity::class.java))
            finish()
        }
        registerText.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun findViewById() {
        phoneNumLayout = findViewById(R.id.phone_number_layout)
        pwdLayout = findViewById(R.id.pwd_layout)
        loginButton = findViewById(R.id.login_button)
        registerText = findViewById(R.id.register_text)
        loginByCodeText = findViewById(R.id.login_by_code_text)
    }

    private fun setFocus() {
        phoneNumLayout.setFocus()
        pwdLayout.setFocus()
    }

    private fun isNotEmpty(phoneNum: String, code: String): Boolean {
        var flag = true
        if (phoneNum.trim().isEmpty()) {
            phoneNumLayout.error = "???????????????"
            flag = false
        } else {
            phoneNumLayout.error = ""
        }
        if (code.trim().isEmpty()) {
            pwdLayout.error = "???????????????"
            flag = false
        } else {
            pwdLayout.error = ""
        }
        return flag
    }

    private fun isPhoneNum(phoneNum: String): Boolean {
        return if (phoneNum.matches(Regex("[1][35789]\\d{9}")) && phoneNum.length == 11) {
            true
        } else {
            phoneNumLayout.error = "????????????????????????"
            false
        }
    }

    private fun savePhoneNum(num: String) {
        val edit = getSharedPreferences("data", Context.MODE_PRIVATE).edit()
        edit.putString("phone_num",num)
        edit.apply()
    }

    private fun getPhoneNum() = getSharedPreferences("data", Context.MODE_PRIVATE).getString("phone_num", "")

    private fun setPhoneNumText() {
        val num = getPhoneNum()
        phoneNumLayout.editText?.setText(num)
    }

    private suspend fun login(phoneNum: String, pwd: String) {
        try {
            val response = NetWork.loginByPwd(phoneNum, pwd)
            CoroutineScope(Dispatchers.Main).launch {
                if (response.code == 0) {
                    startActivity(Intent(this@LoginByPwdActivity, MainActivity::class.java))
                    finish()
                    savePhoneNum(phoneNum)
                    UserInfo.phoneNumber = phoneNum
                    UserInfo.token = response.data.token
                    Log.d("TAG", UserInfo.token)
                }
                response.msg.showToast()
            }
            Log.d("TAG", "login: $response")
        } catch (e: Exception) {
            Log.e("TAG", "login: ", e)
        }
    }
}
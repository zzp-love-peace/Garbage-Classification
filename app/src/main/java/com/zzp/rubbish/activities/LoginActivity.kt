package com.zzp.rubbish.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.zzp.rubbish.R
import com.zzp.rubbish.setFocus
import com.zzp.rubbish.util.UserInfo

class LoginActivity : AppCompatActivity() {

    private lateinit var phoneNumLayout: TextInputLayout
    private lateinit var codeLayout: TextInputLayout
    private lateinit var loginButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewById()
        setFocus()
        setPhoneNumText()
        codeLayout.setEndIconOnClickListener {
            val phoneNum = phoneNumLayout.editText?.text.toString()
            if (phoneNum.isEmpty()) {
                phoneNumLayout.error = "手机号为空"
            } else {
                if (isPhoneNum(phoneNum)) {

                }
            }
        }
        loginButton.setOnClickListener {
            val phoneNum = phoneNumLayout.editText?.text.toString()
            val code = codeLayout.editText?.text.toString()
            if (isNotEmpty(phoneNum, code) && isPhoneNum(phoneNum)) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                savePhoneNum(phoneNum)
                UserInfo.phoneNumber = phoneNum
            }
        }
    }

    private fun findViewById() {
        phoneNumLayout = findViewById(R.id.phone_number_layout)
        codeLayout = findViewById(R.id.code_layout)
        loginButton = findViewById(R.id.login_button)
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
        val edit = getPreferences(Context.MODE_PRIVATE).edit()
        edit.putString("phone_num",num)
        edit.apply()
    }

    private fun getPhoneNum() = getPreferences(Context.MODE_PRIVATE).getString("phone_num", "")

    private fun setPhoneNumText() {
        val num = getPhoneNum()
        phoneNumLayout.editText?.setText(num)
    }
 }
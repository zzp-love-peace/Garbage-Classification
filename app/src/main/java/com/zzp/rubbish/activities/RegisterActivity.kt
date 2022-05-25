package com.zzp.rubbish.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import com.zzp.rubbish.databinding.ActivityRegisterBinding
import com.zzp.rubbish.network.NetWork
import com.zzp.rubbish.setFocus
import com.zzp.rubbish.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private var timer: CountDownTimer? = null

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFocus()

        binding.codeLayout.setEndIconOnClickListener {
            val phoneNum = binding.phoneNumLayout.editText?.text.toString()
            if (phoneNum.isEmpty()) {
                binding.phoneNumLayout.error = "手机号为空"
            } else {
                if (isPhoneNum(phoneNum)) {
                    CoroutineScope(Dispatchers.IO).launch {
                        getCode(phoneNum)
                    }
                }
            }
        }

        binding.registerButton.setOnClickListener {
            val phoneNum = binding.phoneNumLayout.editText?.text.toString()
            val code = binding.codeLayout.editText?.text.toString()
            val pwd = binding.pwdLayout.editText?.text.toString()
            val pwdAgain = binding.pwdAgainLayout.editText?.text.toString()
            if (isNotEmpty(phoneNum, code, pwd, pwdAgain) && isPhoneNum(phoneNum)) {
                if (pwd == pwdAgain) {
                    CoroutineScope(Dispatchers.IO).launch {
                        register(phoneNum, code, pwd)
                    }
                } else {
                    binding.pwdAgainLayout.error = "密码不一致"
                }

            }
        }

        binding.loginText.setOnClickListener {
            startActivity(Intent(this, LoginByPwdActivity::class.java))
            finish()
        }
    }

    private fun setFocus() {
        binding.phoneNumLayout.setFocus()
        binding.codeLayout.setFocus()
        binding.pwdLayout.setFocus()
        binding.pwdAgainLayout.setFocus()
    }

    private fun isNotEmpty(phoneNum: String, code: String, pwd: String, pwdAgain: String): Boolean {
        var flag = true
        if (phoneNum.trim().isEmpty()) {
            binding.phoneNumLayout.error = "手机号为空"
            flag = false
        } else {
            binding.phoneNumLayout.error = ""
        }
        if (code.trim().isEmpty()) {
            binding.codeLayout.error = "验证码为空"
            flag = false
        } else {
            binding.codeLayout.error = ""
        }
        if (pwd.trim().isEmpty()) {
            binding.pwdLayout.error = "手机号为空"
            flag = false
        } else {
            binding.pwdLayout.error = ""
        }
        if (pwdAgain.trim().isEmpty()) {
            binding.pwdAgainLayout.error = "手机号为空"
            flag = false
        } else {
            binding.pwdAgainLayout.error = ""
        }
        return flag
    }

    private fun isPhoneNum(phoneNum: String): Boolean {
        return if (phoneNum.matches(Regex("[1][35789]\\d{9}")) && phoneNum.length == 11) {
            true
        } else {
            binding.phoneNumLayout.error = "手机号格式不正确"
            false
        }
    }

    private suspend fun register(phoneNum: String, code: String, pwd: String) {
        try {
            val response = NetWork.register(phoneNum, pwd, code)
            CoroutineScope(Dispatchers.Main).launch {
                if (response.code == 0) {
                    "注册成功".showToast()
                    startActivity(Intent(this@RegisterActivity, LoginByPwdActivity::class.java))
                    finish()
                } else response.msg.showToast()
            }
            Log.d("TAG", "register: $response")
        } catch (e: Exception) {
            Log.e("TAG", "register: ", e)
        }
    }

    private suspend fun getCode(phoneNum: String) {
        try {
            val response = NetWork.getCodeOfRegister(phoneNum)
            CoroutineScope(Dispatchers.Main).launch {
                if (response.code == 0) {
                    "发送成功".showToast()
                    refreshCodeLayout()
                }
                else response.msg.showToast()
            }
            Log.d("TAG", "getCode: $response")
        } catch (e: Exception) {
            Log.e("TAG", "getCode: ", e)
        }
    }

    private fun refreshCodeLayout() {
        binding.codeLayout.isEndIconVisible = false
        binding.codeLayout.requestFocus()
        timer = object : CountDownTimer(80000, 1000) {
            override fun onTick(p0: Long) {
                binding.codeLayout.suffixText = (p0/1000).toString()
            }

            override fun onFinish() {
                binding.codeLayout.suffixText = ""
                binding.codeLayout.isEndIconVisible = true
                cancel()
            }
        }
        timer?.start()
    }
}
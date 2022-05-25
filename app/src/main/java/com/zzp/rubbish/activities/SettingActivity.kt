package com.zzp.rubbish.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.kongzue.dialog.v2.SelectDialog
import com.zzp.rubbish.R
import com.zzp.rubbish.checkWindow
import com.zzp.rubbish.databinding.ActivitySettingBinding
import com.zzp.rubbish.saveData
import com.zzp.rubbish.showToast
import com.zzp.rubbish.util.ActivityCollector

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ActivityCollector.addActivity(this)
        checkWindow(window)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navSettingsView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_exit_login -> {
                    exitLogin()
                }
            }
            false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun exitLogin() {
        SelectDialog.show(this, "提示", "您确定要退出登录吗？", "确定",
            { dialog, which ->
                dialog.dismiss()
                val intent = Intent(this, LoginByCodeActivity::class.java)
                ActivityCollector.finishAll()
                startActivity(intent)
                "退出登录成功".showToast()
            }, "取消",
            { dialog, which -> })
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }
}
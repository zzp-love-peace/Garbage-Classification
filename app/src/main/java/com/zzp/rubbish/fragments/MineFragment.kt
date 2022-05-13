package com.zzp.rubbish.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.kongzue.dialog.v2.DialogSettings
import com.kongzue.dialog.v2.InputDialog
import com.kongzue.dialog.v2.SelectDialog
import com.kongzue.dialog.v2.TipDialog
import com.zzp.rubbish.R
import com.zzp.rubbish.activities.LoginActivity
import com.zzp.rubbish.saveData
import com.zzp.rubbish.showToast
import com.zzp.rubbish.util.UserInfo
import org.w3c.dom.Text


class MineFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = MineFragment()
        private const val TAG = "MineFragment"
    }

    private lateinit var toolbar: Toolbar
    private lateinit var navigationView: NavigationView
    private lateinit var scoreText: TextView
    private lateinit var usernameText: TextView
    private lateinit var phoneNumberText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_mine, container, false)
        toolbar = view.findViewById(R.id.toolbar)
        navigationView = view.findViewById(R.id.nav_view)
        scoreText = view.findViewById(R.id.score_text)
        usernameText = view.findViewById(R.id.username_text)
        phoneNumberText = view.findViewById(R.id.phone_number_text)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        toolbar.overflowIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_setting)
        refreshData()
        usernameText.text = UserInfo.username
        phoneNumberText.text = UserInfo.phoneNumber
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.back_login -> {
                    SelectDialog.show(context, "提示", "您确定要退出登录吗？", "确定",
                        { dialog, which ->
                            dialog.dismiss()
                            val intent = Intent(requireContext(), LoginActivity::class.java)
                            requireActivity().finish()
                            startActivity(intent)
                            "退出登录成功".showToast()
                        }, "取消",
                        { dialog, which -> })
                }
            }
            false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.mine_toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.alarm -> Log.d(TAG, "alarm")
            R.id.change_username -> {
//                DialogSettings.type = DialogSettings.TYPE_KONGZUE
                InputDialog.show(
                    requireActivity(), "设置昵称", "设置一个好听的名字吧", "确定",
                    { dialog, inputText ->
                        if (inputText.isNotEmpty()) {
                            UserInfo.username = inputText
                            usernameText.text = inputText
                            saveData()
                            dialog.dismiss()
//                            dialog.hide()
                        } else {
                            TipDialog.show(requireContext(), "昵称不能为空", TipDialog.SHOW_TIME_LONG, TipDialog.TYPE_ERROR)
                        }
                    }, "取消"
                ) { dialog, which -> }
            }
        }
        return true
    }

    fun refreshData() { scoreText.text = UserInfo.score.toString() }
}
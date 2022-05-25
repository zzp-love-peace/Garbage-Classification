package com.zzp.rubbish.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import c.t.m.g.by
import com.google.android.material.navigation.NavigationView
import com.kongzue.dialog.v2.InputDialog
import com.kongzue.dialog.v2.SelectDialog
import com.kongzue.dialog.v2.TipDialog
import com.zzp.rubbish.R
import com.zzp.rubbish.activities.LoginByCodeActivity
import com.zzp.rubbish.activities.SettingActivity
import com.zzp.rubbish.saveData
import com.zzp.rubbish.showToast
import com.zzp.rubbish.data.UserInfo
import com.zzp.rubbish.network.NetWork
import kotlinx.coroutines.*


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
    private lateinit var recoveryText: TextView
    private lateinit var exchangeText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mine, container, false)
        toolbar = view.findViewById(R.id.toolbar)
        navigationView = view.findViewById(R.id.nav_view)
        scoreText = view.findViewById(R.id.score_text)
        recoveryText = view.findViewById(R.id.recovery_text)
        exchangeText = view.findViewById(R.id.exchange_text)
        usernameText = view.findViewById(R.id.username_text)
        phoneNumberText = view.findViewById(R.id.phone_number_text)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(toolbar)
//        setHasOptionsMenu(true)
//        toolbar.overflowIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_setting)
        usernameText.text = UserInfo.username
        phoneNumberText.text = UserInfo.phoneNumber
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.my_collect -> {

                }
                R.id.mine_info -> {

                }
                R.id.setting -> {
                    startActivity(Intent(requireContext(), SettingActivity::class.java))
                }
            }
            false
        }
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.IO).launch { refreshData(UserInfo.token) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.mine_toolbar_menu, menu)
    }

    private fun changeNickname() {
        InputDialog.show(
            requireActivity(), "设置昵称", "设置一个好听的名字吧", "确定",
            { dialog, inputText ->
                if (inputText.isNotEmpty()) {
                    UserInfo.username = inputText
                    usernameText.text = inputText
                    saveData()
                    dialog.dismiss()
                } else {
                    TipDialog.show(
                        requireContext(),
                        "昵称不能为空",
                        TipDialog.SHOW_TIME_LONG,
                        TipDialog.TYPE_ERROR
                    )
                }
            }, "取消"
        ) { dialog, which -> }
    }


    private suspend fun refreshData(token: String) {
        try {
            CoroutineScope(Dispatchers.Main).launch {
                val defOfScore = async(Dispatchers.IO) { NetWork.getTotalIntegral(token) }
                val defOfRecovery = async(Dispatchers.IO) { NetWork.getTotalRecoveryOrder(token) }
                val defOfExchange = async(Dispatchers.IO) { NetWork.getTotalExchangeOrder(token) }
                val resultOfScore = defOfScore.await()
                val resultOfRecovery = defOfRecovery.await()
                val resultOfExchange = defOfExchange.await()
                if (resultOfScore.code == 0 && resultOfRecovery.code == 0 && resultOfExchange.code == 0) {
                    UserInfo.score = resultOfScore.data.data
                    UserInfo.recoveryNum = resultOfRecovery.data.data
                    UserInfo.exchangeNum = resultOfRecovery.data.data
                    scoreText.text = UserInfo.score.toString()
                    recoveryText.text = UserInfo.recoveryNum.toString()
                    exchangeText.text = UserInfo.exchangeNum.toString()
                } else {
                    resultOfScore.msg.showToast()
                }
            }
        } catch (e: Exception) {
            Log.e("TAG", "refreshData: ", e)
        }
    }
}
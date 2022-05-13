package com.zzp.rubbish.activities

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.kongzue.dialog.v2.DialogSettings
import com.zzp.rubbish.*
import com.zzp.rubbish.interfaces.UpdateCityListener
import com.zzp.rubbish.fragments.*

class MainActivity : AppCompatActivity(), UpdateCityListener{
    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var bottomNavigationBar: BottomNavigationBar

    private val fragmentMap = HashMap<Int, Fragment>()
    private val homeFragment = HomeFragment.newInstance()
    private val mineFragment = MineFragment.newInstance()
    private val storeFragment = StoreFragment.newInstance()
    private val discernFragment = DiscernFragment.newInstance()
    private val placeFragment = PlaceFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DialogSettings.type = DialogSettings.TYPE_IOS
        initData()
        transparencyBar(this)
        checkWindow(window)
        initBottomNavigationBar()

        initFragments()
        bottomNavigationBar.setTabSelectedListener(
            object : BottomNavigationBar.OnTabSelectedListener {
                @RequiresApi(Build.VERSION_CODES.N)
                override fun onTabSelected(position: Int) {
                    val transaction = supportFragmentManager.beginTransaction()
                    fragmentMap.forEach { (i, fragment) ->
                        if (i == position) {
                            transaction.show(fragment)
                            if (fragment is MineFragment) fragment.refreshData()
                        } else {
                            transaction.hide(fragment)
                        }
                    }
                    transaction.commit()
                }

                override fun onTabUnselected(position: Int) {
                }

                override fun onTabReselected(position: Int) {
                }
            })
    }

    private fun initBottomNavigationBar() {
        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar)
        bottomNavigationBar
            .addItem(
                BottomNavigationItem(R.drawable.ic_home2, "首页")
                    .setInactiveIcon(ResourcesCompat.getDrawable(resources, R.drawable.ic_home, theme))
            )
            .addItem(BottomNavigationItem(R.drawable.ic_place2, "投放")
                .setInactiveIcon(ResourcesCompat.getDrawable(resources, R.drawable.ic_place, theme))
            )
            .addItem(BottomNavigationItem(R.drawable.ic_camera2, "识别")
                .setInactiveIcon(ResourcesCompat.getDrawable(resources, R.drawable.ic_camera, theme))
            )
            .addItem(BottomNavigationItem(R.drawable.ic_store2, "商城")
                .setInactiveIcon(ResourcesCompat.getDrawable(resources, R.drawable.ic_store, theme))
            )
            .addItem(BottomNavigationItem(R.drawable.ic_mine2, "我的")
                .setInactiveIcon(ResourcesCompat.getDrawable(resources, R.drawable.ic_mine, theme))
            )
            .setFirstSelectedPosition(0)
            .setMode(BottomNavigationBar.MODE_SHIFTING)
            .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
            .setBarBackgroundColor(R.color.white)
            .setInActiveColor(R.color.black)
            .setActiveColor(R.color.red)
            .initialise()
    }

    private fun initFragments() {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.host_fragment, homeFragment)
        addToTransaction(transaction, mineFragment)
        addToTransaction(transaction, storeFragment)
        addToTransaction(transaction, discernFragment)
        addToTransaction(transaction, placeFragment)
        transaction.commit()
        fragmentMap[0] = homeFragment
        fragmentMap[1] = placeFragment
        fragmentMap[2] = discernFragment
        fragmentMap[3] = storeFragment
        fragmentMap[4] = mineFragment
    }

    private fun addToTransaction(transaction: FragmentTransaction, fragment: Fragment) {
        transaction.add(R.id.host_fragment, fragment)
        transaction.hide(fragment)
    }

    fun toStoreFragment() {
        bottomNavigationBar.selectTab(3)
    }

    override fun updateCity(province: String, city: String) {
        homeFragment.updateCity(province, city)
    }

    override fun onDestroy() {
        super.onDestroy()
        saveData()
    }


}
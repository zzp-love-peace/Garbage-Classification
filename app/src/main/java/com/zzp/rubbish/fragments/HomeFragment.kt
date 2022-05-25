package com.zzp.rubbish.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import com.kongzue.dialog.v2.CustomDialog
import com.to.aboomy.pager2banner.Banner
import com.to.aboomy.pager2banner.IndicatorView
import com.zzp.rubbish.R
import com.zzp.rubbish.activities.BeforeQuestionActivity
import com.zzp.rubbish.activities.CalendarActivity
import com.zzp.rubbish.activities.CommunityActivity
import com.zzp.rubbish.activities.MainActivity
import com.zzp.rubbish.adapters.HomeAdapter
import com.zzp.rubbish.saveData
import com.zzp.rubbish.data.UserInfo
import java.util.*

class HomeFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
        private const val TAG = "HomeFragment"
    }

    private lateinit var banner: Banner
    private lateinit var quizLayout: MaterialCardView
    private lateinit var calendarLayout: MaterialCardView
    private lateinit var signInLayout: MaterialCardView
    private lateinit var storeLayout: MaterialCardView
    private lateinit var moreText1: TextView
    private lateinit var moreText2: TextView
    private lateinit var cityText: TextView
    private lateinit var provinceText: TextView
    private lateinit var customDialog: CustomDialog
    private lateinit var moreImage: ImageView
    private var city = ""
    private var province = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_home, container, false)
        findViewById(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initBanner()
        quizLayout.setOnClickListener {
            val intent = Intent(requireContext(), BeforeQuestionActivity::class.java)
            startActivity(intent)
        }
        calendarLayout.setOnClickListener {
            val intent = Intent(requireContext(), CalendarActivity::class.java)
            startActivity(intent)
        }
        signInLayout.setOnClickListener {
            val calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            if (day - UserInfo.lastSignInDay != 0) {
                customDialog = CustomDialog.show(context, R.layout.layout_custom_dialog1, CustomDialog.BindView { rootView ->
                    val clearImage = rootView.findViewById(R.id.clear_view) as MaterialCardView
                    val scoreText = rootView.findViewById(R.id.score_text) as TextView
                    val usernameText = rootView.findViewById(R.id.username_text) as TextView
                    clearImage.setOnClickListener {
                        customDialog.doDismiss()
                        UserInfo.score ++
                        UserInfo.lastSignInDay = day
                        saveData()
                    }
                    scoreText.text = (UserInfo.score + 1).toString()
                    usernameText.text = UserInfo.username
                })
            } else {
                customDialog = CustomDialog.show(context, R.layout.layout_custom_dialog2, CustomDialog.BindView { rootView ->
                    val clearImage = rootView.findViewById(R.id.clear_view) as MaterialCardView
                    val usernameText = rootView.findViewById(R.id.username_text) as TextView
                    clearImage.setOnClickListener {
                        customDialog.doDismiss()
                    }
                    usernameText.text = UserInfo.username
                })
            }
        }
        storeLayout.setOnClickListener {
            (requireActivity() as MainActivity).toStoreFragment()
        }
        moreText1.setOnClickListener {
            (requireActivity() as MainActivity).toStoreFragment()
        }
        moreText2.setOnClickListener {
            val intent = Intent(requireContext(), CommunityActivity::class.java)
            startActivity(intent)
        }
        moreImage.setOnClickListener {
            val intent = Intent(requireContext(), CommunityActivity::class.java)
            startActivity(intent)
        }
    }

    private fun findViewById(view: View) {
        banner = view.findViewById(R.id.banner)
        quizLayout = view.findViewById(R.id.quiz_layout)
        calendarLayout = view.findViewById(R.id.calendar_layout)
        signInLayout = view.findViewById(R.id.sign_in_layout)
        storeLayout = view.findViewById(R.id.store_layout)
        moreText1 = view.findViewById(R.id.more_text_1)
        moreText2 = view.findViewById(R.id.more_text_2)
        moreImage = view.findViewById(R.id.more_image)
        cityText = view.findViewById(R.id.city_text)
        provinceText = view.findViewById(R.id.province_text)
    }

    private fun initBanner() {
        //使用内置Indicator
        val indicator = IndicatorView(requireContext())
            .setIndicatorColor(Color.WHITE)
            .setIndicatorSelectorColor(Color.DKGRAY)
            .setIndicatorRadius(3f)
            .setIndicatorRatio(1f)
            .setIndicatorSelectedRatio(1f)

        //创建adapter
        val adapter = HomeAdapter()
        //传入RecyclerView.Adapter 即可实现无限轮播
        banner.setIndicator(indicator).adapter = adapter
        banner.setAutoPlay(true).startTurning()
    }

    fun updateCity(province: String, city: String) {
        if (this.city != city) {
            cityText.text = city
            this.city = city
        }
        if (this.province != province) {
            provinceText.text = province
            this.province = province
        }
    }
}
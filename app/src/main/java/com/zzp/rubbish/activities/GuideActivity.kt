package com.zzp.rubbish.activities


import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.to.aboomy.pager2banner.Banner
import com.to.aboomy.pager2banner.IndicatorView
import com.zzp.rubbish.R
import com.zzp.rubbish.adapters.GuideAdapter


class GuideActivity : AppCompatActivity() {

    private lateinit var banner: Banner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)
        banner = findViewById(R.id.banner)

        //使用内置Indicator
        val indicator = IndicatorView(this)
            .setIndicatorColor(Color.DKGRAY)
                .setIndicatorSelectorColor(Color.WHITE)
            .setIndicatorRadius(5f)
            .setIndicatorRatio(1f)
            .setIndicatorSelectedRatio(2f)


        //创建adapter
        val adapter = GuideAdapter(this)

        //传入RecyclerView.Adapter 即可实现无限轮播
        banner.setAutoTurningTime(1000)
        banner.setIndicator(indicator).adapter = adapter
        banner.setAutoPlay(true).startTurning()
    }
}
package com.zzp.rubbish.bottomdrawer

import android.graphics.Bitmap
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.content.ContextCompat
import com.github.heyalex.bottomdrawer.BottomDrawerDialog
import com.github.heyalex.bottomdrawer.BottomDrawerFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ldf.calendar.model.CalendarDate
import com.zzp.rubbish.R
import com.zzp.rubbish.data.RubbishInfo
import com.zzp.rubbish.data.Task
import com.zzp.rubbish.util.TaskDatabase
import kotlin.concurrent.thread

class RotateExampleDialog(private val type: Int, private val rubbishBitmap: Bitmap) : BottomDrawerFragment() {

    private val rubbishMap = mapOf(
        0 to "传单", 1 to "充电宝", 2 to "包", 3 to "塑料玩具", 4 to "塑料碗盆", 5 to "塑料衣架",
        6 to "快递纸袋", 7 to "报纸", 8 to "插头电线", 9 to "旧书", 10 to "旧衣服", 11 to "易拉罐",
        12 to "杂志", 13 to "枕头", 14 to "毛绒玩具", 15 to "泡沫塑料", 16 to "洗发水瓶", 17 to "牛奶盒等利乐包装",
        18 to "玻璃", 19 to "玻璃瓶罐", 20 to "皮鞋", 21 to "砧板", 22 to "纸板箱", 23 to "调料瓶",
        24 to "酒瓶", 25 to "金属食品罐", 26 to "锅", 27 to "食用油桶", 28 to "饮料瓶",/*可回收垃圾*/
        29 to "废弃水银温度计", 30 to "废旧灯管灯泡", 31 to "杀虫剂容器", 32 to "电池",
        33 to "软膏", 34 to "过期药物", 35 to "除草剂容器",/*有害垃圾*/
        36 to "剩菜剩饭", 37 to "大骨头", 38 to "水果果皮", 39 to "水果果肉", 40 to "茶叶渣",
        41 to "菜梗菜叶", 42 to "落叶", 43 to "蛋壳", 44 to "西餐糕点", 45 to "鱼骨",/*厨余垃圾*/
        46 to "一次性餐具", 47 to "化妆品瓶",
        48 to "卫生纸", 49 to "尿片",
        50 to "污塑料", 51 to "烟蒂",
        52 to "牙签", 53 to "破碎花盆及碟碗", 54 to "竹筷", 55 to "纸杯", 56 to "贝壳"/*其他垃圾*/
    )

    private val infoMap = mapOf(
        "可回收垃圾" to RubbishInfo("应注意轻放应尽量保持清洁干燥，避免污染\n" +
                "立体包装应清空内容物，清洁后压扁投放\n" +
                "易破损或有裹尖锐边角的应包后投放",
            "主要包括废纸、塑料、玻璃、金属和纺织物五大类生活垃圾。\n" +
                "1、废纸主要包括：报纸、杂志、图书、各种包装纸、办公用纸、纸盒等，但是纸巾和卫生用纸由于水溶性太强不可回收；\n" +
                "2、塑料主要包括：各种塑料袋、塑料包装物和餐具、牙刷、杯子、矿泉水瓶、塑料玩具、塑料文具、塑料生活用品、洗发液瓶、洗手液瓶、洗衣液瓶、洗洁精瓶；\n" +
                "3、玻璃主要包括：玻璃饮料瓶、玻璃酒瓶、坏玻璃杯、碎玻璃窗、废玻璃板、镜片、镜子等，根据回收工艺，玻璃分为无色玻璃，绿色玻璃，棕色玻璃；\n" +
                "4、金属主要包括：易拉罐、金属罐头盒、装饰物、铝箔、铁片、铁钉、铁管、废铁丝、旧钢丝球、铜导线等，按照回收材料分类：铁类，非铁类(一般指有色金属)；\n" +
                "5、纺织物主要包括：废弃衣服、裤子、袜子、毛巾、书包、布鞋、床单、被褥、毛绒玩具等。"),
        "有害垃圾" to RubbishInfo("应注意轻放\n" +
                "易破碎的及废弃药品应连带包装或包裹后投放\n" +
                "压力灌装容器应排空内容物后投放", "有害垃圾含有对人体健康有害的重金属、有毒的物质或者对环境造成现实危害或者潜在危害的废弃物。包括电池、荧光灯管、灯泡、水银温度计、油漆桶、部分家电、过期药品及其容器、过期化妆品等。这些垃圾一般使用单独回收或填埋处理。"),
        "厨余垃圾" to RubbishInfo("应从产生时就与其他品种垃圾分开收集\n" +
                "投放前尽量沥干水分\n" +
                "有外包装的应去除外包装投放", "厨余垃圾（上海称湿垃圾）包括剩菜剩饭、骨头、菜根菜叶、果皮等食品类废物。经生物技术就地处理堆肥，每吨可生产0.6~0.7吨有机肥料。"),
        "其他垃圾" to RubbishInfo("投放前尽量沥干水分\n" +
                "难以分辨类别的生活垃圾投放其他垃圾容器内", "其他垃圾（上海称干垃圾）包括除上述几类垃圾之外的砖瓦陶瓷、渣土、卫生间废纸、纸巾等难以回收的废弃物及尘土、食品袋（盒）。采取卫生填埋可有效减少对地下水、地表水、土壤及空气的污染。\n" +
                "大棒骨因为“难腐蚀”被列入“其它垃圾”。玉米核、坚果壳、果核、鸡骨等则是餐厨垃圾。\n" +
                "卫生纸：厕纸、卫生纸遇水即溶，不算可回收的“纸张”，类似的还有烟盒等。\n" +
                "餐厨垃圾装袋：常用的塑料袋，即使是可以降解的也远比餐厨垃圾更难腐蚀。此外塑料袋本身是可回收垃圾。正确做法应该是将餐厨垃圾倒入垃圾桶，塑料袋另扔进“可回收垃圾”桶。\n" +
                "果壳：在垃圾分类中，“果壳瓜皮”的标识就是花生壳，的确属于厨余垃圾。家里用剩的废弃食用油，也归类在“厨余垃圾”。\n" +
                "尘土：在垃圾分类中，尘土属于“其它垃圾”，但残枝落叶属于“厨余垃圾”，包括家里开败的鲜花等。"),
    )

    private lateinit var nameText: TextView
    private lateinit var typeText: TextView
    private lateinit var wayText: TextView
    private lateinit var infoText: TextView
    private lateinit var rubbishImage: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.rotate_bottom_layout, container, false)
        nameText = view.findViewById(R.id.name_text)
        typeText = view.findViewById(R.id.type_text)
        wayText = view.findViewById(R.id.way_text)
        infoText = view.findViewById(R.id.info_text)
        rubbishImage = view.findViewById(R.id.rubbish_image)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val rubbishName = rubbishMap[type]
        val rubbishType = when {
            type > 45 -> "其他垃圾"
            type > 35 -> "厨余垃圾"
            type > 28 -> "有害垃圾"
            else -> "可回收垃圾"
        }
        val rubbishInfo = infoMap[rubbishType]

        rubbishImage.setImageBitmap(rubbishBitmap)
        nameText.text = rubbishName
        typeText.text = rubbishType
        wayText.text = rubbishInfo?.handleWay
        infoText.text = rubbishInfo?.otherInfo

        val currentDate = CalendarDate()
        thread {
            TaskDatabase.getDatabase().taskDao().insertTask(
                Task(
                    "长沙理工大学云塘校区", "自动投放", rubbishType,
                    "${currentDate.year}.${currentDate.month}.${currentDate.day}"
                )
            )
        }

    }

    override fun configureBottomDrawer(): BottomDrawerDialog {
        return BottomDrawerDialog.build(requireContext()) {
            theme = R.style.Rotate
            handleView = RotateHandleView(context).apply {
                val widthHandle =
                    resources.getDimensionPixelSize(R.dimen.rotate_sample_bottom_sheet_handle_width)
                val heightHandle =
                    resources.getDimensionPixelSize(R.dimen.rotate_sample_bottom_sheet_handle_height)
                val params =
                    FrameLayout.LayoutParams(widthHandle, heightHandle, Gravity.END)

                params.topMargin =
                    resources.getDimensionPixelSize(R.dimen.rotate_sample_bottom_sheet_handle_margin)

                params.rightMargin =
                    resources.getDimensionPixelSize(R.dimen.rotate_sample_bottom_sheet_handle_margin)

                layoutParams = params
                background =
                    ContextCompat.getDrawable(context, R.drawable.ic_expand_less_black_24dp)

                setOnClickListener {
                    when (getCurrentState()) {
                        BottomSheetBehavior.STATE_EXPANDED -> dismissWithBehavior()
                        BottomSheetBehavior.STATE_COLLAPSED,
                        BottomSheetBehavior.STATE_HALF_EXPANDED -> expandWithBehaivor()
                    }
                }
            }
        }
    }
}

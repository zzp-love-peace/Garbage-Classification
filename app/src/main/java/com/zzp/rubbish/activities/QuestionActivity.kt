package com.zzp.rubbish.activities

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.hanks.htextview.scale.ScaleText
import com.hanks.htextview.scale.ScaleTextView
import com.hanks.htextview.typer.TyperTextView
import com.zzp.rubbish.R
import com.zzp.rubbish.saveData
import com.zzp.rubbish.showToast
import com.zzp.rubbish.transparencyBar
import com.zzp.rubbish.util.UserInfo

class QuestionActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var questionText: ScaleTextView
    private lateinit var questionRadioGroup: RadioGroup
    private lateinit var answerButtonA: RadioButton
    private lateinit var answerButtonB: RadioButton
    private lateinit var answerButtonC: RadioButton
    private lateinit var answerButtonD: RadioButton
    private lateinit var confirmButton: MaterialButton
    private lateinit var nextButton: MaterialButton
    private lateinit var questionNumText: TextView
    private lateinit var scoreText: TextView
    private lateinit var explainText: TyperTextView

    private val questions = listOf(
        "包了口香糖的纸巾属于哪一类垃圾？", "保鲜膜属于哪类垃圾？",
        "变质的香肠属于哪一类垃圾？", "槟榔渣属于哪类垃圾？", "剥掉的蛋壳属于哪一类垃圾?", "菜刀属于哪类垃圾？",
        "废电池乱丢对人体可能造成____?", "空的灭火器属于哪一类垃圾？", "胶卷属于哪类垃圾？", "鸡毛属于哪类垃圾？"
    )
    private val answers = listOf("A", "A", "A", "D", "D", "C", "A", "C", "D", "C")
    private val answersA = listOf("其他垃圾", "其他垃圾", "厨余垃圾", "其他垃圾", "其他垃圾", "其他垃圾", "镉中毒", "其他垃圾", "可回收物", "可回收物")
    private val answersB = listOf("有害垃圾", "有害垃圾", "其他垃圾 ", "有害垃圾", "有害垃圾", "有害垃圾", "氰中毒", "厨余垃圾", "厨余垃圾", "厨余垃圾")
    private val answersC = listOf("可回收物", "可回收物", "有害垃圾", "可回收物", "可回收物", "可回收物", "碳中毒", "可回收物", "其他垃圾", "其他垃圾")
    private val answersD = listOf("厨余垃圾", "厨余垃圾", "可回收物", "厨余垃圾", "厨余垃圾", "厨余垃圾", "氟中毒", "有害垃圾", "有害垃圾", "有害垃圾")
    private val explains = listOf(
        "包了口香糖、擦了鼻涕的纸巾都是其他垃圾。",
        "除了可回收垃圾、有害垃圾、厨余垃圾以外的垃圾都属于其他垃圾，包括餐盒、餐巾纸、湿纸巾、塑料袋、食品包装袋等。",
        "香肠是易腐烂的生物质废弃物，因此属于厨余垃圾。", "槟榔渣属于厨余垃圾，像茶包、茶叶渣还有冲泡饮料粉末等都属于此类。",
        "蛋壳是易腐烂的生物质废弃物，应该投入厨余垃圾桶。",
        "可回收物分为纸类、塑料橡胶类、玻璃类、金属类等，食品罐盒、剪刀、金属办公用品、刀片等都属于金属类，因此属于可回收物。",
        "废旧电池主要有毒物质：铅，汞，镉。世界卫生组织国际癌症研究机构公布的致癌物清单初步整理参考，铅在2B类致癌物清单中。" + "汞常温下即可蒸发，汞蒸气和汞的化合物多有剧毒（慢性）。镉会对呼吸道产生刺激，长期暴露会造成嗅觉丧失症、牙龈黄斑或渐成黄圈，镉化合物不易被肠道吸收，但可经呼吸被体内吸收，积存于肝或肾脏造成危害，尤以对肾脏损害最为明显。还可导致骨质疏松和软化。",
        "灭火器的外壳是金属材质，可以回收再生利用，里面的内容物多为干粉和二氧化碳，对人体健康和自然环境无危害，因此属于可回收物。",
        "胶片显影技术使用的材料，感光胶片都属于有害垃圾。",
        "因为鸡毛不容易腐烂变质，所以属于其他垃圾。"
    )

    private var questionNum = 0
    private var index = 0
    private var myAnswer = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        findViewById()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initData(0)
        scoreText.text = UserInfo.score.toString()
        questionRadioGroup.setOnCheckedChangeListener { group, id ->
            myAnswer = when (id) {
                R.id.radio_button_A -> "A"
                R.id.radio_button_B -> "B"
                R.id.radio_button_C -> "C"
                R.id.radio_button_D -> "D"
                else -> ""
            }
        }
        confirmButton.setOnClickListener {
            if (isRight(index, myAnswer)) {
                setRightView()
            } else {
                "答案错误了哦".showToast()
            }
        }
        nextButton.setOnClickListener {
            index++
            initData(index)
        }
    }

    private fun findViewById() {
        toolbar = findViewById(R.id.toolbar)
        questionText = findViewById(R.id.question_text)
        questionRadioGroup = findViewById(R.id.question_radioGroup)
        answerButtonA = findViewById(R.id.radio_button_A)
        answerButtonB = findViewById(R.id.radio_button_B)
        answerButtonC = findViewById(R.id.radio_button_C)
        answerButtonD = findViewById(R.id.radio_button_D)
        confirmButton = findViewById(R.id.confirm_button)
        nextButton = findViewById(R.id.next_button)
        questionNumText = findViewById(R.id.question_num)
        scoreText = findViewById(R.id.score_text)
        explainText = findViewById(R.id.explain_text)
    }

    private fun initData(i: Int) {
        if (i >= questions.size || i < 0) return
//        questionText.text = "${questionNum + 1}、${questions[i]}"
        questionText.animateText("${questionNum + 1}、${questions[i]}")
        answerButtonA.text = answersA[i]
        answerButtonB.text = answersB[i]
        answerButtonC.text = answersC[i]
        answerButtonD.text = answersD[i]
//        explainText.text = "解析：${explains[i]}"
        nextButton.isClickable = false
        nextButton.isEnabled = false
        confirmButton.isEnabled = true
//        explainText.visibility = View.GONE
        explainText.text = " "
        questionRadioGroup.clearCheck()
    }

    private fun isRight(i: Int, answer: String) = answers[i] == answer

    private fun setRightView() {
        "回答正确".showToast()
        UserInfo.score ++
        questionNum ++
        scoreText.text = UserInfo.score.toString()
        questionNumText.text = questionNum.toString()
        explainText.animateText("解析：${explains[questionNum-1]}")
        if (questionNum < questions.size) {
            nextButton.isClickable = true
            nextButton.isEnabled = true
        }
        confirmButton.isEnabled = false
//        ObjectAnimator.ofInt(explainText, "visibility", View.VISIBLE).apply {
//            duration = 500
//            start()
//        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        saveData()
    }
}
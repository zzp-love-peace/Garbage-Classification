package com.zzp.rubbish.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import com.zzp.rubbish.R
import com.zzp.rubbish.transparencyBar

class BeforeQuestionActivity : AppCompatActivity() {

    private lateinit var startButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_before_question)
        transparencyBar(this)
        startButton = findViewById(R.id.start_button)
        startButton.setOnClickListener {
            val intent = Intent(this, QuestionActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
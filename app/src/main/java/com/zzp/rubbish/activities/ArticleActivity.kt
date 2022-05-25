package com.zzp.rubbish.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.zzp.rubbish.checkWindow
import com.zzp.rubbish.data.Article
import com.zzp.rubbish.databinding.ActivityArticleBinding


class ArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkWindow(window)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val article = intent.getSerializableExtra("article") as Article
        binding.title.text = article.title
        binding.author.text = article.author
        binding.date.text = article.date
        binding.content.text = article.content
        binding.collect.setOnClickListener {

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
}
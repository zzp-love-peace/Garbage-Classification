package com.zzp.rubbish.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kongzue.dialog.v2.WaitDialog
import com.zzp.rubbish.R
import com.zzp.rubbish.adapters.ArticleAdapter
import com.zzp.rubbish.checkWindow
import com.zzp.rubbish.data.Article

class CommunityActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var articleRecycler: RecyclerView

    private var articleAdapter: ArticleAdapter? = null
    private val articleList =
        arrayListOf(Article(R.drawable.article_bg0, "长沙垃圾分类知识科普。", "靖哥哥营销", "24k"),
            Article(R.drawable.article_bg1, "关于垃圾分类，习总书记这样说。", "吴堆堆", "12k"),
            Article(R.drawable.article_bg2, "文明你我他，为什么要进行垃圾分类？", "代成龙", "8k"),
            Article(R.drawable.article_bg3, "垃圾分类优秀小区，看看他们是怎样分类的。", "靖哥哥营销", "6k"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)
        findViewById()
        checkWindow(window)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initRecyclerView()
    }

    private fun findViewById() {
        toolbar = findViewById(R.id.toolbar)
        articleRecycler = findViewById(R.id.article_recycler)
    }

    private fun initRecyclerView() {
        articleAdapter = ArticleAdapter(articleList)
        val layoutManager = LinearLayoutManager(this)
        articleRecycler.layoutManager = layoutManager
        articleRecycler.adapter = articleAdapter
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
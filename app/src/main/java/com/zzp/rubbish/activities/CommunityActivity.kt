package com.zzp.rubbish.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zzp.rubbish.R
import com.zzp.rubbish.adapters.ArticleAdapter
import com.zzp.rubbish.checkWindow
import com.zzp.rubbish.data.Article
import com.zzp.rubbish.data.Policy
import com.zzp.rubbish.data.UserInfo
import com.zzp.rubbish.network.NetWork
import com.zzp.rubbish.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class CommunityActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var articleRecycler: RecyclerView

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
        val layoutManager = LinearLayoutManager(this)
        articleRecycler.layoutManager = layoutManager
        CoroutineScope(Dispatchers.IO).launch {
            getPolicy(UserInfo.token)
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

    private suspend fun getPolicy(token: String) {
        try {
            val response = NetWork.getPolicy(token)
            CoroutineScope(Dispatchers.Main).launch {
                if (response.code == 0) {
                    articleRecycler.adapter = ArticleAdapter(
                        this@CommunityActivity,
                        policy2Article(response.data.policies)
                    )
                } else {
                    response.msg.showToast()
                }
            }
        } catch (e: Exception) {
            Log.e("TAG", "getPolicy: ", e)
        }
    }

    private fun policy2Article(policies: List<Policy>): List<Article> {
        val articles = ArrayList<Article>()
        for (policy in policies) {
            articles.add(
                Article(
                    policy.image,
                    policy.title,
                    policy.author,
                    policy.content,
                    policy.createDate,
                    Article.TYPE_V
                )
            )
        }
        return articles
    }
}
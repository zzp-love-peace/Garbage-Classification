package com.zzp.rubbish.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zzp.rubbish.R
import com.zzp.rubbish.data.Article
import kotlin.concurrent.timerTask

class ArticleAdapter(private val articleList: List<Article>) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.image_view)
        val titleText: TextView = view.findViewById(R.id.title_text)
        val authorText: TextView = view.findViewById(R.id.author_text)
        val numText: TextView = view.findViewById(R.id.num_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.article_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = articleList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articleList[position]
        holder.apply {
            imageView.setImageResource(article.articleImage)
            titleText.text = article.title
            authorText.text = article.author
            numText.text = article.num
        }

    }

}
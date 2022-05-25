package com.zzp.rubbish.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zzp.rubbish.R
import com.zzp.rubbish.activities.ArticleActivity
import com.zzp.rubbish.data.Article

class ArticleAdapter(private val context: Context, private val articleList: List<Article>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class VerticalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.article_image)
        val titleText: TextView = view.findViewById(R.id.title_text)
        val authorText: TextView = view.findViewById(R.id.author_text)
    }

    inner class HorizontalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.article_image)
        val titleText: TextView = view.findViewById(R.id.title_text)
    }

    override fun getItemViewType(position: Int) = articleList[position].type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = if (viewType == Article.TYPE_H) {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article_horizontal, parent, false)
        HorizontalViewHolder(view)
    } else {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article_vertical, parent, false)
        VerticalViewHolder(view)
    }

    override fun getItemCount() = articleList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val article = articleList[position]
        when (holder) {
            is HorizontalViewHolder -> {
                holder.apply {
                    Glide.with(context).load(article.image).into(imageView)
                    titleText.text = article.title
                }
            }
            is VerticalViewHolder -> {
                holder.apply {
                    Glide.with(context).load(article.image).into(imageView)
                    titleText.text = article.title
                    authorText.text = article.author
                    itemView.setOnClickListener {
                        val intent = Intent(context, ArticleActivity::class.java)
                        intent.putExtra("article", article)
                        context.startActivity(intent)
                    }
                }
            }
            else -> throw IllegalArgumentException()
        }
    }
}
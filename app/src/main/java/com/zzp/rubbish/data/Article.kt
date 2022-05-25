package com.zzp.rubbish.data

import java.io.Serializable

class Article(
    val image: String,
    val title: String,
    val author: String,
    val content: String,
    val date: String,
    val type: Int
): Serializable {
    companion object {
        // 横向
        const val TYPE_H = 0

        // 纵向
        const val TYPE_V = 1
    }
}
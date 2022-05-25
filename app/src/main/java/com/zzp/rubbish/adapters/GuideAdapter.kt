package com.zzp.rubbish.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.zzp.rubbish.activities.LoginByCodeActivity
import com.zzp.rubbish.R
import java.lang.IllegalArgumentException

class GuideAdapter(private val activity: Activity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder01(view: View) : RecyclerView.ViewHolder(view)

    inner class ViewHolder2(view: View) : RecyclerView.ViewHolder(view) {
        val startButton: MaterialButton = view.findViewById(R.id.start_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when(viewType) {
        0 -> {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.guide_item0, parent, false)
            ViewHolder01(view)
        }
        1 -> {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.guide_item1, parent, false)
            ViewHolder01(view)
        }
        2 -> {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.guide_item2, parent, false)
            val viewHolder = ViewHolder2(view)
            viewHolder.startButton.setOnClickListener {
                val intent = Intent(activity, LoginByCodeActivity::class.java)
                activity.startActivity(intent)
                activity.finish()
            }
            viewHolder
        }
        else -> throw IllegalArgumentException()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }

    override fun getItemCount() = 3

    override fun getItemViewType(position: Int) = position
}
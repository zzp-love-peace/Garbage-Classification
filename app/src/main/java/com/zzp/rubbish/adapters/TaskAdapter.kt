package com.zzp.rubbish.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zzp.rubbish.R
import com.zzp.rubbish.data.Task

class TaskAdapter : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    private var taskList: List<Task> = arrayListOf()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val typeText: TextView = view.findViewById(R.id.type_text)
        val placeText: TextView = view.findViewById(R.id.place_text)
        val rubbishTypeTextView: TextView = view.findViewById(R.id.kind_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = taskList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = taskList[position]
        holder.apply {
            typeText.text = task.type
            placeText.text = task.place
            rubbishTypeTextView.text = task.rubbishType
//            (itemView as MaterialCardView).setCardBackgroundColor(ContextCompat.getColor(context, R.color.blue))
        }

    }

    fun setTaskList(list: List<Task>) {
        taskList = list
    }
}
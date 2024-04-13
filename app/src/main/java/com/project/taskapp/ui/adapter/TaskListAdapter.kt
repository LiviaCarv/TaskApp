package com.project.taskapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.taskapp.data.model.Task
import com.project.taskapp.databinding.ItemTaskBinding

class TaskListAdapter(private val taskList: List<Task>): RecyclerView.Adapter<TaskListAdapter.ViewHolder>() {

    inner class ViewHolder (private val binding: ItemTaskBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(itemTask: Task) {
            binding.txtDescription.text = itemTask.description
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = taskList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = taskList[position]
        holder.bind(item)
    }

}
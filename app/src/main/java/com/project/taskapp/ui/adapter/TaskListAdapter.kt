package com.project.taskapp.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.taskapp.R
import com.project.taskapp.data.database.Task
import com.project.taskapp.databinding.ItemTaskBinding

class TaskListAdapter(
    private val onItemClickListener: (Task, Int) -> Unit
) :
    ListAdapter<Task, TaskListAdapter.ViewHolder>(TaskDiffCallback()) {

    inner class ViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemTask: Task) {
            Log.i("TESTE", "BIND CHAMADO")
            binding.txtDescription.text = itemTask.description
            binding.setTaskButtonsListeners(itemTask)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("TESTE", "onCreateViewHolder CHAMADO")
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i("TESTE", "onBindViewHolder CHAMADO")

        val item = getItem(position)
        holder.bind(item)
    }

    private fun ItemTaskBinding.setTaskButtonsListeners(itemTask: Task) {
        btnRemoveTask.setOnClickListener { onItemClickListener(itemTask, R.id.btn_remove_task) }
        btnEditTask.setOnClickListener { onItemClickListener(itemTask, R.id.btn_edit_task) }
        btnTaskDetails.setOnClickListener { onItemClickListener(itemTask, R.id.btn_task_details) }
    }


}

class TaskDiffCallback: DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id &&  oldItem.description == newItem.description
    }

}
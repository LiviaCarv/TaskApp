package com.project.taskapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.taskapp.R
import com.project.taskapp.data.model.Status
import com.project.taskapp.data.model.Task
import com.project.taskapp.databinding.ItemTaskBinding

class TaskListAdapter(
    private val context: Context,
    private val onItemClickListener: (Task, Int) -> Unit
) :
    ListAdapter<Task, TaskListAdapter.ViewHolder>(TaskDiffCallback()) {

    inner class ViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemTask: Task) {
            binding.txtDescription.text = itemTask.description
            setArrowIndicators(itemTask, binding)
            binding.setTaskButtonsListeners(itemTask)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

//    override fun getItemCount() = taskList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    private fun setArrowIndicators(itemTask: Task, binding: ItemTaskBinding) {
        when (itemTask.status) {
            Status.TODO -> {
                binding.apply {
                    btnBack.isVisible = false
                    btnForward.setOnClickListener { onItemClickListener(itemTask, R.id.btn_forward) }
                }
            }

            Status.DONE -> {
                binding.apply {
                    btnForward.isVisible = false
                    btnBack.setOnClickListener { onItemClickListener(itemTask, R.id.btn_back) }
                }

            }

            Status.DOING -> {
                binding.apply {
                    btnBack.setColorFilter(ContextCompat.getColor(context, R.color.color_orange))
                    btnForward.setColorFilter(ContextCompat.getColor(context, R.color.color_green))
                    btnBack.setOnClickListener { onItemClickListener(itemTask, R.id.btn_back) }
                    btnForward.setOnClickListener { onItemClickListener(itemTask, R.id.btn_forward) }
                }
            }
        }
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
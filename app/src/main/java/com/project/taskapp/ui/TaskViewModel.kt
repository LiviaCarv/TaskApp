package com.project.taskapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.taskapp.data.model.Task

class TaskViewModel : ViewModel() {
    private val _taskUpdate  = MutableLiveData<Task>()
    val taskUpdate: LiveData<Task> = _taskUpdate

    fun setUpdatedTask(task: Task) {
        _taskUpdate.value = task
    }
}
package com.project.taskapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.project.taskapp.R
import com.project.taskapp.data.database.Task
import com.project.taskapp.data.database.TaskRepository
import kotlinx.coroutines.launch

class TaskListViewModel(private val repository: TaskRepository) : ViewModel() {
    private val _taskList = MutableLiveData<List<Task>>()
    val taskList: LiveData<List<Task>> = _taskList

    private val _taskStateData = MutableLiveData<StateTask>()
    val taskStateData: LiveData<StateTask> = _taskStateData



    fun getTaskList() = viewModelScope.launch {
        _taskList.postValue(repository.getAllTasks())
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        try {
            repository.deleteTask(task)
            _taskStateData.postValue(StateTask.Deleted)
        } catch (exception: Exception) {

        }
    }

}

class TaskListViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

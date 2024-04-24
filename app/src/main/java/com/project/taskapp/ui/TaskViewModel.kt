package com.project.taskapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.project.taskapp.R
import com.project.taskapp.data.database.Task
import com.project.taskapp.data.database.TaskRepository
import com.project.taskapp.data.model.Status
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
    private val _taskStateData = MutableLiveData<StateTask>()
    val taskStateData: LiveData<StateTask> = _taskStateData

    private val _taskStateMessage = MutableLiveData<Int>()
    val taskStateMessage: LiveData<Int> = _taskStateMessage

    fun insertOrUpdateTask(id: Long = 0L, description: String, status: Status) =
        viewModelScope.launch {
            if (id == 0L) {
                insertTask(Task(description = description, status = status))
            } else {
                updateTask(Task(id = id, description = description, status = status))
            }
        }

    fun getTaskList() = viewModelScope.launch {
        repository.getAllTasks()
    }

    private fun insertTask(task: Task) = viewModelScope.launch {
        try {
            repository.insertTask(task)
            _taskStateData.postValue(StateTask.Inserted)
            _taskStateMessage.postValue(R.string.task_saved_successfully)

        } catch (exception: Exception) {
            _taskStateMessage.postValue(R.string.error_task_creation_message)
        }
    }

    private fun updateTask(task: Task) = viewModelScope.launch {
        repository.updateTask(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.deleteTask(task)
    }
}

class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


sealed class StateTask{
    object Inserted: StateTask()
    object Updated: StateTask()
    object Deleted: StateTask()
    object List: StateTask()

}
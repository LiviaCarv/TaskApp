package com.project.taskapp.ui

import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.project.taskapp.R
import com.project.taskapp.data.model.Status
import com.project.taskapp.data.model.Task
import com.project.taskapp.util.FirebaseHelper
import com.project.taskapp.util.StateView
import com.project.taskapp.util.showBottomSheet
import java.lang.Thread.State

class TaskViewModel : ViewModel() {
    private val _taskList = MutableLiveData<StateView<List<Task>>>()
    val taskList: LiveData<StateView<List<Task>>> = _taskList

    private val _taskInsert = MutableLiveData<StateView<Task>>()
    val taskInsert: LiveData<StateView<Task>> = _taskInsert

    private val _taskUpdate = MutableLiveData<StateView<Task>>()
    val taskUpdate: LiveData<StateView<Task>> = _taskUpdate

    private val _taskDelete = MutableLiveData<StateView<Task>>()
    val taskDelete: LiveData<StateView<Task>> = _taskDelete

    fun getTaskList() {
        try {
            _taskList.postValue(StateView.OnLoading())

            val dbCurrentUser = FirebaseHelper.getDatabase().child("tasks")
                .child(FirebaseHelper.getAuth().currentUser!!.uid)

            dbCurrentUser.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Task>()
                    for (ds in snapshot.children) {
                        val task = ds.getValue(Task::class.java) as Task
                        list.add(task)
                    }
                    list.reverse()
                    _taskList.postValue(StateView.OnSuccess(list))
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.i("INFOTESTE", "onCancelled:")
                }
            })
        } catch (exception: Exception) {
            _taskList.postValue(StateView.OnError(exception.message.toString()))
        }
    }

    fun saveTask(task: Task) {
        try {
            _taskInsert.postValue(StateView.OnLoading())
            FirebaseHelper.getDatabase()
                .child("tasks")
                .child(FirebaseHelper.getUserId())
                .child(task.id)
                .setValue(task)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        _taskInsert.postValue(StateView.OnSuccess(task))
                    } else {

                    }
                }
        } catch (exception: Exception) {
            _taskInsert.postValue(StateView.OnError(exception.message.toString()))
        }
    }

    fun updateTask(task: Task) {
        try {
            _taskUpdate.postValue(StateView.OnLoading())

            val map = mapOf(
                "description" to task.description,
                "status" to task.status,
            )
            FirebaseHelper.getDatabase()
                .child("tasks")
                .child(FirebaseHelper.getUserId())
                .child(task.id)
                .updateChildren(map)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        _taskUpdate.postValue(StateView.OnSuccess(task))
                    }
                }
        } catch (exception: Exception) {
            _taskUpdate.postValue(StateView.OnError(exception.message.toString()))
        }
    }

    fun deleteTask(task: Task) {
        try {
            _taskDelete.postValue(StateView.OnLoading())

            FirebaseHelper.getDatabase()
                .child("tasks")
                .child(FirebaseHelper.getUserId())
                .child(task.id)
                .removeValue().addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        _taskDelete.postValue(StateView.OnSuccess(task))
                    }
                }

        } catch (exception: Exception) {
            _taskDelete.postValue(StateView.OnError(exception.message.toString()))
        }

    }
}
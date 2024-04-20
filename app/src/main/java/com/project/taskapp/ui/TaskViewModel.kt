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
import com.project.taskapp.util.showBottomSheet

class TaskViewModel : ViewModel() {
    private val _taskList  = MutableLiveData<List<Task>>()
    val taskList: LiveData<List<Task>> = _taskList

    private val _taskInsert  = MutableLiveData<Task>()
    val taskInsert: LiveData<Task> = _taskInsert

    private val _taskUpdate  = MutableLiveData<Task>()
    val taskUpdate: LiveData<Task> = _taskUpdate

    private val _taskDelete  = MutableLiveData<Task>()
    val taskDelete: LiveData<Task> = _taskDelete

    fun getTaskList(status: Status) {
        val dbCurrentUser = FirebaseHelper.getDatabase().child("tasks").child(FirebaseHelper.getAuth().currentUser!!.uid)

        dbCurrentUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Task>()
                for (ds in snapshot.children) {
                    val task = ds.getValue(Task::class.java) as Task
                    if (task.status == status) {
                        list.add(task)
                    }
                }
                list.reverse()
                _taskList.postValue(list)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("INFOTESTE", "onCancelled:")
            }
        })
    }

    fun saveTask(task: Task) {
        FirebaseHelper.getDatabase()
            .child("tasks")
            .child(FirebaseHelper.getUserId())
            .child(task.id)
            .setValue(task)
            .addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    _taskInsert.postValue(task)
                } else {

                }
            }
    }

    fun updateTask(task: Task) {
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
                    _taskUpdate.postValue(task)
                }
            }
    }

    fun deleteTask(task: Task) {
        FirebaseHelper.getDatabase()
            .child("tasks")
            .child(FirebaseHelper.getUserId())
            .child(task.id)
            .removeValue().addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    _taskDelete.postValue(task)
                }
            }

    }

}
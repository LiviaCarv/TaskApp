package com.project.taskapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.project.taskapp.data.model.Status


@Entity(tableName = "tasks_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Long,

    var description: String,
    var status: Status = Status.TODO
)
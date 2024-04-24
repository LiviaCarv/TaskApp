package com.project.taskapp.data.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.project.taskapp.data.model.Status
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tasks_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    var description: String,
    var status: Status = Status.TODO
) : Parcelable
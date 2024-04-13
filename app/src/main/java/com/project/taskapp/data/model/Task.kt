package com.project.taskapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    val id: String,
    val description: String,
    var status: Enum<Status> = Status.TODO
) : Parcelable

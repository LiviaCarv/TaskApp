package com.project.taskapp.data.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
data class Task(
    var id: String = "",
    var description: String = "",
    var status: Status = Status.TODO
) : Parcelable

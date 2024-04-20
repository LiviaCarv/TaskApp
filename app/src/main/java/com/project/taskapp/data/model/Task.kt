package com.project.taskapp.data.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import com.project.taskapp.util.FirebaseHelper
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
data class Task(
    var id: String = "",
    var description: String = "",
    var status: Status = Status.TODO
) : Parcelable {
    init {
        this.id = FirebaseHelper.getDatabase().push().key ?: ""
    }

}

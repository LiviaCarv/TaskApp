package com.project.taskapp.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.project.taskapp.R

class FirebaseHelper {
    companion object {
        fun getDatabase() = Firebase.database.reference

        fun getAuth() = FirebaseAuth.getInstance()

        fun getUser() = getAuth().currentUser

        fun getUserId() = getAuth().currentUser?.uid ?: ""

        fun isUserAuthenticated() = getAuth().currentUser != null

        fun validError(error: String): Int {
            return when {
                error.contains("There is no user record corresponding to this identifier") -> {
                    R.string.auth_account_not_registered
                }

                error.contains("The email address is badly formatted") -> {
                    R.string.auth_invalid_email
                }

                error.contains("The password is invalid or the user does not have a password") -> {
                    R.string.auth_invalid_password
                }

                error.contains("The email address is already in use by another account") -> {
                    R.string.auth_email_already_registered
                }

                error.contains("Password should be at least 6 characters") -> {
                    R.string.auth_require_stronger_password
                }

                else -> {
                    R.string.error_generic
                }
            }
        }
    }
}
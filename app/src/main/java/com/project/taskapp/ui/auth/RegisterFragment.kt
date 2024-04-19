package com.project.taskapp.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.project.taskapp.R
import com.project.taskapp.databinding.FragmentRegisterBinding
import com.project.taskapp.ui.BaseFragment
import com.project.taskapp.util.FirebaseHelper
import com.project.taskapp.util.initToolBar
import com.project.taskapp.util.showBottomSheet


class RegisterFragment : BaseFragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolBar(binding.toolbar)
        initListener()

    }

    private fun initListener() {
        binding.btnCreateAccount.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        val email: String = binding.edtInputEmail.text.toString()
        val password: String = binding.edtInputPassword.text.toString()

        if (email.isNotEmpty()) {
            if (password.isNotEmpty()) {
                hideKeyboard()
                binding.progressBar.isVisible = true
                registerUser(email, password)

            } else {
                showBottomSheet(message = getString(R.string.provide_a_password))
            }

        } else {
            showBottomSheet(message = getString(R.string.insert_a_valid_email))
        }
    }

    private fun registerUser(email: String, password: String) {
        FirebaseHelper.getAuth().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.i("REGISTER", "createUserWithEmail:success")
                    val user = FirebaseHelper.getUser()
                    findNavController().navigate(R.id.action_global_homeFragment)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("REGISTER USER", "createUserWithEmail:failure", task.exception)
                    showBottomSheet(message = getString(FirebaseHelper.validError(task.exception?.message.toString())))
                    binding.progressBar.isVisible = false
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
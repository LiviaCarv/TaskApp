package com.project.taskapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.project.taskapp.R
import com.project.taskapp.databinding.FragmentRecoverAccountBinding
import com.project.taskapp.databinding.FragmentRegisterBinding
import com.project.taskapp.util.FirebaseHelper
import com.project.taskapp.util.initToolBar
import com.project.taskapp.util.showBottomSheet


class RecoverAccountFragment : Fragment() {

    private var _binding: FragmentRecoverAccountBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecoverAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolBar(binding.toolbar)
        initListener()

    }

    private fun initListener() {
        binding.btnSendEmail.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        val email: String = binding.edtInputEmail.text.toString()

        if (email.isNotEmpty()) {
            binding.progressBar.isVisible = true
            recoverUserAccount(email)
        } else {
            showBottomSheet(message = getString(R.string.provide_email))
        }
    }

    private fun recoverUserAccount(email: String) {
        FirebaseHelper.getAuth().sendPasswordResetEmail(email)
            .addOnCompleteListener {task ->
                binding.progressBar.isVisible = false
                if (task.isSuccessful) {
                  showBottomSheet(message = getString(R.string.text_message_recover_fragment))

                } else{
                    showBottomSheet(message = getString(FirebaseHelper.validError(task.exception?.message.toString())))
                }
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
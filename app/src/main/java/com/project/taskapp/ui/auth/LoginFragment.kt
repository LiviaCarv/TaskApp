package com.project.taskapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.project.taskapp.R
import com.project.taskapp.databinding.FragmentLoginBinding
import com.project.taskapp.databinding.FragmentSplashBinding
import com.project.taskapp.util.showBottomSheet


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() {
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnRecoverPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recoverAccountFragment)
        }
        binding.btnLogin.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        val email: String = binding.edtInputEmail.text.toString()
        val password: String = binding.edtInputPassword.text.toString()

        if (email.isNotEmpty()) {
            if (password.isNotEmpty()) {
                findNavController().navigate(R.id.action_global_homeFragment)
            } else {
                showBottomSheet(message = getString(R.string.provide_password))
            }

        } else {
            showBottomSheet(message = getString(R.string.provide_email))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
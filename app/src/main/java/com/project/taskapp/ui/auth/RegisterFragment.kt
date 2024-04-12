package com.project.taskapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.project.taskapp.R
import com.project.taskapp.databinding.FragmentRegisterBinding
import com.project.taskapp.util.initToolBar
import com.project.taskapp.util.showBottomSheet


class RegisterFragment : Fragment() {

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
        val password: String  = binding.edtInputPassword.text.toString()

        if (email.isNotEmpty()) {
            if (password.isNotEmpty()) {
                Toast.makeText(requireContext(), "OK", Toast.LENGTH_SHORT).show()
            } else {
                showBottomSheet(message = R.string.provide_a_password)
            }

        } else {
            showBottomSheet(message = R.string.insert_a_valid_email)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
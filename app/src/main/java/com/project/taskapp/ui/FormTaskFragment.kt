package com.project.taskapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import com.project.taskapp.R
import com.project.taskapp.databinding.FragmentFormTaskBinding
import com.project.taskapp.databinding.FragmentRecoverAccountBinding
import com.project.taskapp.util.initToolBar
import com.project.taskapp.util.showBottomSheet


class FormTaskFragment : Fragment() {
    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFormTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initToolBar(binding.toolbar)
    }

    private fun initListener() {
        binding.btnSaveTask.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        val description: String = binding.edtInputTaskDescription.text.toString()
        if (description.isNotEmpty()) {
            Toast.makeText(requireContext(), "OK", Toast.LENGTH_SHORT).show()
        } else {
            showBottomSheet(message = R.string.provide_task_description)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
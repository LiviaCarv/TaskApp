package com.project.taskapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.project.taskapp.R
import com.project.taskapp.data.model.Status
import com.project.taskapp.data.model.Task
import com.project.taskapp.databinding.FragmentFormTaskBinding
import com.project.taskapp.util.initToolBar
import com.project.taskapp.util.showBottomSheet


class FormTaskFragment : Fragment() {
    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var task: Task
    private var newTask: Boolean = true
    private lateinit var reference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFormTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reference = Firebase.database.reference
        auth = Firebase.auth

        initListener()
        initToolBar(binding.toolbar)
    }

    private fun initListener() {
        binding.btnSaveTask.setOnClickListener {
            validateData()
        }
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            task.status = when (checkedId) {
                R.id.radio_opt_todo -> Status.TODO
                R.id.radio_opt_doing -> Status.DOING
                else -> Status.DONE
            }
        }
    }

    private fun validateData() {
        val description: String = binding.edtInputTaskDescription.text.toString()
        if (description.isNotEmpty()) {
            binding.progressBar.isVisible = true
           if (newTask) task = Task(description = description)
            val id = reference.database.reference.push().key
            task.id = id ?: ""
            saveTask()
        } else {
            showBottomSheet(message = getString(R.string.provide_task_description))
        }

    }

    private fun saveTask() {
        reference
            .child("tasks")
            .child(auth.currentUser?.uid  ?: "")
            .child(task.id)
            .setValue(task)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Task saved successfully.", Toast.LENGTH_SHORT).show()
                    if (newTask) {
                        findNavController().popBackStack()
                    }
                } else {
                    showBottomSheet(message = getString(R.string.error_task_creation_message))
                }
            }
        binding.progressBar.isVisible = false

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
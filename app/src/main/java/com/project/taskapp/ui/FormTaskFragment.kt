package com.project.taskapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.project.taskapp.R
import com.project.taskapp.data.model.Status
import com.project.taskapp.data.model.Task
import com.project.taskapp.databinding.FragmentFormTaskBinding
import com.project.taskapp.util.initToolBar
import com.project.taskapp.util.showBottomSheet


class FormTaskFragment : BaseFragment() {
    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var task: Task
    private var newTask: Boolean = true
    private var status: Status = Status.TODO

    private val args: FormTaskFragmentArgs by navArgs()
    private val viewModel: TaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFormTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getArgs()
        initListener()
        initToolBar(binding.toolbar)
    }

    private fun getArgs() {
        args.task.let {
            if (it != null) {
                task = it
                configTask()
            }
        }
    }

    private fun configTask() {
        newTask = false
        status = task.status
        binding.toolbarTitle.text = getString(R.string.toolbar_editing_task)
        binding.edtInputTaskDescription.setText(task.description)
        setStatus()
    }

    private fun setStatus() {
        val id = when (task.status) {
            Status.TODO -> R.id.radio_opt_todo
            Status.DOING -> R.id.radio_opt_doing
            else -> R.id.radio_opt_done
        }
        binding.radioGroup.check(id)
    }


    private fun initListener() {
        binding.btnSaveTask.setOnClickListener {
            observeViewModel()
            validateData()
        }
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            status = when (checkedId) {
                R.id.radio_opt_todo -> Status.TODO
                R.id.radio_opt_doing -> Status.DOING
                else -> Status.DONE
            }
        }
    }

    private fun validateData() {
        val description: String = binding.edtInputTaskDescription.text.toString()
        if (description.isNotEmpty()) {
            hideKeyboard()
            binding.progressBar.isVisible = true
            if (newTask) task = Task()
            task.description = description
            task.status = status
            if (newTask) {
                viewModel.saveTask(task)
            } else {
                viewModel.updateTask(task)
            }
        } else {
            showBottomSheet(message = getString(R.string.provide_task_description))
        }

    }
    private fun observeViewModel() {
        viewModel.taskUpdate.observe(viewLifecycleOwner) { _ ->
            Toast.makeText(
                requireContext(),
                getString(R.string.task_updated_successfully), Toast.LENGTH_SHORT
            ).show()
            binding.progressBar.isVisible = false
        }


        viewModel.taskInsert.observe(viewLifecycleOwner) { _ ->
            Toast.makeText(
                        requireContext(),
                        getString(R.string.task_saved_successfully), Toast.LENGTH_SHORT
                    ).show()
            findNavController().popBackStack()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
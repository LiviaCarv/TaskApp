package com.project.taskapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.project.taskapp.R
import com.project.taskapp.data.model.Status
import com.project.taskapp.data.database.Task
import com.project.taskapp.data.database.TaskDatabase
import com.project.taskapp.data.database.TaskRepository
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
    private lateinit var viewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFormTaskBinding.inflate(inflater, container, false)


        val dataSource = TaskDatabase.getDatabase(requireContext()).taskDao()
        val repository = TaskRepository(dataSource)
        val viewModelFactory = TaskViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TaskViewModel::class.java)

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
            if (newTask) {
                viewModel.insertOrUpdateTask(description=description, status = status)
            } else {
                viewModel.insertOrUpdateTask(id = task.id, description=description, status = status)
            }
        } else {
            showBottomSheet(message = getString(R.string.provide_task_description))
        }
        findNavController().popBackStack()

    }
    private fun observeViewModel() {
        viewModel.taskStateMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(
                requireContext(),
                getString(message),
                Toast.LENGTH_SHORT
            ).show()
            binding.progressBar.isVisible = false
        }


        viewModel.taskStateData.observe(viewLifecycleOwner) { stateTask ->
            if (stateTask == StateTask.Inserted || stateTask == StateTask.Updated) {
                findNavController().popBackStack()
            }

        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
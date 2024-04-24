package com.project.taskapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.project.taskapp.R
import com.project.taskapp.data.model.Status
import com.project.taskapp.data.database.Task
import com.project.taskapp.data.database.TaskDatabase
import com.project.taskapp.data.database.TaskRepository
import com.project.taskapp.databinding.FragmentTaskListBinding
import com.project.taskapp.ui.adapter.TaskListAdapter
import com.project.taskapp.util.StateView
import com.project.taskapp.util.showBottomSheet

class TaskListFragment : Fragment() {
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskListAdapter: TaskListAdapter
    private lateinit var viewModel: TaskListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)

        val dataSource = TaskDatabase.getDatabase(requireContext()).taskDao()
        val repository = TaskRepository(dataSource)
        val viewModelFactory = TaskListViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[TaskListViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        initRecyclerView()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTaskList()
    }

    private fun initListener() {
        binding.fabAddTask.setOnClickListener {
            findNavController().navigate(TaskListFragmentDirections.actionTaskListFragmentToFormTaskFragment(null))
        }

    }

    private fun observeViewModel() {
        viewModel.taskList.observe(viewLifecycleOwner) { taskList ->
            taskListAdapter.submitList(taskList)
            listEmpty(taskList)
        }

        viewModel.taskStateData.observe(viewLifecycleOwner) { stateData ->
           viewModel.getTaskList()
        }

    }

    private fun initRecyclerView() {
        taskListAdapter = TaskListAdapter { taskItem, option ->
            optionSelected(taskItem, option)
        }
        binding.recyclerTaskList.apply {
            adapter = taskListAdapter
            setHasFixedSize(true)
        }
    }

    private fun optionSelected(task: Task, option: Int) {
        when (option) {
            R.id.btn_remove_task ->  showBottomSheet(
                titleButton = R.string.title_button_confirm,
                titleDialog = R.string.title_dialog_confirm_delete,
                message = getString(R.string.text_message_confirm_delete),
                onClick = {
                    viewModel.deleteTask(task)
                })
            R.id.btn_edit_task -> {
                val action = TaskListFragmentDirections.actionTaskListFragmentToFormTaskFragment(task)
                findNavController().navigate(action)
            }
            R.id.btn_task_details -> {Toast.makeText(requireContext(), "details task ${task.description}", Toast.LENGTH_SHORT).show()}

        }
    }


    private fun listEmpty(taskList: List<Task>) {
        binding.txtInfo.text = if (taskList.isEmpty()) {
            getString(R.string.text_task_list_empty)
        } else  {
            ""
        }
        binding.progressBar.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
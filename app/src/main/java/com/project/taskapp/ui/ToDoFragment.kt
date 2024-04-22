package com.project.taskapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.project.taskapp.R
import com.project.taskapp.data.model.Status
import com.project.taskapp.data.model.Task
import com.project.taskapp.databinding.FragmentToDoBinding
import com.project.taskapp.ui.adapter.TaskListAdapter
import com.project.taskapp.util.StateView
import com.project.taskapp.util.showBottomSheet

class ToDoFragment : Fragment() {
    private var _binding: FragmentToDoBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskListAdapter: TaskListAdapter
    private val viewModel: TaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentToDoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        initRecyclerView()
        observeViewModel()
        viewModel.getTaskList()

    }

    private fun initListener() {
        binding.fabAddTask.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFormTaskFragment(null))
        }

    }

    private fun observeViewModel() {

        viewModel.taskList.observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.OnLoading -> {
                    binding.progressBar.isVisible = true
                }
                is StateView.OnSuccess -> {
                    binding.progressBar.isVisible = false

                    val taskList = stateView.data?.filter { it.status == Status.TODO }

                    listEmpty(taskList ?: emptyList())
                    taskListAdapter.submitList(taskList)
                }
                else -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(
                        requireContext(),
                        stateView.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


        }

        viewModel.taskInsert.observe(viewLifecycleOwner) {stateView ->
            when (stateView) {
                is StateView.OnLoading -> {
                    binding.progressBar.isVisible = true
                }
                is StateView.OnSuccess -> {
                    binding.progressBar.isVisible = false
                    if (stateView.data?.status == Status.TODO) {
                        val oldList = taskListAdapter.currentList

                        val newList = oldList.toMutableList().apply {
                            add(0, stateView.data)
                        }
                        listEmpty(newList)
                        taskListAdapter.submitList(newList)
                        setPositionRecyclerView()
                    }
                }
                else -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(
                        requireContext(),
                        stateView.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

        viewModel.taskUpdate.observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.OnLoading -> {
                    binding.progressBar.isVisible = true
                }
                is StateView.OnSuccess -> {
                    binding.progressBar.isVisible = false
                    val oldList = taskListAdapter.currentList

                    val newList = oldList.toMutableList().apply {
                        if (!oldList.contains(stateView.data) && stateView.data?.status == Status.TODO) {
                            add(0, stateView.data)
                            setPositionRecyclerView()
                        }
                        if (stateView.data?.status == Status.TODO) {
                            find { it.id == stateView.data.id }?.description = stateView.data.description }
                         else {
                            remove(stateView.data)
                        }
                    }

                    val position = newList.indexOfFirst { it.id == stateView.data?.id }
                    listEmpty(newList)
                    taskListAdapter.submitList(newList)
                    taskListAdapter.notifyItemChanged(position)
                }
                else -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(
                        requireContext(),
                        stateView.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

        viewModel.taskDelete.observe(viewLifecycleOwner) {stateView ->
            when (stateView) {
                is StateView.OnLoading -> {
                    binding.progressBar.isVisible = true
                }
                is StateView.OnSuccess -> {
                    binding.progressBar.isVisible = false
                    val oldList = taskListAdapter.currentList
                    val newList = oldList.toMutableList().apply {
                        remove(stateView.data)
                    }
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.task_deleted_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                    listEmpty(newList)
                    taskListAdapter.submitList(newList)

                }
                else -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(
                        requireContext(),
                        stateView.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

    }

    private fun initRecyclerView() {

        taskListAdapter = TaskListAdapter( requireContext()) { taskItem, option ->
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
                val action = HomeFragmentDirections.actionHomeFragmentToFormTaskFragment(task)
                findNavController().navigate(action)
            }
            R.id.btn_task_details -> {Toast.makeText(requireContext(), "details task ${task.description}", Toast.LENGTH_SHORT).show()}
            R.id.btn_forward -> {
                task.status = Status.DOING
                viewModel.updateTask(task)
            }
        }
    }

    private fun setPositionRecyclerView() {
        taskListAdapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.recyclerTaskList.scrollToPosition(0)
            }

        })
    }

    private fun listEmpty(taskList: List<Task>) {
        binding.txtInfo.text = if (taskList.isEmpty()) {
            getString(R.string.text_task_list_empty)
        } else  {
            ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
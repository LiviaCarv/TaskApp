package com.project.taskapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.project.taskapp.R
import com.project.taskapp.data.model.Status
import com.project.taskapp.data.model.Task
import com.project.taskapp.databinding.FragmentToDoBinding
import com.project.taskapp.ui.adapter.TaskListAdapter
import com.project.taskapp.util.FirebaseHelper
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
        viewModel.getTaskList(Status.TODO)

    }


    private fun initListener() {
        binding.fabAddTask.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFormTaskFragment(null))
        }

    }

    private fun observeViewModel() {

        viewModel.taskList.observe(viewLifecycleOwner) {taskList ->
            binding.progressBar.isVisible = false
            listEmpty(taskList)
            taskListAdapter.submitList(taskList)

        }

        viewModel.taskInsert.observe(viewLifecycleOwner) {newTask ->
            if (newTask.status == Status.TODO) {
                val oldList = taskListAdapter.currentList

                val newList = oldList.toMutableList().apply {
                    add(0, newTask)
                }
                taskListAdapter.submitList(newList)
                setPositionRecyclerView()
            }
        }

        viewModel.taskUpdate.observe(viewLifecycleOwner) { updatedTask ->
            val oldList = taskListAdapter.currentList

            val newList = oldList.toMutableList().apply {
                if (updatedTask.status == Status.TODO) {
                    find { it.id == updatedTask.id }?.description = updatedTask.description }
                else {
                    remove(updatedTask)
                }
            }

            val position = newList.indexOfFirst { it.id == updatedTask.id }

            taskListAdapter.submitList(newList)
            taskListAdapter.notifyItemChanged(position)

        }

        viewModel.taskDelete.observe(viewLifecycleOwner) {deleteTask ->
            val oldList = taskListAdapter.currentList

            val newList = oldList.toMutableList().apply {
                remove(deleteTask)
            }

            Toast.makeText(
                requireContext(),
                getString(R.string.task_deleted_successfully),
                Toast.LENGTH_SHORT
            ).show()

            taskListAdapter.submitList(newList)

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
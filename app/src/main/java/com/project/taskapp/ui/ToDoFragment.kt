package com.project.taskapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.project.taskapp.R
import com.project.taskapp.data.model.Status
import com.project.taskapp.data.model.Task
import com.project.taskapp.databinding.FragmentToDoBinding
import com.project.taskapp.ui.adapter.TaskListAdapter

class ToDoFragment : Fragment() {
    private var _binding: FragmentToDoBinding? = null
    private val binding get() = _binding!!
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
        initRecyclerView(getTaskList())
    }

    private fun initListener() {
        binding.fabAddTask.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_formTaskFragment)
        }
    }

    private fun initRecyclerView(taskList: List<Task>) {
        binding.recyclerTaskList.apply {
            adapter = TaskListAdapter( requireContext(), taskList) { taskItem, option ->
                optionSelected(taskItem, option)
            }
            setHasFixedSize(true)
        }
    }

    private fun optionSelected(task: Task, option: Int) {
        when (option) {
            R.id.btn_remove_task -> {Toast.makeText(requireContext(), "removendo task ${task.description}", Toast.LENGTH_SHORT).show()}
            R.id.btn_edit_task -> {Toast.makeText(requireContext(), "editando task ${task.description}", Toast.LENGTH_SHORT).show()}
            R.id.btn_task_details -> {Toast.makeText(requireContext(), "details task ${task.description}", Toast.LENGTH_SHORT).show()}
            R.id.btn_forward -> {Toast.makeText(requireContext(), "forward task ${task.description}", Toast.LENGTH_SHORT).show()}
        }
    }

    private fun getTaskList() = listOf(
        Task(id = "1", description = "Teste 1"),
        Task(id = "2", description = "Teste 2"),
        Task(id = "3", description = "Teste 3"),
        Task(id = "4", description = "Teste 4"),
        Task(id = "5", description = "Teste 5"),
        Task(id = "6", description = "Teste 6")
        )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.project.taskapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.taskapp.data.model.Status
import com.project.taskapp.data.model.Task
import com.project.taskapp.databinding.FragmentDoneBinding
import com.project.taskapp.ui.adapter.TaskListAdapter


class DoneFragment : Fragment() {
    private var _binding: FragmentDoneBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDoneBinding.inflate(inflater, container, false)
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(getTaskList())
    }

    private fun initRecyclerView(taskList: List<Task>) {
        binding.recyclerTaskList.apply {
            adapter = TaskListAdapter(taskList)
            setHasFixedSize(true)
        }
    }

    private fun getTaskList() = listOf(
        Task(id = "1", description = "Teste 1 ${Status.DONE}", Status.DONE),
        Task(id = "2", description = "Teste 2 ${Status.DONE}", Status.DONE),
        Task(id = "3", description = "Teste 3 ${Status.DONE}", Status.DONE),
        Task(id = "4", description = "Teste 4 ${Status.DONE}", Status.DONE),
        Task(id = "5", description = "Teste 5 ${Status.DONE}", Status.DONE),
        Task(id = "6", description = "Teste 6 ${Status.DONE}", Status.DONE)
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
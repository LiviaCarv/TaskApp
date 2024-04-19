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
        getTaskList()

    }


    private fun initListener() {
        binding.fabAddTask.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFormTaskFragment(null))
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.taskUpdate.observe(viewLifecycleOwner) { updatedTask ->
            if (updatedTask.status == Status.TODO) {
                val oldList = taskListAdapter.currentList

                val newList = oldList.toMutableList().apply {
                    find { it.id == updatedTask.id }?.description = updatedTask.description
                }

                val position = newList.indexOfFirst { it.id == updatedTask.id }

                taskListAdapter.submitList(newList)
                taskListAdapter.notifyItemChanged(position)
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
                    deleteTask(task)
                })
            R.id.btn_edit_task -> {
                val action = HomeFragmentDirections.actionHomeFragmentToFormTaskFragment(task)
                findNavController().navigate(action)
            }
            R.id.btn_task_details -> {Toast.makeText(requireContext(), "details task ${task.description}", Toast.LENGTH_SHORT).show()}
            R.id.btn_forward -> {
                task.status = Status.DOING
            updateTaskStatus(task)}
        }
    }

    private fun updateTaskStatus(task: Task) {
        FirebaseHelper.getDatabase()
            .child("tasks")
            .child(FirebaseHelper.getUserId())
            .child(task.id)
            .setValue(task).addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    Toast.makeText(requireContext(), getString(R.string.task_saved_successfully), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), getString(R.string.error_generic), Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun getTaskList() {
        val dbCurrentUser = FirebaseHelper.getDatabase().child("tasks").child(FirebaseHelper.getAuth().currentUser!!.uid)

        dbCurrentUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val taskList = mutableListOf<Task>()
                for (ds in snapshot.children) {
                    val task = ds.getValue(Task::class.java) as Task
                    if (task.status == Status.TODO) {
                        taskList.add(task)
                    }
                }
                binding.progressBar.isVisible = false
                listEmpty(taskList)
                taskList.reverse()
                taskListAdapter.submitList(taskList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("INFOTESTE", "onCancelled:")
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

    private fun deleteTask(task: Task) {
        FirebaseHelper.getDatabase()
            .child("tasks")
            .child(FirebaseHelper.getUserId())
            .child(task.id)
            .removeValue().addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    Toast.makeText(requireContext(), getString(R.string.task_deleted_successfully), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), getString(R.string.error_generic), Toast.LENGTH_SHORT).show()
                }
            }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
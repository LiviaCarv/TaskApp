package com.project.taskapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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
import com.project.taskapp.util.showBottomSheet

class ToDoFragment : Fragment() {
    private var _binding: FragmentToDoBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskListAdapter: TaskListAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference

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
        reference = Firebase.database.reference
        auth = Firebase.auth

        initListener()
        initRecyclerView()
        getTaskList()
    }

    private fun initListener() {
        binding.fabAddTask.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_formTaskFragment)
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
            R.id.btn_edit_task -> {Toast.makeText(requireContext(), "editando task ${task.description}", Toast.LENGTH_SHORT).show()}
            R.id.btn_task_details -> {Toast.makeText(requireContext(), "details task ${task.description}", Toast.LENGTH_SHORT).show()}
            R.id.btn_forward -> {Toast.makeText(requireContext(), "forward task ${task.description}", Toast.LENGTH_SHORT).show()}
        }
    }

    private fun getTaskList() {
        Log.i("TESTE", "getTaskList() chamado")
        reference
            .child("tasks")
            .child(auth.currentUser?.uid  ?: "")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.i("TESTE", "onDataChange() chamado")
                    val taskList = mutableListOf<Task>()
                    for (ds in snapshot.children) {
                        Log.i("TESTE", "ds In Snapshot: (${ds.value})")
                        val taskMap = ds.value as? Map<*, *> ?: continue
                        val id = taskMap["id"] as? String ?: ""
                        val description = taskMap["description"] as? String ?: ""
                        val statusString = taskMap["status"] as? String ?: ""
                        val status = Status.valueOf(statusString)
                        val task = Task(id, description, status)

                        if (task.status == Status.TODO) {
                            taskList.add(task)
                        }
                    }

                    taskList.reverse()
                    listEmpty(taskList)
                    taskListAdapter.submitList(taskList)
                    Log.i("TESTE", "taskList size = ${taskList.size} adicionado ao taskList Adapter")
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(),
                        getString(R.string.error_generic), Toast.LENGTH_SHORT).show()

                }
            })
    }

    private fun deleteTask(task: Task) {
        reference
            .child("tasks")
            .child(auth.currentUser?.uid  ?: "")
            .child(task.id)
            .removeValue().addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    Toast.makeText(requireContext(), getString(R.string.task_deleted_successfully), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), getString(R.string.error_generic), Toast.LENGTH_SHORT).show()
                }
            }

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
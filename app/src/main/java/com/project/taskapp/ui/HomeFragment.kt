package com.project.taskapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.project.taskapp.R
import com.project.taskapp.databinding.FragmentHomeBinding
import com.project.taskapp.ui.adapter.ViewPagerAdapter
import com.project.taskapp.util.showBottomSheet


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        initTabLayout()
        logoutListener()
    }

    private fun logoutListener() {
        binding.btnLogout.setOnClickListener {
            showBottomSheet(
                message = getString(R.string.text_message_logout),
                titleDialog = R.string.title_dialog_confirm_logout,
                titleButton = R.string.title_button_confirm,
                onClick = {
                    auth.signOut()
                    findNavController().navigate(R.id.action_homeFragment_to_authentication)
                }
            )
        }
    }

    private fun initTabLayout() {
        val viewPagerAdapter = ViewPagerAdapter(requireActivity())
        binding.viewPager.adapter = viewPagerAdapter

        viewPagerAdapter.addFragment(ToDoFragment(), R.string.toDo)
        viewPagerAdapter.addFragment(DoingFragment(), R.string.doing)
        viewPagerAdapter.addFragment(DoneFragment(), R.string.done)

        binding.viewPager.offscreenPageLimit = viewPagerAdapter.itemCount

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = getString(viewPagerAdapter.getTitle(position))
        }.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
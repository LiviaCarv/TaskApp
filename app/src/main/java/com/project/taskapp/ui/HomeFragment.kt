package com.project.taskapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.project.taskapp.R
import com.project.taskapp.databinding.FragmentHomeBinding
import com.project.taskapp.ui.adapter.ViewPagerAdapter


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPager: ViewPager2
    private lateinit var viewPagerAdapter: ViewPagerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabLayout()

    }

    private fun initTabLayout() {
        viewPagerAdapter = ViewPagerAdapter(requireActivity())
        viewPager = binding.viewPager
        viewPager.adapter = viewPagerAdapter

        viewPagerAdapter.addFragment(ToDoFragment(), R.string.toDo)
        viewPagerAdapter.addFragment(DoingFragment(), R.string.doing)
        viewPagerAdapter.addFragment(DoneFragment(), R.string.done)

        binding.viewPager.offscreenPageLimit = viewPagerAdapter.itemCount

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = getString(viewPagerAdapter.getTitle(position))
        }.attach()

    }

}
package com.project.taskapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    private val titleList = mutableListOf<Int>()
    private val fragmentsList = mutableListOf<Fragment>()

    fun getTitle(position: Int): Int {
        return titleList[position]
    }

    fun addFragment(fragment: Fragment, title: Int) {
        fragmentsList.add(fragment)
        titleList.add(title)
    }

    override fun getItemCount(): Int {
        return titleList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentsList[position]
    }

}

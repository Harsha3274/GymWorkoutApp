package com.wodo.gymapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class WorkoutPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> WorkoutListFragment.newInstance("Beginner")
            1 -> WorkoutListFragment.newInstance("Intermediate")
            2 -> WorkoutListFragment.newInstance("Advanced")
            else -> Fragment()
        }
    }
}

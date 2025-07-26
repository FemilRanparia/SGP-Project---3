package com.example.sgpproject_3

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout

class CourseFragment : Fragment(R.layout.fragment_course) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = view.findViewById<TabLayout>(R.id.topTabLayout)

        val subFragments = listOf(
            MyCoursesFragment(),
            ScheduleFragment(),
            MaterialFragment(),
            MeetingsFragment()
        )

        val tabNames = listOf("My Courses", "Schedule", "Materials", "Meetings")

        tabNames.forEach { name ->
            tabLayout.addTab(tabLayout.newTab().setText(name))
        }

        childFragmentManager.beginTransaction()
            .replace(R.id.subFragmentContainer, subFragments[0])
            .commit()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val index = tab.position
                childFragmentManager.beginTransaction()
                    .replace(R.id.subFragmentContainer, subFragments[index])
                    .commit()
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}

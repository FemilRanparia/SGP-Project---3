package com.example.sgpproject_3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.sgpproject_3.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val notificationIcon = view.findViewById<ImageView>(R.id.notificationIcon)
        val profileIcon = view.findViewById<ImageView>(R.id.profileIcon)
        notificationIcon.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.home_inner_fragment_container, NotificationsFragment())
                .addToBackStack(null)
                .commit()
        }
        profileIcon.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.home_inner_fragment_container, ProfileFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
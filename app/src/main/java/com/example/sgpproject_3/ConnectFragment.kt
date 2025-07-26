package com.example.sgpproject_3

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment

class ConnectFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_connect, container, false)

        val staticChatItem2 = view.findViewById<ConstraintLayout>(R.id.static_chat_item_container_2)
        staticChatItem2.setOnClickListener {
            val intent = Intent(requireContext(), ChatActivity::class.java)
            intent.putExtra("username", "Hitarth Shukla")
            intent.putExtra("profileImageRes", R.drawable.profile)
            startActivity(intent)
        }

        val staticChatItem3 = view.findViewById<ConstraintLayout>(R.id.static_chat_item_container_3)
        staticChatItem3.setOnClickListener {
            val intent = Intent(requireContext(), ChatActivity::class.java)
            intent.putExtra("username", "Hiren Solanki")
            intent.putExtra("profileImageRes", R.drawable.profile)
            startActivity(intent)
        }

        return view
    }
}
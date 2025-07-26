package com.example.sgpproject_3

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChatActivity : AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var profileImage: ImageView
    private lateinit var usernameText: TextView
    private lateinit var menuButton: ImageView
    private lateinit var attachButton: ImageView
    private lateinit var micButton: ImageView
    private lateinit var sendButton: ImageView
    private lateinit var messageInput: EditText
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Bind views
        backButton = findViewById(R.id.backButton)
        profileImage = findViewById(R.id.profileImage)
        usernameText = findViewById(R.id.usernameText)
        menuButton = findViewById(R.id.menuButton)
        attachButton = findViewById(R.id.attachButton)
        micButton = findViewById(R.id.micButton)
        sendButton = findViewById(R.id.sendButton)
        messageInput = findViewById(R.id.messageInput)
        recyclerView = findViewById(R.id.recyclerView)

        // Receive data from ChatListActivity
        val username = intent.getStringExtra("username")
        val profileImageRes = intent.getIntExtra("profileImageRes", R.drawable.sample_profile)

        // Set user info
        usernameText.text = username
        profileImage.setImageResource(profileImageRes)

        // Setup RecyclerView (empty for now)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        // recyclerView.adapter = YourAdapter()

        backButton.setOnClickListener { finish() }

        menuButton.setOnClickListener {
            Toast.makeText(this, "Menu clicked", Toast.LENGTH_SHORT).show()
        }

        attachButton.setOnClickListener {
            Toast.makeText(this, "Attach clicked", Toast.LENGTH_SHORT).show()
        }

        micButton.setOnClickListener {
            Toast.makeText(this, "Mic clicked", Toast.LENGTH_SHORT).show()
        }

        sendButton.setOnClickListener {
            val message = messageInput.text.toString().trim()
            if (message.isNotEmpty()) {
                Toast.makeText(this, "Sending: $message", Toast.LENGTH_SHORT).show()
                messageInput.text.clear()
            } else {
                Toast.makeText(this, "Message is empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

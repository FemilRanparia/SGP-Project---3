package com.example.sgpproject_3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text

class Role : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_role)
        val received_name = intent.getStringExtra("username")

        val username = findViewById<TextView>(R.id.username)
        username.text = "Welcome "+received_name
        val continue_as_learner = findViewById<Button>(R.id.continue_as_learner)
        val continue_as_sharer = findViewById<Button>(R.id.continue_as_sharer)

        continue_as_learner.setOnClickListener {
            val interest_redirect = Intent(this, Interests::class.java)
            startActivity(interest_redirect)
            finish()
        }
        continue_as_sharer.setOnClickListener {
            val interest_redirect = Intent(this, Interests::class.java)
            startActivity(interest_redirect)
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
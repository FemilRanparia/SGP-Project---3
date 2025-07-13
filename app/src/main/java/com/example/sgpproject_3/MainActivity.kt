package com.example.sgpproject_3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val signup_redirect = findViewById<Button>(R.id.signup_redirect)
        val input_name_login = findViewById<EditText>(R.id.input_name_login)
        val login = findViewById<Button>(R.id.login)

        login.setOnClickListener {
            Toast.makeText(this, "You have logged in", Toast.LENGTH_SHORT).show()
            val role_redirect = Intent(this, Role::class.java)
            val input_name = input_name_login.text.toString()
            role_redirect.putExtra("username",input_name)
            startActivity(role_redirect)
            finish()
        }

        signup_redirect.setOnClickListener {
            val sign_re_intent = Intent(this, SignUp::class.java)
            startActivity(sign_re_intent)
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
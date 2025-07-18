package com.example.sgpproject_3

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        val login_redirect = findViewById<Button>(R.id.login_redirect)
        val signup = findViewById<Button>(R.id.signup)
        val input_name_signup = findViewById<EditText>(R.id.input_name_sign_up)

        signup.setOnClickListener {
            val input_name = input_name_signup.text.toString().trim()
            when {
                input_name.isEmpty() -> {
                    showAlertDialog("Username Required", "Please Enter Your username before login.")
                }

                input_name.length > 15 -> {
                    showAlertDialog("Username too long", "Name should not excced 15 characters")
                }

                input_name.contains(" ") -> {
                    showAlertDialog("Invalid Username", "Username cannot contain spaces")
                }

                !input_name.matches(Regex("^[A-Za-z]{1,15}$")) -> {
                    showAlertDialog(
                        "Invalid Characters",
                        "Username must only contain letters no digits or special characters"
                    )
                }

                else -> {
                    Toast.makeText(this, "Your Account Created Successfully!", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
        
        login_redirect.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun showAlertDialog(title: String, message: String){
        AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton("OK"){
                dialog, _ ->dialog.dismiss()
        }
            .setCancelable(false).show()
    }
}
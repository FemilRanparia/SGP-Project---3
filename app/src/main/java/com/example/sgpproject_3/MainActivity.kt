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

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val signup_redirect = findViewById<Button>(R.id.signup_redirect)
        val input_name_login = findViewById<EditText>(R.id.input_name_login)
        val password_login = findViewById<EditText>(R.id.password_login)
        val login = findViewById<Button>(R.id.login)

        login.setOnClickListener {
            Toast.makeText(this, "You have logged in", Toast.LENGTH_SHORT).show()
            val role_redirect = Intent(this, Role::class.java)
            val input_name = input_name_login.text.toString().trim()
            val password_login = password_login.text.toString()
            when{
                input_name.isEmpty() -> {
                    showAlertDialog("Email Required", "Please Enter Your email before login.")
                }
                password_login.isEmpty() -> {
                    showAlertDialog("Password Required", "Please Enter Your Password before login.")
                }
                input_name.length>15 -> {
                    showAlertDialog("Username too long", "Name should not excced 15 characters")
                }
                password_login.length < 6 -> {
                    showAlertDialog("Password too short", "Please enter minimum 6 digit password")
                }
                input_name.contains(" ") -> {
                    showAlertDialog("Invalid Username", "Username cannot contain spaces")
                }
                password_login.contains(" ") -> {
                    showAlertDialog("Invalid Password", "Password should not contain spaces")
                }
                !input_name.matches(Regex("^[A-Za-z]{1,15}$")) -> {
                    showAlertDialog(
                        "Invalid Characters",
                        "Username must only contain letters no digits or special characters"
                    )
                }
                else -> {
                    role_redirect.putExtra("username", input_name)
                    startActivity(role_redirect)
                    finish()
                }
            }

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
    private fun showAlertDialog(title: String, message: String){
        AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton("OK"){
                dialog, _ ->dialog.dismiss()
        }
            .setCancelable(false).show()
    }
}
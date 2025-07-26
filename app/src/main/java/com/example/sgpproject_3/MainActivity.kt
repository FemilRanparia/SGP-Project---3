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
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val signup_redirect = findViewById<Button>(R.id.signup_redirect)
        val input_email_login = findViewById<EditText>(R.id.input_name_login)
        val password_login = findViewById<EditText>(R.id.password_login)
        val login = findViewById<Button>(R.id.login)

        login.setOnClickListener {
            val email = input_email_login.text.toString().trim()
            val password = password_login.text.toString().trim()

            when {
                email.isEmpty() -> showAlertDialog("Email Required", "Please enter your email.")
                password.isEmpty() -> showAlertDialog("Password Required", "Please enter your password.")
                password.length < 6 -> showAlertDialog("Weak Password", "Password must be at least 6 characters.")
                else -> {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                if (user != null && user.isEmailVerified) {
                                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                                    val roleRedirectIntent = Intent(this, Role::class.java)
                                    roleRedirectIntent.putExtra("username", email)
                                    startActivity(roleRedirectIntent)
                                    finish()
                                } else {
                                    auth.signOut()
                                    showAlertDialog("Email Not Verified", "Please verify your email before logging in.")
                                }
                            } else {
                                showAlertDialog("Login Failed", "Invalid email or password.")
                            }
                        }
                }
            }
        }

        signup_redirect.setOnClickListener {
            val signUpIntent = Intent(this, SignUp::class.java)
            startActivity(signUpIntent)
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .show()
    }
}

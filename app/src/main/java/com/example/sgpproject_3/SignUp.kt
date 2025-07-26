package com.example.sgpproject_3

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        val loginRedirect = findViewById<Button>(R.id.login_redirect)
        val signup = findViewById<Button>(R.id.signup)
        val emailInput = findViewById<EditText>(R.id.input_email_sign_up)
        val passwordInput = findViewById<EditText>(R.id.password_signup)

        signup.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            when {
                email.isEmpty() || password.isEmpty() -> {
                    showAlertDialog("Missing Fields", "Please enter both email and password.")
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    showAlertDialog("Invalid Email", "Please enter a valid email address.")
                }
                password.length < 6 -> {
                    showAlertDialog("Weak Password", "Password must be at least 6 characters long.")
                }
                else -> {
                    signup.isEnabled = false
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            signup.isEnabled = true
                            if (task.isSuccessful) {
                                auth.currentUser?.sendEmailVerification()
                                    ?.addOnCompleteListener { verificationTask ->
                                        if (verificationTask.isSuccessful) {
                                            showAlertDialog(
                                                "Verify Email",
                                                "Account created! Please check your email and verify before logging in."
                                            )
                                        } else {
                                            showAlertDialog(
                                                "Verification Failed",
                                                "Account created, but failed to send verification email."
                                            )
                                        }
                                    }
                                // Don't redirect yet — wait until email is verified manually
                            } else {
                                showAlertDialog("Signup Failed", task.exception?.message ?: "Unknown error")
                            }
                        }
                }
            }
        }

        loginRedirect.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
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

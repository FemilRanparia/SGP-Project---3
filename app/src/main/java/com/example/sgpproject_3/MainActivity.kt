package com.example.sgpproject_3

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val signupRedirect = findViewById<Button>(R.id.signup_redirect)
        val inputEmailLogin = findViewById<EditText>(R.id.input_name_login)
        val passwordLogin = findViewById<EditText>(R.id.password_login)
        val login = findViewById<Button>(R.id.login)
        val googleLogin = findViewById<Button>(R.id.login_with_google)

        login.setOnClickListener {
            val email = inputEmailLogin.text.toString().trim()
            val password = passwordLogin.text.toString().trim()

            when {
                email.isEmpty() -> showAlertDialog("Email Required", "Please enter your email.")
                password.isEmpty() -> showAlertDialog("Password Required", "Please enter your password.")
                password.length < 6 -> showAlertDialog("Weak Password", "Password must be at least 6 characters.")
                else -> {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
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
                            showAlertDialog("Login Failed", task.exception?.message ?: "Unknown error")
                        }
                    }
                }
            }
        }

        googleLogin.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        }

        signupRedirect.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d("GoogleLogin", "Result code: ${result.resultCode}")   // <-- log result code
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.result
                    Log.d("GoogleLogin", "Got account: ${account?.email}") // <-- log email

                    if (account != null) {
                        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                        auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                            if (authTask.isSuccessful) {
                                val email = auth.currentUser?.email ?: "Unknown"
                                Log.d("GoogleLogin", "Firebase auth success for $email")
                                Toast.makeText(this, "Google Sign-In successful!", Toast.LENGTH_SHORT).show()
                                val roleRedirectIntent = Intent(this, Role::class.java)
                                roleRedirectIntent.putExtra("username", email)
                                startActivity(roleRedirectIntent)
                                finish()
                            } else {
                                Log.e("GoogleLogin", "Firebase auth failed", authTask.exception)
                                showAlertDialog("Google Login Failed", authTask.exception?.message ?: "Unknown error")
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("GoogleLogin", "Sign-in failed", e)
                    showAlertDialog("Google Sign-In Failed", e.localizedMessage ?: "Unknown error")
                }
            } else {
                Log.e("GoogleLogin", "Result NOT_OK, code: ${result.resultCode}")
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

package com.example.sgpproject_3

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

class Role : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_role)
        val received_name = intent.getStringExtra("username")

        val username = findViewById<TextView>(R.id.username)
        if (received_name != null) {
            username.text = "Welcome "+received_name.substringBefore("@")
        }
        val continue_as_learner = findViewById<Button>(R.id.continue_as_learner)
        val continue_as_sharer = findViewById<Button>(R.id.continue_as_sharer)

        continue_as_learner.setOnClickListener {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid != null) {
                val db = FirebaseFirestore.getInstance()
                val userDoc = db.collection("users").document(uid)
                userDoc.set(mapOf("role" to "learner"), SetOptions.merge())
                    .addOnSuccessListener {
                        val interest_redirect = Intent(this, Interests::class.java)
                        startActivity(interest_redirect)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error saving role", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        continue_as_sharer.setOnClickListener {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid != null) {
                val db = FirebaseFirestore.getInstance()
                val userDoc = db.collection("users").document(uid)
                userDoc.set(mapOf("role" to "sharer"), SetOptions.merge())
                    .addOnSuccessListener {
                        val interest_redirect = Intent(this, Interests::class.java)
                        startActivity(interest_redirect)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error saving role", Toast.LENGTH_SHORT).show()
                    }
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
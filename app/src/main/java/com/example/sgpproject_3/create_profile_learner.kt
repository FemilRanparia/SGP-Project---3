package com.example.sgpproject_3

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class create_profile_learner : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_profile_learner)
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val uid = auth.currentUser?.uid ?: return
        val firstName = findViewById<EditText>(R.id.editTextText3)
        val lastName = findViewById<EditText>(R.id.editTextText4)
        val email = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val phone = findViewById<EditText>(R.id.editTextPhone)
        val city = findViewById<EditText>(R.id.editTextText5)
        val genderSpinner = findViewById<Spinner>(R.id.genderSpinner)
        val btnCreate = findViewById<Button>(R.id.create_profile_final)
        val genderOptions = resources.getStringArray(R.array.gender_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = adapter

        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedGender = parent.getItemAtPosition(position).toString()
                if (selectedGender != "Select Gender") {
                    Toast.makeText(this@create_profile_learner, "Selected: $selectedGender", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        btnCreate.setOnClickListener {
            val first = firstName.text.toString().trim()
            val last = lastName.text.toString().trim()
            val mail = email.text.toString().trim()
            val phoneNo = phone.text.toString().trim()
            val cityName = city.text.toString().trim()
            val gender = genderSpinner.selectedItem.toString()
            if (first.isEmpty() || last.isEmpty() || mail.isEmpty() || phoneNo.isEmpty() || cityName.isEmpty() || gender == "Select Gender") {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val user = hashMapOf(
                "firstName" to first,
                "lastName" to last,
                "email" to mail,
                "phone" to phoneNo,
                "city" to cityName,
                "gender" to gender
            )
            db.collection("users").document(uid)
                .set(user, com.google.firebase.firestore.SetOptions.merge())
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile Created Successfully!", Toast.LENGTH_SHORT).show()
                    val homeFeedRedirectIntent = Intent(this, Home::class.java)
                    startActivity(homeFeedRedirectIntent)
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
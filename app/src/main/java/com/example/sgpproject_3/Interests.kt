package com.example.sgpproject_3

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import android.widget.Toast
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setMargins
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Interests : AppCompatActivity() {

    private lateinit var leftPanel: LinearLayout
    private lateinit var rightPanel: LinearLayout
    private lateinit var selectedOptionsLayout: LinearLayout

    private val selectedOptions = mutableSetOf<String>()

    private val interestMap = mapOf(
        "Art" to listOf("Drawing", "Painting", "Sculpting"),
        "Music" to listOf("Singing", "Instrumental", "Production"),
        "Tech" to listOf("AI/ML", "Android", "Web Dev", "Cybersecurity"),
        "Sports" to listOf("Football", "Cricket", "Basketball", "Badminton"),
        "Photography" to listOf("Portrait", "Wildlife", "Street"),
        "Writing" to listOf("Poetry", "Short Stories", "Novels"),
        "Cooking" to listOf("Baking", "Continental", "Indian"),
        "Gaming" to listOf("PC", "Console", "Mobile"),
        "Dance" to listOf("Hip-hop", "Ballet", "Contemporary")
    )

    private var selectedCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_interests)

        val create_profile_redirect_btn = findViewById<Button>(R.id.create_profile_redirect)
        create_profile_redirect_btn.setOnClickListener {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid != null) {
                val db = FirebaseFirestore.getInstance()
                val userDoc = db.collection("users").document(uid)
                userDoc.set(mapOf("interests" to selectedOptions.toList()), SetOptions.merge())
                    .addOnSuccessListener {
                        Toast.makeText(this, "Interests saved!", Toast.LENGTH_SHORT).show()
                        val createProfileIntent = Intent(this, create_profile_learner::class.java)
                        startActivity(createProfileIntent)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error saving interests: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        leftPanel = findViewById(R.id.leftPanel)
        rightPanel = findViewById(R.id.rightPanel)
        selectedOptionsLayout = findViewById(R.id.selectedOptionsLayout)
        createLeftButtons()
    }
    private fun createLeftButtons() {
        leftPanel.removeAllViews()

        for ((category) in interestMap) {
            val button = Button(this).apply {
                text = category
                isAllCaps = false
                setTextColor(Color.WHITE)
                setPadding(32, 16, 32, 16)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(8) }

                background = createRectDrawable(
                    if (selectedCategory == category) "#331876" else "#5271ff"
                )

                setOnClickListener {
                    selectedCategory = category
                    createLeftButtons()
                    showSubOptions(category)
                }
            }
            leftPanel.addView(button)
        }
    }

    private fun showSubOptions(category: String) {
        rightPanel.removeAllViews()

        val subOptions = interestMap[category] ?: return

        for (option in subOptions) {
            val isSelected = selectedOptions.contains(option)

            val subButton = Button(this).apply {
                text = option
                isAllCaps = false
                setTextColor(Color.WHITE)
                background = createOvalDrawable(
                    if (isSelected) "#00C853" else "#331876"
                )
                setPadding(32, 16, 32, 16)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(8) }

                setOnClickListener {
                    toggleSelection(option)
                    showSubOptions(category)
                }
            }

            rightPanel.addView(subButton)
        }
    }

    private fun toggleSelection(option: String) {
        if (selectedOptions.contains(option)) {
            selectedOptions.remove(option)
        } else {
            selectedOptions.add(option)
        }
        updateSelectedOptionsDisplay()
    }

    private fun updateSelectedOptionsDisplay() {
        selectedOptionsLayout.removeAllViews()

        for (option in selectedOptions) {
            val chip = Button(this).apply {
                text = option
                isAllCaps = false
                setTextColor(Color.WHITE)
                background = createOvalDrawable("#5271ff")
                setPadding(32, 16, 32, 16)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(8) }

                setOnClickListener {
                    selectedOptions.remove(option)
                    updateSelectedOptionsDisplay()
                    selectedCategory?.let { showSubOptions(it) }
                }
            }
            selectedOptionsLayout.addView(chip)
        }
    }

    private fun createOvalDrawable(hexColor: String): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 100f
            setColor(Color.parseColor(hexColor))
        }
    }

    private fun createRectDrawable(hexColor: String): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 16f
            setColor(Color.parseColor(hexColor))
        }
    }
}
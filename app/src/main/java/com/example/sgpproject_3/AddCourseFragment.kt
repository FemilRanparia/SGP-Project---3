package com.example.sgpproject_3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddCourseFragment : Fragment() {

    private lateinit var etCourseTitle: EditText
    private lateinit var spinnerCourseDomain: Spinner
    private lateinit var etCourseDuration: EditText
    private lateinit var etCourseFrequency: EditText
    private lateinit var spinnerAccessibility: Spinner
    private lateinit var btnSaveCourse: Button
    private lateinit var btnBack: Button

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var userName: String = "" // sharer's name
    private var selectedInterests: List<String> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_course, container, false)

        etCourseTitle = view.findViewById(R.id.etCourseTitle)
        spinnerCourseDomain = view.findViewById(R.id.spinnerCourseDomain)
        etCourseDuration = view.findViewById(R.id.etCourseDuration)
        etCourseFrequency = view.findViewById(R.id.etCourseFrequency)
        spinnerAccessibility = view.findViewById(R.id.spinnerAccessibility)
        btnSaveCourse = view.findViewById(R.id.btnSaveCourse)
        btnBack = view.findViewById(R.id.btnBack)

        // Back button
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Load sharer's name & interests from Firestore
        val uid = auth.currentUser?.uid ?: return view
        db.collection("users").document(uid).get().addOnSuccessListener { doc ->
            userName = doc.getString("firstName") + " " + doc.getString("lastName")
            selectedInterests = doc.get("interests") as? List<String> ?: emptyList()

            // Populate Course Domain spinner with only user's interests
            val domainAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                selectedInterests
            )
            domainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCourseDomain.adapter = domainAdapter
        }

        // Accessibility Spinner
        val accessibilityOptions = listOf("Online", "Offline", "Both")
        val accessAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, accessibilityOptions)
        accessAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAccessibility.adapter = accessAdapter

        // Save course
        btnSaveCourse.setOnClickListener {
            val title = etCourseTitle.text.toString().trim()
            val domain = spinnerCourseDomain.selectedItem?.toString() ?: ""
            val duration = etCourseDuration.text.toString().trim()
            val frequency = etCourseFrequency.text.toString().trim()
            val accessibility = spinnerAccessibility.selectedItem?.toString() ?: ""

            if (title.isEmpty() || domain.isEmpty() || duration.isEmpty() || frequency.isEmpty() || accessibility.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val course = hashMapOf(
                "title" to title,
                "domain" to domain,
                "durationMonths" to duration.toInt(),
                "classesPerWeek" to frequency.toInt(),
                "accessibility" to accessibility,
                "sharerName" to userName,
                "sharerUID" to uid
            )

            db.collection("Courses").add(course).addOnSuccessListener {
                Toast.makeText(requireContext(), "Course added successfully!", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }.addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}

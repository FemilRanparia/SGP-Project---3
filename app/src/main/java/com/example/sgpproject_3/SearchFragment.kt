package com.example.sgpproject_3

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SearchFragment : Fragment() {

    private lateinit var searchBar: EditText
    private lateinit var addCourseBtn: Button
    private lateinit var btnSearchIcon: ImageButton
    private lateinit var rvCourses: RecyclerView
    private lateinit var tvEmpty: TextView

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private var isLearner = false
    private lateinit var adapter: CourseAdapter
    private val allCourses = mutableListOf<Course>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchBar = view.findViewById(R.id.searchBar)
        addCourseBtn = view.findViewById(R.id.btnAddCourse)
        btnSearchIcon = view.findViewById(R.id.btnSearchIcon)
        rvCourses = view.findViewById(R.id.rvCourses)
        tvEmpty = view.findViewById(R.id.tvEmpty)

        rvCourses.layoutManager = LinearLayoutManager(requireContext())
        adapter = CourseAdapter(requireContext(), mutableListOf(), isLearner) { course, holderPosition ->
        }
        rvCourses.adapter = adapter


        addCourseBtn.visibility = View.GONE

        val uid = auth.currentUser?.uid
        if (uid != null) {
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val role = document.getString("role") ?: "learner"
                        isLearner = role == "learner"
                        if (role == "sharer") {
                            addCourseBtn.visibility = View.VISIBLE
                            addCourseBtn.setOnClickListener {
                                parentFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, AddCourseFragment())
                                    .addToBackStack(null)
                                    .commit()
                            }
                        } else {
                            addCourseBtn.visibility = View.GONE
                        }

                        adapter.isLearner = isLearner
                        adapter.notifyDataSetChanged()
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("SearchFragment", "Failed to fetch role", e)
                }
        }

        btnSearchIcon.setOnClickListener { performSearch() }

        searchBar.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                performSearch()
                true
            } else false
        }

        return view
    }

    private fun performSearch() {
        val q = searchBar.text.toString().trim()
        if (q.isEmpty()) {
            return
        }

        db.collection("Courses").get()
            .addOnSuccessListener { snap ->
                allCourses.clear()
                for (doc in snap.documents) {
                    val id = doc.id
                    val title = doc.getString("title") ?: ""
                    val domain = doc.getString("domain") ?: ""
                    val durationMonths = doc.getLong("durationMonths")?.toInt() ?: 0
                    val classesPerWeek = doc.getLong("classesPerWeek")?.toInt() ?: 0
                    val accessibility = doc.getString("accessibility") ?: ""
                    val sharerName = doc.getString("sharerName") ?: ""
                    val sharerUID = doc.getString("sharerUID") ?: ""
                    val createdAt = doc.getTimestamp("createdAt") ?: Timestamp.now()

                    val course = Course(
                        id = id,
                        title = title,
                        domain = domain,
                        durationMonths = durationMonths,
                        classesPerWeek = classesPerWeek,
                        accessibility = accessibility,
                        sharerName = sharerName,
                        sharerUID = sharerUID,
                        createdAt = createdAt
                    )

                    if (title.contains(q, ignoreCase = true) || domain.contains(q, ignoreCase = true)) {
                        allCourses.add(course)
                    }
                }

                showResults(allCourses)
            }
            .addOnFailureListener { e ->
                Log.e("SearchFragment", "Failed to fetch courses", e)
            }
    }

    private fun showResults(results: List<Course>) {
        if (results.isEmpty()) {
            tvEmpty.visibility = View.VISIBLE
            rvCourses.visibility = View.GONE
        } else {
            tvEmpty.visibility = View.GONE
            rvCourses.visibility = View.VISIBLE
            adapter.updateData(results.toMutableList(), isLearner)
        }
    }
}

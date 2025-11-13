package com.example.sgpproject_3

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CourseAdapter(
    private val ctx: Context,
    private var courses: MutableList<Course>,
    var isLearner: Boolean,

    private val enrollCallback: ((Course, Int) -> Unit)? = null
) : RecyclerView.Adapter<CourseAdapter.VH>() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvCourseTitle)
        val tvDomain: TextView = itemView.findViewById(R.id.tvCourseDomain)
        val tvDetails: TextView = itemView.findViewById(R.id.tvCourseDetails)
        val tvSharer: TextView = itemView.findViewById(R.id.tvSharer)
        val btnEnroll: Button = itemView.findViewById(R.id.btnEnroll)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(ctx).inflate(R.layout.course_card, parent, false)
        return VH(v)
    }

    override fun getItemCount(): Int = courses.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val c = courses[position]
        holder.tvTitle.text = c.title
        holder.tvDomain.text = c.domain
        holder.tvDetails.text = "Duration: ${c.durationMonths} months • ${c.classesPerWeek} classes/week • ${c.accessibility}"
        holder.tvSharer.text = "By: ${c.sharerName}"

        holder.btnEnroll.visibility = if (isLearner) View.VISIBLE else View.GONE

        val currentUid = auth.currentUser?.uid
        if (currentUid != null) {
            db.collection("Enrollments")
                .whereEqualTo("courseId", c.id)
                .whereEqualTo("userId", currentUid)
                .get()
                .addOnSuccessListener { snap ->
                    if (!snap.isEmpty) {
                        holder.btnEnroll.isEnabled = false
                        holder.btnEnroll.text = "Enrolled"
                    } else {
                        holder.btnEnroll.isEnabled = true
                        holder.btnEnroll.text = "Enroll"
                    }
                }
                .addOnFailureListener {
                }
        }

        holder.btnEnroll.setOnClickListener {
            holder.btnEnroll.isEnabled = false
            holder.btnEnroll.text = "Enrolling..."

            val uid = auth.currentUser?.uid
            if (uid == null) {
                holder.btnEnroll.isEnabled = true
                holder.btnEnroll.text = "Enroll"
                return@setOnClickListener
            }

            val enrollment = hashMapOf(
                "courseId" to c.id,
                "userId" to uid,
                "enrolledAt" to Timestamp.now(),
                "sharerUID" to c.sharerUID,
                "courseTitle" to c.title
            )

            db.collection("Enrollments").add(enrollment)
                .addOnSuccessListener {
                    holder.btnEnroll.isEnabled = false
                    holder.btnEnroll.text = "Enrolled"
                    enrollCallback?.invoke(c, position)
                }
                .addOnFailureListener { e ->
                    holder.btnEnroll.isEnabled = true
                    holder.btnEnroll.text = "Enroll"
                    Log.e("CourseAdapter", "Enroll failed", e)
                }
        }
    }

    fun updateData(newList: MutableList<Course>, learnerFlag: Boolean) {
        isLearner = learnerFlag
        courses = newList
        notifyDataSetChanged()
    }
}

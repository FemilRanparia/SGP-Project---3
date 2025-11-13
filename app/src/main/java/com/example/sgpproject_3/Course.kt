package com.example.sgpproject_3

import com.google.firebase.Timestamp

data class Course(
    val id: String = "",
    val title: String = "",
    val domain: String = "",
    val durationMonths: Int = 0,
    val classesPerWeek: Int = 0,
    val accessibility: String = "",
    val sharerName: String = "",
    val sharerUID: String = "",
    val createdAt: Timestamp? = null
)
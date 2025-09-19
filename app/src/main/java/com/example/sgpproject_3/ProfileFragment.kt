package com.example.sgpproject_3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ProfileFragment : Fragment() {

    private lateinit var tvEmail: TextView
    private lateinit var tvRole: TextView
    private lateinit var tvName: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvCity: TextView
    private lateinit var tvGender: TextView
    private lateinit var viewChipGroup: ChipGroup
    private lateinit var editFieldsContainer: View
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etCity: EditText
    private lateinit var spinnerGender: Spinner
    private lateinit var editInterestsContainer: LinearLayout
    private lateinit var btnEditProfile: Button
    private lateinit var btnSaveProfile: Button
    private lateinit var btnCancelEdit: Button
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

    private val selectedInterests = mutableSetOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        tvEmail = view.findViewById(R.id.tvEmail)
        tvRole = view.findViewById(R.id.tvRole)
        tvName = view.findViewById(R.id.tvName)
        tvPhone = view.findViewById(R.id.tvPhone)
        tvCity = view.findViewById(R.id.tvCity)
        tvGender = view.findViewById(R.id.tvGender)
        viewChipGroup = view.findViewById(R.id.viewChipGroup)

        editFieldsContainer = view.findViewById(R.id.editFieldsContainer)
        etFirstName = view.findViewById(R.id.etFirstName)
        etLastName = view.findViewById(R.id.etLastName)
        etPhone = view.findViewById(R.id.etPhone)
        etCity = view.findViewById(R.id.etCity)
        spinnerGender = view.findViewById(R.id.spinnerGender)
        editInterestsContainer = view.findViewById(R.id.editInterestsContainer)

        btnEditProfile = view.findViewById(R.id.btnEditProfile)
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile)
        btnCancelEdit = view.findViewById(R.id.btnCancelEdit)

        val genderOptions = listOf("Select Gender", "Male", "Female", "Other")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genderOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGender.adapter = adapter

        btnEditProfile.setOnClickListener { enterEditMode() }
        btnCancelEdit.setOnClickListener { exitEditMode(reload = true) }
        btnSaveProfile.setOnClickListener { saveProfileToFirestore() }

        loadUserProfile()

        return view
    }

    private fun loadUserProfile() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                if (!doc.exists()) return@addOnSuccessListener

                val firstName = doc.getString("firstName") ?: ""
                val lastName = doc.getString("lastName") ?: ""
                val phone = doc.getString("phone") ?: ""
                val city = doc.getString("city") ?: ""
                val gender = doc.getString("gender") ?: "Select Gender"
                val email = doc.getString("email") ?: ""
                val role = doc.getString("role") ?: ""
                val interests = (doc.get("interests") as? List<*>)?.map { it.toString() } ?: emptyList()
                tvEmail.text = email
                tvRole.text = role
                tvName.text = "$firstName ${lastName}".trim()
                tvPhone.text = phone
                tvCity.text = city
                tvGender.text = gender
                selectedInterests.clear()
                selectedInterests.addAll(interests)
                populateViewChips()
                etFirstName.setText(firstName)
                etLastName.setText(lastName)
                etPhone.setText(phone)
                etCity.setText(city)
                val spinnerPos = (spinnerGender.adapter as ArrayAdapter<String>).getPosition(gender)
                spinnerGender.setSelection(if (spinnerPos >= 0) spinnerPos else 0)
                setupEditInterestsChips()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load profile", Toast.LENGTH_SHORT).show()
            }
    }

    private fun populateViewChips() {
        viewChipGroup.removeAllViews()
        if (selectedInterests.isEmpty()) {
            val none = TextView(requireContext()).apply {
                text = "No interests selected"
                setPadding(8, 8, 8, 8)
            }
            viewChipGroup.addView(none)
            return
        }

        for (interest in selectedInterests) {
            val chip = Chip(requireContext()).apply {
                text = interest
                isClickable = false
                isCheckable = false
            }
            viewChipGroup.addView(chip)
        }
    }

    private fun setupEditInterestsChips() {
        editInterestsContainer.removeAllViews()

        for ((category, options) in interestMap) {
            val header = TextView(requireContext()).apply {
                text = category
                textSize = 14f
                setPadding(8, 14, 8, 8)
            }
            editInterestsContainer.addView(header)
            val chipGroup = ChipGroup(requireContext()).apply {
                isSingleSelection = false
                isSelectionRequired = false
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            for (option in options) {
                val chip = Chip(requireContext()).apply {
                    text = option
                    isCheckable = true
                    isChecked = selectedInterests.contains(option)
                    setOnCheckedChangeListener { _, checked ->
                        if (checked) selectedInterests.add(option) else selectedInterests.remove(option)
                    }
                }
                chipGroup.addView(chip)
            }
            editInterestsContainer.addView(chipGroup)
        }
    }

    private fun enterEditMode() {
        view?.findViewById<View>(R.id.viewFieldsContainer)?.visibility = View.GONE
        editFieldsContainer.visibility = View.VISIBLE

        viewChipGroup.visibility = View.GONE
        editInterestsContainer.visibility = View.VISIBLE

        btnEditProfile.visibility = View.GONE
        btnSaveProfile.visibility = View.VISIBLE
        btnCancelEdit.visibility = View.VISIBLE
    }

    private fun exitEditMode(reload: Boolean = false) {
        view?.findViewById<View>(R.id.viewFieldsContainer)?.visibility = View.VISIBLE
        editFieldsContainer.visibility = View.GONE

        viewChipGroup.visibility = View.VISIBLE
        editInterestsContainer.visibility = View.GONE

        btnEditProfile.visibility = View.VISIBLE
        btnSaveProfile.visibility = View.GONE
        btnCancelEdit.visibility = View.GONE

        if (reload) {
            loadUserProfile()
        } else {
            tvName.text = "${etFirstName.text} ${etLastName.text}".trim()
            tvPhone.text = etPhone.text.toString()
            tvCity.text = etCity.text.toString()
            tvGender.text = spinnerGender.selectedItem.toString()
            populateViewChips()
        }
    }

    private fun saveProfileToFirestore() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        val firstName = etFirstName.text.toString().trim()
        val lastName = etLastName.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val city = etCity.text.toString().trim()
        val gender = spinnerGender.selectedItem.toString()

        if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || city.isEmpty() || gender == "Select Gender") {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val updates = hashMapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "phone" to phone,
            "city" to city,
            "gender" to gender,
            "interests" to selectedInterests.toList()
        )

        db.collection("users").document(uid)
            .set(updates, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Profile saved", Toast.LENGTH_SHORT).show()
                exitEditMode(reload = true)
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Save failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

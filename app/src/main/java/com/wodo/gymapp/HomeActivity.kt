package com.wodo.gymapp

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.wodo.gymapp.login.LoginActivity
import com.wodo.gymapp.model.Equipment

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var equipmentAdapter: EquipmentAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var sharedPreferences: SharedPreferences
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("GymAppPrefs", MODE_PRIVATE)

        // Initialize UI elements
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        val nextButton = findViewById<Button>(R.id.nextButton)
        val backButton = findViewById<ImageView>(R.id.backButton)

        recyclerView.layoutManager = GridLayoutManager(this, 3)

        // Show the progress bar while loading data
        progressBar.visibility = ProgressBar.VISIBLE

        // Initialize adapter with an empty list and image load callback
        equipmentAdapter = EquipmentAdapter(emptyList(), { selectedEquipment ->
            // Enable or disable the next button based on equipment selection
            nextButton.isEnabled = selectedEquipment.isNotEmpty()
            nextButton.setBackgroundColor(
                if (selectedEquipment.isNotEmpty()) ContextCompat.getColor(this, R.color.Gold)
                else Color.TRANSPARENT
            )
        }) {
            // Callback when all images are loaded
            progressBar.visibility = ProgressBar.GONE
        }

        recyclerView.adapter = equipmentAdapter

        // Fetch equipment from Firestore
        fetchEquipmentFromFirestore()

        // Handle the next button click
        nextButton.setOnClickListener {
            val selectedEquipment = equipmentAdapter.getSelectedItems()
            saveSelectedEquipmentToPreferences(selectedEquipment)  // <-- Add this line

            // Go to the WorkoutsList activity
            val intent = Intent(this, WorkoutsList::class.java)
            startActivity(intent)
        }

        // Handle the back button click
        backButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    // Save selected equipment to SharedPreferences
    private fun saveSelectedEquipmentToPreferences(selectedEquipment: List<Equipment>) {
        val editor = sharedPreferences.edit()

        // Save selected equipment count
        editor.putInt("selectedEquipmentCount", selectedEquipment.size)

        // Save selected equipment names
        val equipmentNames = selectedEquipment.map { it.name }.toSet()
        editor.putStringSet("selectedEquipmentNames", equipmentNames)

        editor.apply()
    }

    // Fetch equipment list from Firestore
    private fun fetchEquipmentFromFirestore() {
        firestore.collection("gymEquipment")
            .get()
            .addOnSuccessListener { result ->
                val equipmentList = mutableListOf<Equipment>()
                for (document in result) {
                    val name = document.getString("name") ?: ""
                    val imageUrl = document.getString("imageUrl") ?: ""
                    equipmentList.add(Equipment(name, imageUrl))
                }

                // Update adapter with the fetched equipment list
                equipmentAdapter.updateEquipmentList(equipmentList)

                // Load selected equipment from preferences if available
                val selectedEquipmentNames = sharedPreferences.getStringSet("selectedEquipmentNames", emptySet())
                if (selectedEquipmentNames != null && selectedEquipmentNames.isNotEmpty()) {
                    equipmentAdapter.updateSelectedEquipmentByName(selectedEquipmentNames)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("HomeActivity", "Error getting documents: ", exception)
                progressBar.visibility = ProgressBar.GONE  // Hide on failure
            }
    }
}

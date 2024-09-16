package com.wodo.gymapp

import android.content.Intent
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
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)

        recyclerView.layoutManager = GridLayoutManager(this, 3) // 3 columns

        val nextButton = findViewById<Button>(R.id.nextButton)
        // Set the initial background of the button to transparent
        nextButton.setBackgroundColor(Color.TRANSPARENT)

        equipmentAdapter = EquipmentAdapter(emptyList()) { selectedEquipment ->
            // Enable the button and change background color to gold if any equipment is selected
            nextButton.isEnabled = selectedEquipment.isNotEmpty()
            if (selectedEquipment.isNotEmpty()) {
                nextButton.setBackgroundColor(ContextCompat.getColor(this, R.color.Gold))
            } else {
                // Reset to transparent background if no equipment is selected
                nextButton.setBackgroundColor(Color.TRANSPARENT)
            }
        }

        recyclerView.adapter = equipmentAdapter

        fetchEquipmentFromFirestore()

        nextButton.setOnClickListener {
            val selectedEquipment = equipmentAdapter.getSelectedItems()
            val intent = Intent(this, WorkoutsList::class.java)
            intent.putExtra("SELECTED_EQUIPMENT", ArrayList(selectedEquipment.map { it.name }))
            startActivity(intent)
        }

        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun fetchEquipmentFromFirestore() {

        // Show the progress bar while fetching data
        progressBar.visibility = ProgressBar.VISIBLE

        firestore.collection("gymEquipment")
            .get()
            .addOnSuccessListener { result ->
                val equipmentList = mutableListOf<Equipment>()
                for (document in result) {
                    val name = document.getString("name") ?: ""
                    val imageUrl = document.getString("imageUrl") ?: ""
                    equipmentList.add(Equipment(name, imageUrl))
                }
                equipmentAdapter.updateEquipmentList(equipmentList)
                // Hide the progress bar when data is loaded
                progressBar.visibility = ProgressBar.GONE
            }
            .addOnFailureListener { exception ->
                Log.w("HomeActivity", "Error getting documents: ", exception)
                // Hide the progress bar if an error occurs
                progressBar.visibility = ProgressBar.GONE
            }
    }
}
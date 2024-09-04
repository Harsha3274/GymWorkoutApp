package com.wodo.gymapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.wodo.gymapp.model.Equipment

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var equipmentAdapter: EquipmentAdapter
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3) // 3 columns

        equipmentAdapter = EquipmentAdapter(emptyList()) { selectedEquipment ->
            findViewById<Button>(R.id.nextButton).isEnabled = selectedEquipment.isNotEmpty()
        }

        recyclerView.adapter = equipmentAdapter

        fetchEquipmentFromFirestore()

        findViewById<Button>(R.id.nextButton).setOnClickListener {
            val selectedEquipment = equipmentAdapter.getSelectedItems()
            val intent = Intent(this, WorkoutLevelActivity::class.java)
            intent.putExtra("SELECTED_EQUIPMENT", ArrayList(selectedEquipment.map { it.name }))
            startActivity(intent)
        }
    }

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
                equipmentAdapter.updateEquipmentList(equipmentList)
            }
            .addOnFailureListener { exception ->
                Log.w("HomeActivity", "Error getting documents: ", exception)
            }
    }
}

package com.wodo.gymapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wodo.gymapp.model.Equipment

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var equipmentAdapter: EquipmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val equipmentList = listOf(
            Equipment("Treadmill", R.drawable.treadmill),
            Equipment("Dumbbells", R.drawable.dumbell),
            Equipment("Kettle bell", R.drawable.kettlebell),
            Equipment("Barbell", R.drawable.barbell),
            Equipment("Battle Rope", R.drawable.battlerope),
            Equipment("Leg Press", R.drawable.legpress),
            Equipment("Row", R.drawable.row),
            Equipment("Weight Training Bench", R.drawable.weighttrainingbench),
        )

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize EquipmentAdapter with the equipment list and handle item selections
        equipmentAdapter = EquipmentAdapter(equipmentList) { selectedEquipment ->
            // This callback is triggered when items are selected or deselected
            // You can update the UI or handle further actions based on the selected items

            // For example, enable/disable the "Next" button based on the selection
            findViewById<Button>(R.id.nextButton).isEnabled = selectedEquipment.isNotEmpty()
        }

        recyclerView.adapter = equipmentAdapter

        // Handle "Next" button click
        findViewById<Button>(R.id.nextButton).setOnClickListener {
            // Get the list of selected equipment
            val selectedEquipment = equipmentAdapter.getSelectedItems()

            // Prepare the intent to navigate to WorkoutLevelActivity
            val intent = Intent(this, WorkoutLevelActivity::class.java)
            intent.putExtra("SELECTED_EQUIPMENT", ArrayList(selectedEquipment.map { it.name }))

            // Start the next activity
            startActivity(intent)
        }
    }
}

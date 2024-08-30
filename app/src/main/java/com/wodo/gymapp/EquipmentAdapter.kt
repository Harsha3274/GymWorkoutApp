package com.wodo.gymapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wodo.gymapp.model.Equipment

class EquipmentAdapter(
    private val equipmentList: List<Equipment>,
    private val onItemClick: (List<Equipment>) -> Unit
) : RecyclerView.Adapter<EquipmentAdapter.EquipmentViewHolder>() {

    private val selectedItems = mutableSetOf<Equipment>()

    inner class EquipmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.equipmentName)
        val imageView: ImageView = itemView.findViewById(R.id.equipmentImage)
        val container: LinearLayout = itemView.findViewById(R.id.itemContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.equipment_item, parent, false)
        return EquipmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: EquipmentViewHolder, position: Int) {
        val equipment = equipmentList[position]
        holder.nameTextView.text = equipment.name
        holder.imageView.setImageResource(equipment.imageResId)

        // Update UI for selection
        if (selectedItems.contains(equipment)) {
            holder.container.setBackgroundColor(Color.LTGRAY)
        } else {
            holder.container.setBackgroundColor(Color.TRANSPARENT)
        }

        holder.itemView.setOnClickListener {
            if (selectedItems.contains(equipment)) {
                selectedItems.remove(equipment)
            } else {
                selectedItems.add(equipment)
            }
            notifyItemChanged(position)

            // Pass the current selection to the callback
            onItemClick(selectedItems.toList())
        }
    }

    override fun getItemCount() = equipmentList.size

    // Method to retrieve selected items
    fun getSelectedItems(): List<Equipment> {
        return selectedItems.toList()
    }
}


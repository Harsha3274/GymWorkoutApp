package com.wodo.gymapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.wodo.gymapp.model.Equipment

class EquipmentAdapter(
    var equipmentList: List<Equipment>,
    private val onSelectionChanged: (List<Equipment>) -> Unit
) : RecyclerView.Adapter<EquipmentAdapter.EquipmentViewHolder>() {

    private val selectedItems = mutableSetOf<Equipment>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.equipment_item, parent, false)
        return EquipmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: EquipmentViewHolder, position: Int) {
        val equipment = equipmentList[position]
        holder.bind(equipment)
    }

    override fun getItemCount(): Int = equipmentList.size

    fun getSelectedItems(): List<Equipment> = selectedItems.toList()

    fun updateEquipmentList(newEquipmentList: List<Equipment>) {
        equipmentList = newEquipmentList
        notifyDataSetChanged()
    }

    // Method to update selected equipment based on names from SharedPreferences
    fun updateSelectedEquipmentByName(selectedEquipmentNames: Set<String>) {
        val selectedEquipmentList = equipmentList.filter { it.name in selectedEquipmentNames }
        selectedItems.clear()
        selectedItems.addAll(selectedEquipmentList)
        notifyDataSetChanged()
        onSelectionChanged(selectedItems.toList())
    }

    inner class EquipmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val equipmentImage: ImageView = itemView.findViewById(R.id.equipmentImage)
        private val equipmentName: TextView = itemView.findViewById(R.id.equipmentName)
        private val checkmarkImage: ImageView = itemView.findViewById(R.id.checkmarkImage)
        private val cardView: CardView = itemView as CardView

        fun bind(equipment: Equipment) {
            equipmentName.text = equipment.name
            Picasso.get().load(equipment.imageUrl).into(equipmentImage)

            updateSelectionState(equipment)

            itemView.setOnClickListener {
                if (selectedItems.contains(equipment)) {
                    selectedItems.remove(equipment)
                } else {
                    selectedItems.add(equipment)
                }
                updateSelectionState(equipment)
                onSelectionChanged(selectedItems.toList())
            }
        }

        private fun updateSelectionState(equipment: Equipment) {
            if (selectedItems.contains(equipment)) {
                checkmarkImage.visibility = View.VISIBLE
                cardView.setCardBackgroundColor(itemView.context.getColor(android.R.color.holo_orange_light))
            } else {
                checkmarkImage.visibility = View.GONE
                cardView.setCardBackgroundColor(itemView.context.getColor(android.R.color.white))
            }
        }
    }
}

package com.wodo.gymapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.wodo.gymapp.model.Equipment

class EquipmentAdapter(
    var equipmentList: List<Equipment>,
    private val onSelectionChanged: (List<Equipment>) -> Unit,
    private val onImageLoaded: () -> Unit // Callback for when images are loaded
) : RecyclerView.Adapter<EquipmentAdapter.EquipmentViewHolder>() {

    private val selectedItems = mutableSetOf<Equipment>()
    private var loadedImageCount = 0  // Keep track of loaded images

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
        private val cardView: CardView = itemView as CardView

        fun bind(equipment: Equipment) {
            equipmentName.text = equipment.name

            // Load image with Picasso and use callback to track when the image is loaded
            Picasso.get().load(equipment.imageUrl).into(equipmentImage, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    // Increment loaded image count
                    loadedImageCount++

                    // Once all images are loaded, trigger the callback to hide the ProgressBar
                    if (loadedImageCount == equipmentList.size) {
                        onImageLoaded()
                    }
                }

                override fun onError(e: Exception?) {
                    // Handle error if needed
                }
            })

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
            cardView.isSelected = selectedItems.contains(equipment)
            if (selectedItems.contains(equipment)) {
                equipmentName.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
            } else {
                equipmentName.setTextColor(ContextCompat.getColor(itemView.context, R.color.lightWhite))
            }
        }

    }
}

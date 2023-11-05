package com.malkinfo.editingrecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malkinfo.editingrecyclerview.activity.WaterActivity
import com.malkinfo.editingrecyclerview.data.Drink

class WaterDrinkAdapter(private val drinks: List<Drink>) :
    RecyclerView.Adapter<WaterDrinkAdapter.WaterDrinkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaterDrinkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_water_drink, parent, false)
        return WaterDrinkViewHolder(view)
    }

    override fun onBindViewHolder(holder: WaterDrinkViewHolder, position: Int) {
        val drink = drinks[position]
        holder.tvName.text = drink.name
        holder.tvVolume.text = "${drink.volume} мл"

        holder.itemView.setOnClickListener {
            val drink = drinks[position]
            (holder.itemView.context as? WaterActivity)?.run {

                updateWaterAmount(drink)
            }
        }

    }

    override fun getItemCount(): Int {
        return drinks.size
    }

    inner class WaterDrinkViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvVolume: TextView = view.findViewById(R.id.tvVolume)
    }
}
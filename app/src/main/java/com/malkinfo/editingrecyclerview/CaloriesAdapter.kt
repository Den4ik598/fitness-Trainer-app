package com.malkinfo.editingrecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malkinfo.editingrecyclerview.activity.CalorieActivity
import com.malkinfo.editingrecyclerview.data.CaloriesData


class CaloriesAdapter(private val activity: CalorieActivity,private val dishes: MutableList<CaloriesData>) :
    RecyclerView.Adapter<CaloriesAdapter.CaloriesAdapterHolder>() {

    private var onItemClick: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaloriesAdapterHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dishes_calories, parent, false)
        return CaloriesAdapterHolder(view)
    }

    override fun onBindViewHolder(holder: CaloriesAdapterHolder, position: Int) {
        val dish = dishes[position]
        holder.tvDName.text = dish.name
        holder.tvDVolume.text = "${dish.volume} кл"

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    fun removeDishes(position: Int) {
        dishes.removeAt(position)
        println(dishes)
        activity.saveWaterDrinksToSharedPreferences()
        notifyItemRemoved(position)
    }

    inner class CaloriesAdapterHolder(view: View) : RecyclerView.ViewHolder(view), View.OnLongClickListener {
        val tvDName: TextView = view.findViewById(R.id.tvDName)
        val tvDVolume: TextView = view.findViewById(R.id.tvDVolume)

        init {
            view.setOnLongClickListener(this)
        }

        override fun onLongClick(v: View?): Boolean {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                removeDishes(position)
            }
            return true
        }
    }


}
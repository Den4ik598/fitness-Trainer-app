package com.malkinfo.editingrecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malkinfo.editingrecyclerview.data.Exercise

class ExerciseAdapter(private val exerciseList: MutableList<Exercise>) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    var onItemClickListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_exercises, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(exerciseList[position])
    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val durationTextView: TextView = itemView.findViewById(R.id.durationTextView)




        fun bind(exercise: Exercise) {
            nameTextView.text = exercise.name
            descriptionTextView.text = exercise.description
            durationTextView.text = "Duration: ${exercise.durationInSeconds} seconds"

        }
    }
}
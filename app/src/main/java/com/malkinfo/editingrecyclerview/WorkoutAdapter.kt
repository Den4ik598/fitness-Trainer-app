package com.malkinfo.editingrecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malkinfo.editingrecyclerview.data.WorkoutClass

class WorkoutAdapter(val workoutList: MutableList<WorkoutClass>) : RecyclerView.Adapter<WorkoutAdapter.ExerciseViewHolder>() {

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun addWorkout(workout: WorkoutClass) {
        workoutList.add(workout)
        notifyItemInserted(workoutList.lastIndex)
    }
    fun setWorkoutList(newList: List<WorkoutClass>) {
        workoutList.clear()
        workoutList.addAll(newList)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        if (position >= 0 && position < workoutList.size) { // проверка на допустимый диапазон
            val workoutName = workoutList[position]
            holder.bind(workoutName)
        }
    }

    override fun getItemCount(): Int = workoutList.size

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titleTextView = itemView.findViewById<TextView>(R.id.titleTextView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION && position >= 0 && position < workoutList.size) {
                    onItemClickListener?.invoke(position)
                }
            }
        }

        fun bind(workoutName: WorkoutClass) {
            titleTextView.text = workoutName.title
        }
    }
}
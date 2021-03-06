package com.example.tracker.exercises

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.R
import com.example.tracker.common.entities.Exercise
import com.example.tracker.databinding.ExercisesListItemBinding


class ExercisesAdapter internal constructor(
    val listener: ExerciseListListener
) : RecyclerView.Adapter<ExercisesAdapter.ExercisesViewHolder>() {
    private var exercises = emptyList<Exercise>();

    inner class ExercisesViewHolder(val binding: ExercisesListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.container.setOnClickListener {
                listener.onExerciseSelected(binding.exercise!!)
            }

            binding.moreButton.setOnClickListener(View.OnClickListener {
                val popup = PopupMenu(it.context, it)
                popup.inflate(R.menu.exercises_item_menu)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_delete -> {
                            listener.onDeleteExercise(binding.exercise!!)
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercisesViewHolder {
        return ExercisesViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.exercises_list_item, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ExercisesViewHolder, position: Int) {
        val current = exercises[position]
        holder.binding.apply {
            exercise = current
        }
    }

    internal fun setExercises(exercises: List<Exercise>) {
        this.exercises = exercises
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return this.exercises.size
    }
}



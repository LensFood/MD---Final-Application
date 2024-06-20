package com.example.lensfood1

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.db.HistoryDB
import com.example.myapplication.db.RepositoryClass

class AdapterHistory(
    private val context: Context,
    private val repository: RepositoryClass,
    private val dataList: MutableList<HistoryDB> = mutableListOf()
) : RecyclerView.Adapter<AdapterHistory.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.bind(currentItem)

        // Set click listener for the exercise image
        holder.rvImageExercise.setOnClickListener {
            val data = dataList[position]
            navigateToExerciseActivity(data)
        }
    }

    override fun getItemCount(): Int = dataList.size



    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val rvDate: TextView = itemView.findViewById(R.id.date)
        private val rvName: TextView = itemView.findViewById(R.id.title)
        private val rvKarbo: TextView = itemView.findViewById(R.id.carbo)
        private val rvLemak: TextView = itemView.findViewById(R.id.fat)
        private val rvProtein: TextView = itemView.findViewById(R.id.protein)
        private val rvKalori: TextView = itemView.findViewById(R.id.cals)
        private val rvExerciseName: TextView = itemView.findViewById(R.id.exercise_name)
        private val rvExerciseDuration: TextView = itemView.findViewById(R.id.duration)
        val rvImageFood: ImageView = itemView.findViewById(R.id.food_image)
        val rvImageExercise: ImageView = itemView.findViewById(R.id.image)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(data: HistoryDB) {
            rvDate.text = data.date
            rvName.text = data.name
            rvKarbo.text = data.karbo.toString()
            rvLemak.text = data.lemak.toString()
            rvProtein.text = data.protein.toString()
            rvKalori.text = data.kalori.toString()
            rvExerciseName.text = data.exerciseName
            rvExerciseDuration.text = "${data.exerciseDuration} Menit"

            // Load images using Glide
            Glide.with(itemView.context).load(data.imageFood).into(rvImageFood)

            // Display GIF based on the exercise name ID
            val exerciseGif: Int
            val exerciseName: String

            when (data.imageExercise) {
                "2131231021" -> {
                    exerciseGif = R.drawable.skipping
                    exerciseName = "Skipping"
                }
                "2131231020" -> {
                    exerciseGif = R.drawable.situp
                    exerciseName = "Sit Up"
                }
                "2131230874" -> {
                    exerciseGif = R.drawable.cardio
                    exerciseName = "Cardio"
                }
                "2131231045" -> {
                    exerciseGif = R.drawable.workout
                    exerciseName = "Workout"
                }
                else -> {
                    exerciseGif = R.drawable.skipping // Default image if no match found
                    exerciseName = "Skipping"
                }
            }

            Log.d("AdapterHistory", "GIF yang dipilih: $exerciseGif, Nama Latihan: $exerciseName")

            // Set the exercise name in the view holder
            rvExerciseName.text = exerciseName

            // Load the GIF into the ImageView
            Glide.with(itemView.context)
                .asGif()  // Load as GIF
                .load(exerciseGif)
                .into(rvImageExercise)
        }

        override fun onClick(v: View?) {
            showContextMenu(adapterPosition)
        }

        private fun showContextMenu(position: Int) {
            val contextMenuItems = arrayOf("Edit", "Delete")

            val builder = android.app.AlertDialog.Builder(itemView.context)
            builder.setItems(contextMenuItems) { _, which ->
                when (which) {
                    0 -> showEditDialog(position)
                    1 -> {
                        val currentData = dataList[position]
                        repository.deleteHistory(currentData.id ?: 0L)
                        removeItem(position)
                    }
                }
            }
            builder.create().show()
        }

        private fun showEditDialog(position: Int) {
            val currentData = dataList[position]

            val builder = android.app.AlertDialog.Builder(itemView.context)
            builder.setTitle("Edit Name")

            val input = android.widget.EditText(itemView.context)
            input.setText(currentData.name)
            builder.setView(input)

            builder.setPositiveButton("Save") { _, _ ->
                val newName = input.text.toString()

                // Update the database via repository
                val updatedRows = repository.updateHistory(
                    currentData.id ?: 0L,
                    currentData.date,
                    currentData.imageFood,
                    newName,
                    currentData.karbo,
                    currentData.lemak,
                    currentData.protein,
                    currentData.kalori,
                    currentData.average,
                    currentData.imageExercise,
                    currentData.exerciseName,
                    currentData.exerciseDuration
                )

                if (updatedRows > 0) {
                    // Update the data in the list after successful database update
                    dataList[position] = currentData.copy(name = newName)
                    notifyItemChanged(position)
                }
            }

            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

            builder.show()
        }

        private fun removeItem(position: Int) {
            dataList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, dataList.size)
        }
    }

    private fun navigateToExerciseActivity(data: HistoryDB) {
        val intent = android.content.Intent(context, exercise::class.java).apply {
            putExtra(exercise.EXTRA_GIF_ID, data.imageExercise)
            putExtra(exercise.EXTRA_DURATION, data.exerciseDuration)
            putExtra(exercise.EXTRA_KALORI, data.kalori)
        }
        context.startActivity(intent)
    }
}

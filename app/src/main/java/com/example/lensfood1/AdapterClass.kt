package com.example.lensfood1

import android.content.Context
import android.content.Intent
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

class AdapterClass(
    private val context: Context,
    private val repository: RepositoryClass,
    private val dataList: MutableList<HistoryDB> = mutableListOf()
) : RecyclerView.Adapter<AdapterClass.ViewHolderClass>() {

    init {
        // Fetch initial data from the repository
        refreshData()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.bind(currentItem)

        // Set click listener for the entire item
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currentItem)
            navigateToExerciseActivity(currentItem)
        }
    }

    override fun getItemCount(): Int = dataList.size

    // Method to fetch data from repository and update the adapter's data list
    fun refreshData() {
        val latestData = repository.getAllHistory() // Fetch all history items
        dataList.clear()
        dataList.addAll(latestData)
        notifyDataSetChanged()
    }



    inner class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

        fun bind(data: HistoryDB) {
            rvDate.text = data.date
            rvName.text = data.name
            rvKarbo.text = data.karbo.toString()
            rvLemak.text = data.lemak.toString()
            rvProtein.text = data.protein.toString()
            rvKalori.text = "${data.kalori} Cals"
            rvExerciseName.text = data.exerciseName
            rvExerciseDuration.text = "${data.exerciseDuration} Menit"

            // Load images using Glide
            Glide.with(itemView.context).load(data.imageFood).into(rvImageFood)

            // Display GIF based on the exercise name ID
            val exerciseGif: Int
            val exerciseName: String

            Log.d("AdapterClass", "GIF yang dipilih: ${data.imageExercise}")
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

            Log.d("AdapterClass", "GIF yang dipilih: $exerciseGif, Nama Latihan: $exerciseName")

            // Set the exercise name in the view holder
            rvExerciseName.text = exerciseName

            // Load the GIF into the ImageView
            Glide.with(itemView.context)
                .asGif()  // Load as GIF
                .load(exerciseGif)
                .into(rvImageExercise)
        }
    }

    private fun navigateToExerciseActivity(data: HistoryDB) {
        val intent = Intent(context, latihan::class.java).apply {
            putExtra(latihan.EXTRA_GIF, data.imageExercise)
            putExtra(latihan.EXTRA_DURATION, data.exerciseDuration.toLong())
            Log.d("durasi", "nilai GIF yang dikirim dari adapterclass: ${data.imageExercise}")
        }
        context.startActivity(intent)
    }

    var onItemClick: ((HistoryDB) -> Unit)? = null
}

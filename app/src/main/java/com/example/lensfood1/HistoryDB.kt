package com.example.myapplication.db

data class HistoryDB(
    val id: Long?,
    val date: String,
    val imageFood: String,
    val name: String,
    val karbo: Float,
    val lemak: Float,
    val protein: Float,
    val kalori: Float,
    val average: Float,
    val imageExercise: String,
    val exerciseName: String,
    val exerciseDuration: Int
)

package com.example.myapplication.db

import android.app.Application
import android.content.ContentValues
import android.content.Context

class RepositoryClass(context: Context) {

    companion object {
        @Volatile
        private var instance: RepositoryClass? = null

        fun getInstance(application: Application): RepositoryClass {
            return instance ?: synchronized(this) {
                instance ?: RepositoryClass(application).also { instance = it }
            }
        }
    }

    private val dbHelper = DatabaseHelper(context)

    fun getAllHistory(): List<HistoryDB> {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            ContractClass.HistoryEntry._ID,
            ContractClass.HistoryEntry.COLUMN_DATE,
            ContractClass.HistoryEntry.COLUMN_IMAGE_FOOD,
            ContractClass.HistoryEntry.COLUMN_NAME,
            ContractClass.HistoryEntry.COLUMN_KARBO,
            ContractClass.HistoryEntry.COLUMN_LEMAK,
            ContractClass.HistoryEntry.COLUMN_PROTEIN,
            ContractClass.HistoryEntry.COLUMN_KALORI,
            ContractClass.HistoryEntry.COLUMN_AVERAGE,
            ContractClass.HistoryEntry.COLUMN_IMAGE_EXERCISE,
            ContractClass.HistoryEntry.COLUMN_EXERCISE_NAME,
            ContractClass.HistoryEntry.COLUMN_EXERCISE_DURATION
        )

        val cursor = db.query(
            ContractClass.HistoryEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        val historyList = mutableListOf<HistoryDB>()

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(ContractClass.HistoryEntry._ID))
                val date = getString(getColumnIndexOrThrow(ContractClass.HistoryEntry.COLUMN_DATE))
                val imageFood = getString(getColumnIndexOrThrow(ContractClass.HistoryEntry.COLUMN_IMAGE_FOOD))
                val name = getString(getColumnIndexOrThrow(ContractClass.HistoryEntry.COLUMN_NAME))
                val karbo = getFloat(getColumnIndexOrThrow(ContractClass.HistoryEntry.COLUMN_KARBO))
                val lemak = getFloat(getColumnIndexOrThrow(ContractClass.HistoryEntry.COLUMN_LEMAK))
                val protein = getFloat(getColumnIndexOrThrow(ContractClass.HistoryEntry.COLUMN_PROTEIN))
                val kalori = getFloat(getColumnIndexOrThrow(ContractClass.HistoryEntry.COLUMN_KALORI))
                val average = getFloat(getColumnIndexOrThrow(ContractClass.HistoryEntry.COLUMN_AVERAGE))
                val imageExercise = getString(getColumnIndexOrThrow(ContractClass.HistoryEntry.COLUMN_IMAGE_EXERCISE))
                val exerciseName = getString(getColumnIndexOrThrow(ContractClass.HistoryEntry.COLUMN_EXERCISE_NAME))
                val exerciseDuration = getInt(getColumnIndexOrThrow(ContractClass.HistoryEntry.COLUMN_EXERCISE_DURATION))

                historyList.add(HistoryDB(id, date, imageFood, name, karbo, lemak, protein, kalori, average, imageExercise, exerciseName, exerciseDuration))
            }
        }
        cursor.close()
        return historyList
    }

    fun deleteHistory(id: Long): Int {
        val db = dbHelper.writableDatabase
        val selection = "${ContractClass.HistoryEntry._ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.delete(ContractClass.HistoryEntry.TABLE_NAME, selection, selectionArgs)
    }

    fun insertHistory(
        date: String,
        imageFood: String,
        name: String,
        karbo: Float,
        lemak: Float,
        protein: Float,
        kalori: Float,
        average: Float,
        imageExercise: String,
        exerciseName: String,
        exerciseDuration: Int
    ): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(ContractClass.HistoryEntry.COLUMN_DATE, date)
            put(ContractClass.HistoryEntry.COLUMN_IMAGE_FOOD, imageFood)
            put(ContractClass.HistoryEntry.COLUMN_NAME, name)
            put(ContractClass.HistoryEntry.COLUMN_KARBO, karbo)
            put(ContractClass.HistoryEntry.COLUMN_LEMAK, lemak)
            put(ContractClass.HistoryEntry.COLUMN_PROTEIN, protein)
            put(ContractClass.HistoryEntry.COLUMN_KALORI, kalori)
            put(ContractClass.HistoryEntry.COLUMN_AVERAGE, average)
            put(ContractClass.HistoryEntry.COLUMN_IMAGE_EXERCISE, imageExercise)
            put(ContractClass.HistoryEntry.COLUMN_EXERCISE_NAME, exerciseName)
            put(ContractClass.HistoryEntry.COLUMN_EXERCISE_DURATION, exerciseDuration)
        }
        return db.insert(ContractClass.HistoryEntry.TABLE_NAME, null, values)
    }

    fun updateHistory(
        id: Long,
        date: String,
        imageFood: String,
        name: String,
        karbo: Float,
        lemak: Float,
        protein: Float,
        kalori: Float,
        average: Float,
        imageExercise: String,
        exerciseName: String,
        exerciseDuration: Int
    ): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(ContractClass.HistoryEntry.COLUMN_DATE, date)
            put(ContractClass.HistoryEntry.COLUMN_IMAGE_FOOD, imageFood)
            put(ContractClass.HistoryEntry.COLUMN_NAME, name)
            put(ContractClass.HistoryEntry.COLUMN_KARBO, karbo)
            put(ContractClass.HistoryEntry.COLUMN_LEMAK, lemak)
            put(ContractClass.HistoryEntry.COLUMN_PROTEIN, protein)
            put(ContractClass.HistoryEntry.COLUMN_KALORI, kalori)
            put(ContractClass.HistoryEntry.COLUMN_AVERAGE, average)
            put(ContractClass.HistoryEntry.COLUMN_IMAGE_EXERCISE, imageExercise)
            put(ContractClass.HistoryEntry.COLUMN_EXERCISE_NAME, exerciseName)
            put(ContractClass.HistoryEntry.COLUMN_EXERCISE_DURATION, exerciseDuration)
        }
        val selection = "${ContractClass.HistoryEntry._ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.update(ContractClass.HistoryEntry.TABLE_NAME, values, selection, selectionArgs)
    }
}

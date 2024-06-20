package com.example.myapplication.db

import android.provider.BaseColumns

object ContractClass {

    object HistoryEntry : BaseColumns {
        const val TABLE_NAME = "history"
        const val COLUMN_DATE = "date"
        const val COLUMN_IMAGE_FOOD = "image_food"
        const val COLUMN_NAME = "name"
        const val COLUMN_KARBO = "karbo"
        const val COLUMN_LEMAK = "lemak"
        const val COLUMN_PROTEIN = "protein"
        const val COLUMN_KALORI = "kalori"
        const val COLUMN_AVERAGE = "average"
        const val COLUMN_IMAGE_EXERCISE = "image_exercise"
        const val COLUMN_EXERCISE_NAME = "exercise_name"
        const val COLUMN_EXERCISE_DURATION = "exercise_duration"

        const val _ID = BaseColumns._ID

        const val SQL_CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME (" +
                    "$_ID INTEGER PRIMARY KEY," +
                    "$COLUMN_DATE TEXT," +
                    "$COLUMN_IMAGE_FOOD TEXT," +
                    "$COLUMN_NAME TEXT," +
                    "$COLUMN_KARBO REAL," +
                    "$COLUMN_LEMAK REAL," +
                    "$COLUMN_PROTEIN REAL," +
                    "$COLUMN_KALORI REAL," +
                    "$COLUMN_AVERAGE REAL," +
                    "$COLUMN_IMAGE_EXERCISE TEXT," +
                    "$COLUMN_EXERCISE_NAME TEXT," +
                    "$COLUMN_EXERCISE_DURATION INTEGER)"

        const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}

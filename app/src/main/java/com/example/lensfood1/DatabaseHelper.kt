package com.example.myapplication.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val SQL_CREATE_HISTORY_TABLE = """
            CREATE TABLE ${ContractClass.HistoryEntry.TABLE_NAME} (
                ${ContractClass.HistoryEntry._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${ContractClass.HistoryEntry.COLUMN_DATE} TEXT NOT NULL,
                ${ContractClass.HistoryEntry.COLUMN_IMAGE_FOOD} TEXT NOT NULL,
                ${ContractClass.HistoryEntry.COLUMN_NAME} TEXT NOT NULL,
                ${ContractClass.HistoryEntry.COLUMN_KARBO} REAL NOT NULL,
                ${ContractClass.HistoryEntry.COLUMN_LEMAK} REAL NOT NULL,
                ${ContractClass.HistoryEntry.COLUMN_PROTEIN} REAL NOT NULL,
                ${ContractClass.HistoryEntry.COLUMN_KALORI} REAL NOT NULL,
                ${ContractClass.HistoryEntry.COLUMN_AVERAGE} REAL NOT NULL,
                ${ContractClass.HistoryEntry.COLUMN_IMAGE_EXERCISE} TEXT NOT NULL,
                ${ContractClass.HistoryEntry.COLUMN_EXERCISE_NAME} TEXT NOT NULL,
                ${ContractClass.HistoryEntry.COLUMN_EXERCISE_DURATION} INTEGER NOT NULL
            );
        """.trimIndent()
        db.execSQL(SQL_CREATE_HISTORY_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Kode peningkatan versi database di sini jika diperlukan
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "History.db"
    }
}

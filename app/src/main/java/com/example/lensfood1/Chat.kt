package com.example.lensfood1

import android.graphics.Bitmap


data class Chat (
    val prompt: String,
    val bitmap: Bitmap?,
    val isFromUser: Boolean
)
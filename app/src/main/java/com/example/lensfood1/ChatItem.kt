package com.example.lensfood1

import android.graphics.Bitmap
sealed class ChatItem {
    data class UserChatItem(val prompt: String, val bitmap: Bitmap?) : ChatItem()
    data class ModelChatItem(val response: String) : ChatItem()
}
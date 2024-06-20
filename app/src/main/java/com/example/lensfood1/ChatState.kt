package com.example.lensfood1

import com.example.lensfood1.ChatItem

data class ChatState(
    val chatList: List<ChatItem> = emptyList(),
    val prompt: String = ""
)

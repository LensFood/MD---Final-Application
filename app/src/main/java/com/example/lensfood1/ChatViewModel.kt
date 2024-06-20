package com.example.lensfood1
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val _chatState = MutableLiveData<ChatState>()
    val chatState: LiveData<ChatState> = _chatState

    init {
        _chatState.value = ChatState() // Inisialisasi dengan ChatState default
    }

    fun sendPrompt(prompt: String, bitmap: Bitmap?) {
        viewModelScope.launch {
            try {
                val chat = if (bitmap != null) {
                    ChatData.getResponseWithImage(prompt, bitmap)
                } else {
                    ChatData.getResponse(prompt)
                }
                // Perbarui LiveData dengan respons chat yang diterima
                val updatedChatList = _chatState.value?.chatList?.toMutableList() ?: mutableListOf()
                updatedChatList.add(ChatItem.UserChatItem(prompt, bitmap)) // Tambahkan input user
                updatedChatList.add(chat.toChatItem()) // Tambahkan respons model
                _chatState.value = _chatState.value?.copy(chatList = updatedChatList, prompt = prompt)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}

// Ekstensi untuk konversi Chat ke ChatItem
fun Chat.toChatItem(): ChatItem {
    return ChatItem.ModelChatItem(
        response = this.prompt // Menggunakan properti 'prompt' sebagai respons
    )
}

package com.example.lensfood1

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class chatbot : AppCompatActivity() {

    private val uriState = MutableStateFlow<String?>(null)

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                uriState.value = it.toString()
            }
        }

    private lateinit var viewModel: ChatViewModel // ViewModel instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)

        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val chatRecyclerView: RecyclerView = findViewById(R.id.chat_recycler_view)
        val addPhotoButton: ImageButton = findViewById(R.id.add_photo_button)
        val sendButton: ImageButton = findViewById(R.id.send_button)
        val promptInput: EditText = findViewById(R.id.prompt_input)
        val pickedImage: ImageView = findViewById(R.id.picked_image)

        chatRecyclerView.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }

        val adapter = ChatAdapter(emptyList()) // Initialize with empty list
        chatRecyclerView.adapter = adapter

        addPhotoButton.setOnClickListener {
            imagePicker.launch("image/*")
        }

        sendButton.setOnClickListener {
            val prompt = promptInput.text.toString()
            lifecycleScope.launch {
                val bitmap = uriState.value?.let { getBitmap(it) }
                viewModel.sendPrompt(prompt, bitmap)
                uriState.value = null
            }
        }

        lifecycleScope.launch {
            uriState.collect { uri ->
                val bitmap = uri?.let { getBitmap(it) }
                if (bitmap != null) {
                    pickedImage.setImageBitmap(bitmap)
                    pickedImage.visibility = ImageView.VISIBLE
                } else {
                    pickedImage.visibility = ImageView.GONE
                }
            }
        }

        viewModel.chatState.observe(this) { chatState ->
            chatState ?: return@observe // Handle null case
            adapter.updateChatList(chatState.chatList)
        }
    }

    private suspend fun getBitmap(uri: String): Bitmap? {
        return try {
            val loader = ImageLoader(this)
            val request = ImageRequest.Builder(this)
                .data(Uri.parse(uri))
                .build()
            val result = (loader.execute(request) as SuccessResult).drawable
            result.toBitmap()
        } catch (e: Exception) {
            null
        }
    }
}

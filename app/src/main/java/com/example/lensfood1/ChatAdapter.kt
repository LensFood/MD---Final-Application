package com.example.lensfood1
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private var chatList: List<ChatItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return when (chatList[position]) {
            is ChatItem.UserChatItem -> 0
            is ChatItem.ModelChatItem -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_user, parent, false)
                UserChatViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_model, parent, false)
                ModelChatViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = chatList[position]) {
            is ChatItem.UserChatItem -> (holder as UserChatViewHolder).bind(item)
            is ChatItem.ModelChatItem -> (holder as ModelChatViewHolder).bind(item)
        }
    }

    override fun getItemCount() = chatList.size

    fun updateChatList(newChatList: List<ChatItem>) {
        chatList = newChatList
        notifyDataSetChanged()
    }

    class UserChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userPrompt: TextView = itemView.findViewById(R.id.user_prompt)
        private val userImage: ImageView = itemView.findViewById(R.id.user_image)

        fun bind(item: ChatItem.UserChatItem) {
            userPrompt.text = item.prompt
            if (item.bitmap != null) {
                userImage.setImageBitmap(item.bitmap)
                userImage.visibility = View.VISIBLE
            } else {
                userImage.visibility = View.GONE
            }
        }
    }

    class ModelChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val modelResponse: TextView = itemView.findViewById(R.id.model_response)

        fun bind(item: ChatItem.ModelChatItem) {
            modelResponse.text = item.response
        }
    }
}

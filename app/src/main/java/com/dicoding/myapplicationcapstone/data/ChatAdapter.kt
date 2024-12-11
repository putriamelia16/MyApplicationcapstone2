package com.dicoding.myapplicationcapstone.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.capstoneproject.databinding.ItemMessageBinding
import com.dicoding.myapplicationcapstone.data.model.Chat


class ChatAdapter(private val chatList: MutableList<Chat>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(private val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            if (chat.isFromUser) {
                // Jika pesan dari pengguna
                binding.tvUserMessage.text = chat.message
                binding.tvUserMessage.visibility = View.VISIBLE
                binding.tvBotMessage.visibility = View.GONE
            } else {
                // Jika pesan dari bot
                binding.tvBotMessage.text = chat.message
                binding.tvBotMessage.visibility = View.VISIBLE
                binding.tvUserMessage.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chatList[position])
    }

    override fun getItemCount(): Int = chatList.size

    fun addChat(chat: Chat) {
        chatList.add(chat)
        notifyItemInserted(chatList.size - 1)
    }
}

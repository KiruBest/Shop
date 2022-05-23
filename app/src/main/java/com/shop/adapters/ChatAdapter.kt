package com.shop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shop.databinding.LayoutMessageItemBinding
import com.shop.models.MessageModel
import com.shop.ui.admin.ADMIN_ID

class ChatAdapter(private val messageList: MutableList<MessageModel>) :
    RecyclerView.Adapter<ChatAdapter.ChatHolder>() {

    private val _messageList: MutableList<MessageModel> = mutableListOf()

    init {
        _messageList.addAll(messageList)
    }

    inner class ChatHolder(private val binding: LayoutMessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val textMessage = binding.textViewMessageText
        val textViewUser = binding.textViewUser
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val binding =
            LayoutMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        holder.textMessage.text = _messageList[position].message
        if (_messageList[position].adminSendCheck && Firebase.auth.currentUser?.uid == ADMIN_ID) {
            holder.textViewUser.text = "Вы"
        } else if (_messageList[position].adminSendCheck && Firebase.auth.currentUser?.uid != ADMIN_ID) {
            holder.textViewUser.text = "Администратор"
        } else if (!_messageList[position].adminSendCheck && Firebase.auth.currentUser?.uid == ADMIN_ID) {
            holder.textViewUser.text = "Клиент"
        } else if (!_messageList[position].adminSendCheck && Firebase.auth.currentUser?.uid != ADMIN_ID) {
            holder.textViewUser.text = "Вы"
        }
    }

    override fun getItemCount(): Int = _messageList.size
}
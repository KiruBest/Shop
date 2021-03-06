package com.shop.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shop.databinding.LayoutMessageItemBinding
import com.shop.models.MessageModel
import com.shop.ui.admin.ADMIN_ID

class ChatAdapter() :
    RecyclerView.Adapter<ChatAdapter.ChatHolder>() {

    private val _messageList: MutableList<MessageModel> = mutableListOf()

    inner class ChatHolder(private val binding: LayoutMessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val textMessage = binding.textViewMessageText
        //val textViewUser = binding.textViewUser
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val binding =
            LayoutMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        holder.textMessage.text = _messageList[position].message

        val layout = holder.itemView as LinearLayout
        if (_messageList[position].adminSendCheck && Firebase.auth.currentUser?.uid == ADMIN_ID) {
            layout.gravity = GravityCompat.END
            holder.textMessage.isActivated = true
            //holder.textViewUser.text = "Вы"
        } else if (_messageList[position].adminSendCheck && Firebase.auth.currentUser?.uid != ADMIN_ID) {
            layout.gravity = GravityCompat.START
            //holder.textViewUser.text = "Администратор"
        } else if (!_messageList[position].adminSendCheck && Firebase.auth.currentUser?.uid == ADMIN_ID) {
            layout.gravity = GravityCompat.START
            //holder.textViewUser.text = "Клиент"
        } else if (!_messageList[position].adminSendCheck && Firebase.auth.currentUser?.uid != ADMIN_ID) {
            layout.gravity = GravityCompat.END
            holder.textMessage.isActivated = true
            //holder.textViewUser.text = "Вы"
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addMessage(message: MessageModel) {
        if(!_messageList.contains(message)) {
            _messageList.add(message)
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAllMessage(messages: MutableList<MessageModel>) {
        _messageList.addAll(messages)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = _messageList.size
}
package com.shop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shop.databinding.LayoutUserItemBinding
import com.shop.models.User

class AdminChatPickerAdapter(
    private val onItemClick: (user: User) -> Unit
) : RecyclerView.Adapter<AdminChatPickerAdapter.AdminChatPickerViewHolder>() {

    private val _users = mutableListOf<User>()

    constructor(users: MutableList<User>, onItemClick: (user: User) -> Unit) : this(onItemClick) {
        _users.addAll(users)
    }

    inner class AdminChatPickerViewHolder(val binding: LayoutUserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imageUser = binding.imageViewAvatar
        val textViewName = binding.texxtViewName
        val textViewEmail = binding.texxtViewEmail
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminChatPickerViewHolder {
        val binding =
            LayoutUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminChatPickerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdminChatPickerViewHolder, position: Int) {
        holder.textViewName.text = _users[position].name

        if (_users[position].login == "") {
            holder.textViewEmail.text = _users[position].email
        } else {
            holder.textViewEmail.text = _users[position].login
        }

        Glide.with(holder.itemView).load(_users[position].photo).into(holder.imageUser)

        holder.itemView.setOnClickListener {
            onItemClick(_users[position])
        }
    }

    override fun getItemCount(): Int = _users.size

    fun addUser(user: User) {
        _users.add(user)
        notifyItemInserted(itemCount - 1)
    }
}
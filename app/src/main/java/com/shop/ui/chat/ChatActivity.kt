package com.shop.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shop.adapters.ChatAdapter
import com.shop.databinding.ActivityChatBinding
import com.shop.firebase.MessageDatabaseObject
import com.shop.models.MessageModel
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var messages: MutableList<MessageModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MessageDatabaseObject.getAllMessage {
            messages = it
            chatAdapter = ChatAdapter(messages)

            binding.recyclerViewMessage.apply {
                adapter = chatAdapter
                layoutManager = LinearLayoutManager(this@ChatActivity)
            }

            scrollDown()
        }

        binding.buttonSendMessage.setOnClickListener {
            val message = binding.editTextInputMessage.text.toString()
            val editTextInputMessage = binding.editTextInputMessage
            val uid = Firebase.auth.currentUser?.uid!!
            val messageModel = MessageModel(message, uid, Calendar.getInstance().time.time)
            if (message.isNotEmpty()) {
                MessageDatabaseObject.sendMessage(messageModel)
                editTextInputMessage.setText("")
            }
            scrollDown()
        }

        binding.buttonClose.setOnClickListener {
            onBackPressed()
        }
    }

    private fun scrollDown() {
        if (chatAdapter.itemCount != 0) recyclerViewMessage.smoothScrollToPosition(chatAdapter.itemCount - 1)
    }
}
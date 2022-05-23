package com.shop.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.shop.adapters.ChatAdapter
import com.shop.databinding.FragmentAdminChatWithUserBinding
import com.shop.firebase.MessageDatabaseObject
import com.shop.models.MessageModel
import com.shop.models.User
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*

class AdminChatWithUserFragment : Fragment() {

    private lateinit var binding: FragmentAdminChatWithUserBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var messages: MutableList<MessageModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminChatWithUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = requireArguments()
        val user: User = bundle.getParcelable("user")!!

        Glide.with(requireActivity()).load(user.photo).into(binding.imageViewAvatar)
        binding.textViewProfile.text = user.email

        MessageDatabaseObject.getAllMessage {
            messages = it
            chatAdapter = ChatAdapter(messages)

            binding.recyclerViewMessage.apply {
                adapter = chatAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }

            scrollDown()
        }

        binding.buttonSendMessage.setOnClickListener {
            val message = binding.editTextInputMessage.text.toString()
            val editTextInputMessage = binding.editTextInputMessage
            val uid = user.uid
            val messageModel =
                MessageModel(message, uid, Calendar.getInstance().time.time, adminSendCheck = true)
            if (message.isNotEmpty()) {
                MessageDatabaseObject.sendMessage(messageModel)
                editTextInputMessage.setText("")
            }
            scrollDown()
        }

        binding.buttonClose.setOnClickListener {
            (requireActivity() as AdminChatActivity).closeClickListener()
        }
    }

    private fun scrollDown() {
        if (chatAdapter.itemCount != 0) recyclerViewMessage.smoothScrollToPosition(chatAdapter.itemCount - 1)
    }
}
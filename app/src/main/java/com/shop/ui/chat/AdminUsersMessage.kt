package com.shop.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.shop.R
import com.shop.adapters.AdminChatPickerAdapter
import com.shop.databinding.FragmentAdminUsersMessageBinding
import com.shop.firebase.MessageDatabaseObject
import com.shop.firebase.UserDatabase
import com.shop.models.User

class AdminUsersMessage : Fragment() {

    private lateinit var binding: FragmentAdminUsersMessageBinding
    private val users = mutableListOf<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminUsersMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adminChatPickerAdapter = AdminChatPickerAdapter { user ->
            val bundle = Bundle()
            bundle.putParcelable("user", user)

            val fragment = AdminChatWithUserFragment()
            fragment.arguments = bundle

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentChatContainer, fragment)
                .commit()
        }

        MessageDatabaseObject.getAllMessage { messages ->
            messages.forEach { message ->
                UserDatabase.instance().readCurrentUser(message.userID) { user ->
                    if(!users.contains(user)) {
                        users.add(user)
                        adminChatPickerAdapter.addUser(user)
                    }
                }
            }

            binding.recyclerViewUsers.apply {
                adapter = adminChatPickerAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }
}
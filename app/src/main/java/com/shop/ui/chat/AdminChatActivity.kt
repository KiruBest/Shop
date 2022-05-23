package com.shop.ui.chat

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.shop.R
import com.shop.adapters.AdminChatPickerAdapter
import com.shop.databinding.ActivityAdminChatBinding
import com.shop.firebase.MessageDatabaseObject
import com.shop.firebase.UserDatabase
import com.shop.models.User

class AdminChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminChatBinding
    private val users = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adminChatPickerAdapter = AdminChatPickerAdapter { user ->
            val bundle = Bundle()
            bundle.putParcelable("user", user)

            val fragment = AdminChatWithUserFragment()
            fragment.arguments = bundle

            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentChatContainer, fragment)
                .commit()

            binding.fragmentChatContainer.visibility = View.VISIBLE
            binding.recyclerViewUsers.visibility = View.GONE
        }

        binding.recyclerViewUsers.apply {
            adapter = adminChatPickerAdapter
            layoutManager = LinearLayoutManager(this@AdminChatActivity)
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
        }
    }

    fun closeClickListener() {
        binding.fragmentChatContainer.visibility = View.GONE
        binding.recyclerViewUsers.visibility = View.VISIBLE
    }
}
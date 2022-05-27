package com.shop.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shop.databinding.ActivityAdminChatBinding

class AdminChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
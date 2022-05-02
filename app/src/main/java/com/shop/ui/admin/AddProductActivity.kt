package com.shop.ui.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shop.databinding.ActivityAddProductBinding
import com.shop.firebase.ProductDatabase

class AddProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProductBinding

    private var productDatabase = ProductDatabase.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
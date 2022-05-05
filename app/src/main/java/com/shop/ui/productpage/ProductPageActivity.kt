package com.shop.ui.productpage

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.shop.R
import com.shop.databinding.ActivityProductPageBinding
import com.shop.models.Product

class ProductPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val product: Product = intent.getParcelableExtra("product")!!
        Log.d("productPage", product.toString())

        binding.mainToolbar.title = product.title
        binding.mainToolbar.setTitleTextAppearance(this, R.style.toolbar_text)
        setSupportActionBar(binding.mainToolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        Glide.with(this).load(product.photo).into(binding.productPhoto)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
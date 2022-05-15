package com.shop.ui.productpage

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shop.R
import com.shop.databinding.ActivityProductPageBinding
import com.shop.firebase.ProductDatabase
import com.shop.models.BasketProduct
import com.shop.models.FavoriteProduct
import com.shop.models.Product

class ProductPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductPageBinding

    @SuppressLint("SetTextI18n")
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
        binding.textViewDescriptionProduct.text = product.description
        binding.textViewPriceProduct.text = "${product.price}₽"
        binding.textViewTitleProduct.text = product.title

        product.sizes.sort()
        product.sizes.add(0, getString(R.string.pick_size))

        binding.spinnerSize.adapter =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, product.sizes)

        binding.spinnerSize.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (binding.spinnerSize.selectedItemId.toInt() != 0) binding.textViewSelected.text =
                    binding.spinnerSize.selectedItem.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }

        FavoriteProduct.products.forEach {
            if (product.id == it.id) {
                binding.imageViewFavorite.isActivated = true
                return@forEach
            }
        }

        BasketProduct.products.forEach {
            if (it.id == product.id) {
                binding.buttonAddBasket.isEnabled = false
            }
        }

        binding.imageViewFavorite.setOnClickListener {
            it.isActivated = !it.isActivated
            if (it.isActivated) {
                Firebase.auth.currentUser?.uid?.let { uid ->
                    ProductDatabase.instance().addToFavorite(product.id, uid)
                    Toast.makeText(
                        this,
                        "Объект добавлен в избранное",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Firebase.auth.currentUser?.uid?.let { uid ->
                    ProductDatabase.instance()
                        .dropProductFromFavorite(uid = uid, productID = product.id) {
                            Toast.makeText(
                                this,
                                "Объект удален из корзины",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
        }

        binding.buttonAddBasket.setOnClickListener {
            Firebase.auth.currentUser?.uid?.let { uid ->
                ProductDatabase.instance().addToBasket(product.id, uid, 1)
                Toast.makeText(
                    this,
                    "Объект добавлен в корзину",
                    Toast.LENGTH_SHORT
                ).show()

                it.isEnabled = false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
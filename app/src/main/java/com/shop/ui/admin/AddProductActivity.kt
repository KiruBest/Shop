package com.shop.ui.admin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.shop.R
import com.shop.databinding.ActivityAddProductBinding
import com.shop.firebase.ProductDatabase
import com.shop.models.Product

class AddProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProductBinding

    private var productDatabase = ProductDatabase.instance()

    private val product = Product()

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let {
            product.photo = it.toString()
            Glide.with(this).load(product.photo).into(binding.imageViewAddProduct)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAddImage.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.buttonAddProduct.setOnClickListener {
            when {
                product.photo == "" -> Toast.makeText(
                    this,
                    getString(R.string.pick_photo),
                    Toast.LENGTH_SHORT
                ).show()

                binding.spinnerCategory.selectedItemPosition == 0 -> Toast.makeText(
                    this,
                    getString(R.string.pick_category),
                    Toast.LENGTH_SHORT
                ).show()

                binding.textViewTitleProduct.text.isEmpty() -> Toast.makeText(
                    this,
                    getString(R.string.title_require),
                    Toast.LENGTH_SHORT
                ).show()

                binding.textViewDescriptionProduct.text.isEmpty() -> Toast.makeText(
                    this,
                    getString(R.string.description_require),
                    Toast.LENGTH_SHORT
                ).show()

                binding.textViewProductAddingPriceProduct.text.isEmpty() -> Toast.makeText(
                    this,
                    getString(R.string.price_require),
                    Toast.LENGTH_SHORT
                ).show()

                binding.textViewProductAddingSizeProduct.text.isEmpty() -> Toast.makeText(
                    this,
                    getString(R.string.size_require),
                    Toast.LENGTH_SHORT
                ).show()

                else -> {
                    product.category = binding.spinnerCategory.selectedItem.toString()
                    product.title = binding.textViewTitleProduct.text.toString().trim()
                    product.description = binding.textViewDescriptionProduct.text.toString().trim()
                    product.price =
                        binding.textViewProductAddingPriceProduct.text.toString().trim().toFloat()

                    val regex = "\\D+".toRegex()
                    product.sizes.addAll(
                        binding.textViewProductAddingSizeProduct.text
                            .toString().trim().split(regex)
                    )

                    productDatabase.writeProduct(
                        product = product,
                        imageUri = product.photo.toUri()
                    ) {
                        if (it) {
                            Toast.makeText(
                                this,
                                getString(R.string.add_product_successfull),
                                Toast.LENGTH_SHORT
                            ).show()
                            onSupportNavigateUp()
                        }
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
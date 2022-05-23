package com.shop.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shop.databinding.ProductCartBinding
import com.shop.models.BasketProduct
import com.shop.models.FavoriteProduct
import com.shop.models.Product

//Таким образом реализуются все Адаптеры
class ProductAdapter(
    var products: MutableList<Product>,
    private val onItemClick: (product: Product) -> Unit,
    private val onBasketClick: (productID: String, uid: String) -> Unit,
    private val onFavoriteClick: (productID: String, uid: String) -> Unit
) :
    RecyclerView.Adapter<ProductAdapter.ProductHolder>() {

    //Объявляются необходимые view
    inner class ProductHolder(binding: ProductCartBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.title
        val price = binding.price
        val photo = binding.photo

        val basketButton = binding.basketImage
        val favoriteButton = binding.star
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val binding = ProductCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductHolder(binding)
    }

    //Связываются view и данные
    @SuppressLint("SetTextI18n", "CheckResult", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.title.text = products[position].title
        holder.price.text = products[position].price.toString() + "₽"
        Glide.with(holder.itemView)
            .load(products[position].photo)
            .into(holder.photo)

        holder.basketButton.isActivated = false
        holder.favoriteButton.isActivated = false

        holder.photo.setOnClickListener {
            onItemClick(products[position])
        }

        BasketProduct.products.forEach { basketProduct ->
            if (basketProduct.id == products[position].id) holder.basketButton.isActivated = true
        }

        FavoriteProduct.products.forEach { favoriteProduct ->
            if (favoriteProduct.id == products[position].id) holder.favoriteButton.isActivated = true
        }

        holder.basketButton.setOnClickListener {
            Firebase.auth.currentUser?.uid?.let { uid ->
                onBasketClick(products[position].id, uid)
            }
        }

        holder.favoriteButton.setOnClickListener {
            Firebase.auth.currentUser?.uid?.let { uid ->
                onFavoriteClick(products[position].id, uid)
            }
        }
    }

    override fun getItemCount(): Int = Product.products.size
}
package com.shop.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shop.databinding.LayoutFavoriteItemBinding
import com.shop.models.BasketProduct
import com.shop.models.Product

class FavoriteRecyclerViewAdapter(
    private val products: MutableList<Product>,
    private val onItemClick: (product: Product) -> Unit,
    private val onCloseClick: (productID: String) -> Unit,
    private val onBasketClick: (productID: String, uid: String) -> Unit
) : RecyclerView.Adapter<FavoriteRecyclerViewAdapter.BasketProductHolder>() {

    inner class BasketProductHolder(binding: LayoutFavoriteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.titleFavorite
        val price = binding.priceFavorite
        val photo = binding.photoFavorite
        val close = binding.favoriteClose
        val basket = binding.favoriteBasket
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketProductHolder {
        val binding = LayoutFavoriteItemBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        return BasketProductHolder(binding)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: BasketProductHolder, position: Int) {
        holder.title.text = products[position].title
        holder.price.text = products[position].price.toString() + "₽"
        Glide.with(holder.itemView)
            .load(products[position].photo)
            .into(holder.photo)

        BasketProduct.products.forEach { basketProduct ->
            if (basketProduct.id == products[position].id) holder.basket.isActivated = true
        }

        holder.photo.setOnClickListener {
            onItemClick(products[position])
        }

        holder.close.setOnClickListener {
            onCloseClick(products[position].id)
        }

        holder.basket.setOnClickListener {
            it.isActivated = true
            Firebase.auth.currentUser?.uid?.let { uid ->
                onBasketClick(products[position].id, uid)
            }
        }
    }

    override fun getItemCount(): Int = products.size
}
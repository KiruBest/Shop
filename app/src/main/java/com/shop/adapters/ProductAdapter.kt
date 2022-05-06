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
    private val products: MutableList<Product>,
    private val onItemClickListener: OnItemClickListener
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

        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(products[position])
        }

        if (BasketProduct.products.contains(products[position]))
            holder.basketButton.isActivated = true

        if (FavoriteProduct.products.contains(products[position]))
            holder.favoriteButton.isActivated = true

        holder.basketButton.setOnClickListener {
            if (it.isActivated) return@setOnClickListener
            else {
                it.isActivated = true
                onItemClickListener.onBasketClick(products[position].id,
                    Firebase.auth.currentUser?.uid.toString())
            }
        }

        holder.favoriteButton.setOnClickListener {
            if (it.isActivated) return@setOnClickListener
            else {
                it.isActivated = true
                onItemClickListener.onFavoriteClick(products[position].id,
                    Firebase.auth.currentUser?.uid.toString())
            }
        }
    }

    override fun getItemCount(): Int = Product.products.size

    interface OnItemClickListener {
        fun onItemClick(product: Product)
        fun onBasketClick(productID: String, uid: String)
        fun onFavoriteClick(productID: String, uid: String)
    }
}
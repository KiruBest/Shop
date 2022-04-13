package com.shop.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shop.databinding.ProductCartBinding
import com.shop.models.Product

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductHolder>() {
    class ProductHolder(binding: ProductCartBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.title
        val price = binding.price
        val photo = binding.photo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val binding = ProductCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductHolder(binding)
    }

    @SuppressLint("SetTextI18n", "CheckResult", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.title.text = Product.products[position].title
        holder.price.text = Product.products[position].price.toString() + "₽"
        Glide.with(holder.itemView)
            .load("https://sun9-5.userapi.com/impg/c857628/v857628060/169753/2l2vzOX9xZY.jpg?size=1080x720&quality=96&sign=2f61c3552e32740b8c00ad3ab9b2824c&type=album")
            .into(holder.photo)
    }

    override fun getItemCount(): Int = Product.products.size
}
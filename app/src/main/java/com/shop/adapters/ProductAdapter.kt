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
        holder.title.text = Product.products.elementAt(position).title
        holder.price.text = Product.products.elementAt(position).price.toString() + "₽"
        Glide.with(holder.itemView)
            .load(Product.products.elementAt(position).photo)
            .into(holder.photo)
    }

    override fun getItemCount(): Int = Product.products.size
}
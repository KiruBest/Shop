package com.shop.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shop.databinding.LayoutBasketProductItemBinding
import com.shop.models.Product

class BasketRecyclerViewAdapter(
    val products: MutableList<Product>,
    private val onItemClick: (product: Product) -> Unit,
    private val onDeleteClick: (productID: String) -> Unit,
) : RecyclerView.Adapter<BasketRecyclerViewAdapter.BasketProductHolder>() {

    inner class BasketProductHolder(binding: LayoutBasketProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.title
        val price = binding.price
        val photo = binding.photo
        val delete = binding.delete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketProductHolder {
        val binding = LayoutBasketProductItemBinding.inflate(LayoutInflater.from(parent.context),
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

        holder.photo.setOnClickListener {
            onItemClick(products[position])
        }

        holder.delete.setOnClickListener {
            onDeleteClick(products[position].id)
        }
    }

    override fun getItemCount(): Int = products.size
}
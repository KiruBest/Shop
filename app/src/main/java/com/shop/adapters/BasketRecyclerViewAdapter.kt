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
    private val countChangeCallback: () -> Unit
) : RecyclerView.Adapter<BasketRecyclerViewAdapter.BasketProductHolder>() {

    inner class BasketProductHolder(binding: LayoutBasketProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.title
        val price = binding.price
        val photo = binding.photo
        val delete = binding.delete
        val addCount = binding.plus
        val dropCount = binding.minus
        val count = binding.count
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
        holder.price.text = "${products[position].price}₽"
        holder.count.text = products[position].count.toString()
        Glide.with(holder.itemView)
            .load(products[position].photo)
            .into(holder.photo)

        holder.photo.setOnClickListener {
            onItemClick(products[position])
        }

        holder.delete.setOnClickListener {
            onDeleteClick(products[position].id)
        }

        holder.addCount.setOnClickListener {
            addCount(position)
            countChangeCallback.invoke()
        }

        holder.dropCount.setOnClickListener {
            dropCount(position)
            countChangeCallback.invoke()
        }
    }

    override fun getItemCount(): Int = products.size

    private fun addCount(position: Int) {
        if (products[position].count < 20) {
            products[position].count++
            notifyItemChanged(position)
        }
    }

    private fun dropCount(position: Int) {
        if (products[position].count != 1) {
            products[position].count--
            notifyItemChanged(position)
        }
    }
}
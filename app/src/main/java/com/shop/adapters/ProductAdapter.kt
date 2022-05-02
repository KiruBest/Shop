package com.shop.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shop.databinding.ProductCartBinding
import com.shop.models.Product

//Таким образом реализуются все Адаптеры
class ProductAdapter(private val onItemClickListener: OnItemClickListener) :
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
        holder.title.text = Product.products[position].title
        holder.price.text = Product.products[position].price.toString() + "₽"
        Glide.with(holder.itemView)
            .load(Product.products[position].photo)
            .into(holder.photo)

        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(Product.products[position])
        }

        holder.basketButton.setOnClickListener {
            //TODO(Клик на добавление в корзину)
        }

        holder.favoriteButton.setOnClickListener {
            it.isActivated = !it.isActivated
            //TODO(Клик на добавление в избранное)
        }
    }

    override fun getItemCount(): Int = Product.products.size

    interface OnItemClickListener {
        fun onItemClick(product: Product)
    }
}
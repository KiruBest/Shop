package com.shop.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shop.R
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

    private var mProducts = mutableListOf<Product>()

    init {
        mProducts = products
    }

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
    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.title.text = mProducts[position].title
        holder.price.text = holder.itemView.context.getString(R.string.price, mProducts[position].price.toString())
        Glide.with(holder.itemView)
            .load(mProducts[position].photo)
            .into(holder.photo)

        holder.basketButton.isActivated = false
        holder.favoriteButton.isActivated = false

        holder.photo.setOnClickListener {
            onItemClick(mProducts[position])
        }

        BasketProduct.products.forEach { basketProduct ->
            if (basketProduct.id == mProducts[position].id) holder.basketButton.isActivated = true
        }

        FavoriteProduct.products.forEach { favoriteProduct ->
            if (favoriteProduct.id == mProducts[position].id) holder.favoriteButton.isActivated = true
        }

        holder.basketButton.setOnClickListener {
            Firebase.auth.currentUser?.uid?.let { uid ->
                onBasketClick(mProducts[position].id, uid)
            }
        }

        holder.favoriteButton.setOnClickListener {
            Firebase.auth.currentUser?.uid?.let { uid ->
                onFavoriteClick(mProducts[position].id, uid)
            }
        }
    }

    override fun getItemCount(): Int = mProducts.size

    @SuppressLint("NotifyDataSetChanged")
    fun filter(filterProducts: MutableList<Product>) {
        mProducts = filterProducts
        notifyDataSetChanged()
    }
}
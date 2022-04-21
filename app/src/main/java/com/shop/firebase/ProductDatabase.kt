package com.shop.firebase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import android.widget.ProgressBar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shop.adapters.ProductAdapter
import com.shop.models.Product

class ProductDatabase private constructor(): IProductDatabase {
    //Добавление всех товаров из БД
    override fun readProduct(
        progressBar: ProgressBar,
        adapter: ProductAdapter,
    ): ArrayList<Product> {
        progressBar.visibility = ProgressBar.VISIBLE
        val products = mutableListOf<Product>()

        Firebase.database.getReference("products")
            .addValueEventListener(object : ValueEventListener {

                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    val keys = mutableListOf<String>()
                    snapshot.children.forEach {
                        it.key?.let { key1 -> keys.add(key1) }
                        val product = it.getValue(Product::class.java)
                        product?.let { product1 -> products.add(product1) }
                    }
                    adapter.notifyDataSetChanged()
                    progressBar.visibility = ProgressBar.INVISIBLE
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                }

            })

        return products as ArrayList<Product>
    }

    override fun writeProduct(product: Product) {
        TODO("Not yet implemented")
    }

    companion object {
        private var productDatabase: IProductDatabase? = null
        fun instance() : IProductDatabase {
            if (productDatabase == null) productDatabase = ProductDatabase()
            return productDatabase as IProductDatabase
        }
    }
}

interface IProductDatabase {
    fun readProduct(progressBar: ProgressBar, adapter: ProductAdapter): ArrayList<Product>
    fun writeProduct(product: Product)
}
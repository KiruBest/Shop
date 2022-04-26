package com.shop.data.firebase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shop.domain.firebase.GetArrayProductCallback
import com.shop.domain.firebase.IProductDatabase
import com.shop.domain.models.Product
import com.shop.domain.models.ProductArray

class ProductDatabase : IProductDatabase {
    //Добавление всех товаров из БД
    override fun readProduct(callback: GetArrayProductCallback): ProductArray {
        val productArray = ProductArray(products = mutableListOf<Product>())
        Firebase.database.getReference("products")
            .addListenerForSingleValueEvent(object : ValueEventListener {

                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    productArray.products.clear()
                    snapshot.children.forEach {
                        val product = it.getValue(Product::class.java)
                        productArray.products.add(product!!)
                    }
                    callback.onCallback(productArray)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                    callback.onCallback(productArray)
                }

            })

        return productArray
    }

    override fun writeProduct(product: Product) {
        TODO("Not yet implemented")
    }
}
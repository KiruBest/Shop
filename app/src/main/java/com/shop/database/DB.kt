package com.shop.database

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shop.models.Product
import com.shop.ui.ShopFragment

class DB private constructor() {
    fun getProduct() {
        Firebase.database.getReference("products")
            .addValueEventListener(object : ValueEventListener {

                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { product: DataSnapshot ->
                        val id = product.key.toString()
                        val brand = product.child("brand").value.toString().toInt()
                        val category = product.child("category").value.toString().toInt()
                        val description = product.child("description").value.toString()
                        val photo = product.child("photo").value.toString()
                        val price = product.child("price").value.toString().toFloat()
                        val title = product.child("title").value.toString()
                        Product.products.add(Product(id,
                            brand,
                            category,
                            description,
                            photo,
                            price,
                            title))
                    }

                    ShopFragment.productAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }

            })
    }

    companion object {
        val instance = DB()
    }
}
package com.shop.firebase

import android.content.ContentValues
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shop.models.Product

class ProductDatabase private constructor() : IProductDatabase {
    private var cashProducts: MutableList<Product> = mutableListOf<Product>()

    //Добавление всех товаров из БД
    override fun readProduct(
        callback: GetProductCallback,
    ) {
        if (cashProducts.isNotEmpty()) {
            Log.d("cashProducts", cashProducts.toString())
            callback.callback(cashProducts)
            return
        }

        Firebase.database.getReference("products")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    cashProducts.clear()

                    snapshot.children.forEach {
                        Log.d("postSnapshot", it.toString())
                        val product = it.getValue(Product::class.java)
                        cashProducts.add(product!!)
                    }

                    callback.callback(cashProducts)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                }
            })
    }

    override fun writeProduct(product: Product) {
        TODO("Not yet implemented")
    }

    companion object {
        private var productDatabase: IProductDatabase? = null
        fun instance(): IProductDatabase {
            if (productDatabase == null) productDatabase = ProductDatabase()
            return productDatabase as IProductDatabase
        }
    }
}

interface IProductDatabase {
    fun readProduct(callback: GetProductCallback)
    fun writeProduct(product: Product)
}

interface GetProductCallback {
    fun callback(products: MutableList<Product>)
}
package com.shop.firebase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Continuation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.shop.models.Product
import java.text.SimpleDateFormat
import java.util.*

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

    @SuppressLint("SimpleDateFormat")
    override fun writeProduct(product: Product, imageUri: Uri, callback: WriteProductCallback) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.getReference("product_photo")

        val currentTime =
            SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().time)
        val key = currentTime.toString()

        val filePath: StorageReference = storageRef.child("${imageUri.lastPathSegment}${key}.webm")

        try {
            val uploadTask: UploadTask = filePath.putFile(imageUri)

            uploadTask.addOnFailureListener {
                Log.d("uploadInStorageError", it.stackTraceToString())
            }
                .addOnSuccessListener {
                    Log.d("successful", "Successful")

                    uploadTask.continueWithTask(Continuation {
                        return@Continuation filePath.downloadUrl
                    }).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.d("successful", "Successful")
                            product.photo = it.result.toString()
                            Firebase.database.reference.child("products")
                                .child("${product.title}$key").setValue(product)

                            callback.callback(true)
                        }
                    }
                }
        } catch (e: Exception) {
            Log.d("download", e.stackTraceToString())
        }
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
    fun writeProduct(product: Product, imageUri: Uri, callback: WriteProductCallback)
}

interface GetProductCallback {
    fun callback(products: MutableList<Product>)
}

interface WriteProductCallback {
    fun callback(status: Boolean)
}
package com.shop.models

data class Product(
    val id: String,
    val brand: Int,
    val category: Int,
    val description: String,
    val photo: String,
    val price: Float,
    val title: String,
) {
    companion object {
        var products = mutableSetOf<Product>()
    }
}

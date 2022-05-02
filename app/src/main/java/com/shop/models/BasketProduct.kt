package com.shop.models

data class BasketProduct(
    val id: String = "",
    val brand: Int = -1,
    val category: Int = -1,
    val description: String = "",
    val photo: String = "",
    val price: Float = -1f,
    val title: String = "",
    var count: Int = 0
) {
    companion object {
        var products: MutableList<BasketProduct> = mutableListOf()
    }
}

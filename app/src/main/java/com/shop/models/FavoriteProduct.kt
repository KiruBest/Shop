package com.shop.models

data class FavoriteProduct(
    val id: String = "",
    val brand: Int = -1,
    val category: Int = -1,
    val description: String = "",
    val photo: String = "",
    val price: Float = -1f,
    val title: String = "",
) {
    companion object {
        var products: MutableList<FavoriteProduct> = mutableListOf()
    }
}

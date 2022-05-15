package com.shop.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//Класс хранит информацию о конкретном товаре\
@Parcelize
data class Product(
    var id: String = "",
    var brand: Int = -1,
    var category: String = "",
    var description: String = "",
    var photo: String = "",
    var price: Float = -1f,
    var title: String = "",
    var count: Int = 1,
    val sizes: MutableList<String> = mutableListOf()
) : Parcelable {

    companion object {
        var products: MutableList<Product> = mutableListOf()
    }
}

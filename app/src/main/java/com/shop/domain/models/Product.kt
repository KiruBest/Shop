package com.shop.domain.models

//Класс хранит информацию о конкретном товаре
data class Product(val id: String = "",
                   val brand: Int = -1,
                   val category: Int = -1,
                   val description: String = "",
                   val photo: String = "",
                   val price: Float = -1f,
                   val title: String = "")
